public interface ScrabbleView {

    /**
     * Handle new game update to the view.
     */
    void handleNewGameUpdate();

    /**
     * Handle quit update to the view.
     */
    void handleQuitUpdate();

    /**
     * Handle help call in the view.
     * @param message Help message to display.
     */
    void handleHelpCall(String message);

    /**
     * Handle about call in the view.
     * @param message About message to display.
     */
    void handleAboutCall(String message);

    /**
     * Handle the change turn update.
     * @param playerName The name of the player with the updated turn.
     */
    void handleChangeTurn(String playerName);

    /**
     * Handle the board update.
     */
    void handleBoardUpdate(String word, int[] coordinates, Board.Direction direction);

    /**
     * Handle the score update.
     */
    void handleScoreUpdate();

    /**
     * Handle the rack update();
     */
    void handleRackUpdate();

    /**
     * Get a letter to set the blank tile.
     */
    char handleBlankTile();
}
