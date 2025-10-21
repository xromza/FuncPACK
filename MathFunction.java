import java.util.Set;
import java.util.Map;

public interface MathFunction {
    Set<String> getVariables();
    double evaluate(Map<String, Double> args);
}