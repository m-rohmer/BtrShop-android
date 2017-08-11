package fr.inria.spirals.sensorscollect.reporter;

import android.util.Log;

import java.util.Collections;
import java.util.Set;

import fr.inria.spirals.sensorscollect.api.reporter.AbstractReporter;
import fr.inria.spirals.sensorscollect.api.reporter.report.ErrorReport;
import fr.inria.spirals.sensorscollect.api.reporter.report.MarkReport;
import fr.inria.spirals.sensorscollect.api.reporter.report.Report;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Consumer;

import static io.btrshop.BtrShopApplication.getRepository;

public class LogReporter extends AbstractReporter {

    private final String source;
    Disposable disposable = Disposables.empty();

    public LogReporter(Class<?> source) {
        this.source = source.getCanonicalName();
    }

    @Override
    protected boolean canStart() {
        return true;
    }

    @Override
    protected void doStart() {
        // Nothing to do
    }

    @Override
    protected void doStop() {
        // Nothing to do
    }

    @Override
    public void doReport(Report report) {
        Log.i(source, report.toString());
        disposable.dispose();
        disposable = getRepository().getStrings(report.toString())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        //...
                    }
                });
    }

    @Override
    public void doReport(MarkReport markReport) {
        Log.i(source+" troll", markReport.toString());
    }

    @Override
    public void doReport(ErrorReport errorReport) {
        Log.e(source, errorReport.toString());
    }

    @Override
    public Set<String> getRequiredPermissions() {
        return Collections.emptySet();
    }

}
