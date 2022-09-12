import java.util.Stack;

public class ToPostfixConverter implements Converter{

    /**
     * The fundamental method of any class implementing this interface. It converts the given
     * arithmetic expression to another type, depending on the implementation.
     *
     * @param expression the given arithmetic expression
     */

    public String associative_property(String expression){
        String result_expression = new String();
        char last_symbol = '&';
        for(char c: expression.toCharArray()){
            if(c == Operator.LEFT_PARENTHESIS.getSymbol()) {
                int result_length = result_expression.length();

                if (result_length <= 0) {
                    result_expression += c;
                    continue;
                }
                else if(isOperand(result_expression.substring(result_length - 1, result_length))){
                    result_expression += Operator.MULTIPLICATION.getSymbol();
                    result_expression += c;
                }
                else
                    result_expression += c;
            }else if(isOperand(String.valueOf(c))){
                int result_length = result_expression.length();

                if (result_length <= 0) {
                    result_expression += c;
                    continue;
                }
                else if(last_symbol == Operator.RIGHT_PARENTHESIS.getSymbol()){
                    result_expression += Operator.MULTIPLICATION.getSymbol();
                    result_expression += c;
                }
                else
                    result_expression += c;
            }
            else
                result_expression += c;

            last_symbol = c;
        }
        return result_expression;
    }

    @Override
    public String convert(ArithmeticExpression expression) {
        Stack<Operator> stack = new Stack<Operator>();
        String string = associative_property(expression.getExpression());
        String output = new String();

        System.out.println(string);

        for(int i = 0; i < string.length(); i++){
            String token = nextToken(string, i);
            if(isOperand(token)) {
                output += token + " ";
                i += token.length() - 1;
                continue;
            }
            Operator operator = Operator.of(token);
            if(operator.getRank() == 3){
                if(operator.equals(Operator.LEFT_PARENTHESIS)){
                    stack.push(operator);
                }
                else if(operator.equals(Operator.RIGHT_PARENTHESIS)){
                    while(!stack.peek().equals(Operator.LEFT_PARENTHESIS)){
                        output += stack.pop().getSymbol() + " ";
                    }
                    //remove the left parenthesis
                    stack.pop();
                }
            }
            //if operator/not parenthesis
            else{
                if(stack.isEmpty() || stack.peek().equals(Operator.LEFT_PARENTHESIS)){
                    stack.push(operator);
                }
                else if(operator.getRank() < stack.peek().getRank()){
                    stack.push(operator);
                }
                else if(operator.getRank() == stack.peek().getRank()){
                    output += stack.pop().getSymbol() + " ";
                    stack.push(operator);
                }
                else if(operator.getRank() > stack.peek().getRank()){
                    output += stack.pop().getSymbol() + " ";
                    //re-test the input operator against the new top element;
                    --i;
                    continue;
                }
            }
        }
        while(!stack.isEmpty())
            output += stack.pop().getSymbol() + " ";
        return output;
    }

    /**
     * Given a string and a specific index, this method returns the next token starting at that index.
     *
     * @param s     the given string
     * @param start the given index
     * @return the next token starting at the given index in the given string
     */
    @Override
    public String nextToken(String s, int start) {
        Converter.TokenBuilder result = new Converter.TokenBuilder();
        if(Operator.isOperator(s.charAt(start)) || s.charAt(start) == Operator.LEFT_PARENTHESIS.getSymbol() || s.charAt(start) == Operator.RIGHT_PARENTHESIS.getSymbol()){
            result.append(s.charAt(start));
        }else{
            //Check for parenthesis
            while(!Operator.isOperator(s.charAt(start)) && s.charAt(start) != ' ' && (s.charAt(start) != Operator.LEFT_PARENTHESIS.getSymbol() && s.charAt(start) != Operator.RIGHT_PARENTHESIS.getSymbol())) {
                result.append(s.charAt(start));
                start++;
                if (start >= s.length()) {
                    break;
                }
            }

        }
        return result.build();
    }

    /**
     * Determines whether or not a string is a valid operand.
     *
     * @param s the given string
     * @return <code>true</code> if the given string is a valid operand, and <code>false</code> otherwise
     */
    @Override
    public boolean isOperand(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
