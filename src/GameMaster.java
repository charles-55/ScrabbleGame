public class GameMaster {

    private Board board;
    private Bag bag;
    private Player[] players;
    private int turn;
    private Parser parser;

    /**
     * Create and initialize the game.
     */
    public GameMaster() {

    }

    /**
     * Return the opening message for the player.
     * @return The opening message.
     */
    private String getWelcomeMessage() {
        return "";
    }

    /**
     * Initialize all the players.
     * @param numPlayers The number of players playing the game.
     */
    private void initializePlayers(int numPlayers) {

    }

    /**
     * Given a command, process (that is: execute) the command
     * @param command The command to be processed.
     * @return true if the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) {
        return false;
    }

    // TODO: 2022-10-18 complete implementation
    /**
     * Return a message of help information and
     * a list of command words.
     * @return A message of help information and
     * a list of command words.
     */
    private String help() {
        return "";
    }

    // TODO: 2022-10-18 complete implementation
    /**
     * Attempts to play a particular word on the board.
     * @param command Command containing the word to play.
     * @return true if the word is playable on the board,
     * false otherwise.
     */
    private boolean attemptPlay(Command command) {
        return false;
    }

    /**
     * Changes the players' turn.
     */
    private void changeTurn() {

    }

    // TODO: 2022-10-18 complete implementation
    /**
     * Save the current game being played.
     * @param command Command to save the game.
     * @return true if the game saved, false otherwise.
     */
    private boolean save(Command command) {
        return false;
    }

    // TODO: 2022-10-18 complete implementation
    /**
     * Load a saved game.
     * @param command Command to load the saved game
     * @return true if the game loaded, false otherwise.
     */
    private boolean load(Command command) {
        return false;
    }

    /**
     * Quits the game.
     * @param command command to quit the game.
     * @return true if command quits the game,
     * false otherwise.
     */
    private boolean quit(Command command) {
        return false;
    }

    /**
     * Main play routine.  Loops until end of play.
     */
    public void playGame() {

    }
}
