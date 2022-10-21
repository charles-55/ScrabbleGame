import java.util.*;

/**
 * A Bag is an Arraylsit of Tiles.
 *
 * @author Group 3
 * @version 1.0
 */
public class Bag
{
    private ArrayList <Tile> bag = new ArrayList <Tile>();
    private Random random  = new Random ();

    /**
     * Constructor for objects of class Bag
     */
    public Bag()
    {
        this.resetBag();
    }

    /**
     * Add the default number of Tile to bag
     */
    private void addTiles(int frequency, char letter, int points)
    {
        for(int i=0; i<frequency; i++)
        {
            bag.add(new Tile(letter,points));
        }
    }

    /**
     * Return true if the Bag is empty, else false
     */
    public boolean isEmpty()
    {
        return bag.isEmpty();
    }

    /**
     * Swap tile for tile in bag if there is any
     */
    public Tile swapTile(Tile tile)
    {
        Tile returnTile = bag.remove(random.nextInt(bag.size()));
        bag.add(tile);
        return returnTile;
    }

    /**
     * Return a random Tile from bag
     */

    public Tile drawTile()
    {
        if(bag.isEmpty())
        {
            return null;
        }
        return bag.remove(random.nextInt(bag.size()));
    }

    public void resetBag() {
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
