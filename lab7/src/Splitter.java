import java.util.ArrayList;

/**
 * Created by seven-teen on 14.05.17.
 */
class Splitter {

    private ArrayList<Token> tokens;

    Splitter(ArrayList<Token> tokens){
        this.tokens = new ArrayList<>(tokens);
    }

    ArrayList<ArrayList<Token>> split(){
        ArrayList<ArrayList<Token>> split = new ArrayList<>();
        ArrayList<Token> chain = new ArrayList<>();
        for(Token t : tokens){
            chain.add(t);
            if(t.getDomain().equals("$")){
                split.add(new ArrayList<>(chain));
                chain.clear();
            }
        }
        return split;
    }
}
