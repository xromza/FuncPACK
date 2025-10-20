public class Main {
    public static void main(String[] args) {
        Function func = new SingleVar("{[2x + (3 - x^2)] * ([x] - 5)}^3 - 125(x + 1)(x - 1)");
        System.out.println(func.toString());
    }
}