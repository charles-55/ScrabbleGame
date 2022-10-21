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

    /**
     * Create and initialize the game.
     */
    public GameMaster() {
        board = new Board();
        bag = new Bag();
        turn = 0;
        parser = new Parser();
        gameFileName = "";
    }

    /**
     * Return the opening message for the player.
     * @return The opening message.
     */
    private String getWelcomeMessage() {
        return """
                Welcome to the game of Scrabble!
                Scrabble is a board spelling game.
                Type 'help' if you need help.
                How many players would be playing today?
                """;
    }

    /**
     * Initialize all the players.
     * @param numPlayers The number of players playing the game.
     */
    private void initializePlayers(int numPlayers) {
        players = new Player[numPlayers];

        for(int i = 0; i < players.length; i++) {
            String playerName = parser.getPlayerName(i);
            players[i] = new Player(playerName);  // needs to initialize each player
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
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if(commandWord.equals("help")) {
            System.out.println(help());
        }
        else if(commandWord.equals("play")) {
            if(attemptPlay(command))
                System.out.println(board.toString());
            else
                System.out.println("Play unsuccessful, try again.");
        }
        else if(commandWord.equals("exchange")) {
            if(exchangeTile(command))
                System.out.println(board.toString());
            else
                System.out.println("Play unsuccessful, try again.");
        }
        else if(commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if(commandWord.equals("save")) {
            if(save(command))
                System.out.println("Save successful");
            else
                System.out.println("Save unsuccessful.");
        }
        else if(commandWord.equals("saveAs")) {
            if(saveAs(command))
                System.out.println("Save As successful");
            else
                System.out.println("Save As unsuccessful.");
        }
        else if(commandWord.equals("load")) {
            if(load(command))
                System.out.println("Load successful");
            else
                System.out.println("Load unsuccessful.");
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
                help, play, exchange, quit, save, saveAs, load
                """;
    }

    // TODO: 2022-10-18 complete implementation
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

        String wordAttempt = command.getSecondWord();

        /* Get the tiles from the player */
        Tile[] tilesToPlay = new Tile[wordAttempt.length()];
        for(int i = 0; i < wordAttempt.length(); i++) {
            for(Tile tile : players[turn].getRack().getTiles()) {
                if(tile.getLetter().equals(wordAttempt.charAt(i)))
                    tilesToPlay[i] = tile;
            }
            if(tilesToPlay[i] == null) {
                System.out.println("You do not have the tiles to spell \"" + wordAttempt + "\".");
                return false;
            }
        }

        try {
            /* Check if the word exists */
            Scanner dictionary = new Scanner(new File("WordList.txt"));
            while(dictionary.hasNextLine()) {
                if(wordAttempt.equals(dictionary.nextLine())) {
                    /* If word exists, attempt to play it on the board */
                    if(this.board.attemptPlay(tilesToPlay, parser.getCoordinates(), parser.getDirection())) {
                        /* If word is playable */
                        for(Tile tile : tilesToPlay)
                            players[turn].getRack().removeTile(tile);
                        players[turn].updateScore();
                        this.changeTurn();
                        return true;
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

    // TODO: 2022-10-20 complete implementation
    /**
     * Exchange one or more tiles in a player's rack
     * @param command Command containing number of tiles to exchange.
     * @return true if exchange was successful, false otherwise.
     */
    public boolean exchangeTile(Command command) {
        if(!command.hasSecondWord()) {
            System.out.println("Exchange how many tiles?");
            return false;
        }

        try {
            int exchangeNum = Integer.parseInt(command.getSecondWord());
            int[] tilesToExchangeIndex = new int[exchangeNum];

            for(int tileIndex : tilesToExchangeIndex) {
                tileIndex = parser.getTileIndex(players[turn].getRack().getTiles());
            }

            if(players[turn].getRack().exchangeTiles(bag, tilesToExchangeIndex)) {
                this.changeTurn();
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
        this.turn = (this.turn + 1) % players.length;
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
            if(gameFileName.equals("")) {
                System.out.println("Use the saveAs command to name the current game.");
                return false;
            }
            File gameFile = new File(gameFileName + ".txt");
            if(gameFile.createNewFile()) {
                FileWriter fileWriter = new FileWriter(gameFile);
                fileWriter.write("");

                for(Player player : players) {
                    fileWriter.write("");
                }
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
            return true;
        }
    }

    /**
     * Main play routine.  Loops until end of play.
     */
    public void playGame() {
        System.out.println(this.getWelcomeMessage());
        int numPlayers = parser.getNumPlayers();
        if(numPlayers >= 0)
            this.initializePlayers(numPlayers);

        boolean finished = false;
        while(!finished) {
            Command command = parser.getCommand();
            finished = this.processCommand(command);
        }
    }
}
