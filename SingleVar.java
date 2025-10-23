import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class SingleVar extends Function {

    public SingleVar(String s, String name) {
        super(s, name);
    }
    public SingleVar(Function f) {
        super(f);
        if (f.getVarNameSet().size() > 1) {
            System.err.println("Преобразование невозможно. Переменных больше, чем 1");
        }
    }
    public SingleVar(ArrayList<String> functionTokens, Set<String> varNameSet, String name) {
        super(functionTokens, varNameSet, name);
    }
    @Override
    public double evaluate(Map<String, Double> args) {
        return evaluate(args.get("x"));
    }

    @Override
    public double evaluate(double arg) {
        try {
            Evaluator evaluator = new Evaluator(functionTokens, varNameSet);
            return evaluator.evaluate(arg);
        } catch (EvaluationException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return 1;
    }

}