/**
 * Created by seven-teen on 13.05.17.
 */
class ErrorMessage {
    private String text;
    private Position pos;

    ErrorMessage(String text, Position pos){
        this.text = text;
        this.pos = pos;
    }

    @Override
    public String toString(){
        return "Error at (" + pos.toString() + "): " + text;
    }
}
