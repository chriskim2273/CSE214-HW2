import java.util.Stack;

public class PostfixEvaluator implements Evaluator{

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

    @Override
    public boolean isOperand(String s) {
        for(char c: s.toCharArray()){
            try{
                Operator operator = Operator.of(c);
                return false;
            }catch(IllegalArgumentException iae){};
        }
        return true;
    }

    @Override
    public double evaluate(String expressionString) {
        Stack<String> stack = new Stack<String>();
        for(int i = 0; i < expressionString.length(); i++){

            String token = nextToken(expressionString, i);
            if(isOperand(token)){
                stack.push(token);
                i += token.length();
            }else if(Operator.isOperator(token)){
                i++;
                String one = stack.pop();
                String two = stack.pop();
                double element_one = Double.parseDouble(one);
                double element_two = Double.parseDouble(two);
                Operator operator = Operator.of(token);

                if(operator.equals(Operator.MULTIPLICATION)){
                    stack.push(Double.toString(element_two*element_one));
                }
                else if(operator.equals(Operator.DIVISION)){
                    if(element_one == 0.0)
                        throw new RuntimeException();
                    stack.push(Double.toString(element_two/element_one));
                }
                else if(operator.equals(Operator.ADDITION)){
                    stack.push(Double.toString(element_two+element_one));
                }
                else if(operator.equals(Operator.SUBTRACTION)){
                    stack.push(Double.toString(element_two-element_one));
                }
            }
        }
        return Double.parseDouble(stack.pop());
    }
}
