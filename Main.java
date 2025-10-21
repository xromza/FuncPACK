import java.util.HashMap;
import java.util.Scanner;
import java.util.Map;
import java.util.Set;
public class Main {
    public static void main(String[] args) {
        SingleVar f = new SingleVar("x^3+2x^2-1");
        SingleVar g = new SingleVar("x");
        Equation eq = new Equation(f, g);
        System.out.println(eq.toString());
        Set<Map<String,Double>> roots = eq.solve(-100, 100);
        System.out.println("Корни: ");
        System.out.println(roots.toString());
    }


    /*
    public static void main1(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("f = ");
        MultipleVar func = new MultipleVar(in.nextLine());
        System.out.println(func.toString());
        Set<String> varNames = func.getVarNameList();
        while (true) {
            Map<String, Double> arguments = new HashMap<>();
            String varNamesString = "";
            boolean flag = false;
            for (String c : varNames) {
                System.out.printf("\n%s = ", c);
                double arg = Double.parseDouble(in.nextLine());
                arguments.put(c, arg);
                if (!flag) {
                    varNamesString += c;
                    flag = true;
                }
                else varNamesString += ", "+c;
            }

            System.out.printf("f("+varNamesString+") = %.1f", func.evaluate(arguments));
        }
    }*/
}