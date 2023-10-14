package pl.bodzioch.damian.exception;

public class SchedulerNotFoundException extends RuntimeException {

    public SchedulerNotFoundException() {
    }

    public SchedulerNotFoundException(String message) {
        super(message);
    }
}
