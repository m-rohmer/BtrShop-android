package fr.inria.spirals.sensorscollect.sensor.ble;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import fr.inria.spirals.sensorscollect.api.manualfeature.ManualFeature;
import fr.inria.spirals.sensorscollect.api.manualfeature.ManualFeatures;
import fr.inria.spirals.sensorscollect.api.reporter.Reporter;
import fr.inria.spirals.sensorscollect.api.sensor.AbstractSensor;

public abstract class AbstractBleSensor extends AbstractSensor {

    private static class UserConfigurationInitialState {
        private boolean bluetoothEnabled;

        public UserConfigurationInitialState(boolean bluetoothEnabled) {
            this.bluetoothEnabled = bluetoothEnabled;
        }

        public boolean isBluetoothEnabled() {
            return bluetoothEnabled;
        }
    }

    private class BluetoothStateChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (bluetoothAdapter.getState()) {
                case BluetoothAdapter.STATE_ON:
                    Log.i(getName(), "Bluetooth device has been turned on. Starting scan...");
                    startScan();
                    return;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    Log.i(getName(), "Bluetooth device has been turned off. Stopping scan...");
                    stopScan();
                default:
                    // Nothing do to
            }
        }
    }

    private static final Set<String> REQUIRED_PERMISSIONS = new HashSet<String>() {
        {
            add(Manifest.permission.BLUETOOTH);
            add(Manifest.permission.BLUETOOTH_ADMIN);
            add(Manifest.permission.ACCESS_COARSE_LOCATION);
            add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    };

    private BluetoothAdapter bluetoothAdapter;
    private LocationManager locationManager;
    private BluetoothStateChangeReceiver bluetoothStateChangeReceiver;
    private UserConfigurationInitialState userConfigurationInitialState;

    public AbstractBleSensor(Context context, Reporter reporter) {
        super(context, reporter);
        init();
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    @Override
    public Set<String> getRequiredPermissions() {
        final Set<String> requiredPermissions = new HashSet<>(REQUIRED_PERMISSIONS);
        requiredPermissions.addAll(getReporter().getRequiredPermissions());
        return requiredPermissions;
    }

    @Override
    public Set<ManualFeature> getManualFeaturesToEnable() {
        final Set<ManualFeature> manualFeatures = new HashSet<>();
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            manualFeatures.add(ManualFeatures.OPEN_LOCATION_SETTINGS);
        }
        return manualFeatures;
    }

    @Override
    protected boolean canStart() {
        return super.canStart() && canStartBluetoothDevice();
    }

    @Override
    protected void doStart() {
        getContext().registerReceiver(bluetoothStateChangeReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        userConfigurationInitialState = new UserConfigurationInitialState(bluetoothAdapter.isEnabled());
        if (!userConfigurationInitialState.isBluetoothEnabled()) {
            bluetoothAdapter.enable();
        }
        if (isBluetoothOn()) {
            startScan();
        }
    }

    @Override
    protected void doStop() {
        getContext().unregisterReceiver(bluetoothStateChangeReceiver);
        stopScan();
        if (bluetoothAdapter.isEnabled() && !userConfigurationInitialState.isBluetoothEnabled()) {
            bluetoothAdapter.disable();
        }
    }

    private void init() {
        bluetoothAdapter = ((BluetoothManager) getContext().getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        bluetoothStateChangeReceiver = new BluetoothStateChangeReceiver();
    }

    private boolean isBluetoothOn() {
        return bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON;
    }

    private boolean canStartBluetoothDevice() {
        if (bluetoothAdapter == null) {
            Log.e(getName(), "Unable to start bluetooth scan because bluetooth adapter cannot be found. Does device have a bluetooth antenna?");
            return false;
        }
        return true;
    }
}
