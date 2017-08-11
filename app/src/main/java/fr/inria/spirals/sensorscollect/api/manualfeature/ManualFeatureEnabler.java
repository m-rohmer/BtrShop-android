package fr.inria.spirals.sensorscollect.api.manualfeature;

import java.util.Set;

public interface ManualFeatureEnabler {

    Set<ManualFeature> getManualFeaturesToEnable();

    void onManualFeatureChange(ManualFeature action);

}
