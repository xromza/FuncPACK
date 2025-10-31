package model;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import exception.*;
import evaluation.*;

/**
 * Represents a multiple-variable math function Can be evaluated in point and
 * used in equations
 *
 * @author xromza
 */
public class MultipleVar extends Function {

    public MultipleVar(String functionString, String name) throws FunctionFormatException {
        super(functionString, name);
    }

    public MultipleVar(ArrayList<String> functionTokens, Set<String> varNameSet, String name) throws FunctionFormatException {
        super(functionTokens, varNameSet, name);
    }

    @Override
    public double evaluate(Map<String, Double> args) throws EvaluationException {
        Evaluator evaluator = new Evaluator(functionTokens, varNameSet);
        return evaluator.evaluate(args);
    }

    @Override
    public double evaluate(double x) throws EvaluationException {
        Map<String, Double> temp = new HashMap<>();
        temp.put("x", x);
        return evaluate(temp);
    }

}
