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
     * Handle the score update.
     */
    void handleScoreUpdate();

    /**
     * Handle the rack update();
     */
    void handleRackUpdate();
}
