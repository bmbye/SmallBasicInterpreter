import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MainApp {
    public static void main(String [] args) throws FileNotFoundException {

        String basic = "BASIC_INPUT_FILE_1.txt";
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

        /*while(lexer.canLex())
        {
            System.out.println(lexer.lex());
            System.out.print("[");
            for(int i = 0; i < 10; i++)
                System.out.print("=====");
            System.out.println("]\n");
        }*/

        //This parses one line at a time right now. It should parse it all in one call when I'm fully done.
        printNodeInOrder(parser.parse());
        printNodeInOrder(parser.parse());
        printNodeInOrder(parser.parse());
        printNodeInOrder(parser.parse());
        printNodeInOrder(parser.parse());
        printNodeInOrder(parser.parse());
        printNodeInOrder(parser.parse());
        printNodeInOrder(parser.parse());
        printNodeInOrder(parser.parse());
        printNodeInOrder(parser.parse());
        printNodeInOrder(parser.parse());
        printNodeInOrder(parser.parse());
        printNodeInOrder(parser.parse());
        printNodeInOrder(parser.parse());
        printNodeInOrder(parser.parse());
        printNodeInOrder(parser.parse());
        printNodeInOrder(parser.parse());
        printNodeInOrder(parser.parse());

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

    public static void printNode(Node n)
    {
        if(n == null)
        {
            //System.out.println("Empty node");
            return;
        }

        printNode(n.getLeft());
        printNode(n.getRight());
        System.out.println(n);
    }
}
