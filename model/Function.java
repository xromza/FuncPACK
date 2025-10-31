package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import exception.*;
import parsing.*;

/**
 * Math function class Uses a tokenizator to simplify the evaluations and using
 * within code
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

    public Function(ArrayList<String> functionTokens, Set<String> varNameSet, String name) throws FunctionFormatException {
        this.functionTokens = functionTokens;
        this.varNameSet = varNameSet;
        this.functionString = "";
        this.name = name;
        for (String s : functionTokens) {
            this.functionString += s;
        }
        for (String key : varNameSet) {
            if (key.equals(name)) {
                throw new FunctionFormatException("Название функции не может совпадать с названиями переменных");
            }
        }
    }

    public Function(String functionString, String name) throws FunctionFormatException {
        this.functionString = functionString;
        this.name = name;
        FunctionTokenizator.tokenize(functionString, functionTokens, varNameSet);
        for (String key : varNameSet) {
            if (key.equals(name)) {
                throw new FunctionFormatException("Название функции не может совпадать с названиями переменных");
            }
        }
    }

    @Override
    public String toString() {
        String s = name + "(";
        boolean flag = false;
        for (String temp : varNameSet) {
            if (!flag) {
                s += temp;
                flag = true;
            } else {
                s += ", " + temp;
            }
        }
        s += ") = " + this.functionString;
        return s;
    }

    public String toString(Map<String, Double> args, double value) {
        String s = name + "(";
        boolean flag = false;
        for (String temp : varNameSet) {
            if (!flag) {
                s += args.get(temp);
                flag = true;
            } else {
                s += ", " + args.get(temp);
            }
        }
        s += ") = " + value;
        return s;
    }

    @Override
    public String getFunctionString() {
        return this.functionString;
    }

    @Override
    public ArrayList<String> getFunctionTokens() {
        return new ArrayList<>(this.functionTokens);
    }

    @Override
    public Set<String> getVarNameSet() {
        return new HashSet<>(varNameSet);
    }

    public String tokensToString() {
        return "f(x) = " + functionTokens.toString();
    }

    public void setFunction(String s) throws FunctionFormatException {
        FunctionTokenizator.tokenize(s, functionTokens, varNameSet);
    }

    public String getName() {
        return name;
    }
}
