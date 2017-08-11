package fr.inria.spirals.sensorscollect.sensor.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;

import com.github.pwittchen.reactivewifi.ReactiveWifi;

import java.util.Date;
import java.util.List;

import fr.inria.spirals.sensorscollect.api.reporter.Reporter;
import rx.Subscription;
import rx.functions.Action1;

public class ReactiveWifiSensor extends AbstractWifiSensor {

    private Subscription subscription;

    public ReactiveWifiSensor(Context context, Reporter reporter) {
        super(context, reporter);
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    public void doStartScan() {
        subscription = ReactiveWifi.observeWifiAccessPoints(getContext())
                .subscribe(new Action1<List<ScanResult>>() {
                    @Override
                    public void call(List<ScanResult> scanResults) {
                        final Date now = new Date();
                        for (final ScanResult scanResult : scanResults) {
                            getReporter().report(WifiSensorReport.Builder.builder()
                                    .source(ReactiveWifiSensor.this)
                                    .date(now)
                                    .bssid(scanResult.BSSID)
                                    .ssid(scanResult.SSID)
                                    .level(scanResult.level)
                                    .build()
                            );
                        }
                    }
                });
    }

    @Override
    public void doStopScan() {
        subscription.unsubscribe();
    }
}
