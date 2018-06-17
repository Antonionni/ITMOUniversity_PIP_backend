package Exceptions;

public class WrongThresholdException extends BusinessException {
    public WrongThresholdException() {
    }

    public WrongThresholdException(String message) {
        super(message);
    }
}
