/**
 * The AI Player Class
 *
 * @version 1.0
 */
public class AiPlayer extends Player {

    /**
     * Create and initialize the AI Player.
     */
    public AiPlayer(String name) {
        super(name, true);
    }

    public PlayEvent play() {
        String wordAttempt = "";
        int[] coordinates = new int[2];
        Board.Direction direction = Board.Direction.FORWARD;



        return new PlayEvent(wordAttempt, coordinates, direction);
    }
}
