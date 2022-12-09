import java.io.Serializable;
import java.util.*;

/**
 * This class is part of the "Scrabble" application.
 * A Bag is an ArrayList of Tiles consisting all the Tiles played in a scrabble game.
 *
 * @author Meyiwa Temile
 * @version 1.0
 */
public class Bag implements Serializable {
    private ArrayList <Tile> bag = new ArrayList <Tile>();
    private final Random random  = new Random ();

    /**
     * Constructor for objects of class Bag, initializes bag to contain the default amount
     * of tiles in a scrabble game as an ArrayList.
     */
    public Bag()
    {
        this.resetBag();
    }

    /**
     * Creates and insert the default tiles onto the board.
     *
     * @param frequency number of tiles
     * @param letter char representation of scrabble tile
     * @param points the point value for the letter
     */
    private void addTiles(int frequency, char letter, int points)
    {
        for(int i=0; i<frequency; i++)
        {
            bag.add(new Tile(letter,points));
        }
    }

    /**
     * Checks if there are any letters in the board.
     *
     * @return boolean true if the Bag is empty, else false
     */
    public boolean isEmpty()
    {
        return bag.isEmpty();
    }

    /**
     * Swap tile for any tile in bag if there is any
     * swapped tile, else return null.
     *
     * @param tile Tile requested to be swapped
     * @return returnTile swapped Tile from the bag
     */
    public Tile swapTile(Tile tile)
    {
        Tile returnTile = this.drawTile();
        bag.add(tile);
        return returnTile;
    }

    /**
     * Return the number of Tile in bag
     * @return int size of bag
     */
    public int getBagSize()
    {
        return bag.size();
    }

    /**
     * Checks of there is any Tile in bag and return a random Tile from bag if there is, else
     * return null.
     *
     * @return Tile a new Tile from the bag
     */

    public Tile drawTile() {
        if(!isEmpty())
            return bag.remove(random.nextInt(bag.size()));
        else
            return null;
    }

    /**
     * Creates a new bag and add the default number of tiles into bag.
     */
    private void resetBag() {
        bag = new ArrayList <Tile>();
        addTiles (2, '-', 0);
        addTiles (12, 'E', 1);
        addTiles (9, 'A', 1);
        addTiles (9, 'I', 1);
        addTiles (8, 'O', 1);
        addTiles (6, 'N', 1);
        addTiles (6, 'R', 1);
        addTiles (6, 'T', 1);
        addTiles (4, 'L', 1);
        addTiles (4, 'S', 1);
        addTiles (4, 'U', 1);
        addTiles (4, 'D', 2);
        addTiles (3, 'G', 2);
        addTiles (2, 'B', 3);
        addTiles (2, 'C', 3);
        addTiles (2, 'M', 3);
        addTiles (2, 'P', 3);
        addTiles (2, 'F', 4);
        addTiles (2, 'H', 4);
        addTiles (2, 'V', 4);
        addTiles (2, 'W', 4);
        addTiles (2, 'Y', 4);
        addTiles (1, 'K', 5);
        addTiles (1, 'J', 8);
        addTiles (1, 'X', 8);
        addTiles (1, 'Q', 10);
        addTiles (1, 'Z', 10);
    }
}
