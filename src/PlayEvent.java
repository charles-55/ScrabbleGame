import java.util.EventObject;

public class PlayEvent extends EventObject {

    private int[] startingCoordinate;
    private Board.Direction direction;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public PlayEvent(GameMaster source, int[] startingCoordinate, Board.Direction direction) {
        super(source);
        this.startingCoordinate = startingCoordinate;
        this.direction = direction;
    }

    /**
     * Return the source of the event.
     * @return The source of the event.
     */
    public GameMaster getSource() {
        return (GameMaster) super.getSource();
    }

    /**
     * Return the starting coordinate of the play.
     * @return The starting coordinate of the play.
     */
    public int[] getStartingCoordinate() {
        return startingCoordinate;
    }

    /**
     * Return the direction of the play.
     * @return The direction of the play.
     */
    public Board.Direction getDirection() {
        return direction;
    }
}
