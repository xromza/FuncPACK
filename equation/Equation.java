package equation;

import java.util.Set;
import java.util.LinkedHashSet;
import model.*;
import exception.*;

/**
 * Represents a mathematical equation f(x) = g(x). Solves the equation using a
 * scanning + bisection method.
 *
 * @author xromza
 */
public class Equation {

    private final SingleVar f;
    private final SingleVar g;
    private final double MIN_INTERVAL = 1e-9;
    private final double EPS = 1e-9;
    private final double step = 0.01;

    public Equation(SingleVar f) throws FunctionFormatException {
        this.f = f;
        g = new SingleVar("0", "g");
    }

    public Equation(SingleVar f, SingleVar g) {
        this.f = f;
        this.g = g;
    }

    @Override
    public String toString() {
        return f.getFunctionString() + " = " + g.getFunctionString();
    }

    public Set<Double> solve(double a, double b) throws EvaluationException {
        Set<Double> roots = new LinkedHashSet<>();

        double current = a;
        while (current < b) {
            double next = Math.min(current + step, b);
            double deltaA = f.evaluate(current) - g.evaluate(current);
            double deltaB = f.evaluate(next) - g.evaluate(next);
            if (Math.abs(deltaA) < EPS) {
                addRoot(roots, current);
            }
            if (Math.abs(deltaB) < EPS) {
                addRoot(roots, next);
            }
            if (deltaA * deltaB < 0) {
                solveRecursive(roots, current, next);
            }
            current = next;
        }
        return roots;
    }

    private void addRoot(Set<Double> roots, double x) throws EvaluationException {
        double rounded = Math.round(x * 1_000_000) / 1_000_000.0;
        roots.add(rounded);
    }

    private void solveRecursive(Set<Double> roots, double a, double b) throws EvaluationException {

        double mid = (a + b) / 2;
        double deltaMid = f.evaluate(mid) - g.evaluate(mid);

        if (b - a < MIN_INTERVAL || Math.abs(deltaMid) < EPS) {
            double root = (a + b) / 2;
            addRoot(roots, root);
        } else {
            double deltaA = f.evaluate(a) - g.evaluate(a);
            if (deltaA * deltaMid < 0) {
                solveRecursive(roots, a, mid);
            } else {
                solveRecursive(roots, mid, b);
            }
        }

    }
}
