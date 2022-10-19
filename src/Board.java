public class Board {

    public enum Direction {FORWARD, DOWNWARD}

    private Square[][] board;
    private final int BOARD_SIZE = 15;

    /**
     * Create the board.
     */
    public Board() {
        board = new Square[BOARD_SIZE][BOARD_SIZE];
        this.initializeBoard();
    }

    /**
     * Initialize the board.
     */
    private void initializeBoard() {
        for(Square[] squares : board) {
            for(Square square : squares) {
                square = new Square();
            }
        }
    }

    // TODO: 2022-10-19 complete implementation
    /**
     * Attempts to play a particular word on the board.
     * @param word An array of tiles that spells the word.
     * @param coordinates The coordinate of the starting
     *                    point of the word on the board.
     * @param direction The direction in which the word is
     *                  spelt on the board.
     * @return true if the attempt was successful, false
     * otherwise.
     */
    public boolean attemptPlay(Tile[] word, int[] coordinates, Direction direction) {
        return false;
    }

    /**
     * Return a string representation of the board.
     * @return A string representation of the board.
     */
    @Override
    public String toString() {
        String boardString = "  || 0";

        for(int i = 1; i < BOARD_SIZE; i++) {
            if(i <= 10)
                boardString += " | " + i;
            else
                boardString += "| " + i;
        }
        boardString += """

                ---------------------------------------------------------------
                """;

        int verticalCoordinate = 0;
        for(Square[] squares : board) {
            if(verticalCoordinate < 10)
                boardString += " " + verticalCoordinate + "|";
            else
                boardString += verticalCoordinate + "|";

            for(Square square : squares) {
                Tile tile = square.getTile();
                if(tile == null)
                    boardString += " |   ";
                else
                    boardString += " | " + tile.getLetter() + " ";
            }
            boardString += "\n";

            verticalCoordinate++;
        }

        return boardString;
    }
}
