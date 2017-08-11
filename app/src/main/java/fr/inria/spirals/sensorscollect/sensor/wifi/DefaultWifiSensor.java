package fr.inria.spirals.sensorscollect.sensor.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.Date;

import fr.inria.spirals.sensorscollect.api.reporter.Reporter;

public class DefaultWifiSensor extends AbstractWifiSensor {

    private WifiScanResultReceiver wifiScanResultReceiver;

    public DefaultWifiSensor(Context context, Reporter reporter) {
        super(context, reporter);
        init();
    }

    private class WifiScanResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final Date now = new Date();
            for (final ScanResult scanResult : getWifiManager().getScanResults()) {
                getReporter().report(WifiSensorReport.Builder.builder()
                        .source(DefaultWifiSensor.this)
                        .date(now)
                        .bssid(scanResult.BSSID)
                        .ssid(scanResult.SSID)
                        .level(scanResult.level)
                        .build()
                );
            }
        }
    }

    @Override
    public void doStartScan() {
        getContext().registerReceiver(wifiScanResultReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        getWifiManager().startScan();
    }

    @Override
    public void doStopScan() {
        getContext().unregisterReceiver(wifiScanResultReceiver);

    }

    private void init() {
        wifiScanResultReceiver = new WifiScanResultReceiver();
    }

}
