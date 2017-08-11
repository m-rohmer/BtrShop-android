package fr.inria.spirals.sensorscollect.sensor;

import android.content.Context;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.inria.spirals.sensorscollect.api.manualfeature.ManualFeature;
import fr.inria.spirals.sensorscollect.api.reporter.Reporter;
import fr.inria.spirals.sensorscollect.api.reporter.report.MarkReport;
import fr.inria.spirals.sensorscollect.api.sensor.Sensor;
import fr.inria.spirals.sensorscollect.api.toggle.AbstractToggle;
import fr.inria.spirals.sensorscollect.reporter.LogReporter;
import fr.inria.spirals.sensorscollect.reporter.NoReporter;
import fr.inria.spirals.sensorscollect.sensor.ble.RxAndroidBleSensor;
import fr.inria.spirals.sensorscollect.sensor.wifi.ReactiveWifiSensor;

public final class SensorManager extends AbstractToggle implements Sensor {

    private List<? extends Sensor> sensors;
    private SetMultimap<String, Sensor> requiredPermissions;
    private SetMultimap<ManualFeature, Sensor> manualFeaturesToEnable;

    public SensorManager(final Context context) {
        initSensors(context);
        initRequiredPermissions();
        initManualFeaturesToEnable();
    }

    @Override
    public Reporter getReporter() {
        return new NoReporter();
    }

    @Override
    protected boolean canStart() {
        // Always true
        return true;
    }

    @Override
    protected void doStart() {
        for (final Sensor sensor : sensors) {
            sensor.start();
        }
    }

    @Override
    protected void doStop() {
        for (final Sensor sensor : sensors) {
            sensor.stop();
        }
    }

    @Override
    public void startScan() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void stopScan() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> getRequiredPermissions() {
        return new HashSet<>(requiredPermissions.keySet());
    }

    @Override
    public void onRequiredPermissionChange(final String permission, final int state) {
        final Set<Sensor> associatedSensors = requiredPermissions.get(permission);
        for (final Sensor sensor : associatedSensors) {
            sensor.onRequiredPermissionChange(permission, state);
        }
    }

    @Override
    public Set<ManualFeature> getManualFeaturesToEnable() {
        return new HashSet<>(manualFeaturesToEnable.keySet());
    }

    @Override
    public void onManualFeatureChange(final ManualFeature manualFeature) {
        throw new UnsupportedOperationException("Use #onManualFeatureChange(int) instead");
    }

    public void applyMark(final String mark) {
        final Date now = new Date();
        for (final Sensor sensor : sensors) {
            sensor.getReporter().report(MarkReport.Builder.builder()
                    .source(this)
                    .date(now)
                    .message(mark)
                    .build()
            );
        }
    }

    public void onManualFeatureChange(final int manualFeatureId) {
        for (final ManualFeature manualFeature : manualFeaturesToEnable.keys()) {
            if (manualFeature.getId() == manualFeatureId) {
                for (final Sensor associatedSensor : manualFeaturesToEnable.get(manualFeature)) {
                    associatedSensor.onManualFeatureChange(manualFeature);
                }
            }
        }
    }

    private void initSensors(final Context context) {
        sensors = Arrays.asList(
                new RxAndroidBleSensor(context, new LogReporter(RxAndroidBleSensor.class)),
                new ReactiveWifiSensor(context, new LogReporter(ReactiveWifiSensor.class))
        );
    }

    private void initRequiredPermissions() {
        requiredPermissions = HashMultimap.create();
        for (final Sensor sensor : sensors) {
            for (final String requiredPermission : sensor.getRequiredPermissions()) {
                requiredPermissions.put(requiredPermission, sensor);
            }
        }
    }

    private void initManualFeaturesToEnable() {
        manualFeaturesToEnable = HashMultimap.create();
        for (final Sensor sensor : sensors) {
            for (final ManualFeature manualFeatureToEnable : sensor.getManualFeaturesToEnable()) {
                manualFeaturesToEnable.put(manualFeatureToEnable, sensor);
            }
        }
    }

}
