import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
/**
 * Created by seven-teen on 21.05.17.
 */
class Printer {

    private ParseTree graph;


    Printer(ArrayList<Object> rules) throws ParseException{
        this.graph = new ParseTree();
        graph.initTree(rules);
        //graph.removeTokens();
    }

    void print(){
        for(ParseTree.Node n : graph.getNodes()){
            System.out.println(n.toString());
            System.out.println(n.getBindings());
        }
    }

    void toPDF(){
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("out.gv"), "utf-8"))) {
            writer.write("digraph G{\n");
            for(ParseTree.Node n : graph.getNodes()){
                for(ParseTree.Node s : n.getBindings()) {
                    String val = s.getVertex().toString();
                    if(val.contains("\"")){
                        val = "\\" + val.substring(1, val.length()-2) + "\\\"";
                    }
                    //я знаю, что так писать нельзя, конечно. Но что поделать?
                    if(!s.getIsToken()) {
                        writer.write("{" + n.hashCode() + "[label=\"" + n.getVertex() + "\"]} -> {" + s.hashCode() + "[label=\"" + val + "\"]}\n");
                    }
                    else {
                        writer.write("{" + n.hashCode() + "[label=\"" + n.getVertex() + "\"]} -> {" + s.hashCode() + "[label=\"" + val + "\"][shape = box]}\n");
                    }
                }
            }
            writer.write("}");
        }
        catch (IOException e){
            e.printStackTrace();
        }
        try {
            Runtime.getRuntime().exec("dot -Tpdf out.gv -o out.pdf");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
