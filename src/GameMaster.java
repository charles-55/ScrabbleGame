import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * The GameMaster class.
 *
 * @author Osamudiamen Nwoko 101152520
 * @version 1.0
 */
public class GameMaster {

    private final Board board;
    private final Bag bag;
    private Player[] players;
    private int turn;
    private final Parser parser;
    private String gameFileName;
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
        playGame();
    }

    /**
     * Return the opening message for the player.
     * @return The opening message.
     */
    private String getWelcomeMessage() {
        return """
                Welcome to the game of Scrabble!
                
                Scrabble is a board-and-tile game in which two to four players compete in forming words with
                lettered tiles on a 225-square board; words spelled out by letters on the tiles interlock like
                words in a crossword puzzle. Players draw seven tiles from a pool at the start and replenish
                their supply after each turn.
                
                Type 'help' if you need help.
                """;
    }

    /**
     * Initialize all the players.
     * @param numPlayers The number of players playing the game.
     */
    private void initializePlayers(int numPlayers) {
        players = new Player[numPlayers];

        for(int i = 0; i < players.length; i++) {
            players[i] = new Player(parser.getPlayerName(i));
            if(!players[i].getRack().fillRack(bag))
                System.out.println("Bag is empty!");
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
            case "help":
                System.out.println(help());
                break;
            case "play":
                if(attemptPlay(command)) {
                    System.out.println(board.toString());
                    System.out.println("It is " + players[turn].getName() + "'s turn.\n");
                }
                else
                    System.out.println("Play unsuccessful, try again.");
                break;
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
            case "quit":
                wantToQuit = quit(command);
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
     * Return a message of help information and
     * a list of command words.
     * @return A message of help information and
     * a list of command words.
     */
    private String help() {
        return """
                Your command words are:
                help, play, exchange, pass, showBoard, showRack, printGame, quit, save, saveAs, load
                """;
    }

    /**
     * Attempts to play a particular word on the board.
     * @param command Command containing the word to play.
     * @return true if the word is playable on the board,
     * false otherwise.
     */
    private boolean attemptPlay(Command command) {
        if(!command.hasSecondWord()) {
            System.out.println("Play what?");
            return false;
        }

        String wordAttempt = command.getSecondWord().toUpperCase();
        int[] coordinates = parser.getCoordinates();
        Board.Direction direction = parser.getDirection();
        boolean hasABlankTile = false;

        /* Get the tiles from the player */
        Tile[] tilesToPlay = new Tile[wordAttempt.length()];
        boolean connected = false;
        for(int i = 0; i < wordAttempt.length(); i++) {
            for(Tile tile : players[turn].getRack().getTiles()) {
                if(tile.getLetter() == '-')
                    hasABlankTile = true;
                if(tile.getLetter() == wordAttempt.charAt(i))
                    tilesToPlay[i] = tile;
            }
            if(tilesToPlay[i] == null) {
                if(direction == Board.Direction.FORWARD) {
                    try {
                        if (board.getBoard()[coordinates[1]][coordinates[0] + i].getTile().getLetter() == wordAttempt.charAt(i))
                            connected = true;
                        else if(hasABlankTile) {
                            for(Tile tile : players[turn].getRack().getTiles()) {
                                if (tile.getLetter() == '-')
                                    tile.setBlankTileLetter(parser.getBlankTileLetter());
                                if(tile.getLetter() == wordAttempt.charAt(i))
                                    tilesToPlay[i] = tile;
                            }
                        }
                        else {
                            System.out.println("You do not have the tiles to spell \"" + wordAttempt + "\".");
                            return false;
                        }
                    }
                    catch(NullPointerException exception) {
                        System.out.println("You do not have the tiles to spell \"" + wordAttempt + "\".");
                        return false;
                    }
                }
                if(direction == Board.Direction.DOWNWARD) {
                    try {
                        if (board.getBoard()[coordinates[1] + i][coordinates[0]].getTile().getLetter() == wordAttempt.charAt(i))
                            connected = true;
                        else {
                            System.out.println("You do not have the tiles to spell \"" + wordAttempt + "\".");
                            return false;
                        }
                    }
                    catch(NullPointerException exception) {
                        System.out.println("You do not have the tiles to spell \"" + wordAttempt + "\".");
                        return false;
                    }
                }
            }
        }

        if((!connected) && (!board.isEmpty())) {
            System.out.println("You have to connect your word to a previously spelt word!");
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
                        System.out.println(players[turn].getName() + " score: " + players[turn].getScore());
                        players[turn].getRack().fillRack(bag);
                        System.out.println(players[turn].getRack().toString());
                        changeTurn();
                        return true;
                    }
                    else {
                        System.out.println("You can not play there!");
                        return false;
                    }
                }
            }
            System.out.println("\"" + wordAttempt + "\" does not exist.");
        }
        catch (FileNotFoundException exception) {
            System.out.println("Dictionary not found");
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
    private void changeTurn() {
        turn = (turn + 1) % players.length;
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
     * @param command command to quit the game.
     * @return true if command quits the game,
     * false otherwise.
     */
    private boolean quit(Command command) {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            System.out.println(this + "\n");
            System.out.println("Thank you for playing. Goodbye...");
            return true;
        }
    }

    /**
     * Main play routine.  Loops until end of play.
     */
    private void playGame() {
        System.out.println(getWelcomeMessage());
        int numPlayers = -1;
        while(numPlayers == -1)
            numPlayers = parser.getNumPlayers(MIN_PLAYERS, MAX_PLAYERS);
        initializePlayers(numPlayers);
        System.out.println("\nIt is " + players[turn].getName() + "'s turn." + help());

        boolean finished = false;
        while(!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
    }

    /**
     * Return a string representation of the game.
     * @return A string representation of the game.
     */
    @Override
    public String toString() {
        String gameToString = "---------------------------------------------------------------\n";
        gameToString += gameFileName + "\n\n";

        for(Player player : players) {
            gameToString += player.toString() + "\n";
        }
        gameToString += "\n" + board.toString() + "\n";
        gameToString += "---------------------------------------------------------------";

        return gameToString;
    }
}
