import java.util.ArrayList;
import java.util.List;

public class OrPhrase {
    private List<AndPhrase> andPhraseList = new ArrayList<>();
    private boolean phraseValue = false;

    public OrPhrase(String query) {
        String wordsInQuery[] = query.split(Operators.OR.name());
        for (int i = 0; i < wordsInQuery.length; i++) {
            andPhraseList.add(new AndPhrase(wordsInQuery[i].trim()));
        }
    }

    public boolean setPhraseValues(String line) {
        for (int i = 0; i < andPhraseList.size(); i++) {
            //One phrase get true so all the phrase is true.
            if (andPhraseList.get(i).setPhraseValues(line)) {
                phraseValue = true;
                return phraseValue;
            }
        }
        return evaluate();
    }


    public boolean evaluate() {
        boolean result = false;
        if (andPhraseList.size() > 0) {
            result = andPhraseList.get(0).getPhraseValue();
            if (result) {
                phraseValue = result;
                return result;
            }
            for (int i = 1; i < andPhraseList.size(); i++) {
                if (andPhraseList.get(i).getPhraseValue()) {
                    result = true;
                    break;
                }
            }
            if (phraseValue != result)
                phraseValue = result;
        }
        return result;
    }

}


