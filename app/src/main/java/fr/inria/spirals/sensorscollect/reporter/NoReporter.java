package fr.inria.spirals.sensorscollect.reporter;

import java.util.Collections;
import java.util.Set;

import fr.inria.spirals.sensorscollect.api.reporter.AbstractReporter;
import fr.inria.spirals.sensorscollect.api.reporter.report.ErrorReport;
import fr.inria.spirals.sensorscollect.api.reporter.report.MarkReport;
import fr.inria.spirals.sensorscollect.api.reporter.report.Report;

public class NoReporter extends AbstractReporter {

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
    public void doReport(Report data) {
        // Nothing to do
    }

    @Override
    public void doReport(MarkReport markReport) {
        // Nothing to do
    }

    @Override
    public void doReport(ErrorReport errorReport) {
        // Nothing to do
    }

    @Override
    public Set<String> getRequiredPermissions() {
        return Collections.emptySet();
    }

}
