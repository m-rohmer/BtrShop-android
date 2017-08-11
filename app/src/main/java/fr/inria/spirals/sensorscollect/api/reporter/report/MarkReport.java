package fr.inria.spirals.sensorscollect.api.reporter.report;

import java.util.Date;

public class MarkReport extends AbstractReport implements SingleMessageReport {

    public static class Builder {
        public static Builder builder() {
            return new Builder();
        }

        private String message;
        private Object source;
        private Date date;

        private Builder() {

        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder source(Object source) {
            this.source = source;
            return this;
        }

        public Builder date(Date date) {
            this.date = date;
            return this;
        }

        public MarkReport build() {
            return new MarkReport(source, date, message);
        }
    }

    private final String message;

    private MarkReport(Object source, Date date, String message) {
        super(source, date);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "MarkReport{" +
                "source=" + super.getSource() +
                ", date='" + super.getDate() + '\'' +
                ", formattedDate='" + super.getFormattedDate() + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

}
