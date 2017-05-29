import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by seven-teen on 22.05.17.
 */

class Sets {

    private ParseTree graph;
    private HashMap<ParseTree.Node, ArrayList<Rule.Entry>> nodesFirst;
    private HashMap<Rule.Entry, HashSet<Rule.Entry>> first;
    private HashMap<ParseTree.Node, ArrayList<Rule.Entry>> nodesFollow;
    private HashMap<Rule.Entry, HashSet<Rule.Entry>> follow;

    Sets(ParseTree t, ArrayList<Object> rules) throws ParseException {
        this.graph = new ParseTree(t);
        this.graph.initTree(rules);
        this.graph.removeTokens();
        this.nodesFirst = new HashMap<>();
        initNodesFirst();
        this.first = new HashMap<>();
        initFirst();
        calcFirst();
        this.nodesFollow = new HashMap<>();
        ArrayList<Rule.Entry> temp = new ArrayList<>();
        temp.add(new Rule.Entry("$", true));
        nodesFollow.put(graph.getRoot(), temp);
        this.follow = new HashMap<>();
        initFollow();
        calcFollow();
    }

    ParseTree getGraph(){
        return graph;
    }

    private void initNodesFirst() {
        for (ParseTree.Node n : graph.getNodes()) {
            if (n.getVertex().getIsTerm()) {
                ArrayList<Rule.Entry> rules = new ArrayList<>();
                rules.add(new Rule.Entry(n.getVertex().getValue(), true));
                nodesFirst.put(n, rules);
            }
        }
    }

    private void initFirst() {
        first.put(new Rule.Entry("Symbol", false), new HashSet<>());
        first.put(new Rule.Entry("Symbols", false), new HashSet<>());
        first.put(new Rule.Entry("Alternative", false), new HashSet<>());
        first.put(new Rule.Entry("Alternatives", false), new HashSet<>());
        first.put(new Rule.Entry("AxiomTag", false), new HashSet<>());
        first.put(new Rule.Entry("Rule", false), new HashSet<>());
        first.put(new Rule.Entry("Rules", false), new HashSet<>());
        first.put(new Rule.Entry("Spec", false), new HashSet<>());
    }

    private void initFollow() {
        follow.put(new Rule.Entry("Symbol", false), new HashSet<>());
        follow.put(new Rule.Entry("Symbols", false), new HashSet<>());
        follow.put(new Rule.Entry("Alternative", false), new HashSet<>());
        follow.put(new Rule.Entry("Alternatives", false), new HashSet<>());
        follow.put(new Rule.Entry("AxiomTag", false), new HashSet<>());
        follow.put(new Rule.Entry("Rule", false), new HashSet<>());
        follow.put(new Rule.Entry("Rules", false), new HashSet<>());
        follow.put(new Rule.Entry("Spec", false), new HashSet<>());
    }


    private void calcFirstForNode(ParseTree.Node n) {
        ParseTree.Node firstChild;
        ArrayList<Rule.Entry> temp = new ArrayList<>();
        Rule.Entry x = new Rule.Entry("eps", true);
        try {
            firstChild = n.getBindings().get(0);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return;
        }
        if (firstChild.getVertex().getIsTerm() && !firstChild.getVertex().equals(x)) {
            temp.add(firstChild.getVertex());
            nodesFirst.put(n, temp);
        } else if (firstChild.getVertex().getIsTerm() && firstChild.getVertex().equals(x)) {
            temp.add(x);
            nodesFirst.put(n, temp);
        } else if (!firstChild.getVertex().getIsTerm()) {
            ArrayList<Rule.Entry> fChild = new ArrayList<>(nodesFirst.get(firstChild));
            if (fChild.contains(x)) {
                fChild.remove(x);
                temp.addAll(fChild);
                ParseTree.Node secondChild;
                secondChild = n.getBindings().get(1);
                temp.addAll(nodesFirst.get(secondChild));
                nodesFirst.put(n, temp);
            } else {
                temp.addAll(fChild);
                nodesFirst.put(n, temp);
            }
        } else {
            throw new IllegalStateException();
        }
    }

    //переписать. bfs?
    private void calcFirstAllNodes() {
        for (ParseTree.Node n : graph.getNodes()) {
            if (n.getVertex().getValue().equals("Symbol")) {
                calcFirstForNode(n);
            }
        }
        for (ParseTree.Node n : graph.getNodes()) {
            if (n.getVertex().getValue().equals("Symbols")) {
                calcFirstForNode(n);
            }
        }
        for (ParseTree.Node n : graph.getNodes()) {
            if (n.getVertex().getValue().equals("Alternative")) {
                calcFirstForNode(n);
            }
        }
        for (ParseTree.Node n : graph.getNodes()) {
            if (n.getVertex().getValue().equals("Alternatives")) {
                calcFirstForNode(n);
            }
        }
        for (ParseTree.Node n : graph.getNodes()) {
            if (n.getVertex().getValue().equals("AxiomTag")) {
                calcFirstForNode(n);
            }
        }
        for (ParseTree.Node n : graph.getNodes()) {
            if (n.getVertex().getValue().equals("Rule")) {
                calcFirstForNode(n);
            }
        }
        ArrayList<Rule.Entry> spec = new ArrayList<>();
        for (ParseTree.Node n : graph.getNodes()) {
            if (n.getVertex().getValue().equals("Rules")) {
                calcFirstForNode(n);
                spec.addAll(nodesFirst.get(n));
            }
        }
        nodesFirst.put(graph.getRoot(), spec);
    }

    private void calcFirst() {
        calcFirstAllNodes();
        for (ParseTree.Node n : nodesFirst.keySet()) {
            for (Rule.Entry e : first.keySet()) {
                if (e.getValue().equals(n.getVertex().getValue())) {
                    first.get(n.getVertex()).addAll(nodesFirst.get(n));
                }
            }
        }
    }


    private void calcFollowForNode(ParseTree.Node parent, ParseTree.Node child) {
        ParseTree.Node s;
        nodesFollow.put(child, new ArrayList<>());
        int i = parent.getBindings().indexOf(child);
        if (i != parent.getBindings().size() - 1) {
            boolean epsilon = false;
            s = parent.getBindings().get(i + 1);
            ArrayList<Rule.Entry> curFirst;
            if(!s.getVertex().getValue().equals("eps") && !s.getVertex().getIsTerm()) {
                curFirst = new ArrayList<>(first.get(s.getVertex()));
            }
            else curFirst = new ArrayList<>(nodesFirst.get(s));
            for (int j = 0; j < curFirst.size(); j++) {
                if (curFirst.get(j).getValue().equals("eps")) {
                    epsilon = true;
                    curFirst.remove(j);
                }
            }
            if (!curFirst.isEmpty()) {
                nodesFollow.get(child).addAll(curFirst);
            }
            if(epsilon){
                nodesFollow.get(child).addAll(nodesFollow.get(parent));
            }
        } else {
            nodesFollow.get(child).addAll(nodesFollow.get(parent));
        }
    }

    private void calcFollowAllNodes() {
        for(ParseTree.Node n : graph.getNodes()){
            for(ParseTree.Node s : n.getBindings()){
                calcFollowForNode(n, s);
            }
        }
    }

    private void calcFollow() {
        calcFollowAllNodes();
        for (ParseTree.Node n : nodesFollow.keySet()) {
            //System.out.println(n + " " + nodesFollow.get(n));
            for (Rule.Entry e : follow.keySet()) {
                if (e.getValue().equals(n.getVertex().getValue())) {
                    follow.get(n.getVertex()).addAll(nodesFollow.get(n));
                }
            }
        }
    }

    HashMap<Rule.Entry, HashSet<Rule.Entry>> getFirst() {
        System.out.println();
        for (Rule.Entry e : first.keySet()) {
            System.out.println(e.getValue() + " " + first.get(e));
        }
        return first;
    }

    HashMap<Rule.Entry, HashSet<Rule.Entry>> getFollow(){
        System.out.println();
        for (Rule.Entry e : follow.keySet()) {
            System.out.println(e.getValue() + " " + follow.get(e));
        }
        return follow;
    }
}
