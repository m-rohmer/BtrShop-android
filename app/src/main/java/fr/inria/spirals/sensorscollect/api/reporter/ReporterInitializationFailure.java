package fr.inria.spirals.sensorscollect.api.reporter;

public class ReporterInitializationFailure extends RuntimeException {

    public ReporterInitializationFailure(String message) {
        super(message);
    }

    public ReporterInitializationFailure(String message, Throwable cause) {
        super(message, cause);
    }
}
