package Exceptions;

public class UserHasAlreadyInPassageException extends BusinessException {
    public UserHasAlreadyInPassageException() {
    }

    public UserHasAlreadyInPassageException(String message) {
        super(message);
    }
}
