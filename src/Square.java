import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.Serializable;

/**
 * This class concerns the squares on the scrabble board.
 *
 * @author LONGJOHN DAGOGO
 * @version 1.0
 */
public class Square implements Serializable {

    private Tile tile;
    private SquareType squareType;
    private ImageIcon icon;
    public enum SquareType {BLANK, ORIGIN, DLS, TLS, DWS, TWS}

    /**
     * Constructor for Square class.
     */
    public Square() {
        tile = Tile.getDefaultTile();
        squareType = SquareType.BLANK;
        File image = new File("src/Graphics/BLANK.png");
        if(image.exists())
            icon = new ImageIcon(image.toString());
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
        Image image = new ImageIcon("src/Graphics/" + squareType.toString() + ".png").getImage();
        Image resizedImage = image.getScaledInstance(50,50, Image.SCALE_SMOOTH);
        if(icon == null)
            icon = new ImageIcon(image);
        else
            icon.setImage(resizedImage);
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    /**
     * Places a tile on the square.
     * @param tileToPlace
     * @return false if the tile is null(no tile) else return true.
     */
    public boolean placeTile(Tile tileToPlace){
        if(tile == tileToPlace)
            return true;
        if(tile != Tile.getDefaultTile())
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
