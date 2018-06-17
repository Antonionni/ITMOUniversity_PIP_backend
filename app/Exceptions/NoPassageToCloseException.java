package Exceptions;

public class NoPassageToCloseException extends BusinessException {
    public NoPassageToCloseException() {
    }

    public NoPassageToCloseException(String message) {
        super(message);
    }
}
