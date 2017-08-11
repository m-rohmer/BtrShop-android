package fr.inria.spirals.sensorscollect.reporter;

import android.support.annotation.NonNull;

import fr.inria.spirals.sensorscollect.api.reporter.report.ErrorReport;
import fr.inria.spirals.sensorscollect.api.reporter.report.MarkReport;
import fr.inria.spirals.sensorscollect.api.reporter.report.Report;

/**
 * Created by martin on 08/08/2017.
 */

public class RxCacheReporter extends AbstractFileReporter {

    public RxCacheReporter(@NonNull String outputFilePrefix) {
        super(outputFilePrefix);
    }

    @Override
    public String getOutputFileExtension() {
        return null;
    }

    @Override
    public String getErrorOutputFileExtension() {
        return null;
    }

    @Override
    public void doReport(Report report) {

    }

    @Override
    public void doReport(MarkReport markReport) {

    }

    @Override
    public void doReport(ErrorReport errorReport) {

    }
}
