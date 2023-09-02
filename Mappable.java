public abstract class Mappable {
    private int row;
    private int col;

    public Mappable(int r, int c) {
        row = r;
        col = c;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setRow(int r) {
        row = r;
    }

    public void setCol(int c) {
        col = c;
    }

    public void setLocation(int r, int c) {
        row = r;
        col = c;
    }

    //overloaded by Portal
    public String getMap() { return "";}

    //overloaded by Portal
    public int getPlayerRow() {return 0;}

    //overloaded by Portal
    public int getPlayerCol() {return 0;}

    public abstract String getSymbol();

    public abstract int interactableID(); 
}