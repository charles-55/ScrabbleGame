
import java.util.*;
public class Player {

    private String name;
    private int score=0;
    private Rack rack;
    private Stack playedWords;

    public Player(String name) {
        this.name = name;
        this.rack = new Rack();
        this.playedWords = new Stack();
    }
    public String getName(){
        return this.name;
    }
    public void updateScore(int updateScore){
        this.score+=updateScore;
    }
    public int getScore(){
        return this.score;
    }
    public Rack getRack(){
        return rack;
    }
    public String toString(){
        String s = "name = " + getName()+" Score = "+getScore()+ "\n rack = "+rack.toString();
        return s;
    }

}
