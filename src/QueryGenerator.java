public class QueryGenerator {

    public String convertQueryToRegex(String query) {
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
            regex.append("(?=.*" + operand[j].toLowerCase().trim() + ")");
        }
        regex.append(".*");
        return regex;
    }

}
