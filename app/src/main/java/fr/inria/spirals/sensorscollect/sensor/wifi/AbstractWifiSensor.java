package fr.inria.spirals.sensorscollect.sensor.wifi;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import fr.inria.spirals.sensorscollect.api.manualfeature.ManualFeature;
import fr.inria.spirals.sensorscollect.api.manualfeature.ManualFeatures;
import fr.inria.spirals.sensorscollect.api.reporter.Reporter;
import fr.inria.spirals.sensorscollect.api.sensor.AbstractSensor;

public abstract class AbstractWifiSensor extends AbstractSensor {

    private static class UserConfigurationInitialState {
        private boolean wifiEnabled;

        public UserConfigurationInitialState(boolean wifiEnabled) {
            this.wifiEnabled = wifiEnabled;
        }

        public boolean isWifiEnabled() {
            return wifiEnabled;
        }
    }

    private class WifiStateChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (wifiManager.getWifiState()) {
                case WifiManager.WIFI_STATE_ENABLED:
                    Log.i(getName(), "Wifi device has been turned on. Starting scan...");
                    startScan();
                    return;
                case WifiManager.WIFI_STATE_DISABLING:
                    Log.i(getName(), "Wifi device has been turned off. Stopping scan...");
                    stopScan();
                default:
                    // Nothing to do
            }
        }
    }

    private static final Set<String> REQUIRED_PERMISSIONS = new HashSet<String>() {
        {
            add(Manifest.permission.ACCESS_WIFI_STATE);
            add(Manifest.permission.CHANGE_WIFI_STATE);
            add(Manifest.permission.ACCESS_COARSE_LOCATION);
            add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    };

    private WifiManager wifiManager;
    private LocationManager locationManager;
    private WifiStateChangeReceiver wifiStateChangeReceiver;
    private UserConfigurationInitialState userConfigurationInitialState;

    public AbstractWifiSensor(final Context context, final Reporter reporter) {
        super(context, reporter);
        init();
    }

    public WifiManager getWifiManager() {
        return wifiManager;
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
    protected void doStart() {
        getContext().registerReceiver(wifiStateChangeReceiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
        userConfigurationInitialState = new UserConfigurationInitialState(wifiManager.isWifiEnabled());
        if (!userConfigurationInitialState.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        if (isWifiOn()) {
            startScan();
        }
    }

    @Override
    protected void doStop() {
        getContext().unregisterReceiver(wifiStateChangeReceiver);
        stopScan();
        wifiManager.setWifiEnabled(userConfigurationInitialState.isWifiEnabled());
    }

    private void init() {
        wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        wifiStateChangeReceiver = new WifiStateChangeReceiver();
    }

    private boolean isWifiOn() {
        return wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED;
    }

}
