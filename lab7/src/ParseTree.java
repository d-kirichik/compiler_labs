import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by seven-teen on 21.05.17.
 */
class ParseTree {

    static class Node {

        private Rule.Entry vertex;
        private ArrayList<Node> bindings;
        private boolean isToken;

        Node(Rule.Entry r, boolean isToken){
            this.vertex = new Rule.Entry(r);
            this.bindings = new ArrayList<>();
            this.isToken = isToken;
        }

        void addBinding(Node n){
            this.bindings.add(n);
        }

        Rule.Entry getVertex(){
            return this.vertex;
        }

        ArrayList<Node> getBindings() { return this.bindings; }

        boolean getIsToken(){
            return this.isToken;
        }

        @Override
        public String toString(){
            return vertex + " " + this.hashCode() + " " + this.isToken + " ";
        }

        //hashcode не переопределен, потому что так надо. хешкоды равны, если ссылки равны.
    }

    private ArrayList<Node> nodes;

    ParseTree(){
        this.nodes = new ArrayList<>();
    }

    ParseTree(ParseTree t){
        this.nodes = new ArrayList<>(t.nodes);
    }

    void removeTokens(){
        for(Node n : nodes){
            for(int j = 0; j < n.bindings.size(); j++){
                if(n.bindings.get(j).isToken)
                    n.bindings.remove(n.bindings.get(j));
            }
        }
    }

    ArrayList<Node> getNodes(){
        return nodes;
    }

    Node getRoot(){
        for(Node n : nodes){
            if(n.getVertex().getValue().equals("Spec")){
                return n;
            }
        }
        return null;
    }

    private Node getLast(Rule.Entry e){
        for(int i = nodes.size() - 1; i > 0; i--){
            if(nodes.get(i).vertex.equals(e)){
                return nodes.get(i);
            }
        }
        return null;
    }

    private Node getLast(String domain){
        for(int i = nodes.size() - 1; i > 0; i--){
            if(nodes.get(i).vertex.getValue().equals(domain)){
                return nodes.get(i);
            }
        }
        return null;
    }

    void initTree(ArrayList<Object> o) throws ParseException{
        for(int i = 0; i < o.size(); i++){
            Object cur = o.get(i);
            if(cur instanceof Rule){
                Rule.Entry head = new Rule.Entry(((Rule) cur).getMembers().get(0));
                Node temp = this.getLast(head);
                if(temp == null){
                    temp = new Node(head, false);
                    nodes.add(temp);
                }
                for(int j = 1; j < ((Rule) cur).getMembers().size(); j++){
                    Node sub = new Node(((Rule) cur).getMembers().get(j), false);
                    temp.addBinding(sub);
                    nodes.add(sub);
                }
            }
            else if(cur instanceof Token){
                Node temp = this.getLast(((Token) cur).getDomain());
                if(temp != null){
                    Rule.Entry x = new Rule.Entry(((Token) cur).getValue(), true);
                    temp.addBinding(new Node(x, true));
                }
                else throw new ParseException(cur.toString(), i);
            }
            else{
                throw new ParseException(cur.toString(), i);
            }
        }
    }
}
