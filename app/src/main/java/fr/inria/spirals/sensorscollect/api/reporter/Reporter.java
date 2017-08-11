package fr.inria.spirals.sensorscollect.api.reporter;

import java.util.Set;

import fr.inria.spirals.sensorscollect.api.reporter.report.ErrorReport;
import fr.inria.spirals.sensorscollect.api.reporter.report.MarkReport;
import fr.inria.spirals.sensorscollect.api.reporter.report.Report;
import fr.inria.spirals.sensorscollect.api.toggle.Toggle;

public interface Reporter extends Toggle {

    void report(Report report);

    void report(MarkReport markReport);

    void report(ErrorReport errorReport);

    Set<String> getRequiredPermissions();

}
