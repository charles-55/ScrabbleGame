import javax.swing.*;
import java.awt.*;

/**
 * This class concerns the squares on the scrabble board.
 *
 * @author LONGJOHN DAGOGO
 * @version 1.0
 */
public class Square {

    private Tile tile;
    private SquareType squareType;
    private ImageIcon icon;
    public enum SquareType {ORIGIN, DLS, TLS, DWS, TWS}

    /**
     * Constructor for Square class.
     */
    public Square() {
        tile = null;
        icon = new ImageIcon();
    }

    /**
     * Gets the tile.
     * @return the tile.
     */
    public Tile getTile() {
        return tile;
    }

    public SquareType getSquareType() {
        return squareType;
    }

    public void setSquareType(SquareType squareType) {
        this.squareType = squareType;
//        ImageIcon imageIcon = n
//        icon.setImage(new ImageIcon());
    }

    public ImageIcon getIcon() {
        return icon;
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
