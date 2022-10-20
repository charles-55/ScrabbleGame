/**
 * A (playing) Tile is a letter and point.
 */
public class Tile
{
    private char letter;
    public int points;

    /**
     * Constructor for objects of class Tile, create a Tile given
     * a letter and points.
     */
    public Tile(char letter, int points)
    {
        this.letter = letter;
        this.points = points;
    }

    /**
     * Return the letter of Tile.
     */
    public char getLetter()
    {
        return letter;
    }

    /**
     * Return the points of Tile.
     */
    public int getPoints()
    {
        return points;
    }

    public boolean equals(Tile tile)
    {
        if((this.getLetter() == tile.getLetter()) && (this.getPoints() == tile.getPoints()))
        {
            return true;
        }
        return false;
    }

    public void tileDescription()
    {
        System.out.println("Letter: "+this.getLetter() +" Points: "+ this.getPoints());
    }
}
