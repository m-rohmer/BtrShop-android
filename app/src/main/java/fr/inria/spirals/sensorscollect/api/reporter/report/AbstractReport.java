package fr.inria.spirals.sensorscollect.api.reporter.report;

import java.util.Date;

import fr.inria.spirals.sensorscollect.util.DateUtils;

public abstract class AbstractReport implements Report {

    private final Object source;
    private final Date date;
    private final String formattedDate;

    public AbstractReport(Object source, Date date) {
        this.source = source;
        this.date = date;
        formattedDate = DateUtils.toString(date);
    }

    @Override
    public Object getSource() {
        return source;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public String getFormattedDate() {
        return formattedDate;
    }

}
