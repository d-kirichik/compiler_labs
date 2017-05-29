import javafx.util.Pair;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;

/**
 * Created by seven-teen on 14.05.17.
 */
class Parser {
    private ArrayList<Token> tokens;
    private Deque<Rule.Entry> store;
    private int pos;

    private static final Rule[][] table;

    static{
        table = new Rule[8][6];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 6; j++){
                table[i][j] = new Rule();
            }
        }
        ArrayList<Rule.Entry> temp = new ArrayList<>();
        temp.add(new Rule.Entry("Rules", false));
        table[0][0] = new Rule(temp);
        table[0][4] = new Rule(temp);
        table[0][5] = new Rule(temp);
        temp.clear();
        temp.add(new Rule.Entry("eps", true));
        table[1][5] = new Rule(temp);
        table[3][0] = new Rule(temp);
        table[4][0] = new Rule(temp);
        table[4][4] = new Rule(temp);
        table[4][5] = new Rule(temp);
        table[6][2] = new Rule(temp);
        temp.clear();
        temp.add(new Rule.Entry("AxiomTag", false));
        temp.add(new Rule.Entry("NONTERMINAL", true));
        temp.add(new Rule.Entry("Alternatives", false));
        table[2][0] = new Rule(temp);
        table[2][4] = new Rule(temp);
        temp.clear();
        temp.add(new Rule.Entry("Rule", false));
        temp.add(new Rule.Entry("Rules", false));
        table[1][0] = new Rule(temp);
        table[1][4] = new Rule(temp);
        temp.clear();
        temp.add(new Rule.Entry("TAG", true));
        table[3][4] = new Rule(temp);
        temp.clear();
        temp.add(new Rule.Entry("Alternative", false));
        temp.add(new Rule.Entry("Alternatives", false));
        table[4][1] = new Rule(temp);
        temp.clear();
        temp.add(new Rule.Entry("LP", true));
        temp.add(new Rule.Entry("Symbols", false));
        temp.add(new Rule.Entry("RP", true));
        table[5][1] = new Rule(temp);
        temp.clear();
        temp.add(new Rule.Entry("Symbol", false));
        temp.add(new Rule.Entry("Symbols", false));
        table[6][0] = new Rule(temp);
        table[6][3] = new Rule(temp);
        temp.clear();
        temp.add(new Rule.Entry("NONTERMINAL", true));
        table[7][0] = new Rule(temp);
        temp.clear();
        temp.add(new Rule.Entry("TERMINAL", true));
        table[7][3] = new Rule(temp);
    }

    static Rule[][] getTable(){
        return table;
    }

    Parser(ArrayList<Token> chain){
        this.store = new ArrayDeque<>();
        store.addFirst(new Rule.Entry("$", false));
        store.addLast(new Rule.Entry("Spec", false));
        tokens = new ArrayList<>(chain);
        pos = 0;
    }

    private Token getCurToken(){
        return tokens.get(pos);
    }

    private Token getNextToken(){
        pos++;
        return tokens.get(pos);
    }

    Pair<ArrayList<Object>, ArrayList<ErrorMessage>> parse (){
        Token curTok = this.getCurToken();
        ArrayList<Object> result = new ArrayList<>();
        ArrayList<ErrorMessage> errors = new ArrayList<>();
        Rule.Entry x;
        do {
            x = store.peekLast();
            if(x.getValue().equals("eps") || x.getValue().equals("$")){
                store.removeLast();
                continue;
            }
            if(x.getIsTerm()){
                if(x.getValue().equals(curTok.getDomain())){
                    store.removeLast();
                    result.add(curTok);
                    curTok = this.getNextToken();
                }
                else {
                    errors.add(new ErrorMessage("Parse error", curTok.getStart()));
                    curTok = this.getNextToken();
                }
            }
            else{
                ArrayList<Rule.Entry> entries = table[x.calcIndex()][curTok.calcIndex()].getMembers();
                if(!entries.get(0).getValue().equals("err")){
                    store.removeLast();
                    ArrayList<Rule.Entry> temp = new ArrayList<>();
                    temp.add(x);
                    temp.addAll(entries);
                    result.add(new Rule(temp));
                    ArrayList<Rule.Entry> rev = new ArrayList<>(entries);
                    Collections.reverse(rev);
                    for(Rule.Entry e : rev){
                        store.addLast(e);
                    }
                }
                else {
                    errors.add(new ErrorMessage("Parse error", curTok.getStart()));
                    curTok = this.getNextToken();
                }
            }
        } while (!x.getValue().equals("$"));
        return new Pair<>(result, errors);
    }
}
