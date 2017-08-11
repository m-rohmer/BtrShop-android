package fr.inria.spirals.sensorscollect.api.reporter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import fr.inria.spirals.sensorscollect.api.reporter.report.ErrorReport;
import fr.inria.spirals.sensorscollect.api.reporter.report.MarkReport;
import fr.inria.spirals.sensorscollect.api.reporter.report.Report;
import fr.inria.spirals.sensorscollect.api.toggle.AbstractToggle;

public abstract class AbstractReporter extends AbstractToggle implements Reporter {

    private Lock reportLock = new ReentrantLock();
    private Lock markReportLock = new ReentrantLock();
    private Lock reportErrorLock = new ReentrantLock();

    @Override
    public final void report(Report report) {
        try {
            reportLock.lock();
            if (isRunning()) {
                doReport(report);
            }
        } finally {
            reportLock.unlock();
        }
    }

    @Override
    public final void report(MarkReport markReport) {
        try {
            markReportLock.lock();
            if (isRunning()) {
                doReport(markReport);
            }
        } finally {
            markReportLock.unlock();
        }
    }

    @Override
    public final void report(ErrorReport errorReport) {
        try {
            reportErrorLock.lock();
            if (isRunning()) {
                doReport(errorReport);
            }
        } finally {
            reportErrorLock.unlock();
        }
    }

    public abstract void doReport(Report report);

    public abstract void doReport(MarkReport markReport);

    public abstract void doReport(ErrorReport errorReport);

}
