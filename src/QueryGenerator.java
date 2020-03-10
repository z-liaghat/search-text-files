public class QueryGenerator {

    public String convertQueryToRegex(String query) {
       query = replaceMetaCharacter(query);
        StringBuilder regexQuery = new StringBuilder();
        String[] operandBetweenORList;
        if (query.contains(Operators.OR.name())) {
            operandBetweenORList = query.split(Operators.OR.name());
            for (int i = 0; i < operandBetweenORList.length; i++) {
                StringBuilder andExpression = convertAndExpressionToRegex(operandBetweenORList[i]);
                regexQuery.append(andExpression);
                regexQuery.append("|");
                if (i == operandBetweenORList.length - 1)
                    regexQuery.deleteCharAt(regexQuery.length() - 1);
            }
        } else {
            regexQuery = convertAndExpressionToRegex(query);
        }
        System.out.println(regexQuery);

        return regexQuery.toString().toLowerCase();
    }

    private StringBuilder convertAndExpressionToRegex(String andExpression) {
        String[] operand = andExpression.split(Operators.AND.name());
        StringBuilder regex = new StringBuilder();
        for (int j = 0; j < operand.length; j++) {
//            regex.append("(?=.*\\Q" + operand[j].toLowerCase().trim() + "\\E)");
            regex.append("(?=.*" + operand[j].toLowerCase().trim() + ")");
        }
        regex.append(".*");
        return regex;
    }

    private String replaceMetaCharacter(String expression) {
        String newExpression = "";
        //      ([{\^-=$!|]})?*+.
        char[] metaChar = {'+', '*', '?', '|' ,'^','.','[',']','(',')','-','=','!','$','\\','{','}'};
        for (int i = 0; i < metaChar.length; i++)
            if (expression.indexOf(metaChar[i]) != -1) {
               expression = expression.replace(metaChar[i] + "", "\\" + metaChar[i]);
            }


        return expression;
    }
}
