package Exceptions;

public class WrongAmountOfAnswersException extends BusinessException {
    public WrongAmountOfAnswersException() {
    }

    public WrongAmountOfAnswersException(String message) {
        super(message);
    }
}
