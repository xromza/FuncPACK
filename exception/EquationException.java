package exception;

public class EquationException extends Exception {

    public EquationException(String message) {
        super("Ошибка вычисления: " + message);
    }
}
