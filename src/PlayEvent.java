public class PlayEvent {

    private final String wordAttempt;
    private final int[] coordinates;
    private final Board.Direction direction;

    public PlayEvent(String wordAttempt, int[] coordinates, Board.Direction direction) {
        this.wordAttempt = wordAttempt;
        this.coordinates = coordinates;
        this.direction = direction;
    }

    public String getWordAttempt() {
        return wordAttempt;
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public Board.Direction getDirection() {
        return direction;
    }
}
