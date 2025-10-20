
import java.util.EmptyStackException;
import java.util.ArrayList;
import java.util.Stack;

public abstract class Function {

    protected ArrayList<String> function = new ArrayList<>();

    public Function(String s) {
        try {
            functionParser(s);
        } catch (FunctionFormatException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private void functionParser(String s) throws FunctionFormatException {
        s = s.replaceAll(" ", "");
        if (!validateBrackets(s)) {
            throw new FunctionFormatException("неправильная расстановка скобок");
        }
        s = s.replace('[', '(').replace(']', ')').replace('{', '(').replace('}', ')');
        String buffer = "";
        int i = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            if (isNumber(c)) {
                int j = 1;
                for (; j < s.length() - i && isNumber(s.substring(i, i + j + 1)); j++) {
                }
                if (!function.isEmpty() && (isNumber(function.getLast()) || s.charAt(i-1) == ')')) {
                    function.add("*");
                }
                buffer = s.substring(i, i + j);
                i += j;

                function.add(buffer);
                buffer = "";
            } else if (isVar(s.charAt(i))) {
                if (!function.isEmpty() && (isNumber(function.getLast()) || s.charAt(i-1) == ')')) {
                    function.add("*");
                }
                buffer += c;
                function.add(buffer);
                buffer = "";
                i++;
            } else if (isOperator(c)) {
                if (c == '^') {
                    if (!isNumber(function.getLast()) && !isVar(function.getLast()) && s.charAt(i-1) != ')') {
                        throw new FunctionFormatException("неожиданная \"^\"");
                    }
                }
                buffer += c;
                function.add(buffer);
                buffer = "";
                i++;
            } else if (isBrackets(c)) {
                if (c == '(' && !function.isEmpty() && (isNumber(function.getLast()) || function.getLast().equals(")") || isVar(function.getLast()))) {
                    function.add("*");
                }
                buffer += c;
                function.add(buffer);
                buffer = "";
                i++;
                
            }
            else {
                i++;
            }
        }
    }
    @Override
    public String toString() {
        return "f(x) = " + function.toString();
    }

    public void setFunction(String s) {
        try {
            functionParser(s);
        } catch (FunctionFormatException e) {
            System.out.println(e.getMessage());
            System.exit(1);
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
    private boolean isNumber(String s) {
            try {
                Double.parseDouble(s);
            } catch (NumberFormatException e) {
                return false;
            }
            return true;
    }
    private boolean isNumber(char c) {
        String temp = "" + c;
        return isNumber(temp);
    }
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }
    private boolean isBrackets(String s) {
        return isBrackets(s.charAt(0));
    }
    private boolean isBrackets(char c) {
        return c == '(' || c == '[' || c == '{' || c == ')' || c == ']' || c == '}'; 
    }
    private boolean isVar(String s) {
        return isVar(s.charAt(0));
    }
    private boolean isVar(char c) {
        return c >= 'a' && c <= 'z';
    }
}
