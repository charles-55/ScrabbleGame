import java.util.Scanner;

/**
 * The parser reads and interprets user inputs(commands).
 *
 * @author LONGJOHN DAGOGO
 * @version 1.0
 */
public class Parser
{
    private CommandWords commands;  // holds all valid command words
    private Scanner reader;         // source of command input

    /**
     * Create a parser to read from the terminal window.
     */
    public Parser()
    {
        commands = new CommandWords();
        reader = new Scanner(System.in);
    }

    /**
     * @return The next command from the user.
     */
    public Command getCommand()
    {
        String inputLine;   // will hold the full input line
        String word1 = null;
        String word2 = null;

        System.out.print("> ");     // print prompt

        inputLine = reader.nextLine();

        // Find up to two words on the line.
        Scanner tokenizer = new Scanner(inputLine);
        if(tokenizer.hasNext()) {
            word1 = tokenizer.next();      // get first word
            if(tokenizer.hasNext()) {
                word2 = tokenizer.next();      // get second word
                // note: we just ignore the rest of the input line.
            }
        }

        // Now check whether this word is known. If so, create a command
        // with it. If not, create a "null" command (for unknown command).
        if(commands.isCommand(word1)) {
            return new Command(word1, word2);
        }
        else {
            return new Command(null, word2);
        }
    }

    /**
     * Prints the name of the players.
     * @param playerX
     * @return Continues to print the names of all the players in the game.
     */
    public String getPlayerName(int playerX){
        System.out.print("Player " + (playerX + 1) + " name: ");
        return reader.nextLine();
    }

    /**
     * Gets the coordinates of a square on the board.
     * @return null
     */
    public int[] getCoordinates(){
        int x, y;
        System.out.println("input Starting Coordinate(x y): " );
        String[] stringCoordinate = reader.nextLine().split(" ");
        try {
            x = Integer.parseInt(stringCoordinate[0]);
            y = Integer.parseInt(stringCoordinate[1]);
            return new int[] {x, y};
        } catch (NumberFormatException e) {
            System.out.println("Invalid Input");
        }
        return null;
    }

    /**
     * Get the number of players.
     * @param min minimum number of players 2.
     * @param max maximum number of players 4.
     * @return number of players playing the game.
     */
    public int getNumPlayers(int min, int max){
        System.out.print("Number of players (" + min + "-" + max +"): ");
        try {
            int numPlayers = Integer.parseInt(reader.nextLine());
            if(numPlayers >= min && numPlayers <= max)
                return numPlayers;
            else
                System.out.println("Number of players not in Range.");
            return -1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid Input");
        }
        return -1;
    }

    /**
     * Get the way(direction) a word will be placed on the board.
     * FORWARD - a word will be placed horizontally.
     * Downward - a word will be played vertically.
     * @return null
     */
    public Board.Direction getDirection(){
        System.out.println("Direction of Word (FORWARD or DOWNWARD): ");
        String inputLine = reader.nextLine().toUpperCase();
        if (inputLine.equals(Board.Direction.DOWNWARD.toString()))
            return Board.Direction.DOWNWARD;
        else if (inputLine.equals(Board.Direction.FORWARD.toString()))
            return Board.Direction.FORWARD;
        else
            System.out.println("Invalid Input");
        return null;
    }

    /**
     * Get the position of a tile among the tiles a player has in their rack.
     * @param tiles
     * @return the index of the tile.
     */
    public int getTileIndex(Tile[] tiles) {
        System.out.println("Choose the index of a tile to exchange: ");
        System.out.print("| ");
        for(int i = 0; i < tiles.length; i++) {
            if(tiles[i] != null)
                System.out.print("Letter: " + tiles[i].getLetter() + ", Index: " + i + " | ");
        }
        System.out.println("");

        String inputLine = reader.nextLine();
        try {
            return Integer.parseInt(inputLine);
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid input, try again!");
            getTileIndex(tiles);
        }
        return -1;
    }

    /**
     * Determine what letter the blank tile played on the board would
     * represent.
     * @return the letter the player has decided to use the blank
     * tile to represent.
     */
    public char getBlankTileLetter() {
        System.out.println("You attempted to play a blank tile.");
        System.out.print("What letter should the blank tile represent: ");

        String inputLine = reader.nextLine();
        return inputLine.charAt(0);
    }
}