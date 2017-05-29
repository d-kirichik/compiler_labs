import javafx.util.Pair;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by seven-teen on 13.05.17.
 */
class Lexer {

    private String program;
    private Position pos;
    private ArrayList<Token> tokens;
    private ArrayList<ErrorMessage> errors;
    private char curChar;

    Lexer(String program){
        this.program = program;
        this.pos = new Position();
        this.tokens = new ArrayList<>();
        this.errors = new ArrayList<>();
        curChar = this.program.charAt(this.pos.getIndex());
    }

    private void incPos() {
        if (program.charAt(pos.getIndex()) == '\n') {
            pos.incLine();
            pos.resetPos();
        }
        pos.incIndex();
        pos.incPos();
    }

    private char moveScanner(){
        this.incPos();
        return this.program.charAt(pos.getIndex());
    }

     Pair<ArrayList<Token>, ArrayList<ErrorMessage>> getTokens() {
        String nonterm = "[A-Z]`?";
        String lParen = "\\(";
        String rParen = "\\)";
        String terminal= "\"[(A-Za-z0-9+*)]\"";
        String newline = "\\n";
        String tag = "\\*";
        String whitespace = "\\s";
        String pattern = "(^" + nonterm + ")|(^" + lParen + ")|(^" + rParen + ")|(^" + terminal + ")|(^" + newline + ")|(^" + tag + ")|(^" + whitespace + ")";
        Pattern p = Pattern.compile(pattern);
        Position start = new Position(pos), following;
        Position end = null;
        boolean error = false;
        while (curChar != Main.EOF) {
            Matcher m = p.matcher(this.program.substring(pos.getIndex()));
            if (m.find()) {
                pos.inc(m.end());
                following = new Position(pos);
                curChar = this.program.charAt(pos.getIndex());
                error = false;
                if (m.group(1) != null) {
                    tokens.add(new Token("NONTERMINAL", m.group(1), start, following));
                }
                if (m.group(2) != null){
                    tokens.add(new Token("LP", m.group(2), start, following));
                }
                if (m.group(3) != null) {
                    end = new Position(pos);
                    tokens.add(new Token("RP", m.group(3), start, following));
                    if(curChar == Main.EOF){
                        tokens.add(new Token("$", "$", end, end));
                    }
                }
                if (m.group(4) != null) {
                    tokens.add(new Token("TERMINAL", m.group(4), start, following));
                }
                if (m.group(5) != null){
                    this.pos.incLine();
                    this.pos.resetPos();
                }
                if (m.group(6) != null) {
                    tokens.add(new Token("TAG", m.group(6), start, following));
                }
                start = new Position(pos);
            } else {
                if (!error) {
                    errors.add( new ErrorMessage("Syntax error", start));
                    error = true;
                }
                curChar = this.moveScanner();
                start = new Position(pos);
            }
        }
        tokens.add(new Token("$", "$", end, end));
        return new Pair<>(tokens, errors);
    }
}
