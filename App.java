import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class App {
    public static void main(String[] args) throws Exception {
        String[] keywords = {"Else", "ElseIf", "EndFor", "EndIf", "EndSub", "EndWhile", "For", "Goto", "If", "Step", "Sub", "Then", "To", "While"};
        String[] objects ={"Array", "Clock", "Controls", "Desktop", "Dictionary", "File", "Flickr", "GraphicsWindow", "ImageList", "Math", "Mouse", "Network", "Program", "Shapes", "Sound", "Stack", "Text", "TextWindow", "Timer", "Turtle"};
        ArrayList<Token> AllTokens = new ArrayList<Token>();
        AllTokens = getKeywords(keywords);
        AllTokens = getObjects(AllTokens, objects);
        AllTokens = getVariables(AllTokens, objects);
        AllTokens = getMethods(AllTokens);
        //getLiterals(AllTokens);
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
                        Token t = new Token("Keyword", keywords[i]);
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
                    boolean isPresent = false;
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
                                Variable newVariable = new Variable(dataList[0], dataList[1]);
                                tokens.add(newVariable);
                                declared.add(nameList[0]);
                                //System.out.println(dataList[0]);
                            /*}
                        }
                        else{
                            Variable newVariable = new Variable(dataList[0], dataList[1]);
                            tokens.add(newVariable);
                            declared.add(nameList[0]);
                            //System.out.println(dataList[0]);
                        }*/
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
                        Token newMethod = new Token("Method", name[0].substring(0,end));
                        tokens.add(newMethod);                      
                        //System.out.println(name[0].substring(0,end));
                    }
                    else{
                        Token newMethod = new Token("Method", name[0]); 
                        tokens.add(newMethod);
                        //System.out.println(name[0]);
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

    /*static ArrayList<Token> getLiterals(ArrayList<Token> t){
        ArrayList<Token> tokens = new ArrayList<Token>();
        tokens = t;
        try {
            File subset = new File("C:/Users/Babz/Documents/Interpreter-2021/Interpreter/subset.txt");
            Scanner reader = new Scanner(subset); 
            while (reader.hasNext()) {
                String data = reader.next();
                boolean isPresent = false;
                for (int i = 0;i < tokens.size();i++){
                    if (data.contains(tokens.get(i).getValue())){
                        isPresent = true;
                    }
                }
                if (!isPresent) {
                    System.out.println(data);
                }
            }
            reader.close();
        } catch (FileNotFoundException e){
            System.out.println("File not found!");
            e.printStackTrace();
        }

        //return tokens;
    }*/
}
