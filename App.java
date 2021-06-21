/* * Class:       CS 4308 Section 01 
* Term:        Summer 2021 
* Name:        Erik Delgadillo, Ahsan Jamal, Momodou Mbye
* Instructor:   Deepa Muralidhar 
* Project:  Deliverable 1 Scanner - Java */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class App {
    public static void main(String[] args) throws Exception {        
        ArrayList<Token> AllTokens = new ArrayList<Token>();
        /*AllTokens =*/ lex();
        /*for (int i = 0; i < AllTokens.size();i++){
            System.out.println(AllTokens.get(i).toString());
        }*/
    }

    static void /*ArrayList<Token>*/ lex() {
        String[] keywords = {"Else", "ElseIf", "EndFor", "EndIf", "EndSub", "EndWhile", "For", "Goto", "If", "Step", "Sub", "Then", "To", "While"};
        String[] objects ={"Array", "Clock", "Controls", "Desktop", "Dictionary", "File", "Flickr", "GraphicsWindow", "ImageList", "Math", "Mouse", "Network", "Program", "Shapes", "Sound", "Stack", "Text", "TextWindow", "Timer", "Turtle"};
        String[] operators ={"=", "+", "-", "*", "/", ">", "<", ">=", "<=", "<>", "And", "Or"};
        String prev = "0";
        String curr = "";
        String next = "";
        int line = 0;
        ArrayList<Token> tokens = new ArrayList<Token>();
        String divider = "===========================================================================================================================================================================================================";       
        //tokens = getLiterals(tokens, objects);
        try {
            File subset = new File("C:/Users/Babz/Documents/Interpreter-2021/Interpreter/subset.txt");
            Scanner reader = new Scanner(subset);
            while (reader.hasNextLine()){
                line++;
                String lineData = reader.nextLine();
                String[] data = lineData.split("\\s+");
                boolean buildingString = false;                                                    
                StringBuilder buildString = new StringBuilder(100);
                for (int i = 0;i < data.length;i++){
                    curr = data[i]; 
                    if (data.length > 1&&i < data.length-1){
                        next = data[i+1];
                        if (i > 0){
                            prev = data[i-1];
                        }
                        else {                            
                            prev = "StartofLine";
                        }
                    }
                    else{
                        next = "EndOfLine";
                    }
                    
                    if (!curr.equals(" ")){
                        //Check for keywords
                        String[] response = getKeywords(keywords, curr);
                        if (response[0].equals("true")){
                            Token t = new Token("Keyword", response[1]);
                            tokens.add(t);
                            System.out.println(divider + "\n" + "Line: " + line + "\t\t" + "Current Token: " + curr + "\t\t" + "Previous Token: " + prev + "\t\t" + "Next Token: " + next + "\t\t" + t.toString() + "\n" + divider);
                        }
                        //Check for operators
                        else {
                            response = getOperators(operators, curr);
                            if (response[0].equals("true")){
                                Token newOperator = new Token("Operator", response[1]);
                                tokens.add(newOperator);
                                System.out.println(divider + "\n" + "Line: " + line + "\t\t" + "Current Token: " + curr + "\t\t" + "Previous Token: " + prev + "\t\t" + "Next Token: " + next + "\t\t" + newOperator.toString() + "\n" + divider);
                                if (response[1].equals("=")){  
                                    if (next.startsWith("\"")&&!next.endsWith("\"")){
                                        buildingString = true;
                                        buildString.append(next);
                                    }
                                    else if (next.startsWith("\"")&&next.endsWith("\"")){
                                        buildString.append(next);
                                        Token t = new Token("Literal", buildString.toString());
                                        tokens.add(t);
                                        System.out.println(divider + "\n" + "Line: " + line + "\t\t" + "Current Token: " + curr + "\t\t" + "Previous Token: " + prev + "\t\t" + "Next Token: " + next + "\t\t" + t.toString() + "\n" + divider);
                                        buildString = new StringBuilder(100);
                                    }
                                    else {
                                        boolean exists = false;
                                        boolean isObject = false;
                                        for (int j = 0; j < objects.length;j++){
                                            if (next.contains(objects[j])){
                                                isObject = true;
                                            }
                                        }
                                        for (int j = 0; j < tokens.size();j++){
                                            if (tokens.get(j).getValue().equals(next)){
                                                exists = true;
                                            }
                                        } 
                                        if (!isObject&&!exists){     
                                            if (next.contains(")")){
                                                int end = next.indexOf(")");                                                                                           
                                                Token t = new Token("Literal", next.substring(0, end));
                                                tokens.add(t);
                                                System.out.println(divider + "\n" + "Line: " + line + "\t\t" + "Current Token: " + next + "\t\t" + "Previous Token: " + prev + "\t\t" + "Next Token: " + next + "\t\t" + t.toString() + "\n" + divider);
                                                
                                            }
                                            else {                                                                                           
                                                Token t = new Token("Literal", next);
                                                tokens.add(t);
                                                System.out.println(divider + "\n" + "Line: " + line + "\t\t" + "Current Token: " + next + "\t\t" + "Previous Token: " + prev + "\t\t" + "Next Token: " + next + "\t\t" + t.toString() + "\n" + divider);
                                                
                                            }
                                        }
                                    }                                  
                                                               
                            }
                            }
                            else{
                                //check for objects
                                response = getObjects(objects, curr);
                                if (response[0].equals("true")){
                                    Token t = new Token("Object", response[1]);
                                    tokens.add(t);
                                    System.out.println(divider + "\n" + "Line: " + line + "\t\t" + "Current Token: " + curr + "\t\t" + "Previous Token: " + prev + "\t\t" + "Next Token: " + next + "\t\t" + t.toString() + "\n" + divider);
                                }
                                //check for methods
                                if (curr.contains(".")&&!curr.endsWith(".")){
                                    String[] newString = curr.split("\\.");
                                    if (newString[1].contains("(")){
                                        int end =  newString[1].indexOf("(");  
                                        Token t = new Token("Method", newString[1].substring(0,end).trim());
                                        tokens.add(t);
                                        System.out.println(divider + "\n" + "Line: " + line + "\t\t" + "Current Token: " + curr + "\t\t" + "Previous Token: " + prev + "\t\t" + "Next Token: " + next + "\t\t" + t.toString() + "\n" + divider);                                            
                                    }
                                    else{
                                        Token t = new Token("Method", newString[1]);
                                        tokens.add(t);
                                        System.out.println(divider + "\n" + "Line: " + line + "\t\t" + "Current Token: " + curr + "\t\t" + "Previous Token: " + prev + "\t\t" + "Next Token: " + next + "\t\t" + t.toString() + "\n" + divider);                                        
                                    }
                                    //System.out.println(newString[1]);                                    
                                }
                                else if (curr.endsWith("()")){
                                    Token t = new Token("Method", curr.trim());
                                    System.out.println(divider + "\n" + "Line: " + line + "\t\t" + "Current Token: " + curr + "\t\t" + "Previous Token: " + prev + "\t\t" + "Next Token: " + next + "\t\t" + t.toString() + "\n" + divider);                                  
                                }
                                else if(next.equals("=")){
                                        //Check for variables 
                                    int initSize = tokens.size();
                                    tokens = getVariables(objects, curr, tokens);
                                    if (tokens.size() > initSize){
                                        System.out.println(divider + "\n" + "Line: " + line + "\t\t" + "Current Token: " + curr + "\t\t" + "Previous Token: " + prev + "\t\t" + "Next Token: " + next + "\t\t" + tokens.get(tokens.size()-1).toString() + "\n" + divider);
                                    }                                    
                                }
                                else if (buildingString&&!curr.endsWith("\"")&&!curr.startsWith("\"")) {
                                    buildString.append(" " + curr);
                                }
                                else if (curr.endsWith("\"")){
                                    buildingString = false;
                                    buildString.append(" " + curr);
                                    Token t = new Token("Literal", buildString.toString());
                                    tokens.add(t);
                                    System.out.println(divider + "\n" + "Line: " + line + "\t\t" + "Current Token: " + curr + "\t\t" + "Previous Token: " + prev + "\t\t" + "Next Token: " + next + "\t\t" + t.toString() + "\n" + divider);
                                    buildString = new StringBuilder(100);
                                }     
                            }                            
                        }
                    }
                }
                
            }
            reader.close();
        } catch (FileNotFoundException e){
            System.out.println("File not found!");
            e.printStackTrace();
        }
    }

    static String[] getKeywords(String[] keywords, String data){
        String[] response = new String[2];
        response[0] = "false";
        for (int i=0;i<keywords.length;i++){                    
            if (data.equals(keywords[i])){  
                response[0] = "true";
                response[1] = keywords[i];
                //System.out.println("data: " + data + ", keyword: " + keywords[i]);
            }
        }
        return response;
    }

    static String[] getObjects(String[] objects, String data){
        String[] response = new String[2];
        response[0] = "false";
        if (data.contains(".")){
            String[] newObject = data.split("\\.");
            
            for (int i=0;i< objects.length;i++){
                if (newObject[0].equals(objects[i])){
                    response[0] = "true";
                    response[1] = objects[i];
                }
            }
        }
        else {            
            for (int i=0;i< objects.length;i++){
                if (data.equals(objects[i])){
                    response[0] = "true";
                    response[1] = objects[i];
                }
            }
        }
        return response;
    }

    static ArrayList<Token> getVariables(String[] objects, String curr, ArrayList<Token> tokens){
        boolean isPresent = false;
        ArrayList<Token> tempList = tokens;
        for (int j = 0;j < objects.length;j++){
            if (curr.contains(objects[j])){
                isPresent = true;
            }
        }
        if (!isPresent){
            if (curr.contains("(")){
                int begin = curr.indexOf("(");               
                Token t = new Token("Variable", curr.substring(begin+1));
                tempList.add(t);
            }
            else{                
                Token t = new Token("Variable", curr);
                tempList.add(t);
            }
        }
        
        return tempList;
    }

    /*static ArrayList<Token> getMethods(ArrayList<Token> t){
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
    }*/

    /*static ArrayList<Token> getLiterals(ArrayList<Token> oToken, String[] objects, String curr, boolean buildingString){
        String newString = "";                                                                        
        boolean exists = false;
        int tokenIndex = 900;
        String[] response = getObjects(objects, curr);
        ArrayList<Token> tokens = oToken;
        for (int j = 0;j < tokens.size();j++){
            if (tokens.get(j).toString().contains(curr)){
                exists = true;
                tokenIndex = j;
            }
        }
        if (exists&&!buildingString){
            Token t = new Token(tokens.get(tokenIndex).getType(),tokens.get(tokenIndex).getValue());
            tokens.add(t);
        }
        else if (response[0].equals("true")&&!buildingString){
            Token t = new Token("Object", response[1]);
            tokens.add(t);
        }
        else if (curr.startsWith("\"")){
            buildingString = true;
            newString.concat(curr);
        }
        else if (buildingString&&!curr.endsWith("\"")){                                            
            newString.concat(curr);
        }
        else if (curr.endsWith("\"")){
            newString.concat(curr);
            buildingString = false;
            Token finalString = new Token("Literal", newString);
            tokens.add(finalString);        
        }
        else {
            Token t = new Token("Literal", curr);
            tokens.add(t);
        }

        return tokens;
    }*/

    static String[] getOperators(String[] operators, String data){
        String[] response = new String[2];
        response[0] = "false";
        for (int i = 0;i < operators.length;i++){
            if (data.contains(operators[i])){
                response[0] = "true";
                response[1] = operators[i];
            }
        }
        return response;
    }
}
