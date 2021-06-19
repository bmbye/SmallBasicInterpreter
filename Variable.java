public class Variable extends Token{
    private String name;

    public Variable(String n, String v){
        super("Variable", v);
        this.name = n;
    }

    public String toString() {
        String text = "Type: " + this.type + "\t\tName: " + this.name /*+"\t\tValue: " + this.value*/;
        return text;
    }

    public String getName() {
        return this.name;
    }

    /*public String getValue(){
        return this.value;
    }*/
}
