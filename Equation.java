import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class Equation {
    private SingleVar f;
    private SingleVar g;
    private final double MIN_INTERVAL = 1e-6;
    private final double EPS = 1e-9;
    private final double step = 0.001;

    public Equation(SingleVar f) {
        this.f = f;
        g = new SingleVar("0"); // default: g = 0, so equation is f = 0
    }
    public Equation(SingleVar f, SingleVar g) {
        this.f = f;
        this.g = g;
    }
    @Override
    public String toString() {
        return f.toStringFunc() + " = " + g.toStringFunc();
    }

    public Set<Map<String, Double>> solve(double a, double b){
        Set<Map<String, Double>> points = new HashSet<>();
        double deltaA = f.evaluate(a) - g.evaluate(a);
        double deltaB = f.evaluate(b) - g.evaluate(b);

        if (Math.abs(deltaA) < EPS) {
            Map<String, Double> temp = new HashMap<>();
            double root = Math.ceil(a*1000)/1000;
            temp.put("x", root);
            temp.put("y", g.evaluate(root));
            points.add(temp);
        }
        if (Math.abs(deltaB) < EPS) {
            Map<String, Double> temp = new HashMap<>();
            double root = Math.ceil(b*1000)/1000;
            temp.put("x", root);
            temp.put("y", g.evaluate(root));
            points.add(temp);
        }
        double newA = a, newB = Math.min(newA+step,b);
        while (newB < b) {
            deltaA = f.evaluate(newA) - g.evaluate(newA);
            deltaB = f.evaluate(newB) - g.evaluate(newB);
            if (deltaA * deltaB < 0) {
                solveRecursive(points, newA, newB);
            }
            newA += step;
            newB += step;
        }

        return points;

    }

    private void solveRecursive(Set<Map<String, Double>> points, double a, double b) {

        double mid = (a + b) / 2;
        double deltaMid = f.evaluate(mid) - g.evaluate(mid);

        if (b-a < MIN_INTERVAL || Math.abs(deltaMid) < EPS) {
            Map<String, Double> temp = new HashMap<>();
            double root = Math.ceil((a+b)/2*1000)/1000;
            temp.put("x", root);
            temp.put("y", g.evaluate(root));
            points.add(temp);
        } 
        else {
            double deltaA = f.evaluate(a) - g.evaluate(a);
            if (deltaA * deltaMid < 0) {
                solveRecursive(points, a, mid);
            } else {
                solveRecursive(points, mid, b);
            }
        }

    }

    private boolean validate() {
        return f.getVarNameList().containsAll(g.getVarNameList()) && g.getVarNameList().containsAll(f.getVarNameList());
    }
}