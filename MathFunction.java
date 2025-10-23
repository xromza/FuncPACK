import java.util.Set;
import java.util.Map;

public interface MathFunction {
    Set<String> getVarNameSet();
    String getFunctionString();
    double evaluate(Map<String, Double> args);
    double evaluate(double x);
}