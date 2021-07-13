/* * Class:       CS 4308 Section 01
 * Term:        Summer 2021
 * Name:        Erik Delgadillo, Ahsan Jamal, Momodou Mbye
 * Instructor:   Deepa Muralidhar
 * Project:  Deliverable 2 Parser - Java */

public class Token {
    private final String line;
    private String type;
    private String value;

    public Token(String l,String t, String v)
    {
        line = l;
        type = t;
        value = v;
    }

    public String getLine() {
        return line;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setType(String t)
    {
        type = t;
    }

    public void add(Token token)
    {
        value += token.getValue();
    }

    @Override
    public String toString()
    {
        return "Line: " + line + ", Type: " + type + ", Value: " + value;
    }
}
