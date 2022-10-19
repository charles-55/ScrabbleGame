import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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
        board = new Board();
        bag = new Bag();
        turn = 0;
        parser = new Parser();
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
        if (commandWord.equals("help")) {
            System.out.println(help());
        }
        else if (commandWord.equals("play")) {
            attemptPlay(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("save")) {
            save(command);
        }
        else if (commandWord.equals("load")) {
            load(command);
        }

        return wantToQuit;
    }

    // TODO: 2022-10-18 complete implementation
    /**
     * Return a message of help information and
     * a list of command words.
     * @return A message of help information and
     * a list of command words.
     */
    private String help() {
        return """
                You are supposed to 
                
                Your command words are:
                help, play, quit, save, load
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

        try {
            Scanner dictionary = new Scanner(new File("WordList.txt"));
            while(dictionary.hasNextLine()) {
                if(wordAttempt.equals(dictionary.nextLine())) {
                    this.board.attemptPlay();
                }
            }
        }
        catch (FileNotFoundException exception) {
            System.out.println("Dictionary not found");
            return false;
        }

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            history.push(currentRoom);
            currentRoom = nextRoom;
            this.printDescription();
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
        this.initializePlayers(numPlayers);

        boolean finished = false;
        while(!finished) {
            Command command = parser.getCommand();
            finished = this.processCommand(command);
        }
    }
}
