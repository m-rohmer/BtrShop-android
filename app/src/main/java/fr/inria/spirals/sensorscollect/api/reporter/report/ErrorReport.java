package fr.inria.spirals.sensorscollect.api.reporter.report;

import java.util.Date;

public class ErrorReport extends AbstractReport implements SingleMessageReport {

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

        public ErrorReport build() {
            return new ErrorReport(source, date, message);
        }
    }

    private final String message;

    private ErrorReport(Object source, Date date, String message) {
        super(source, date);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ErrorReport{" +
                "source=" + super.getSource() +
                ", date='" + super.getDate() + '\'' +
                ", formattedDate='" + super.getFormattedDate() + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
