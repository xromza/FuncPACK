import java.util.HashMap;
import java.util.Scanner;
import java.util.Map;
import java.util.Set;
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("f = ");
        SingleVar func = new SingleVar(in.nextLine());
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
    }
}