package model;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import exception.*;
import evaluation.*;

/**
 * Represents a single-variable math function Can be evaluated in point and used
 * in equations
 *
 * @author xromza
 */
public class SingleVar extends Function {

    public SingleVar(String s, String name) throws FunctionFormatException {
        super(s, name);
    }

    public SingleVar(Function f) {
        super(f);
        if (f.getVarNameSet().size() > 1) {
            System.err.println("Преобразование невозможно. Переменных больше, чем 1");
        }
    }

    public SingleVar(ArrayList<String> functionTokens, Set<String> varNameSet, String name) throws FunctionFormatException {
        super(functionTokens, varNameSet, name);
    }

    @Override
    public double evaluate(Map<String, Double> args) throws EvaluationException {
        Evaluator evaluator = new Evaluator(functionTokens, varNameSet);
        return evaluator.evaluate(args);
    }

    @Override
    public double evaluate(double arg) throws EvaluationException {
        Map<String, Double> args = Map.of("x", arg);
        return evaluate(args);
    }

}
