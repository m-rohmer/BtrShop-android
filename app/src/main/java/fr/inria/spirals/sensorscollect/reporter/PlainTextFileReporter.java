package fr.inria.spirals.sensorscollect.reporter;

import android.support.annotation.NonNull;

import fr.inria.spirals.sensorscollect.api.reporter.report.ErrorReport;
import fr.inria.spirals.sensorscollect.api.reporter.report.MarkReport;
import fr.inria.spirals.sensorscollect.api.reporter.report.Report;

public class PlainTextFileReporter extends AbstractFileReporter {

    private static final String PLAIN_TEXT_FILE_EXTENSION = "txt";

    public PlainTextFileReporter(@NonNull String outputFilePrefix) {
        super(outputFilePrefix);
    }

    @Override
    public void doReport(Report report) {
        getOutputFileWriter().println(report);
        getOutputFileWriter().flush();
    }

    @Override
    public void doReport(MarkReport markReport) {
        getOutputErrorFileWriter().println(markReport);
        getOutputFileWriter().flush();
    }

    @Override
    public void doReport(ErrorReport errorReport) {
        getOutputErrorFileWriter().println(errorReport);
        getOutputFileWriter().flush();
    }

    @Override
    public String getOutputFileExtension() {
        return PLAIN_TEXT_FILE_EXTENSION;
    }

    @Override
    public String getErrorOutputFileExtension() {
        return PLAIN_TEXT_FILE_EXTENSION;
    }

}
