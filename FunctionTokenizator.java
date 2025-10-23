import java.util.EmptyStackException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Set;

public class FunctionTokenizator {

    public static void functionTokenizator(String functionString, ArrayList<String> functionTokens,
                                      Set<String> varNameSet) throws FunctionFormatException {
        functionString = functionString.replaceAll(" ", "");
        if (!validateBrackets(functionString)) {
            throw new FunctionFormatException("неправильная расстановка скобок");
        }
        functionString = functionString.replace('[', '(').replace(']', ')').replace('{', '(').replace('}', ')');
        String buffer = "";
        int i = 0;
        while (i < functionString.length()) {
            char c = functionString.charAt(i);
            if (isNumber(c)) {
                int j = 1;
                for (; j < functionString.length() - i && isNumber(functionString.substring(i, i + j + 1)); j++) {
                }
                if (!functionTokens.isEmpty() && (isNumber(functionTokens.getLast()) || functionString.charAt(i-1) == ')')) {
                    functionTokens.add("*");
                }
                buffer = functionString.substring(i, i + j);
                i += j;

                functionTokens.add(buffer);
                buffer = "";
            } else if (isVar(c)) {
                if (!varNameSet.contains(""+c)) {
                    varNameSet.add(""+c);
                }
                if (!functionTokens.isEmpty() && (isNumber(functionTokens.getLast()) || functionString.charAt(i-1) == ')' || isVar(functionTokens.getLast()))) {
                    functionTokens.add("*");
                }
                buffer += c;
                functionTokens.add(buffer);
                buffer = "";
                i++;
            } else if (isOperator(c)) {
                if (c == '^') {
                    if (!isNumber(functionTokens.getLast()) && !isVar(functionTokens.getLast()) && functionString.charAt(i-1) != ')') {
                        throw new FunctionFormatException("неожиданная \"^\"");
                    }
                }
                buffer += c;
                functionTokens.add(buffer);
                buffer = "";
                i++;
            } else if (isBrackets(c)) {
                if (c == '(' && !functionTokens.isEmpty() && (isNumber(functionTokens.getLast()) || functionTokens.getLast().equals(")") || isVar(functionTokens.getLast()))) {
                    functionTokens.add("*");
                }
                buffer += c;
                functionTokens.add(buffer);
                buffer = "";
                i++;
                
            }
            else {
                i++;
            }
        }
    }
    public static boolean validateBrackets(String s) {
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            try {
                switch (s.charAt(i)) {
                    case '(', '[', '{' -> stack.push(s.charAt(i));
                    case ')' -> {
                        if (stack.pop() != '(') {
                            return false;
                        }
                    }
                    case ']' -> {
                        if (stack.pop() != '[') {
                            return false;
                        }
                    }
                    case '}' -> {
                        if (stack.pop() != '{') {
                            return false;
                        }
                    }
                }
            } catch (EmptyStackException e) {
                return false;
            }
        }
        return stack.isEmpty();
    }
    public static void getVarNameSet(String s, ArrayList<String> functionTokens, Set<String> varNameSet) {
        try {
        functionTokenizator(s, functionTokens, varNameSet);
        } catch (FunctionFormatException e) {
            System.err.println(e.getMessage());
        }
    }
    private static boolean isNumber(String s) {
        return s.matches("-?\\d+(\\.\\d+)?"); 
    }
    private static boolean isNumber(char c) {
        String temp = "" + c;
        return isNumber(temp);
    }
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }
    private static boolean isBrackets(char c) {
        return c == '(' || c == '[' || c == '{' || c == ')' || c == ']' || c == '}'; 
    }
    private static boolean isVar(String s) {
        return isVar(s.charAt(0));
    }
    private static boolean isVar(char c) {
        return c >= 'a' && c <= 'z';
    }

}