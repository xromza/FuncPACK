package model;

import java.util.Set;
import java.util.Map;
import java.util.ArrayList;
import exception.*;

public interface MathFunction {

    Set<String> getVarNameSet();

    String getFunctionString();

    ArrayList<String> getFunctionTokens();

    double evaluate(Map<String, Double> args) throws EvaluationException;

    double evaluate(double x) throws EvaluationException;
}
