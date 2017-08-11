package fr.inria.spirals.sensorscollect.sensor.ble;

import android.content.Context;

import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.scan.ScanFilter;
import com.polidea.rxandroidble.scan.ScanResult;
import com.polidea.rxandroidble.scan.ScanSettings;

import java.util.Date;

import fr.inria.spirals.sensorscollect.api.reporter.Reporter;
import io.btrshop.BtrShopApplication;
import io.btrshop.rxcache.Repository;
import rx.Subscription;
import rx.functions.Action1;

public class RxAndroidBleSensor extends AbstractBleSensor {

    private RxBleClient rxBleClient;
    private Subscription scanSubscription;
    private ScanSettings bleScanSettings;
    private ScanFilter[] bleScanFilters;

    public RxAndroidBleSensor(Context context, Reporter reporter) {
        super(context, reporter);
        init();
    }

    protected Repository getRepository() {
        return BtrShopApplication.getRepository();
    }

    private void init() {
        rxBleClient = RxBleClient.create(getContext());
        bleScanSettings = new ScanSettings.Builder().build();
        bleScanFilters = new ScanFilter[]{};
    }

    @Override
    public void doStartScan() {
        scanSubscription = rxBleClient.scanBleDevices(bleScanSettings, bleScanFilters)
                .subscribe(new Action1<ScanResult>() {
                    @Override
                    public void call(ScanResult scanResult) {
                        final Date now = new Date();
                        getReporter().report(
                                BleSensorReport.Builder.builder()
                                        .source(RxAndroidBleSensor.this)
                                        .date(now)
                                        .address(scanResult.getBleDevice().getMacAddress())
                                        .name(scanResult.getBleDevice().getName())
                                        .rssi(scanResult.getRssi())
                                        .build()
                        );
                    }
                });
    }

    @Override
    public void doStopScan() {
        scanSubscription.unsubscribe();
    }

}
