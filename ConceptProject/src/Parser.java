import java.io.File;
import java.io.FileNotFoundException;

public class Parser {
    private Lexer lexer;
    private Token currentToken;
    private Node root;

    public Parser(Lexer l)
    {
        lexer = l;
        currentToken = lexer.lex();
    }

    private void Error(String typeExpected)
    {
        String tempLine = currentToken.getLine();
        System.out.println("Syntax error at line " + tempLine + ". Expected type: " + typeExpected + ", Type Received: " + currentToken.getType());
    }

    private Node ErrorNode()
    {
        return new Node(new Token("-1", "ERROR", "Error"));
    }

    private void consume(String type)
    {
        if(!currentToken.getType().equals(type))
        {
            Error(type);
        }
        currentToken = lexer.lex();
    }

    private Node string()
    {
        Node node;// = expr();
        Token token = currentToken;
        //System.out.println(token);

        if(token.getType().equals("STRING"))
        {
            consume("STRING");
            return new Node(token);
        }

        if(token.getType().equals("SEMI-COLON"))
        {
            consume("SEMI-COLON");
            if(currentToken.getType().equals("VARIABLE"))
                return new Node(token, expr());
            else
                return new Node(token, string());

            //return new Node(token, expr());
        }

        return new Node(token);
    }

    private Node factor()
    {
        Node node = null;
        Token token = currentToken;

        if(token.getType().equals("INTEGER"))
        {
            consume("INTEGER");
            return new Node(token);
        } else if(token.getType().equals("FLOAT"))
        {
            consume("FLOAT");
            return new Node(token);
        }
        else if(token.getType().equals("VARIABLE"))
        {
            consume("VARIABLE");
            if(currentToken.getType().equals("OP_EQU"))
            {
                return new Node(token, expr());
            }else if(currentToken.getType().equals("SEMI-COLON"))
            {
                return new Node(token, string());
            }
            return new Node(token);
        }else if(token.getType().equals("STRING"))
        {
            consume("STRING");
            if(currentToken.getType().equals("SEMI-COLON"))
                return new Node(token, string());
        }else if(token.getType().equals("SEMI-COLON"))
        {
            //System.out.println(currentToken);
            consume("SEMI-COLON");
            //System.out.println(currentToken);
            if(currentToken.getType().equals("STRING"))
                return new Node(token, string());
            else
                return new Node(token, expr());
        }
        else
        {
            if(token.getType().equals("LP"))
            {
                consume("LP");
                node = expr();
                consume("RP");
            }
        }

        return node;
    }

    private Node term()
    {
        Node node = factor();
        Token token;

        while(currentToken.getType().equals("OP_MUL") || currentToken.getType().equals("OP_DIV"))
        {
            token = currentToken;

            if(currentToken.getType().equals("OP_MUL"))
            {
                consume("OP_MUL");

            } else if(currentToken.getType().equals("OP_DIV"))
            {
                consume("OP_DIV");
            }

            node = new Node(node, token, factor());
        }

        return node;
    }

    private Node expr()
    {
        Node node = term();
        Token token;

        while(currentToken.getType().equals("KEYWORD") ||currentToken.getType().equals("OP_ADD") || currentToken.getType().equals("OP_SUB") || currentToken.getType().equals("OP_EQU"))
        {
            token = currentToken;

            if(currentToken.getType().equals("OP_ADD"))
            {
                consume("OP_ADD");

            } else if(currentToken.getType().equals("OP_SUB"))
            {
                consume("OP_SUB");
            } else if(currentToken.getType().equals("OP_EQU"))
            {
                consume("OP_EQU");
            } else if(currentToken.getType().equals("KEYWORD"))
            {
                consume("KEYWORD");
            }

            node = new Node(node, token, term());
        }

        return node;
    }

    private Node condition()
    {
        Node node = expr();
        Token token = currentToken;
        //System.out.println("Con1: " + node + " Token: " + token);

        if(token.getType().equals("THEN") ||token.getType().equals("OP_LESS") || token.getType().equals("OP_EQU") || token.getType().equals("OP_GREATER"))
        {
            if(token.getType().equals("OP_LESS"))
            {
                consume("OP_LESS");
                node = new Node(node, token, expr());
            } else if(token.getType().equals("OP_EQU"))
            {
                consume("OP_EQU");
                node = new Node(node, token, expr());
            } else if(token.getType().equals("OP_GREATER"))
            {
                consume("OP_GREATER");
                node = new Node(node, token, expr());
            } else if(token.getType().equals("THEN"))
            {
                consume("THEN");
                node = new Node(node, token, expr());
            }
        }

        return node;
    }

    private Node ifStatement()
    {
        Node node = condition();
        Token token = currentToken;
        System.out.println(token);
        if(token.getType().equals("THEN") || token.getType().equals("OP_LESS"))
        {
            if(token.getType().equals("OP_LESS"))
            {
                consume("OP_LESS");
                node = new Node(node, token, expr());
            }

            if(token.getType().equals("THEN"))
            {
                consume("THEN");
                node = new Node(node, token, expr());
            }
        }
        /*if(token.getType().equals("VARIABLE"))
        {
            consume("VARIABLE");
            node = condition();
            return new Node(token, node);
        }*/

        return node;
    }

    private Node function()
    {
        Node node = null;
        Token token = currentToken;

        if(token.getType().equals("VARIABLE"))
        {
            consume("VARIABLE");
            node = new Node(token, expr());
        } else if(token.getType().equals("STRING"))
        {
            consume("STRING");
            node = new Node(token, string());
        }

        return node;
    }

    private Node statement()
    {
        Node node = null;
        Token token = currentToken;

        //System.out.println(token);

        if(token.getType().equals("REM"))
        {
            consume("REM");
            return new Node(token);
        }else if(token.getType().equals("KEYWORD"))
        {
            System.out.println("Token: " + token);
            consume("KEYWORD");
            System.out.println("Token Current: " + currentToken);
            return new Node(token, expr());
        }else if(token.getType().equals("FUNCTION"))
        {
            consume("FUNCTION");
            return new Node(token, function());
        }else if(token.getType().equals("IF_STATEMENT"))
        {
            consume("IF_STATEMENT");
            return new Node(token, ifStatement());
        } else if(token.getType().equals("EOF"))
        {
            return new Node(token);
        }


        return node;
    }

    private Node lineNumber()
    {
        Node node = null;
        Token token = currentToken;

        if(token.getType().equals("LINE_NUMBER"))
        {
            consume("LINE_NUMBER");
            return new Node(token, statement());
        }

        return node;
    }

    private Node line()
    {
        Node node = lineNumber();
        /*Token token = currentToken;

        if(token.getType().equals("FUNCTION"))
        {
            consume("FUNCTION");
            node = new Node(node, token, function());
        }*/

        return node;
    }

    private Node program()
    {
        Node node = line();
        Token token;

        return node;
    }

    public boolean canParse()
    {
        return lexer.canLex();
    }

    public Node parse()
    {
        return program();
    }
}
