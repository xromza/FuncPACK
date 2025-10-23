import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

public class MultipleVar extends Function {

    public MultipleVar(String s, String name) {
        super(s, name);
    }
    public MultipleVar(ArrayList<String> functionTokens, Set<String> varNameSet, String name) {
        super(functionTokens, varNameSet, name);
    }
    @Override
    public double evaluate(Map<String, Double> args) {
        try {
            Evaluator evaluator = new Evaluator(functionTokens, varNameSet);
            return evaluator.evaluate(args);
        } catch (EvaluationException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return 1;
    }

    @Override
    public double evaluate(double x) {
        Map<String, Double> temp = new HashMap<>();
        temp.put("x", x);
        return evaluate(temp);
    }

}
