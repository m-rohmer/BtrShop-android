package fr.inria.spirals.sensorscollect.api.sensor;

import fr.inria.spirals.sensorscollect.api.manualfeature.ManualFeatureEnabler;
import fr.inria.spirals.sensorscollect.api.permission.PermissionsRequester;
import fr.inria.spirals.sensorscollect.api.reporter.CanReport;
import fr.inria.spirals.sensorscollect.api.toggle.Toggle;

public interface Sensor extends Toggle, PermissionsRequester, ManualFeatureEnabler, CanReport {

    void startScan();

    void stopScan();

}
