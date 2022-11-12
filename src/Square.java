/**
 * This class concerns the squares on the scrabble board.
 *
 * @author LONGJOHN DAGOGO
 * @version 1.0
 */
public class Square {

    private Tile tile;

    /**
     * Constructor for Square class.
     */
    public Square() {
        tile = null;
    }

    /**
     * Gets the tile.
     * @return the tile.
     */
    public Tile getTile() {
        return tile;
    }

    /**
     * Places a tile on the square.
     * @param tileToPlace
     * @return false if the tile is null(no tile) else return true.
     */
    public boolean placeTile(Tile tileToPlace){
        if(tile == tileToPlace)
            return true;
        if(tile != null)
            return false;
        tile = tileToPlace;
        return true;
    }

    /**
     * Remove a tile from the square.
     */
    public void removeTile() {
        tile = null;
    }
}
