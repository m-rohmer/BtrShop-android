package fr.inria.spirals.sensorscollect.reporter;

import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.inria.spirals.sensorscollect.api.reporter.report.ErrorReport;
import fr.inria.spirals.sensorscollect.api.reporter.report.MarkReport;
import fr.inria.spirals.sensorscollect.api.reporter.report.Report;
import fr.inria.spirals.sensorscollect.util.ReflectionUtils;

public class CsvFileReporter extends AbstractFileReporter {

    private static final String LOG_TAG = CsvFileReporter.class.getCanonicalName();

    private static final String CSV_FILE_EXTENSION = "csv";
    private static final String PLAIN_TEXT_FILE_EXTENSION = "txt";
    private static final String CSV_SEPARATOR = ",";

    private final Class<? extends Report> reportClass;
    private List<String> columns;

    public static String encloseString(final String str) {
        return String.format("\"%s\"", str);
    }

    public CsvFileReporter(@NonNull String outputFilePrefix, @NonNull Class<? extends Report> reportClass) {
        super(outputFilePrefix);
        this.reportClass = reportClass;
    }

    @Override
    protected void doStart() {
        super.doStart();
        createHeader();
    }

    @Override
    public void doReport(@NonNull Report report) {
        if (!reportClass.equals(report.getClass())) {
            Log.w(LOG_TAG, String.format("Unable to report non %s class (received %s)", reportClass, report.getClass()));
            return;
        }
        final String[] values = new String[columns.size()];
        for (Map.Entry<String, Object> reportColumn : ReflectionUtils.mapOfFields(report).entrySet()) {
            values[columns.indexOf(reportColumn.getKey())] = String.valueOf(reportColumn.getValue());
        }
        final StringBuilder line = new StringBuilder();
        for (String value : values) {
            line.append(value).append(CSV_SEPARATOR);
        }
        if (line.length() > 0) {
            line.delete(line.length() - CSV_SEPARATOR.length(), line.length());
        }
        getOutputFileWriter().println(line);
        getOutputFileWriter().flush();
    }

    @Override
    public void doReport(MarkReport markReport) {
        getOutputFileWriter().println(encloseString(markReport.toString()));
        getOutputFileWriter().flush();
    }

    @Override
    public void doReport(ErrorReport error) {
        getOutputErrorFileWriter().println(encloseString(error.toString()));
        getOutputErrorFileWriter().flush();
    }

    @Override
    public String getOutputFileExtension() {
        return CSV_FILE_EXTENSION;
    }

    @Override
    public String getErrorOutputFileExtension() {
        return PLAIN_TEXT_FILE_EXTENSION;
    }

    private void createHeader() {
        columns = new ArrayList<>();
        final StringBuilder header = new StringBuilder();
        for (final Field field : ReflectionUtils.getAllFields(reportClass)) {
            columns.add(field.getName());
            header.append(field.getName()).append(CSV_SEPARATOR);
        }
        if (header.length() > 0) {
            header.delete(header.length() - CSV_SEPARATOR.length(), header.length());
        }
        getOutputFileWriter().println(header);
        getOutputFileWriter().flush();
    }

}
