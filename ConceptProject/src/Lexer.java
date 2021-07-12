import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Lexer {
    //These won't change throughout the lexer.
    private final String[] keywords = {"Else", "ElseIf", "EndFor", "EndIf", "EndSub", "EndWhile", "For", "Goto", "If", "Step",
                                        "Sub", "Then", "To", "While", "THEN", "IF", "FOR", "TO", "END", "DEF", "REM",
                                            "STEP"};
    private final char[] splitter = {',', ';'};
    private final String[] splitDescription = {"COMMA", "SEMI-COLON"};
    private final String[] function = {"PRINT", "LET", "INPUT", "NEXT"};
    private final char[] operators = {'+', '-', '*', '/', '(', ')', '=', '<', '>', '^'};
    private final String[] opDescription = {"OP_ADD", "OP_SUB", "OP_MUL", "OP_DIV", "LP", "RP", "OP_EQU", "OP_LESS", "OP_GREATER", "OP_POW"};
    private final File file;
    private final Scanner reader;

    //Help build the tokens.
    private boolean isString;
    private boolean lineNum;
    private boolean remark;
    private boolean hasSplit;
    private String currentLineNumber;
    //private int lineNum;
    private String currentLine;
    private Token lastToken;

    public Lexer(File f) throws FileNotFoundException {
        currentLineNumber = "-1";
        remark = false;
        lineNum = false;
        isString = false;
        hasSplit = false;
        file = f;
        reader = new Scanner(file);
    }

    //Returns the next token in the line.
    public Token lex()
    {
        Token result;
        currentLine = setNext();

        if(lastToken == null)
        {
            lastToken = nextToken();
            result = lastToken;
        }
        else
        {
            result = nextToken();
        }

        if(isKeyword(lastToken))
        {
            if(lastToken.getValue().equals("DEF"))
            {
                result.setType("FUNCTION");
            }else if(lastToken.getValue().equals("END"))
            {
                result.setType("EOF");
            }
        }

        if(isKeyword(result))
        {
            if(result.getValue().equals("END"))
            {
                result.setType("EOF");
            }

            if(result.getValue().equals("IF") || result.getValue().equals("THEN"))
            {
                if(result.getValue().equals("IF"))
                    result.setType("IF_STATEMENT");
                else
                    result.setType("THEN");
            }

            if(result.getValue().equals("REM"))
            {
                result = new Token(result.getLine(), "REM",  currentLine);
                //remark = true;
            }
        }

        lastToken = result;

        /*System.out.println(currentLine);
        System.out.println(result.getValue().length());
        System.out.println(result);*/
        //System.out.println("Current Line:_" + currentLine + "_|");
        currentLine = currentLine.substring(result.getValue().length());
        //System.out.println("Current Line:_" + currentLine + "_|");

        return result;
    }

    private boolean isSplitter(char d)
    {
        for(int i = 0; i < splitter.length; i++)
        {
            if(d == splitter[i])
                return true;
        }

        return false;
    }

    private boolean isKeyword(Token t)
    {

        for(int i = 0; i < keywords.length; i++)
        {
            if(t.getType().equals("KEYWORD"))
            {
                return true;
            }
        }

        return false;
    }

    private Token nextToken()
    {
        char temp = ' ';
        int i = 0;
        String next = "";
        Token result;
        boolean endLine = false;

        while(true)
        {
            if(i >= currentLine.length())
                endLine = true;

            if(!endLine)
                temp = currentLine.charAt(i);

            if(temp == '"' && !isString)
            {
                isString = true;
                hasSplit = false;
                //currentLine = currentLine.substring(1);

                next += temp;
                //System.out.println(temp);
                //temp = currentLine.charAt(i+1);
                //System.out.println(temp);
                i++;
                continue;
            }

            if(!lineNum) // get the line number
            {
                result = LineNumber();
                lineNum = true;
                //currentLine = currentLine.substring(result.getValue().length());
                /*System.out.println("| " + currentLine.charAt(0));
                System.out.println(currentLine.charAt(1));
                System.out.println(currentLine.charAt(2));
                System.out.println(currentLine.charAt(3));*/
                return result;
            }
            if(isOperator(Character.toString(temp)) && next.length() == 0 && !isString) // get the line number
            {
                //System.out.println(next);
                //System.out.println(temp);

                next += temp;
                //System.out.println(next);
                result = checkToken(next);
                return result;
                /*if(currentLine.length() > 1)
                {
                    if(!Character.isDigit(currentLine.charAt(i+1)))
                    {
                        result = checkToken(next);
                        return result;
                    }
                }
                else
                {
                    result = checkToken(next);
                    return result;
                }*/
            }else if(isOperator(Character.toString(temp)) && next.length() != 0 && isString)
            {
                next += temp;
            } else if(isOperator(Character.toString(temp)) && next.length() != 0)
            {
                result = checkToken(next);
                return result;
            } else if(isSplitter(temp) && !isString)
            {
                if(! (next.length() > 0))
                {
                    next += temp;

                }

                result = checkToken(next);
                return result;
            }
            else if(temp == ' ' && !isString || endLine) // gets token if a space is found and it is not a string
            {
                if(next.length() == 0 && !endLine)
                {
                    currentLine = currentLine.substring(1);
                    result = nextToken();
                }
                else
                {
                    result = checkToken(next);
                }

                //currentLine = currentLine.substring(result.getValue().length());
                return result;
            } else if(Character.isAlphabetic(temp))
            {
                next += Character.toString(temp);
            }
            else if(isString) // handle it if it is a string
            {
                if(temp == '"') // get string token
                {
                    next += temp;
                    result = checkToken(next);
                    isString = false;
                    //currentLine = currentLine.substring(next.length());
                    return result;
                } else // add to string for string token
                {
                    next += temp;
                }
            } else // add to string for token
            {

                next += temp;
            }

            i++;
        }

        //return new Token("-1","EOF", "");
    }

    private boolean isOperator(String n)
    {
        if(n.length() > 1)
            return false;

        for(int i = 0; i < operators.length; i++)
        {
            if(n.charAt(0) == operators[i])
            {
                return true;
            }
        }

        return false;
    }

    private Token LineNumber()
    {
        String temp = "";
        boolean isDigit = false;
        //int length = currentLine.length();
        int i = 0;
        char t = currentLine.charAt(i);

        //System.out.println("CurrentLine in Number:_" + currentLine+ "_|");

        while(Character.isDigit(t))
        {
            if(!isDigit)
                isDigit = true;

            temp += Character.toString(t);
            i++;

            if(i == currentLine.length())
                break;

            t = currentLine.charAt(i);

        }
        if(!isDigit)
            return new Token("-1", "UNKNOWN", temp);
        else
        {
            currentLineNumber = temp;
            return new Token(temp, "LINE_NUMBER", temp);
        }
    }

    private Token checkToken(String n)
    {

        //System.out.println("Check Token: " + n);

        if(checkString(n))
        {
            //System.out.println(n);
            //System.out.println(currentLine);
            return new Token(currentLineNumber, "STRING", n);
        }

        for(int i = 0; i < function.length; i++)
        {
            if(n.equals(function[i]))
                return new Token(currentLineNumber, "FUNCTION", n);
        }

        if(checkKeyword(n))
        {
            return new Token(currentLineNumber, "KEYWORD", n);
        }

        if(n.length() == 1)
        {
            if(isOperator(n)) {
                for (int i = 0; i < operators.length; i++) {
                    if (n.charAt(0) == operators[i]) {
                        return new Token(currentLineNumber, opDescription[i], n);
                    }
                }
            }

            if(isSplitter(n.charAt(0)))
            {
                for (int i = 0; i < splitter.length; i++) {
                    if (n.charAt(0) == splitter[i]) {
                        return new Token(currentLineNumber, splitDescription[i], n);
                    }
                }
            }
        }

        Token temp = checkNumber(n);

        if(temp != null)
            return temp;
        
        return new Token(currentLineNumber, "VARIABLE", n);
    }

    private boolean checkKeyword(String c)
    {
        for(int i = 0; i < keywords.length; i++)
        {
            if(c.equals(keywords[i]))
                return true;
        }

        return false;
    }

    private boolean checkString(String c) {
        if(c.length() > 1)
        {
            if(c.charAt(0) == '"' && c.charAt(c.length()-1) == '"')
                return true;
        }

        return false;
    }

    private Token checkNumber(String n)
    {
        if(n.length() == 0)
            return null;

        boolean isValid = true;
        int i = 0;

        if(n.charAt(0) == '.')
        {
            for(i = 1; i < n.length(); i++)
            {
                if(!Character.isDigit(n.charAt(i)))
                {
                    isValid = false;
                    break;
                }
            }

            if(isValid)
            {
                currentLine += "0";
                return new Token(currentLineNumber, "FLOAT", "0" + n);
            }

        }

        isValid = true;

        if(n.charAt(0) == '-' || n.charAt(0) == '+')
        {
            if(n.charAt(1) == '.')//handle floats
            {
                for(i = 1; i < n.length(); i++)
                {
                    if(!Character.isDigit(n.charAt(i)))
                    {
                        isValid = false;
                        break;
                    }
                }

                if(isValid && n.charAt(0) == '-')
                    return new Token(currentLineNumber, "FLOAT", "-0" + n.substring(1));
                else if(isValid && n.charAt(0) == '+')
                    return new Token(currentLineNumber, "FLOAT","0" + n.substring(1));
            }

            isValid = true;

            for(i = 1; i < n.length(); i++)
            {
                if(!Character.isDigit(n.charAt(i)))
                {
                    isValid = false;
                    break;
                }
            }

            if(isValid && n.charAt(0) == '-')
                return new Token(currentLineNumber, "INTEGER", n);
            else if(isValid && n.charAt(0) == '+')
                return new Token(currentLineNumber, "INTEGER", n.substring(1));
        }

        isValid = true;

        for(i = 0; i < n.length(); i++)
        {
            if(!Character.isDigit(n.charAt(i)))
            {
                isValid = false;
                break;
            }
        }

        if(isValid)
            return new Token(currentLineNumber, "INTEGER", n);

        return null;
    }

    public void close()
    {
        reader.close();
    }

    //If the current line has nothing else to be read, then go to next line if its there. Otherwise return the current line.
    private String setNext()
    {
        if(currentLine == null || currentLine.length() == 0 || currentLine.length() == 1 && currentLine.charAt(0) == ' ')
        {
            lineNum = false;
            if(reader.hasNextLine())
            {
                String temp = reader.nextLine();
                //System.out.println("Temp:" + temp);
                return temp;
            }

        }/*else if(remark)
        {
            remark = false;
            lineNum = false;
            if(reader.hasNext())
                return reader.nextLine();
        }*/

        return currentLine;
    }

    //Checks the current line then determines if there is anything left to lex.
    public boolean canLex()
    {
        currentLine = setNext();

        return currentLine.length() > 0;
    }
}
