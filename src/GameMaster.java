import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The GameMaster class.
 * This class is responsible for running the game and making sure the rules are kept.
 *
 * @author Osamudiamen Nwoko 101152520
 * @version 2.0
 */
public class GameMaster {

    private Board board;
    private Bag bag;
    private Player[] players;
    private int turn;
    private String gameFileName;
    private ArrayList<ScrabbleView> views;
    private static final int MIN_PLAYERS = 2, MAX_PLAYERS = 4;
    private static final String DICTIONARY = "src/WordList.txt";

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
            view.handleAboutCall(aboutMessage);
        }
    }

    /**
     * Prompt a help message on all views.
     */
    public void help() {
        String helpMessage = """
                For making any move, enter "play" followed by the word to play.
                Then enter the starting coordinates, and after the prompt of the direction to play, enter "DOWNWARD" or "FORWARD".
                Every play must be connected to at least one previous tile, if it's not the first move of the game.
                If you can't come up with a word, enter "pass" to skip your turn or "exchange" to get new tiles and skip your turn.
                You can't give up turn in the first move.
                If you make an illegal move or a wrong word, you will be notified and required to enter a new word and direction of play.
                """;
        for(ScrabbleView view : views) {
            view.handleHelpCall(helpMessage);
        }
    }

    /**
     * Attempt to play a word on the board.
     * @param wordAttempt The word to attempt to play.
     * @param coordinates The coordinates of the starting point to play.
     * @param direction The direction of the play.
     * @return true if the play was successful, false otherwise.
     */
    public boolean attemptPlay(String wordAttempt, int[] coordinates, Board.Direction direction) {
        int blankTileAmount = 0;

        /* Get the tiles from the player */
        Tile[] tilesToPlay = new Tile[wordAttempt.length()];
        boolean connected = false;
        for(int i = 0; i < wordAttempt.length(); i++) {
            if(board.getBoard()[coordinates[1]][coordinates[0] + i].getTile() != null) {
                if((board.getBoard()[coordinates[1]][coordinates[0] + i].getTile().getLetter() == wordAttempt.charAt(i)) && (direction == Board.Direction.FORWARD)) {
                    connected = true;
                    tilesToPlay[i] = board.getBoard()[coordinates[1]][coordinates[0] + i].getTile();
                    continue;
                }
            }
            if(board.getBoard()[coordinates[1] + i][coordinates[0]].getTile() != null) {
                if((board.getBoard()[coordinates[1] + i][coordinates[0]].getTile().getLetter() == wordAttempt.charAt(i)) && (direction == Board.Direction.DOWNWARD)) {
                    connected = true;
                    tilesToPlay[i] = board.getBoard()[coordinates[1] + i][coordinates[0]].getTile();
                    continue;
                }
            }
            for(Tile tile : players[turn].getRack().getTiles()) {
                if(tile.getLetter() == '-')
                    blankTileAmount++;
                else if(tile.getLetter() == wordAttempt.charAt(i)) {
                    tilesToPlay[i] = tile;
                    break;
                }
            }
            if(tilesToPlay[i] == null) {
                if(direction == Board.Direction.FORWARD) {
                    try {
                        if(blankTileAmount > 0) {
                            for(Tile tile : players[turn].getRack().getTiles()) {
                                if(tile.getLetter() == '-') {
                                    for(ScrabbleView view : views) {
                                        tile.setBlankTileLetter(view.handleBlankTile());
                                    }
                                }
                                if(tile.getLetter() == wordAttempt.charAt(i)) {
                                    tilesToPlay[i] = tile;
                                    blankTileAmount--;
                                }
                            }
                        }
                        else {
                            for(ScrabbleView view : views) {
                                JOptionPane.showMessageDialog((JFrame) view, "You do not have the tiles to spell \"" + wordAttempt + "\".");
                            }
                            return false;
                        }
                    }
                    catch(NullPointerException exception) {
                        for(ScrabbleView view : views) {
                            JOptionPane.showMessageDialog((JFrame) view, "You do not have the tiles to spell \"" + wordAttempt + "\".");
                        }
                        return false;
                    }
                }
                if(direction == Board.Direction.DOWNWARD) {
                    try {
                        if(blankTileAmount > 0) {
                            for(Tile tile : players[turn].getRack().getTiles()) {
                                if (tile.getLetter() == '-') {
                                    for(ScrabbleView view : views) {
                                        tile.setBlankTileLetter(view.handleBlankTile());
                                    }
                                }
                                if(tile.getLetter() == wordAttempt.charAt(i)) {
                                    tilesToPlay[i] = tile;
                                    blankTileAmount--;
                                }
                            }
                        }
                        else {
                            for(ScrabbleView view : views) {
                                JOptionPane.showMessageDialog((JFrame) view, "You do not have the tiles to spell \"" + wordAttempt + "\".");
                            }
                            return false;
                        }
                    }
                    catch(NullPointerException exception) {
                        for(ScrabbleView view : views) {
                            JOptionPane.showMessageDialog((JFrame) view, "You do not have the tiles to spell \"" + wordAttempt + "\".");
                        }
                        return false;
                    }
                }
            }
        }

        if((!connected) && (!board.isEmpty())) {
            for(ScrabbleView view : views) {
                JOptionPane.showMessageDialog((JFrame) view, "You have to connect your word to a previously spelt word!");
            }
            return false;
        }

        try {
            /* Check if the word exists */
            Scanner dictionary = new Scanner(new File(DICTIONARY));
            while(dictionary.hasNextLine()) {
                if(wordAttempt.equalsIgnoreCase(dictionary.nextLine())) {
                    /* If word exists, attempt to play it on the board */
                    if(board.attemptPlay(tilesToPlay, coordinates, direction)) {
                        /* If word is playable */
                        players[turn].addPlayedWords(wordAttempt);
                        for(Tile tile : tilesToPlay) {
                            players[turn].getRack().removeTile(tile);
                        }
                        players[turn].updateScore(board.getScore(coordinates, direction));
                        players[turn].getRack().fillRack(bag);
                        for(ScrabbleView view : views) {
                            view.handleBoardUpdate(wordAttempt, coordinates, direction);
                            view.handleScoreUpdate();
                            view.handleRackUpdate();
                        }
                        changeTurn();
                        return true;
                    }
                    else {
                        for(ScrabbleView view : views) {
                            JOptionPane.showMessageDialog((JFrame) view, "You can not play there!");
                        }
                        return false;
                    }
                }
            }
            for(ScrabbleView view : views) {
                JOptionPane.showMessageDialog((JFrame) view, "\"" + wordAttempt + "\" does not exist.");
            }
        }
        catch (FileNotFoundException exception) {
            for(ScrabbleView view : views) {
                JOptionPane.showMessageDialog((JFrame) view, "Dictionary not found");
            }
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
            System.out.println("Bag size is below 7, cannot exchange tiles!");
            return false;
        }

        if(players[turn].getRack().exchangeTiles(bag, tilesToExchangeIndices)) {
            for(ScrabbleView view : views) {
                view.handleRackUpdate();
            }
            System.out.println(players[turn].getRack().toString());
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
            if(gameFileName.equals("New Game")) {
                System.out.println("Use the saveAs command to name the current game.");
                return false;
            }
            File gameFile = new File(gameFileName + ".txt");
            if(gameFile.createNewFile()) {
                FileWriter fileWriter = new FileWriter(gameFile);
                fileWriter.write(this.toString());
                fileWriter.close();
                return true;
            }
        }
        catch (IOException e) {
            System.out.println("An error occurred!");
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
