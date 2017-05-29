import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by seven-teen on 13.05.17.
 */

public class Main {

    static final char EOF = (char) 0xFFFFFF;

    public static void main(String[] args) throws IOException {
        String program = "";
        Scanner scanner = new Scanner(new File(args[0]));
        while (scanner.hasNextLine())
            program += scanner.nextLine() + "\n";
        program += EOF;
        scanner.close();
        Lexer lex = new Lexer(program);
        Pair<ArrayList<Token>, ArrayList<ErrorMessage>> res = lex.getTokens();
        for (Token t : res.getKey()) {
            System.out.println(t);
        }
        if (!res.getValue().isEmpty()) {
            for (ErrorMessage e : res.getValue()) {
                System.out.println(e);
            }
            System.out.println("Errors occurred during lexical analysis. Parsing is impossible.");
            return;
        }

        Parser p = new Parser(res.getKey());
        Pair<ArrayList<Object>, ArrayList<ErrorMessage>> parsed = p.parse();
        Printer printer;
        try {
            printer = new Printer(parsed.getKey());
            Generator g = new Generator(parsed.getKey());
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        printer.toPDF();

    }
}
