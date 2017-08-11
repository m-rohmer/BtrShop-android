package fr.inria.spirals.sensorscollect.api.sensor;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import fr.inria.spirals.sensorscollect.api.manualfeature.ManualFeature;
import fr.inria.spirals.sensorscollect.api.reporter.Reporter;
import fr.inria.spirals.sensorscollect.api.toggle.AbstractToggle;
import fr.inria.spirals.sensorscollect.api.toggle.ToggleException;

public abstract class AbstractSensor extends AbstractToggle implements Sensor {

    private final Context context;
    private final Reporter reporter;

    private SetMultimap<Integer, String> requiredPermissionsState;
    private Lock permissionChangeLock;

    private boolean scanRunning;
    private Lock scanLock;


    public AbstractSensor(@NonNull final Context context, @NonNull final Reporter reporter) {
        this.context = context;
        this.reporter = reporter;
        requiredPermissionsState = HashMultimap.create();
        permissionChangeLock = new ReentrantLock();
        scanLock = new ReentrantLock();
    }

    public Context getContext() {
        return context;
    }

    @Override
    public Reporter getReporter() {
        return reporter;
    }

    @Override
    public final void onRequiredPermissionChange(String permission, int state) {
        try {
            permissionChangeLock.lock();
            requiredPermissionsState.put(state, permission);
        } finally {
            permissionChangeLock.unlock();
        }
    }

    @Override
    public final void onManualFeatureChange(ManualFeature action) {
        // Nothing to do here
    }

    @Override
    protected boolean canStart() {
        return canStartWithRequiredPermissions() && canStartWithRequiredManualFeatures();
    }

    @Override
    public final void startScan() {
        try {
            scanLock.lock();
            if (scanRunning) {
                Log.i(getName(), "Scan is already started");
            } else {
                try {
                    getReporter().start();
                    Log.i(getName(), "Scan is starting...");
                    scanRunning = true;
                    doStartScan();
                    Log.i(getName(), "Scan is started");
                } catch (final ToggleException e) {
                    Log.e(getName(), "Unable to start scan because of reporter initialization failure", e);
                }
            }
        } finally {
            scanLock.unlock();
        }
    }

    @Override
    public final void stopScan() {
        try {
            scanLock.lock();
            if (scanRunning) {
                Log.i(getName(), "Scan is stopping...");
                doStopScan();
                scanRunning = false;
                Log.i(getName(), "Scan is stopped");
                try {
                    getReporter().stop();
                } catch (final ToggleException e) {
                    Log.e(getName(), "Unable to properly stop scan because of reporter close failure", e);
                }
            } else {
                Log.i(getName(), "Scan is already stopped");
            }
        } finally {
            scanLock.unlock();
        }
    }

    public abstract void doStartScan();

    public abstract void doStopScan();

    private boolean canStartWithRequiredPermissions() {
        final Set<String> currentlyDeniedPermissions;
        try {
            permissionChangeLock.lock();
            currentlyDeniedPermissions = new HashSet<>(requiredPermissionsState.get(PackageManager.PERMISSION_DENIED));
        } finally {
            permissionChangeLock.unlock();
        }
        if (currentlyDeniedPermissions.isEmpty()) {
            return true;
        } else {
            Log.e(getName(), "I'm unable to start due to the following not granted permissions: " + currentlyDeniedPermissions);
            return false;
        }
    }

    private boolean canStartWithRequiredManualFeatures() {
        final Set<ManualFeature> currentlyManualFeaturesToEnable = getManualFeaturesToEnable();
        if (currentlyManualFeaturesToEnable.isEmpty()) {
            return true;
        } else {
            Log.e(getName(), "I'm unable to start due to the following disable required features: " + currentlyManualFeaturesToEnable);
            return false;
        }
    }

}
