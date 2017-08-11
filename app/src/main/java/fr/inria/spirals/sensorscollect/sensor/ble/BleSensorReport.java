package fr.inria.spirals.sensorscollect.sensor.ble;

import java.util.Date;

import fr.inria.spirals.sensorscollect.api.reporter.report.AbstractReport;

public class BleSensorReport extends AbstractReport {

    public static class Builder {

        public static Builder builder() {
            return new Builder();
        }

        private Object source;
        private Date date;
        private String address;
        private String name;
        private int rssi;

        public Builder source(final Object source) {
            this.source = source;
            return this;
        }

        public Builder date(final Date date) {
            this.date = date;
            return this;
        }

        public Builder address(final String address) {
            this.address = address;
            return this;
        }

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public Builder rssi(final int rssi) {
            this.rssi = rssi;
            return this;
        }

        public BleSensorReport build() {
            return new BleSensorReport(source, date, address, name, rssi);
        }

        private Builder() {
        }
    }

    private final String address;
    private final String name;
    private final int rssi;

    private BleSensorReport(Object source, Date date, String address, String name, int rssi) {
        super(source, date);
        this.address = address;
        this.name = name;
        this.rssi = rssi;
    }

    public String getAddress() {
        return address;
    }


    public String getName() {
        return name;
    }


    public int getRssi() {
        return rssi;
    }

    @Override
    public String toString() {
        return "BleSensorReport{" +
                "source='" + getSource() + '\'' +
                ", date='" + getDate() + '\'' +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", rssi=" + rssi +
                '}';
    }
}