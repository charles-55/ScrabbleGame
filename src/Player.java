/**
 * The Player class.
 *
 * @author Ejeh Leslie 101161386
 * @version 2.0
 */
import java.io.Serializable;
import java.util.*;
public class Player implements Serializable {

    private final String name;
    private int score;
    private Rack rack;
    private Stack<String> playedWords;
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

    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Gets the players rack
     * @return Data of type rack
     */
    public Rack getRack(){
        return rack;
    }

    public void setRack(Rack rack) {
        this.rack = rack;
    }

    /**
     * Gets the players name, score and rack in a string
     * @return a string
     */
    public String toString(){
        StringBuilder s = new StringBuilder("Name = " + getName() + ", Score = " + getScore() + "\nPlayer Rack = " + rack + "\n\n");
        s.append("PLAYED WORDS\n");
        for(String word : playedWords) {
            s.append(word).append("\n");
        }

        return s.toString();
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
    public Stack<String> getPlayedWords(){
        return playedWords;
    }

    public void setPlayedWords(Stack<String> playedWords) {
        this.playedWords = playedWords;
    }

    public static Player getCopyPlayer(Player player) {
        Player copyPlayer = new Player(player.getName(), player.IS_AI);
        copyPlayer.setScore(player.getScore());
        copyPlayer.setRack(Rack.getCopyRack(player.getRack()));
        Stack<String> copyPlayedWords = new Stack<>();
        for(String word : player.getPlayedWords())
            copyPlayedWords.push(word);
        copyPlayer.setPlayedWords(copyPlayedWords);
        return copyPlayer;
    }
}
