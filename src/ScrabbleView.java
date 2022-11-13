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

    /**
     * Handle a message from the model.
     * @param message The message from the model.
     */
    void handleMessage(String message);
}
