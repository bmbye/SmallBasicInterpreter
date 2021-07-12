import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MainApp {
    public static void main(String [] args) throws FileNotFoundException {

        String basic = "BASIC_INPUT_FILE_2.txt";
        String bnf = "Non-terminals\n" +
                "<program> -> start <line> end\n" +
                "<line> -> {<line_number> <statement>}\n" +
                "<statement> -> <rem> | <keyword> <expr> | <function>| <if_statement>\n" +
                "<rem> -> REM\n" +
                "<keyword> -> LET | NEXT | END | INPUT\n" +
                "<function> -> (<expr> | <string> {<splitter> {<function>}})\n" +
                "<splitter> -> , | ; | :\n" +
                "<string> -> STRING\n" +
                "<if_statement> -> IF <condition> THEN <expr>\n" +
                "<condition> -> <expr> (> | < | =) <expr>\n" +
                "<expr> -> <term> (+ | -) <term>\n" +
                "<term> -> <factor> (+ | -) <factor>\n" +
                "<factor> -> INTEGER | FLOAT | VARIABLE\n" +
                "\n" +
                "Terminals\n" +
                "REM = {letter}\n" +
                "ENDLINE = \\n\n" +
                "VARIABLE = {letter} {digit}\n" +
                "STRING = \"{character}\"\n" +
                "INTEGER = [-]{digit}\n" +
                "FLOAT = [-]{digit}[.digit]";
        Scanner input = new Scanner(System.in);
        File file;
        Lexer lexer;
        Parser parser;

        while(true) {
            try {
                file = new File(basic);
                lexer = new Lexer(file);
                parser = new Parser(lexer);
                break;
            } catch (FileNotFoundException e) {
                    System.out.println("Could not find file, enter in file path:");
                    basic = input.nextLine();
            }
        }

        boolean toggle = false;

        if(toggle)
        {
            while(lexer.canLex())
            {
                System.out.println(lexer.lex());
                System.out.print("[");
                for(int i = 0; i < 10; i++)
                    System.out.print("=====");
                System.out.println("]\n");
            }
        }else
        {
            System.out.println("BNF:");
            System.out.println(bnf);
            System.out.print("[");
            for(int i = 0; i < 6; i++)
                System.out.print("=====");
            System.out.println("]\n");


            Node n = parser.parse();

            printNodeInOrder(n);

            System.out.println();
            System.out.print("[");
            for(int i = 0; i < 6; i++)
                System.out.print("=====");
            System.out.println("]\n");

            System.out.println("Post-fix:");
            printNodePostOrder(n);
        }

        lexer.close();
    }

    private static void printNodeInOrder(Node n)
    {
        if(n == null)
        {
            //System.out.println("Empty node");
            return;
        }

        printNodeInOrder(n.getLeft());
        System.out.println(n);
        printNodeInOrder(n.getRight());
    }

    public static void printNodePostOrder(Node n)
    {
        if(n == null)
        {
            //System.out.println("Empty node");
            return;
        }

        printNodePostOrder(n.getLeft());
        printNodePostOrder(n.getRight());
        System.out.print(n.getToken().getValue() + "\t");
        if(n.getToken().getType().equals("LINE_NUMBER"))
            System.out.println();
    }
}
