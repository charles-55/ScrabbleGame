/**
 * The Player class.
 *
 * @author Ejeh Leslie 101161386
 * @version 2.0
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
public class Player {

    private final String name;
    private int score;
    private final Rack rack;
    private final Stack<String> playedWords;
    private final boolean IS_AI;

    /**
     * Create and initialize the Player.
     */
    public Player(String name, boolean isAi) {
        this.name = name;
        this.IS_AI = isAi;
        this.score = 0;
        this.rack = new Rack();
        this.playedWords = new Stack<>();
    }

    /**
     * gets the players name
     * @return string of the players name.
     */
    public String getName(){
        return this.name;
    }

    /**
     * Get if the player is an AI
     * @return if the player is an AI
     */
    public boolean isAI() {
        return IS_AI;
    }

    /**
     * Updates the players score
     */
    public void updateScore(int updateScore){
        this.score+=updateScore;
    }

    /**
     * Gets the player score
     * @return data type of int corresponding to a  player score
     */
    public int getScore(){
        return this.score;
    }
    /**
     * Gets the players rack
     * @return Data of type rack
     */
    public Rack getRack(){
        return rack;
    }

    /**
     * Gets the players name, score and rack in a string
     * @return a string
     */
    public String toString(){
        String s = "Name = " + getName() + ", Score = "+getScore()+ "\nPlayer Rack = "+rack.toString() + "\n\n";
        s += "PLAYED WORDS\n";
        for(String word : playedWords) {
            s += word + "\n";
        }

        return s;
    }
    /**
     * Stores played words in a stack
     * @param  word of  type Strings
     */
    public void addPlayedWords(String word){
        playedWords.add(word );
    }
    /**
     *Returns played words
     * @return a stack of played words
     */
    public Stack<String> returnsPlayedWords(){
        return playedWords;
    }

    public PlayEvent play() {
        if(!IS_AI)
            throw new RuntimeException("Not an AI!");

        String wordAttempt = "";
        int[] coordinates = new int[2];
        Board.Direction direction = Board.Direction.FORWARD;

        String word = "";
        for(int i = 0; i < 7; i++)
            word += getRack().getTiles()[i].getLetter();

        ArrayList<String> possibleWords = combine(word, new StringBuffer(), 0);
        possibleWords.sort(Comparator.comparingInt(String::length).reversed());

        for(String s : possibleWords)
            System.out.println(s);

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
}
