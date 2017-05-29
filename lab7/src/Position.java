/**
 * Created by seven-teen on 13.05.17.
 */
class Position {

    private int index;
    private int line;
    private int col;

    Position(Position pos){
        this.index = pos.index;
        this.line = pos.line;
        this.col = pos.col;
    }

    Position(){
        this.index = 0;
        this.line = 1;
        this.col = 1;
    }

    void incLine() {this.line ++;}

    void incPos() {this.col ++;}

    void inc(int inc){
        this.index += inc;
        this.col += inc;
    }

    void resetPos() {this.col = 1;}

    int getIndex() {
        return index;
    }

    void incIndex() {
        this.index ++;
    }

    @Override
    public String toString(){
        return  this.line + ", " + this.col;
    }
}
