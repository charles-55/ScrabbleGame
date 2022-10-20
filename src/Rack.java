public class Rack {

    private Tile[] tiles;
    private final int MAX_RACK_SIZE = 7;


    public Rack() {
        this.tiles = new Tile[MAX_RACK_SIZE];
    }
    public void fillRack(){
        for(int i=0; i<7;i++){
            tiles[i]=new Tile();
        }
    }
    public void exchangeTiles(int[] tilesToExchangeIndex){
        for(int i=0;i<7;i++){

        }



    }
    public Tile[] getTiles(){
        return tiles;
    }
    public String toString(){
        String characterAndPoints;
        for(Tile t:tiles){
            characterAndPoints="letter="+" "+t.getLetter()+" "+ t.getPoints()+"\n" ;
            return characterAndPoints;
        }
        return "this is all tiles";

    }

}
