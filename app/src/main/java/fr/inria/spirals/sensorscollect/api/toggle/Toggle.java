package fr.inria.spirals.sensorscollect.api.toggle;

public interface Toggle {

    String getName();

    void start();

    void stop();

    void toggle();

    boolean isRunning();

}
