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
    private final Parser parser;
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
        parser = new Parser();
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
     * Given a command, process (that is: execute) the command
     * @param command The command to be processed.
     * @return true if the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("Invalid command!");
            return false;
        }

        String commandWord = command.getCommandWord();
        switch (commandWord) {
            case "exchange":
                if(exchangeTile(command)) {
                    System.out.println("It is " + players[turn].getName() + "'s turn.\n");
                }
                else
                    System.out.println("Exchange unsuccessful, try again.");
                break;
            case "pass":
                if(players[turn].returnsPlayedWords().size() == 0)
                    System.out.println("You cannot skip first turn!");
                else {
                    changeTurn();
                    System.out.println("It is " + players[turn].getName() + "'s turn.\n");
                }
                break;
            case "showBoard":
                System.out.println(board.toString());
                break;
            case "showRack":
                System.out.println(players[turn].getRack().toString());
                break;
            case "printGame":
                System.out.println(this);
                break;
            case "save":
                if(save(command))
                    System.out.println("Save successful");
                else
                    System.out.println("Save unsuccessful.");
                break;
            case "saveAs":
                if(saveAs(command))
                    System.out.println("Save As successful");
                else
                    System.out.println("Save As unsuccessful.");
                break;
            case "load":
                if(load(command))
                    System.out.println("Load successful");
                else
                    System.out.println("Load unsuccessful.");
                break;
        }

        return wantToQuit;
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

    public boolean attemptPlay(String wordAttempt, int[] coordinates, Board.Direction direction) {
        boolean hasABlankTile = false;

        /* Get the tiles from the player */
        Tile[] tilesToPlay = new Tile[wordAttempt.length()];
        boolean connected = false;
        for(int i = 0; i < wordAttempt.length(); i++) {
            if((board.getBoard()[coordinates[1]][coordinates[0] + i].getTile().getLetter() == wordAttempt.charAt(i)) && (direction == Board.Direction.FORWARD)) {
                connected = true;
                continue;
            }
            else if((board.getBoard()[coordinates[1] + i][coordinates[0]].getTile().getLetter() == wordAttempt.charAt(i)) && (direction == Board.Direction.DOWNWARD)) {
                connected = true;
                continue;
            }
            for(Tile tile : players[turn].getRack().getTiles()) {
                if(tile.getLetter() == '-')
                    hasABlankTile = true;
                if(tile.getLetter() == wordAttempt.charAt(i))
                    tilesToPlay[i] = tile;
            }
            if(tilesToPlay[i] == null) {
                if(direction == Board.Direction.FORWARD) {
                    try {
                        if(hasABlankTile) {
                            for(Tile tile : players[turn].getRack().getTiles()) {
                                if (tile.getLetter() == '-')
                                    tile.setBlankTileLetter(parser.getBlankTileLetter());
                                if(tile.getLetter() == wordAttempt.charAt(i))
                                    tilesToPlay[i] = tile;
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
                        if(hasABlankTile) {
                            for(Tile tile : players[turn].getRack().getTiles()) {
                                if (tile.getLetter() == '-')
                                    tile.setBlankTileLetter(parser.getBlankTileLetter());
                                if(tile.getLetter() == wordAttempt.charAt(i))
                                    tilesToPlay[i] = tile;
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
     * Exchange one or more tiles in a player's rack
     * @param command Command containing number of tiles to exchange.
     * @return true if exchange was successful, false otherwise.
     */
    private boolean exchangeTile(Command command) {
        if(!command.hasSecondWord()) {
            System.out.println("Exchange how many tiles?");
            return false;
        }
        if(bag.getBagSize() < 7) {
            System.out.println("Bag size is below 7, cannot exchange tiles!");
            return false;
        }

        try {
            int exchangeNum = Integer.parseInt(command.getSecondWord());
            int[] tilesToExchangeIndex = new int[exchangeNum];

            for(int i = 0; i < exchangeNum; i++) {
                tilesToExchangeIndex[i] = parser.getTileIndex(players[turn].getRack().getTiles());
            }

            if(players[turn].getRack().exchangeTiles(bag, tilesToExchangeIndex)) {
                System.out.println(players[turn].getRack().toString());
                changeTurn();
                return true;
            }
        }
        catch(NumberFormatException e) {
            System.out.println("Second word was not a number");
        }

        return false;
    }

    /**
     * Changes the players' turn.
     */
    public void changeTurn() {
        turn = (turn + 1) % players.length;
    }

    /**
     * Reset the current game to a new game.
     */
    public void newGame() {
        for(ScrabbleView view : views) {
            view.handleNewGameUpdate();
        }
    }

    // TODO: 2022-10-18 complete implementation
    /**
     * Save the current game being played.
     * @param command Command to save the game.
     * @return true if the game saved, false otherwise.
     */
    private boolean save(Command command) {
        if(command.hasSecondWord()) {
            System.out.println("Use the saveAs command to change the current game name.");
            return false;
        }

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
     * @param command Command containing the specified name.
     * @return true if the game saved, false otherwise.
     */
    private boolean saveAs(Command command) {
        if(!command.hasSecondWord()) {
            System.out.println("saveAs what?");
            return false;
        }

        gameFileName = command.getSecondWord();
        return save(new Command("save", null));
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
     */
    public void quit() {
        for(ScrabbleView view : views) {
            view.handleQuitUpdate();
        }
    }
}
