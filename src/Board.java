public class Board {

    public enum Direction {FORWARD, DOWNWARD}

    private Square[][] board;
    private final int BOARD_SIZE = 15;

    public Board() {
        board = new Square[BOARD_SIZE][BOARD_SIZE];
        this.initializeBoard();
    }

    private void initializeBoard() {
        for(Square[] squares : board) {
            for(Square square : squares) {
                square = new Square();
            }
        }
    }

    public boolean attemptPlay(Tile[] word, int[] coordinates, Direction direction) {
        return false;
    }

    @Override
    public String toString() {
        return "";
    }
}
