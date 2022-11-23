import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * The GameMaster class.
 * This class is responsible for running the game and making sure the rules are kept.
 *
 * @author Osamudiamen Nwoko 101152520
 * @version 3.0
 */
public class GameMaster {

    private final Board board;
    private final Bag bag;
    private Player[] players;
    private int turn;
    private String gameFileName;
    private final ArrayList<ScrabbleView> views;
    private static final int MIN_PLAYERS = 2, MAX_PLAYERS = 4;
    public static final String DICTIONARY = "src/WordList.txt";

    /**
     * Create and initialize the game.
     */
    public GameMaster() {
        board = new Board();
        bag = new Bag();
        turn = 0;
        gameFileName = "New Game";
        views = new ArrayList<>();
    }

    /**
     * Return the board.
     * @return The board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Return the index of the current player's turn.
     * @return The index of the current player's turn.
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Changes the players' turn.
     */
    public void changeTurn() {
        turn = (turn + 1) % players.length;
        for(ScrabbleView view : views)
            view.handleChangeTurn(players[turn].getName());
        if(players[turn].isAI()) {
            int randomNumber = (new Random()).nextInt(3);
            switch (randomNumber) {
                case (0) -> attemptPlay(players[turn].play(board));
                case (1) -> changeTurn();
                case (2) -> {
                    randomNumber = (new Random()).nextInt(8);
                    int[] indices = new int[randomNumber];
                    for (int i = 0; i < randomNumber; i++) {
                        int randomIndex = (new Random()).nextInt(randomNumber);
                        while (List.of(indices).contains(randomIndex)) {
                            randomIndex = (new Random()).nextInt(randomNumber);
                        }
                        indices[i] = randomIndex;
                    }
                    exchangeTile(indices);
                }
            }

        }
    }

    /**
     * Return a list of the players in the game.
     * @return List of players.
     */
    public Player[] getPlayers() {
        return players;
    }

    /**
     * Add a player to the game
     * @param player Player to add.
     * @return true if player was added, false otherwise.
     */
    public boolean addPlayer(Player player) {
        for(int i = 0; i < players.length; i++) {
            if(players[i] == null) {
                player.getRack().fillRack(bag);
                players[i] = player;
                return true;
            }
        }
        return false;
    }

    /**
     * Set the size of the players of the game.
     * @param playerSize The size of the players of the game.
     */
    public boolean setPlayerSize(int playerSize) {
        if((MIN_PLAYERS <= playerSize) && (playerSize  <= MAX_PLAYERS)) {
            players = new Player[playerSize];
            return true;
        }
        return false;
    }

    /**
     * Return the game filename.
     * @return The game file name.
     */
    public String getGameFileName() {
        return gameFileName;
    }

    /**
     * Set the game filename.
     * @param gameFileName The game filename
     */
    public void setGameFileName(String gameFileName) {
        this.gameFileName = gameFileName;
    }

    /**
     * Add a view to the list of views.
     * @param view The view to add.
     */
    public void addView(ScrabbleView view) {
        views.add(view);
    }

    public ScrabbleView getView(int i) {
        return views.get(i);
    }

    /**
     * Return the min number of players.
     * @return The min number of players.
     */
    public int getMinPlayers() {
        return MIN_PLAYERS;
    }

    /**
     * Return the max number of players.
     * @return The max number of players.
     */
    public int getMaxPlayers() {
        return MAX_PLAYERS;
    }

    /**
     * Return a string representation of the game.
     * @return A string representation of the game.
     */
    @Override
    public String toString() {
        StringBuilder gameToString = new StringBuilder("---------------------------------------------------------------\n");
        gameToString.append(gameFileName).append("\n\n");

        for(Player player : players) {
            gameToString.append(player.toString()).append("\n");
        }
        gameToString.append("\n").append(board.toString()).append("\n");
        gameToString.append("---------------------------------------------------------------");

        return gameToString.toString();
    }

    /**
     * Prompt a message about scrabble on all views.
     */
    public void about() {
        String aboutMessage =  """
                Welcome to the game of Scrabble!
                
                Scrabble is a board-and-tile game in which two to four players compete in forming words with
                lettered tiles on a 225-square board; words spelled out by letters on the tiles interlock like
                words in a crossword puzzle. Players draw seven tiles from a pool at the start and replenish
                their supply after each turn.
                
                Click 'help' if you need help.
                """;
        for(ScrabbleView view : views) {
            view.handleMessage(aboutMessage);
        }
    }

    /**
     * Prompt a help message on all views.
     */
    public void help() {
        String helpMessage = """
                For making any move, select the position on the board you intend to play, then enter the word and direction to play.
                Every play must be connected to at least one previous tile, if it's not the first move of the game.
                If you can't come up with a word, select "pass" to skip your turn or "exchange" to get new tiles and skip your turn.
                If you make an illegal move or a wrong word, you will be notified and required to enter a new word and direction of play.
                """;
        for(ScrabbleView view : views) {
            view.handleMessage(helpMessage);
        }
    }

    /**
     * Attempt to play a word on the board.
     * @param event The play event.
     * @return true if the play was successful, false otherwise.
     */
    public boolean attemptPlay(PlayEvent event) {
        int blankTileAmount = 0;

        /* Get the tiles from the player */
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

        try {
            /* Check if the word exists */
            Scanner dictionary = new Scanner(new File(DICTIONARY));
            while(dictionary.hasNextLine()) {
                if(event.getWordAttempt().equalsIgnoreCase(dictionary.nextLine())) {
                    /* If word exists, attempt to play it on the board */
                    if(board.attemptPlay(tilesToPlay, event.getCoordinates(), event.getDirection())) {
                        /* If word is playable */
                        players[turn].addPlayedWords(event.getWordAttempt());
                        for(Tile tile : tilesToPlay) {
                            players[turn].getRack().removeTile(tile);
                        }
                        players[turn].updateScore(board.getScore(event.getCoordinates(), event.getDirection()));
                        players[turn].getRack().fillRack(bag);
                        for(ScrabbleView view : views) {
                            view.handleBoardUpdate(event.getWordAttempt(), event.getCoordinates(), event.getDirection());
                            view.handleScoreUpdate();
                            view.handleRackUpdate();
                        }
                        changeTurn();
                        return true;
                    }
                    else {
                        for(ScrabbleView view : views)
                            view.handleMessage("You can not play there!");
                        return false;
                    }
                }
            }
            for(ScrabbleView view : views)
                view.handleMessage("\"" + event.getWordAttempt() + "\" does not exist.");
        }
        catch (FileNotFoundException exception) {
            for(ScrabbleView view : views)
                view.handleMessage("Dictionary not found");
            return false;
        }

        return false;
    }

    /**
     * Exchange the tiles in a player's rack given the indices of the tiles to exchange
     * @param tilesToExchangeIndices The indices of the tiles to exchange
     * @return true if exchange was successful, false otherwise
     */
    public boolean exchangeTile(int[] tilesToExchangeIndices) {
        if((tilesToExchangeIndices.length < 1) || (tilesToExchangeIndices.length > 7))
            return false;

        if(bag.getBagSize() < 7) {
            for(ScrabbleView view : views)
                view.handleMessage("Bag size is below 7, cannot exchange tiles!");
            return false;
        }

        if(players[turn].getRack().exchangeTiles(bag, tilesToExchangeIndices)) {
            for(ScrabbleView view : views) {
                view.handleRackUpdate();
            }
            changeTurn();
            return true;
        }

        return false;
    }

    /**
     * Reset the current game to a new game.
     */
    public void newGame() {
        for(ScrabbleView view : views) {
            view.handleNewGameUpdate();
        }
    }

    /**
     * Quits the game.
     */
    public void quit() {
        for(ScrabbleView view : views) {
            view.handleQuitUpdate();
        }
    }
    // TODO: 2022-10-18 complete implementation

    /**
     * Save the current game being played.
     * @return true if the game saved, false otherwise.
     */
    private boolean save() {
        try {
            File gameFile = new File(gameFileName + ".txt");
            if(gameFile.createNewFile()) {
                FileWriter fileWriter = new FileWriter(gameFile);
                fileWriter.write(this.toString());
                fileWriter.close();
                return true;
            }
        }
        catch (IOException e) {
            for(ScrabbleView view : views)
                view.handleMessage("An error occurred!");
        }

        return false;
    }

    /**
     * Save the current game using the specified name.
     * @param newName New game name.
     * @return true if the game saved, false otherwise.
     */
    private boolean saveAs(String newName) {
        gameFileName = newName;
        return save();
    }
    // TODO: 2022-10-18 complete implementation

    /**
     * Load a saved game.
     * @return true if the game loaded, false otherwise.
     */
    private boolean load() {
        return false;
    }
}
