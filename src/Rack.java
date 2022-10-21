public class Rack {

    private Tile[] tiles;
    private final int MAX_RACK_SIZE = 7;


    public Rack() {
        this.tiles = new Tile[MAX_RACK_SIZE];
    }
    public boolean fillRack(Bag bag){
        boolean filled=true;
        for(int i=0;i<MAX_RACK_SIZE;i++){
            if(tiles[i]==null){
                tiles[i]=bag.drawTile();
                filled=!(tiles[i]==null);

            }

        }
        return filled;
    }
    public boolean exchangeTiles(Bag bag,int[] index){
        for(int i : index) {
            if(!bag.isEmpty()) {
                tiles[i] = bag.swapTile(tiles[i]);
            }
        }
        return true;
    }
    public Tile[] getTiles(){
        return tiles;
    }
    public String toString(){
        String characterAndPoints="";
        for(Tile t:tiles){
            if(t!=null){
                characterAndPoints=t.getLetter()+" : "+ t.getPoints()+" | ";
            }
        }
        return characterAndPoints;
    }

}
