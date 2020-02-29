public class AndExpression {
    private String expression;
    private Boolean value;

    public AndExpression(String expression, Boolean value) {
        this.expression = expression;
        this.value = value;
    }

    public String getExpression() {
        return expression;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }
}
