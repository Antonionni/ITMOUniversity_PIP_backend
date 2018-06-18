package Exceptions;

public class NoCurrentPassageException extends BusinessException {
    public NoCurrentPassageException() {
    }

    public NoCurrentPassageException(String message) {
        super(message);
    }
}
