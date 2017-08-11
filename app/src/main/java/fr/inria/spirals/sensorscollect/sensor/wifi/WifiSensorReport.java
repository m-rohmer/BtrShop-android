package fr.inria.spirals.sensorscollect.sensor.wifi;

import java.util.Date;

import fr.inria.spirals.sensorscollect.api.reporter.report.AbstractReport;

public class WifiSensorReport extends AbstractReport {

    public static class Builder {

        public static Builder builder() {
            return new Builder();
        }

        private Object source;
        private Date date;
        private String bssid;
        private String ssid;
        private int level;

        private Builder() {

        }

        public Builder source(final Object source) {
            this.source = source;
            return this;
        }

        public Builder date(final Date date) {
            this.date = date;
            return this;
        }

        public Builder bssid(final String bssid) {
            this.bssid = bssid;
            return this;
        }

        public Builder ssid(final String ssid) {
            this.ssid = ssid;
            return this;
        }

        public Builder level(final int level) {
            this.level = level;
            return this;
        }

        public WifiSensorReport build() {
            return new WifiSensorReport(source, date, bssid, ssid, level);
        }
    }

    private final String bssid;
    private final String ssid;
    private final int level;

    private WifiSensorReport(Object source, Date date, String bssid, String ssid, int level) {
        super(source, date);
        this.bssid = bssid;
        this.ssid = ssid;
        this.level = level;
    }

    public String getBssid() {
        return bssid;
    }

    public String getSsid() {
        return ssid;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "WifiSensorReport{" +
                "source=" + getSource() +
                ", date=" + getDate() +
                ", bssid='" + bssid + '\'' +
                ", ssid='" + ssid + '\'' +
                ", level=" + level +
                '}';
    }

}
