/* * Class:       CS 4308 Section 01
 * Term:        Summer 2021
 * Name:        Erik Delgadillo, Ahsan Jamal, Momodou Mbye
 * Instructor:   Deepa Muralidhar
 * Project:  Deliverable 2 Parser - Java */

public class Parser {
    private Lexer lexer;
    private Token currentToken;

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

    private Node ErrorNode(Token token)
    {
        return new Node(new Token(token.getLine(), "ERROR", token.getValue()));
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
        Node node = null;// = expr();
        Token token = currentToken;
        //System.out.println(token);

        if(token.getType().equals("STRING"))
        {
            consume("STRING");
            node = new Node(token);
        }else if(token.getType().equals("SEMI-COLON"))
        {
            consume("SEMI-COLON");
            if(currentToken.getType().equals("VARIABLE"))
                node = new Node(token, expr());
            else
                node = new Node(token, string());

            //return new Node(token, expr());
        }

        return node;
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
            return new Node(token, expr());
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

        while(currentToken.getType().equals("OP_MUL") || currentToken.getType().equals("OP_DIV") || currentToken.getType().equals("OP_POW"))
        {
            token = currentToken;

            if(currentToken.getType().equals("OP_MUL"))
            {
                consume("OP_MUL");

            } else if(currentToken.getType().equals("OP_DIV"))
            {
                consume("OP_DIV");
            } else if(currentToken.getType().equals("OP_POW"))
            {
                consume("OP_POW");
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
                node = new Node(node, token, condition());
            } else if(token.getType().equals("OP_GREATER"))
            {
                consume("OP_GREATER");
                node = new Node(node, token, condition());
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
        //System.out.println(token);
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
            node = new Node(token);
        }else if(token.getType().equals("KEYWORD"))
        {
            //System.out.println("Token: " + token);
            consume("KEYWORD");
            //System.out.println("Token Current: " + currentToken);
            node = new Node(token, expr());
        }else if(token.getType().equals("FUNCTION"))
        {
            consume("FUNCTION");
            node = new Node(token, function());
        }else if(token.getType().equals("IF_STATEMENT"))
        {
            consume("IF_STATEMENT");
            node = new Node(token, ifStatement());
        } else if(token.getType().equals("EOF"))
        {
            node = new Node(token);
        }
        else if(!token.getType().equals("LINE_NUMBER"))
        {
            consume("FUNCTION");
            node = ErrorNode(token);
        }

        return node;
    }

    private Node lineNumber()
    {
        Node node = null;
        Token token = currentToken;
        //System.out.println(currentToken);

        if(token.getType().equals("LINE_NUMBER"))
        {
            //System.out.println( currentToken);
            consume("LINE_NUMBER");
            //System.out.println( currentToken);
            node = new Node(token, statement());
        }

        return node;
    }

    private Node line()
    {
        Node node = lineNumber();
        Token token;

        while(currentToken.getType().equals("LINE_NUMBER"))
        {
            token = currentToken;

            node = new Node(node, token, lineNumber());

            //System.out.println(node);

            //this fixes a bug that I have little idea how else to fix.
            //I know why it fixes it, but not how to fix it before this, but I will look into it for the future.
            node.setRight(node.getRight().getRight());


            //System.out.println(node.getLeft());
            //System.out.println(node.getRight());
            //System.out.println(node.getRight().getRight());
            //System.out.println(node.getRight().getLeft());
            //System.out.println();
        }



        return node;
    }

    private Node program()
    {
        Node node = line();

        return node;
    }

    public Node parse()
    {
        return program();
    }
}
