package fr.inria.spirals.sensorscollect.api.manualfeature;

import android.provider.Settings;

public final class ManualFeatures {

    private static int manualFeatureId = 0;

    public static final ManualFeature OPEN_LOCATION_SETTINGS = new ManualFeature(manualFeatureId++, Settings.ACTION_LOCATION_SOURCE_SETTINGS);

    private ManualFeatures() {

    }

}
