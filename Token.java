public class Token {
    protected String type;
    protected String value;

    public Token(String t, String v) {
        this.type = t;
        this.value = v;
    }

    public String toString(){
        String text = "Type: " + this.type + "\t\tValue: " + this.value;
        return text;
    }

    public String getType() {
        return this.type;
    }

    public String getValue(){
        return this.value;
    }
}
