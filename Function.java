import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;


/**
 * <h1>Абстрактный класс "Функция"</h1>
 * <h3>Реализует интерфейс "Математическая функция"</h3>
 * <p>Представляет собой 
 * <p>
 * <h3>Аргументы</h3>
 * <ul>
 *  <li> String s - Задаёт функцию из строки</li>
 * </ul>
 * <p>
 * <h3>Поддерживает:</h3>
 * <ul>
 *  <li>Решение уравнения относительно одной переменной (реализован методом дихотомии)</li>
 * </ul>
 * Пример использования:
 * <pre>{@code
 * Function f = new Function("x+1");
 * Function g = new Function("x^2-2x");
 * Equation eq = new Equation(f,g);
 * double result = eq.solve();
 * }</pre>
 *
 * @author xromza
 */
public abstract class Function implements MathFunction {

    protected String functionString;
    protected String name;
    protected ArrayList<String> functionTokens = new ArrayList<>();
    protected Set<String> varNameSet = new HashSet<>();

    public Function(Function f) {
        this.functionString = f.getFunctionString();
        this.name = f.getName();
        this.functionTokens = f.getFunctionTokens();
        this.varNameSet = f.getVarNameSet();
    }

    public Function(ArrayList<String> functionTokens, Set<String> varNameSet, String name) {
        this.functionTokens = functionTokens;
        this.varNameSet = varNameSet;
        this.functionString = "";
        this.name = name;
        for (String s : functionTokens) {
            this.functionString += s;
        }
    }


    public Function(String functionString, String name) {
        this.functionString = functionString;
        this.name = name;
        try {
            FunctionTokenizator.functionTokenizator(functionString, functionTokens, varNameSet);
        } catch (FunctionFormatException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public String toString() {
        String s = name+"(";
        boolean flag = false;
        for (String temp : varNameSet) {
            if (!flag) {
                s += temp;
                flag = true;
            } else {
                s += ", " + temp;
            }
        }
        s +=") = " + this.functionString;
        return s;
    }

    public String toString(Map<String, Double> args, double value) {
                String s = name+"(";
        boolean flag = false;
        for (String temp : varNameSet) {
            if (!flag) {
                s += args.get(temp);
                flag = true;
            } else {
                s += ", " + args.get(temp);
            }
        }
        s +=") = " + value;
        return s;
    }

    @Override
    public String getFunctionString() {
        return this.functionString;
    }

    @Override
    public Set<String> getVarNameSet() {
        return varNameSet;
    }

    public String tokensToString() {
        return "f(x) = " + functionTokens.toString();
    }

    public void setFunction(String s) {
        try {
            FunctionTokenizator.functionTokenizator(s, functionTokens, varNameSet);
        } catch (FunctionFormatException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getFunctionTokens() {
        return functionTokens;
    }
}
