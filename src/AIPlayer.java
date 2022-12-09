import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.*;

/**
 * The AI Player Class
 *
 * @version 1.0
 */
public class AIPlayer extends Player implements Serializable {

    /**
     * Create and initialize the AI Player.
     */
    public AIPlayer(String name) {
        super(name, true);
    }

    /**
     * Return a play event if player is an AI
     * @param board Board to play on
     * @return the play event
     */
    public PlayEvent play(Board board) {
        Board tempBoard = board;
        String wordAttempt = "";
        int[] coordinates = new int[2];
        Board.Direction direction = Board.Direction.FORWARD;

        StringBuilder allTileLetters = new StringBuilder();
        for(int i = 0; i < 7; i++)
            allTileLetters.append(getRack().getTiles()[i].getLetter());

        ArrayList<String> possibleWords = combine(allTileLetters.toString(), new StringBuffer(), 0);
        possibleWords.sort(Comparator.comparingInt(String::length).reversed());

        for(int i = 0; i < tempBoard.getBoardSize()[0]; i++) {
            for(int j = 0; j < tempBoard.getBoardSize()[1]; j++) {
                for(String word : possibleWords) {
                    Tile[] wordTiles = new Tile[word.length()];
                    for(int k = 0; k < word.length(); k++) {
                        for(Tile tile : getRack().getTiles()) {
                            if(tile.getLetter() == word.charAt(k))
                                wordTiles[k] = tile;
                        }
                    }

                    if(tempBoard.attemptPlay(wordTiles, new int[]{i, j}, Board.Direction.FORWARD))
                        return new PlayEvent(word, new int[]{i, j}, Board.Direction.FORWARD);
                    else if(tempBoard.attemptPlay(wordTiles, new int[]{i, j}, Board.Direction.DOWNWARD))
                        return new PlayEvent(word, new int[]{i, j}, Board.Direction.DOWNWARD);
                }

            }
        }

        return new PlayEvent(wordAttempt, coordinates, direction);
    }

    private ArrayList<String> combine(String instr, StringBuffer outstr, int index) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = index; i < instr.length(); i++) {
            outstr.append(instr.charAt(i));
            String word = outstr.toString();
            try {
                Scanner dictionary = new Scanner(new File(GameMaster.DICTIONARY));
                while(dictionary.hasNextLine()) {
                    if(word.equalsIgnoreCase(dictionary.nextLine()))
                        arrayList.add(word);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            arrayList.addAll(combine(instr, outstr, i + 1));
            outstr.deleteCharAt(outstr.length() - 1);
        }
        return arrayList;
    }

    public int[] getExchangeTileIndices() {
        int randomNumber = (new Random()).nextInt(8);
        int[] indices = new int[randomNumber];
        for (int i = 0; i < randomNumber; i++) {
            int randomIndex = (new Random()).nextInt(randomNumber);
            while (List.of(indices).contains(randomIndex))
                randomIndex = (new Random()).nextInt(randomNumber);
            indices[i] = randomIndex;
        }
        return indices;
    }
}
