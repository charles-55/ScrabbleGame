import javax.swing.*;
import java.awt.*;
import java.awt.color.ICC_ColorSpace;

/**
 * This class is part of the "Scrabble" application.
 * A (playing) Tile in a scrabble game representing a letter and point.
 *
 * This Tile creates a new tile for the game of scrabble every time it is called.
 * The Tile stores its point value and is used to get the letter, point or
 * a string description of each Tile of the game.
 *
 * @author Meyiwa Temile
 * @version 1.0
 */
public class Tile
{
    private char letter;
    public int points;
    private final ImageIcon icon;

    /**
     * Constructor for objects of class Tile, initializes letter and points with the given parameters.
     *
     * @param letter char representation of scrabble tile
     * @param points the point value for the letter
     */
    public Tile(char letter, int points)
    {
        this.letter = letter;
        this.points = points;

        try {
            Image img = new ImageIcon("Graphics/" + letter + ".png").getImage();
            Image resizeImaged = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            icon = new ImageIcon(resizeImaged);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ImageIcon getIcon() {
        return icon;
    }

    /**
     * Update the tile's letter to the players new letter if the current letter is a blank tile and
     * return true, else return false if the letter is not a blank tile.
     *
     * @param newLetter new char letter to update current letter
     * @return boolean true if letter was updated, else false
     */
    public boolean setBlankTileLetter(char newLetter) {
        if(this.letter == '-') {
            this.letter = newLetter;
        }
        return false;
    }

    /**
     * Return the letter of Tile.
     *
     * @return letter current letter of tile
     */
    public char getLetter()
    {
        return letter;
    }

    /**
     * Return the points of Tile.
     *
     * @return points current points of tile
     */
    public int getPoints()
    {
        return points;
    }

    /**
     * Return a String description of Tile, consisting of letter and point.
     *
     * @return String description of tile
     */
    public String toString()
    {
        return "Letter: "+this.getLetter() +" Points: "+ this.getPoints();
    }
}
