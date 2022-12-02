public class Main {

    public static void main(String[] args) {
        new ScrabbleFrame();
    }

    /*

    int blankTileAmount = 0;

    Tile[] tilesToPlay = new Tile[event.getWordAttempt().length()];
    boolean connected = false;
        for(int i = 0; i < event.getWordAttempt().length(); i++) {
        if(board.getBoard()[event.getCoordinates()[1]][event.getCoordinates()[0] + i].getTile() != null) {
            if((board.getBoard()[event.getCoordinates()[1]][event.getCoordinates()[0] + i].getTile().getLetter() == event.getWordAttempt().charAt(i)) && (event.getDirection() == Board.Direction.FORWARD)) {
                connected = true;
                tilesToPlay[i] = board.getBoard()[event.getCoordinates()[1]][event.getCoordinates()[0] + i].getTile();
                continue;
            }
        }
        if(board.getBoard()[event.getCoordinates()[1] + i][event.getCoordinates()[0]].getTile() != null) {
            if((board.getBoard()[event.getCoordinates()[1] + i][event.getCoordinates()[0]].getTile().getLetter() == event.getWordAttempt().charAt(i)) && (event.getDirection() == Board.Direction.DOWNWARD)) {
                connected = true;
                tilesToPlay[i] = board.getBoard()[event.getCoordinates()[1] + i][event.getCoordinates()[0]].getTile();
                continue;
            }
        }
        for(Tile tile : players[turn].getRack().getTiles()) {
            if(tile.getLetter() == '-')
                blankTileAmount++;
            else if(tile.getLetter() == event.getWordAttempt().charAt(i)) {
                tilesToPlay[i] = tile;
                break;
            }
        }
        if(tilesToPlay[i] == null) {
            if(event.getDirection() == Board.Direction.FORWARD) {
                try {
                    if(blankTileAmount > 0) {
                        for(Tile tile : players[turn].getRack().getTiles()) {
                            if(tile.getLetter() == '-') {
                                for(ScrabbleView view : views) {
                                    tile.setBlankTileLetter(view.handleBlankTile());
                                }
                            }
                            if(tile.getLetter() == event.getWordAttempt().charAt(i)) {
                                tilesToPlay[i] = tile;
                                blankTileAmount--;
                            }
                        }
                    }
                    else {
                        for(ScrabbleView view : views)
                            view.handleMessage("You do not have the tiles to spell \"" + event.getWordAttempt() + "\".");
                        return false;
                    }
                }
                catch(NullPointerException exception) {
                    for(ScrabbleView view : views)
                        view.handleMessage("You do not have the tiles to spell \"" + event.getWordAttempt() + "\".");
                    return false;
                }
            }
            if(event.getDirection() == Board.Direction.DOWNWARD) {
                try {
                    if(blankTileAmount > 0) {
                        for(Tile tile : players[turn].getRack().getTiles()) {
                            if (tile.getLetter() == '-') {
                                for(ScrabbleView view : views) {
                                    tile.setBlankTileLetter(view.handleBlankTile());
                                }
                            }
                            if(tile.getLetter() == event.getWordAttempt().charAt(i)) {
                                tilesToPlay[i] = tile;
                                blankTileAmount--;
                            }
                        }
                    }
                    else {
                        for(ScrabbleView view : views)
                            view.handleMessage("You do not have the tiles to spell \"" + event.getWordAttempt() + "\".");
                        return false;
                    }
                }
                catch(NullPointerException exception) {
                    for(ScrabbleView view : views)
                        view.handleMessage("You do not have the tiles to spell \"" + event.getWordAttempt() + "\".");
                    return false;
                }
            }
        }
    }

        if((!connected) && (!board.isEmpty())) {
        for(ScrabbleView view : views)
            view.handleMessage("You have to connect your word to a previously spelt word!");
        return false;
    }
     */
}
