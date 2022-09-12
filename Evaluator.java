public interface Evaluator {

    boolean isOperand(String token);

    String nextToken(String s, int start);

    double evaluate(String expressionString);

}
