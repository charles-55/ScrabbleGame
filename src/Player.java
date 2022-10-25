/**
 * The Player class.
 *
 * @author Ejeh Leslie 101161386
 * @version 1.0
 */
import java.util.*;
public class Player {

    private String name;
    private int score=0;
    private Rack rack;
    private Stack playedWords;

    /**
     * Create and initialize the Player.
     */
    public Player(String name) {
        this.name = name;
        this.rack = new Rack();
        this.playedWords = new Stack();
    }

    /**
     * gets the players name
     * @return string  0f the players name.
     */
    public String getName(){
        return this.name;
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
        String s = "name = " + getName()+" Score = "+getScore()+ "\n rack = "+rack.toString();
        return s;
    }

}
