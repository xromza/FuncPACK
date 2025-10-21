import java.util.Map;
import java.util.Set;

public class MultipleVar extends Function {
    // function = ArrayList<String> 
    public MultipleVar(String s) {
        super(s);
    }
    public double evaluate(Map<String, Double> args) {
        try {
            Evaluation evaluator = new Evaluation();
            return evaluator.evaluate(args);
        } catch (EvaluateError e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return 1;
    }

    private class Evaluation {
        int pos = 0;
        public double evaluate(Map<String, Double> args) throws EvaluateError {
            if (!args.keySet().containsAll(varNameList)) {
                Set<String> temp = varNameList;
                temp.removeAll(args.keySet());
                throw new EvaluateError("Недостаточно аргументов" + temp.toString() + ". Ожидалось: " + varNameList.toString() + ". Получено: " + args.keySet().toString());
            }
            double result = parseExpression(args);
            
            if (pos != function.size()) {
                throw new EvaluateError("Лишние токены");
            }
            return result;
        }
        private double parseExpression(Map<String, Double> args) {
            double left = parseTerm(args);
            while (pos < function.size()) {
                String op = function.get(pos);
                if ("+".equals(op) || "-".equals(op)) {
                    pos++;
                    double right = parseTerm(args);
                    if ("+".equals(op)) left += right;
                    else left -= right;
                } else {
                    break;
                }
            }
            return left;
        }
        private double parseTerm(Map<String, Double> args) {
            double left = parseFactor(args);
            while (pos < function.size()) {
                String op = function.get(pos);
                if ("*".equals(op) || "/".equals(op)) {
                    pos++;
                    double right = parseFactor(args);
                    if ("*".equals(op)) {
                        left *= right;
                    }
                    else left /= right;
                }
                else {
                    break;
                }
            }
            return left;
        }
        private double parseFactor(Map<String, Double> args) {
            double left = parsePower(args);
            if (pos < function.size() && "^".equals(function.get(pos))) {
                pos++;
                double right = parseFactor(args);
                return Math.pow(left,right);
            }
            return left;
        }
        private double parsePower(Map<String, Double> args) {
            try {
                return parsePrimary(args);
            } catch (EvaluateError e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
            return 1;
        }
        private double parsePrimary(Map<String, Double> args) throws EvaluateError {
            if (pos >= function.size()) {
                throw new EvaluateError("Неожиданный конец");
            }
            String token = function.get(pos++);
            if ("(".equals(token)) {
                double result = parseExpression(args);
                if (pos >= function.size() || !")".equals(function.get(pos))) {
                    throw new EvaluateError("ожидается \")\"");
                }
                pos++;
                return result;
            }
            else if (token.matches("-?\\d+(\\.\\d+)?")) {
                return Double.parseDouble(token);
            }
            else if (args.keySet().contains(token)) {
                return args.get(token);
            }
            else {
                throw new EvaluateError("Неизвестный токен");
            }
        }
    }
}