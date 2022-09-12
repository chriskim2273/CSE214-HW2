import java.util.EmptyStackException;
import java.util.Stack;

/**
 * @author Ritwik Banerjee
 */
public class DyckWord {
    
    private final String word;
    
    public DyckWord(String word) {
        if (isDyckWord(word))
            this.word = word;
        else
            throw new IllegalArgumentException(String.format("%s is not a valid Dyck word.", word));
    }
    /*
    private static boolean isDyckWord(String word) {
        int open_count = 0;
        int close_count = 0;
        for(char c: word.toCharArray()){
            if(c == Operator.LEFT_PARENTHESIS.getSymbol())
                open_count++;
            else if(c == Operator.RIGHT_PARENTHESIS.getSymbol())
                close_count++;
        }
        if(open_count == close_count)
            return true;
        return false;
    }
    */
    private static boolean isDyckWord(String word) {
        Stack<Character> parenthesis = new Stack<Character>();

        for(char c: word.toCharArray()){
            if(c == Operator.LEFT_PARENTHESIS.getSymbol())
                parenthesis.push(c);
            else if(c == Operator.RIGHT_PARENTHESIS.getSymbol()){
                if(parenthesis.isEmpty())
                    return false;
                else
                    parenthesis.pop();
            }
        }
        if(parenthesis.isEmpty())
            return true;
        return false;
    }

    public String getWord() {
        return word;
    }
    
}
