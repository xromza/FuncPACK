import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Map;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Map<String, Function> functions = new HashMap<>();
        int choose;
        while (true) { 
            clearScreen();
            System.out.print("Команды:\n1) Задать функцию\n2) Решить уравнение\n3) Получить значение функции\n4) Список заданных функций\n\n>>>");
            try {
                choose = Integer.parseInt(in.nextLine());
            } catch (NumberFormatException e) {
                continue;
            }
            switch (choose) {
                case (1) -> setFunctionWrapper(functions, in);
                case (2) -> equationSolverWrapper(functions, in);
                case (3) -> evaluateFuncWrapper(functions, in);
                case (4) -> printFunctions(functions);
                default -> {
                    System.out.println("\nНеизвестная команда. Попробуйте ещё раз");
                }
            }
            System.out.println("\nНажмите \"Enter\", чтобы продолжить...");
            new Scanner(System.in).nextLine();
        }
    }


    // mini-parser
    private static void equationSolverWrapper(Map<String,Function> functions, Scanner in) {
        clearScreen();
        System.out.println("Определённые функции:");
        printFunctions(functions);
        System.out.println("\nВведите уравнение >>> ");
        String input = in.nextLine().replaceAll(" ", "");
        String fString = "";
        String gString;
        SingleVar f;
        SingleVar g;
        int i;
        for (i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '=') {
                fString = input.substring(0,i);
                break;
            }
        }
        if (i == input.length()) {
            System.err.println("Не найден символ ="); 
            return;
        }
        gString = input.substring(i+1, input.length());
        
        if (fString.length() == 1 && functions.containsKey(fString)) {
            f = new SingleVar(functions.get(fString));
        } else {
            f = new SingleVar(fString, "anon");
        }
        if (gString.length() == 1 && functions.containsKey(gString)) {
            g = new SingleVar(functions.get(gString));
        } else {
            g = new SingleVar(gString, "anon");
        }

        Equation eq = new Equation(f,g);

        double a,b;
        System.out.println("Введите отрезок, в котором будем искать решения:");
        while (true) {
            try {
                System.out.print("\na >>> ");
                a = Double.parseDouble(in.nextLine());
                System.out.print("\nb >>> ");
                b = Double.parseDouble(in.nextLine());
            } catch (NumberFormatException e) {
                System.err.println("Неверный формат. Введите отрезок ещё раз");
                continue;
            }
            break;
        }
        System.out.println("Решения уравнения:\n");

        Set<Map<String, Double>> solutions = eq.solve(a,b);
        for (Map<String, Double> m : solutions) {
            System.out.print("\n(");
            for (String key : m.keySet()) {
                System.out.println(key + ": " + m.get(key)+ "; ");
            }
            System.out.print(")");
        }
    }

    private static void evaluateFuncWrapper(Map<String, Function> functions, Scanner in) {
        String choose;
        clearScreen();
        System.out.println("Значение какой функции вы хотите получить (буква)?\n");
        printFunctions(functions);
        System.out.print("\n\n >>> ");
        try {
            choose = in.nextLine();
        } catch (NumberFormatException e) {
            return;
        }
        if (!functions.containsKey(choose)) {
            return;
        }
        evaluateFunc(functions.get(choose), in);
    }
    private static void evaluateFunc(Function func, Scanner in) {
        Map<String, Double> arguments = new HashMap<>();
        for (String arg : func.getVarNameSet()) {
            System.out.printf("%s = ", arg);
            double value;
            try {
                value = Double.parseDouble(in.nextLine());
            } catch (NumberFormatException e) {
                return;
            }
            arguments.put(arg, value);
        }
        double result = func.evaluate(arguments);
        System.out.println(func.toString(arguments, result));
    }


    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void setFunctionWrapper(Map<String, Function> functions, Scanner in) {
        System.out.print("Выберите имя для функции >>> ");
        String name = in.nextLine();
        System.out.print(name+"= ");
        String functionString = in.nextLine();
        Set<String> varNameSet = new HashSet<>();
        ArrayList<String> functionTokens = new ArrayList<>();
        Function func = setFunction(functionString, functionTokens, varNameSet, name);
        functions.put(name, func);
    }

    private static Function setFunction(String functionString, ArrayList<String> functionTokens, Set<String> varNameSet, String name) {
        FunctionTokenizator.getVarNameSet(functionString, functionTokens, varNameSet);
        Function func;
        if (varNameSet.size() > 1) {
            func = new MultipleVar(functionTokens, varNameSet, name);
        }
        else {
            func = new SingleVar(functionTokens, varNameSet, name);
        }
        return func;
    }

    private static void printFunctions(Map<String, Function> functions) {
        if (functions.isEmpty()) {
            System.out.print("\nНе определена ни одна функция");
            return;
        }
        int i = 1;
        for (String key : functions.keySet()) {
            System.out.print("\n"+i+": " + functions.get(key).toString());
            i++;
        }
    }
}
