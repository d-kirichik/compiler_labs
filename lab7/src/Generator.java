import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by seven-teen on 29.05.17.
 */
class Generator {

    private HashMap<Rule.Entry, HashSet<Rule.Entry>> first;
    private HashMap<Rule.Entry, HashSet<Rule.Entry>> follow;
    private Rule[][] table;
    private ParseTree graph;
    private HashSet<Rule> uniqueRules;

    Generator(ArrayList<Object> rules) throws ParseException{
        Sets s = new Sets(new ParseTree(), rules);
        this.graph = s.getGraph();
        this.first = s.getFirst();
        this.follow = s.getFollow();
        uniqueRules = new HashSet<>();
        table = new Rule[8][6];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 6; j++){
                table[i][j] = new Rule();
            }
        }
        getUniqueRules();
    }

    private void getUniqueRules(){
        for(ParseTree.Node n : graph.getNodes()){
            ArrayList<Rule.Entry> buff = new ArrayList<>();
            buff.add(n.getVertex());
            for(ParseTree.Node s : n.getBindings()){
                buff.add(s.getVertex());
            }
            Rule temp = new Rule(buff);
            uniqueRules.add(temp);
        }
    }

}
