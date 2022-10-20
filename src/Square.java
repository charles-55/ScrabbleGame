public class Square {

    private Tile tile;

    public Square() {
        tile = null;
    }

    public Tile getTile() {
        return tile;
    }

    public boolean placeTile(Tile tileToPlace){
        if(tile != null)
            return false;
        tile = tileToPlace;
        return true;
    }
}
