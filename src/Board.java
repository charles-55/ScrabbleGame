public class Board {

    private Square[][] board;
    private final int BOARDSIZE = 15;

    public Board() {
        board = new Square[BOARDSIZE][BOARDSIZE];
    }
}
