package exception;

public class FunctionFormatException extends Exception {

    public FunctionFormatException(String message) {
        super("Функция введена неверно: " + message);
    }
}
