/**
 * The Rack class.
 *
 * @author Ejeh Leslie 101161386
 * @version 1.0
 */
public class Rack {

    private Tile[] tiles;
    private final int MAX_RACK_SIZE = 7;

    /**
     * Create and initialize the Rack.
     */

    public Rack() {
        this.tiles = new Tile[MAX_RACK_SIZE];
    }

    /**
     * takes a tile from the bag and fills the rack up
     * @param bag The Bag to be processed.
     * @return true if the bag was filled , false otherwise.
     */
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
    /**
     * checks the bag and true  if the amount of tile to be exhanged was succesful
     * @param bag and an array of int.
     * @return true if the tile was exchanged succesfully , false otherwise.
     */
    public boolean exchangeTiles(Bag bag,int[] index){
        for(int i : index) {
            if(!bag.isEmpty()) {
                tiles[i] = bag.swapTile(tiles[i]);
            }
        }
        return true;
    }
    /**
     * returns the array of  tile on the rack
     * @return an array of tile
     */
    public Tile[] getTiles(){
        return tiles;
    }

    /**
     * gets the tiles character and string
     * @return String  of the tiles character and points
     */
    public String toString(){
        String characterAndPoints="| ";
        for(Tile t:tiles){
            if(t!=null){
                characterAndPoints += t.toString() +" | ";
            }
        }
        return characterAndPoints;
    }

    /**
     * removes a tile from the rack
     * @return true if tile removed else otherwise
     * @param tile to remove a tile
     */

    public boolean removeTile(Tile tile) {
        for(int i = 0; i < MAX_RACK_SIZE; i++) {
            if(tiles[i] == tile) {
                tiles[i] = null;
                return true;
            }
        }
        return false;
    }
}
