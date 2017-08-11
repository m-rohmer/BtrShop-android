package fr.inria.spirals.sensorscollect.api.reporter.report;

import java.util.Date;

public interface Report {

    Object getSource();

    Date getDate();

    String getFormattedDate();

}
