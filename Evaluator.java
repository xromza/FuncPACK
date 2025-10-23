
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;

public class Evaluator {
    private int pos = 0;
    private final ArrayList<String> functionTokens;
    private final Set<String> varNameSet;

    public Evaluator(ArrayList<String> functionTokens, Set<String> varNameSet) {
        this.functionTokens = functionTokens;
        this.varNameSet = varNameSet;
    }

    public double evaluate(double arg) throws EvaluationException {
        Map<String, Double> temp = new HashMap<>();
        temp.put("x", arg);
        return evaluate(temp);
    }

    public double evaluate(Map<String, Double> args) throws EvaluationException {
        if (!args.keySet().containsAll(varNameSet)) {
            Set<String> temp = varNameSet;
            temp.removeAll(args.keySet());
            throw new EvaluationException("Недостаточно аргументов" + temp.toString() + ". Ожидалось: " + varNameSet.toString() + ". Получено: " + args.keySet().toString());
        }
        double result = parseExpression(args);

        if (pos != functionTokens.size()) {
            throw new EvaluationException("Лишние токены");
        }
        return result;
    }

    private double parseExpression(Map<String, Double> args) {
        double left = parseTerm(args);
        while (pos < functionTokens.size()) {
            String op = functionTokens.get(pos);
            if ("+".equals(op) || "-".equals(op)) {
                pos++;
                double right = parseTerm(args);
                if ("+".equals(op)) {
                    left += right; 
                }else {
                    left -= right;
                }
            } else {
                break;
            }
        }
        return left;
    }

    private double parseTerm(Map<String, Double> args) {
        double left = parseFactor(args);
        while (pos < functionTokens.size()) {
            String op = functionTokens.get(pos);
            if ("*".equals(op) || "/".equals(op)) {
                pos++;
                double right = parseFactor(args);
                if ("*".equals(op)) {
                    left *= right;
                } else {
                    left /= right;
                }
            } else {
                break;
            }
        }
        return left;
    }

    private double parseFactor(Map<String, Double> args) {
        double left = parseUnary(args);
        if (pos < functionTokens.size() && "^".equals(functionTokens.get(pos))) {
            pos++;
            double right = parseFactor(args);
            return Math.pow(left, right);
        }
        return left;
    }

    private double parseUnary(Map<String, Double> args) {
        if (pos < functionTokens.size() && "-".equals(functionTokens.get(pos))) {
            pos++;
            return -parseUnary(args);
        }
        if (pos < functionTokens.size() && "+".equals(functionTokens.get(pos))) {
            pos++;
            return parseUnary(args);
        }
        try {
            return parsePrimary(args);
        } catch (EvaluationException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return 1;
    }

    private double parsePrimary(Map<String, Double> args) throws EvaluationException {
        if (pos >= functionTokens.size()) {
            throw new EvaluationException("Неожиданный конец");
        }
        String token = functionTokens.get(pos++);
        if ("(".equals(token)) {
            double result = parseExpression(args);
            if (pos >= functionTokens.size() || !")".equals(functionTokens.get(pos))) {
                throw new EvaluationException("ожидается \")\"");
            }
            pos++;
            return result;
        } else if (token.matches("-?\\d+(\\.\\d+)?")) {
            return Double.parseDouble(token);
        } else if (args.keySet().contains(token)) {
            return args.get(token);
        } else {
            throw new EvaluationException("Неизвестный токен");
        }
    }
}
