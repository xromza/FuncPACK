
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Map;
import java.util.Set;

import model.*;
import parsing.*;
import equation.*;
import exception.*;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Map<String, Function> functions = new HashMap<>();
        int choose;
        while (true) {
            clearScreen();
            System.out.print("Команды:\n1) Задать функцию\n2) Решить уравнение\n3) Получить значение функции\n4) Список заданных функций\n5) Забыть функцию\n6) Выход\n\n>>> ");
            try {
                choose = Integer.parseInt(in.nextLine());
            } catch (NumberFormatException e) {
                continue;
            }
            switch (choose) {
                case (1) ->
                    defineFunction(functions, in);
                case (2) ->
                    solveEquation(functions, in);
                case (3) ->
                    evaluateFuncWrapper(functions, in);
                case (4) ->
                    printFunctions(functions);
                case (5) ->
                    deleteFunction(functions, in);
                case (6) -> {
                    System.out.println("Пока!");
                    System.exit(0);
                }
                default -> {
                    System.out.println("\nНеизвестная команда. Попробуйте ещё раз");
                }
            }
            System.out.println("\nНажмите \"Enter\", чтобы продолжить...");
            in.nextLine();
        }
    }

    // mini-parser
    private static void solveEquation(Map<String, Function> functions, Scanner in) {
        clearScreen();
        System.out.println("Определённые функции:");
        printFunctions(functions);
        System.out.println("\nВведите уравнение >>> ");
        String input = in.nextLine().replaceAll(" ", "");
        String fString = "";
        String gString;
        SingleVar f;
        SingleVar g;
        Set<Double> solutions;
        int i;
        for (i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '=') {
                fString = input.substring(0, i);
                break;
            }
        }
        if (i == input.length()) {
            System.err.println("Не найден символ =");
            return;
        }
        gString = input.substring(i + 1, input.length());

        if (fString.isBlank() || gString.isBlank()) {
            System.err.println("Одна из частей уравнения пустая!");
            return;
        }

        if (functions.containsKey(fString)) {
            f = new SingleVar(functions.get(fString));
        } else {
            try {
                f = new SingleVar(fString, "anon");
            } catch (FunctionFormatException e) {
                System.err.println(e.getMessage());
                return;
            }
        }
        if (functions.containsKey(gString)) {
            g = new SingleVar(functions.get(gString));
        } else {
            try {
                g = new SingleVar(gString, "anon");
            } catch (FunctionFormatException e) {
                System.err.println(e.getMessage());
                return;
            }
        }
        Set<String> allVars = f.getVarNameSet();
        allVars.addAll(g.getVarNameSet());
        for (String key : allVars) {
            if (key.equals(fString) || key.equals(gString)) {
                System.out.println("Переменная не может иметь такое же название, как функция");
                return;
            }
        }
        Equation eq = new Equation(f, g);
        double a, b;
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
        try {
            solutions = eq.solve(a, b);
        } catch (EvaluationException e) {
            System.err.println(e.getMessage());
            return;
        }
        for (double m : solutions) {
            try {
                System.out.printf("%n(x: %.6f; y: %.6f; )%n", m, f.evaluate(m));
            } catch (EvaluationException e) {
                System.err.printf("Ошибка вычисления первой функции в точке " + m);
                return;
            }
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
        double result;
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
        try {
            result = func.evaluate(arguments);
        } catch (EvaluationException e) {
            System.err.println(e.getMessage());
            return;
        }
        System.out.println(func.toString(arguments, result));
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void defineFunction(Map<String, Function> functions, Scanner in) {
        System.out.print("Выберите имя для функции >>> ");
        String name = in.nextLine();
        if (!name.matches("[a-zA-Z][a-zA-Z0-9]*")) {
            System.out.println("Неверно введено имя функции.");
            return;
        }
        System.out.print(name + " = ");
        String functionString = in.nextLine();
        Set<String> varNameSet = new HashSet<>();
        ArrayList<String> functionTokens = new ArrayList<>();
        Function func;
        try {
            func = setFunction(functionString, functionTokens, varNameSet, name);
        } catch (FunctionFormatException e) {
            System.out.println(e.getMessage());
            return;
        }
        functions.put(name, func);
    }

    private static void deleteFunction(Map<String, Function> functions, Scanner in) {
        clearScreen();
        System.out.println("Список определённых функций:");
        printFunctions(functions);
        System.out.print("Какую функцию вы хотите удалить (имя функции)?\n>>> ");
        String name = in.nextLine();
        if (!functions.containsKey(name)) {
            System.out.println("Функция с названием \"" + name + "\" не определена");
            return;
        }
        functions.remove(name);
        System.out.println("Функция с названием \"" + name + "\" удалена");
    }

    private static Function setFunction(String functionString, ArrayList<String> functionTokens, Set<String> varNameSet, String name) throws FunctionFormatException {
        if (functionString.isBlank()) {
            throw new FunctionFormatException("пустая функция");
        }
        FunctionTokenizator.getVarNameSet(functionString, functionTokens, varNameSet);
        Function func;
        if (varNameSet.size() > 1) {
            func = new MultipleVar(functionTokens, varNameSet, name);
        } else {
            func = new SingleVar(functionTokens, varNameSet, name);
        }
        return func;
    }

    private static void printFunctions(Map<String, Function> functions) {
        if (functions.isEmpty()) {
            System.out.println("Не определена ни одна функция");
            return;
        }
        int i = 1;
        for (Map.Entry<String, Function> entry : functions.entrySet()) {
            System.out.printf("%n%d: %s", i++, entry.getValue());
        }
    }
}
