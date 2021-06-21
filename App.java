import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class App {
    public static void main(String[] args) throws Exception {
        String[] keywords = {"Else", "ElseIf", "EndFor", "EndIf", "EndSub", "EndWhile", "For", "Goto", "If", "Step", "Sub", "Then", "To", "While"};
        String[] objects ={"Array", "Clock", "Controls", "Desktop", "Dictionary", "File", "Flickr", "GraphicsWindow", "ImageList", "Math", "Mouse", "Network", "Program", "Shapes", "Sound", "Stack", "Text", "TextWindow", "Timer", "Turtle"};
        String[] operators ={"=", "+", "-", "*", "/", ">", "<", ">=", "<=", "<>", "And", "Or"};
        ArrayList<Token> AllTokens = new ArrayList<Token>();
        AllTokens = getKeywords(keywords);
        AllTokens = getObjects(AllTokens, objects);
        AllTokens = getVariables(AllTokens, objects);
        AllTokens = getMethods(AllTokens);        
        AllTokens = getOperators(AllTokens, operators);
        AllTokens = getLiterals(AllTokens, objects);
        for (int i = 0; i < AllTokens.size();i++){
            System.out.println(AllTokens.get(i).toString());
        }
    }

    static ArrayList<Token> getKeywords(String[] keywords){
        ArrayList<Token> tokens = new ArrayList<Token>();
        try {
            File subset = new File("C:/Users/Babz/Documents/Interpreter-2021/Interpreter/subset.txt");
            Scanner reader = new Scanner(subset);
            while (reader.hasNext()) {
                String data = reader.next();
                for (int i=0;i<keywords.length;i++){                    
                    if (data.equals(keywords[i])){  
                        Token t = new Token("Keyword", keywords[i].trim());
                        tokens.add(t);
                        
                        //System.out.println("data: " + data + ", keyword: " + keywords[i]);
                    }
                }
            }
            reader.close();
        } catch (FileNotFoundException e){
            System.out.println("File not found!");
            e.printStackTrace();
        }

        return tokens;
    }

    static ArrayList<Token> getObjects(ArrayList<Token> t, String[] objects){
        ArrayList<Token> tokens = new ArrayList<Token>();
        tokens = t;
        try {
            File subset = new File("C:/Users/Babz/Documents/Interpreter-2021/Interpreter/subset.txt");
            Scanner reader = new Scanner(subset);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                for (int i=0;i< objects.length;i++){
                    if (data.contains(objects[i])){
                        Token newToken = new Token("Object", objects[i]);
                        tokens.add(newToken);
                    }
                }
            }
            reader.close();
        } catch (FileNotFoundException e){
            System.out.println("File not found!");
            e.printStackTrace();
        }

        return tokens;
    }

    static ArrayList<Token> getVariables(ArrayList<Token> t, String[] objects){
        ArrayList<Token> tokens = new ArrayList<Token>();
        ArrayList<String> declared = new ArrayList<String>();
        tokens = t;
        try {
            File subset = new File("C:/Users/Babz/Documents/Interpreter-2021/Interpreter/subset.txt");
            Scanner reader = new Scanner(subset);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                if (data.contains("=")&&!data.startsWith("If")&&!data.startsWith("ElseIf")){
                    String[] dataList = data.split("=");
                    String[] nameList = dataList[0].split("\\.");                     
                    /*boolean isPresent = false;
                    for (int i = 0;i < objects.length;i++){
                        if (objects[i].compareTo(nameList[0])==0){
                           isPresent = true;
                        }
                    }
                    if (!isPresent){
                        //boolean isDeclared = false;
                        /*if (declared.size() != 0){
                            for (int i = 0;i < declared.size();i++){
                                if (nameList[0].equals(declared.get(i))){
                                    isDeclared = true;
                                }
                            }
                            if (!isDeclared){*/
                                /*********************
                                 * Delete when uncommenting the rest*/
                                boolean isPresent = false;
                                for (int i = 0;i < objects.length;i++){
                                    if (objects[i].compareTo(nameList[0])==0){
                                    isPresent = true;
                                    }
                                }
                                 /**/
                                 if(!isPresent){
                                    Token newVariable = new Token("Variable", dataList[0].trim());
                                    tokens.add(newVariable);
                                    declared.add(nameList[0]);
                                 }
                                //System.out.println(dataList[0]);
                            /*}
                        }
                        else{
                            Variable newVariable = new Variable(dataList[0], dataList[1]);
                            tokens.add(newVariable);
                            declared.add(nameList[0]);
                            //System.out.println(dataList[0]);
                        }*/
                    //}
                }
            }
            reader.close();
        } catch (FileNotFoundException e){
            System.out.println("File not found!");
            e.printStackTrace();
        }

        return tokens;
    }

    static ArrayList<Token> getMethods(ArrayList<Token> t){
        ArrayList<Token> tokens = new ArrayList<Token>();
        tokens = t;
        try {
            File subset = new File("C:/Users/Babz/Documents/Interpreter-2021/Interpreter/subset.txt");
            Scanner reader = new Scanner(subset);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                if (data.contains(".")){
                    String[] methodList = data.split("\\.");
                    String[] name = methodList[1].split(" ");
                    if (name[0].contains("(")){
                        int end = name[0].indexOf("(");  
                        Token newMethod = new Token("Method", name[0].substring(0,end).trim());
                        tokens.add(newMethod);                      
                        //System.out.println(name[0].substring(0,end));
                    }
                    else{
                        Token newMethod = new Token("Method", name[0].trim()); 
                        tokens.add(newMethod);
                        //System.out.println(name[0]);
                    }
                }
                else if(data.endsWith("()")){
                    Token newMethod = new Token("Method", data.trim());
                    tokens.add(newMethod); 
                }

                boolean isPresent = false;
                int tokenIndex = 900;
                for (int i = 0;i<tokens.size();i++){
                    if(tokens.get(i).getType().equals("Method")&&(tokens.get(i).toString().contains(data))){
                        isPresent = true;
                        tokenIndex = i;
                    }
                }
                if(isPresent){
                    Token newMethod = new Token("Method", tokens.get(tokenIndex).getValue());
                    tokens.add(newMethod);
                }
            }
            reader.close();
        } catch (FileNotFoundException e){
            System.out.println("File not found!");
            e.printStackTrace();
        }

        return tokens;
    }

    static ArrayList<Token> getLiterals(ArrayList<Token> t, String[] objects){
        ArrayList<Token> tokens = new ArrayList<Token>();
        tokens = t;
        try {
            File subset = new File("C:/Users/Babz/Documents/Interpreter-2021/Interpreter/subset.txt");
            Scanner reader = new Scanner(subset); 
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                boolean isPresent = false;
                if (data.contains("(")){   
                    //Check for strings and other values    
                    String enclosedString = data.substring(data.indexOf("(")+1, data.indexOf(")"));                                            
                    String[] components = enclosedString.split(",");
                    if (enclosedString.contains("\"")){
                        components = enclosedString.split("\",");
                        components[0] = components[0].concat("\"");
                        for (int i = 0;i < components.length;i++){
                            if(components[i].contains("\"")){
                                Token newString = new Token("Literal", components[i]);
                                tokens.add(newString);
                            }
                            else {                                
                                boolean exists = false;
                                int tokenIndex = 900;
                                for(int j = 0;j <tokens.size();j++){
                                    if (tokens.get(j).toString().contains(components[i])){
                                        exists = true;
                                        tokenIndex = j;
                                    }
                                }
                                if (exists) {                                    
                                    Token newToken = new Token(tokens.get(tokenIndex).getType(),tokens.get(tokenIndex).getValue());
                                    tokens.add(newToken);
                                }                                
                                else{
                                    Token newToken = new Token("Literal", components[i].trim());
                                    tokens.add(newToken);
                                }
                            }
                        }
                    }
                    else if(components.length < 2&&!components[0].equals("")&&!data.startsWith("If")&&!data.startsWith("ElseIf")){
                            Token newToken = new Token("Literal", components[0].trim());
                            tokens.add(newToken);
                    }
                    else if (components.length > 2){
                        for (int i = 0;i < components.length;i++){
                                boolean exists = false;
                                int tokenIndex = 900;
                                for(int j = 0;j <tokens.size();j++){
                                    if (tokens.get(j).toString().contains(components[i])){
                                        exists = true;
                                        tokenIndex = j;
                                    }
                                }
                                if (exists) {                                    
                                    Token newToken = new Token(tokens.get(tokenIndex).getType(),tokens.get(tokenIndex).getValue());
                                    tokens.add(newToken);
                                }                                
                                else{
                                    Token newToken = new Token("Literal", components[i].trim());
                                    tokens.add(newToken);
                                }
                        }
                    }
                    else if((data.startsWith("If")&&enclosedString.contains("="))||data.startsWith("ElseIf")&&enclosedString.contains("=")){
                        String[] conditionList = enclosedString.split("=");
                        Token newToken = new Token("Variable", conditionList[0]);
                        Token newToken2 = new Token("Literal", conditionList[1]);
                        tokens.add(newToken);
                        tokens.add(newToken2);
                    }
                }
                else if (data.contains("=")){
                        if (data.contains("\"")){                        
                            int start = data.indexOf("\"");
                            int end = data.lastIndexOf("\"");
                            String newString = data.substring(start, end+1);
                            Token newToken = new Token("Literal", newString);
                            tokens.add(newToken);
                        }
                        else {   
                            String[] stringList = data.split("=");                   
                            for (int i = 0;i<objects.length;i++){
                                if(stringList[1].contains(objects[i])&&stringList[1].contains(".")){                                    
                                    isPresent = true;
                                }
                            }
                            if (!isPresent){
                                Token newString = new Token("Literal", stringList[1]);
                                tokens.add(newString);
                            }
                        }
                }
            }
            reader.close();
        } catch (FileNotFoundException e){
            System.out.println("File not found!");
            e.printStackTrace();
        }

        return tokens;
    }

    static ArrayList<Token> getOperators(ArrayList<Token> t, String[] operators){
        ArrayList<Token> tokens = new ArrayList<Token>();
        tokens = t;
        try {
            File subset = new File("C:/Users/Babz/Documents/Interpreter-2021/Interpreter/subset.txt");
            Scanner reader = new Scanner(subset); 
            while (reader.hasNext()) {
                String data = reader.next();
                for (int i = 0;i < operators.length;i++){
                    if (data.contains(operators[i])){
                        Token newOperator = new Token("Operator", operators[i]);
                        tokens.add(newOperator);
                    }
                }
            }
            reader.close();
        } catch (FileNotFoundException e){
            System.out.println("File not found!");
            e.printStackTrace();
        }

        return tokens;
    }
}
