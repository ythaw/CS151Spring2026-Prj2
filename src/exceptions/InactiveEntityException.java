package exceptions;

public class InactiveEntityException extends Exception {
    public InactiveEntityException(String message) {
        super(message);
    }
}