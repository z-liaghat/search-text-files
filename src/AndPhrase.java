import java.util.ArrayList;
import java.util.List;

public class AndPhrase {

    private List<AndExpression> phrase = new ArrayList<>();
    private boolean phraseValue = false;

    public AndPhrase(String andQuery) {
        String wordsInQuery[] = andQuery.split(Operators.AND.name());
        for (int i = 0; i < wordsInQuery.length; i++) {
            phrase.add(new AndExpression(wordsInQuery[i].toLowerCase().trim(), false));
        }
    }

    public boolean setPhraseValues(String line) {
        for (int i = 0; i < phrase.size(); i++) {
            if (line.contains(phrase.get(i).getExpression()))
                phrase.get(i).setValue(true);
        }
        return evaluate();
    }

    public boolean evaluate() {
        if (phrase.size() <= 0) {
            return false;
        }
        boolean result = phrase.get(0).getValue();
        //The first element in the phrase is false so the and phrase is false
        if(!result) {
            phraseValue = result;
            return result;
        }
        //Check all elements in the list are true or not
        for (int i = 1; i < phrase.size(); i++) {
            if (phrase.get(i).getValue() == false) {
                result = false;
                break;
            }
        }
        //Check if the result changes to true or not, if it get true change the phraseValue to true
        if (phraseValue != result)
            phraseValue = result;
        return result;
    }

    public boolean getPhraseValue() {
        return phraseValue;
    }
}
