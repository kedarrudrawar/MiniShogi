public class Location {
    private int row;
    private int col;


    public Location(int col, int row){
        this.col = col;
        this.row = row;
    }

    public Location(String pos){
        this.col = pos.charAt(0) - 97;
        this.row = Character.getNumericValue(pos.charAt(1)) - 1;
    }

    public int getCol() {
        return this.col;
    }

    public int getRow(){
        return this.row;
    }

    @Override
    public String toString(){
        String loc = "";
        loc += (char)(this.getCol() + 97);
        loc += Integer.toString(this.getRow() + 1);

        return loc;
    }

}

