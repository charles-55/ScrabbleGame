/**
 * The Board class.
 *
 * @author Osamudiamen Nwoko 101152520
 * @version 1.0
 */
public class Board {

    public enum Direction {FORWARD, DOWNWARD}

    private Square[][] board;
    private static final int BOARD_SIZE = 15;

    /**
     * Create the board.
     */
    public Board() {
        board = new Square[BOARD_SIZE][BOARD_SIZE];
        for(int i = 0; i < BOARD_SIZE; i++) {
            for(int j = 0; j < BOARD_SIZE; j++)
                board[j][i] = new Square();
        }
    }

    // TODO: 2022-10-22 Does not connect to a previous play
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
        if(direction == Direction.FORWARD) {
            if(coordinates[0] + word.length - 1 >= BOARD_SIZE)
                return false;
            for(int i = 0; i < word.length; i++) {
                boolean placed = board[coordinates[1]][coordinates[0] + i].placeTile(word[i]);
                if(!placed)
                    return false;
            }
        }
        else if(direction == Direction.DOWNWARD) {
            if(coordinates[1] + word.length - 1 >= BOARD_SIZE)
                return false;
            for(int i = 0; i < word.length; i++) {
                boolean placed = board[coordinates[0] + i][coordinates[1]].placeTile(word[i]);
                if(!placed)
                    return false;
            }
        }
        return true;
    }

    // TODO: 2022-10-22 fix scoring
    /**
     * Return the score of the play made.
     * @return Score of the play made.
     */
    public int getScore(int[] coordinates, Direction direction) {
        int score = 0;

        if(direction == Direction.FORWARD) {
            int i = 0;
            while(board[coordinates[1]][coordinates[0] - i].getTile() != null) {
                int  j = 0;
                while(board[coordinates[1] - j][coordinates[0] - i].getTile() != null) {
                    score += board[coordinates[1] - j][coordinates[0] - i].getTile().getPoints();
                    System.out.println(board[coordinates[1] - j][coordinates[0] - i].getTile().getLetter());
                    j++;
                    if(coordinates[1] - j < 0) {
                        j = 0;
                        break;
                    }
                }

                while(board[coordinates[1] + j][coordinates[0] - i].getTile() != null) {
                    score += board[coordinates[1] + j][coordinates[0] - i].getTile().getPoints();
                    System.out.println(board[coordinates[1] + j][coordinates[0] - i].getTile().getLetter());
                    j++;
                    if(coordinates[1] + j  >= BOARD_SIZE)
                        break;
                }

                i++;
                if(coordinates[0] - i < 0) {
                    i = 0;
                    break;
                }
            }

            while(board[coordinates[1]][coordinates[0] + i].getTile() != null) {
                score += board[coordinates[1]][coordinates[0] + i].getTile().getPoints();
                System.out.println(board[coordinates[1]][coordinates[0] + i].getTile().getLetter());
                i++;
                if (coordinates[1] + i >= BOARD_SIZE)
                    break;
            }
        }
        else if(direction == Direction.DOWNWARD) {
            int j = 0;
            while(board[coordinates[1] - j][coordinates[0]].getTile() != null) {
                int  i = 0;
                while(board[coordinates[1] - j][coordinates[0] - i].getTile() != null) {
                    score += board[coordinates[1] - j][coordinates[0] - i].getTile().getPoints();
                    i++;
                    if(coordinates[1] - i  < 0) {
                        i = 0;
                        break;
                    }
                }

                while(board[coordinates[1] - j][coordinates[0] + i].getTile() != null) {
                    score += board[coordinates[1] - j][coordinates[0] + i].getTile().getPoints();
                    i++;
                    if(coordinates[1] + i >= BOARD_SIZE)
                        break;
                }

                j++;
                if(coordinates[0] - j < 0) {
                    j = 0;
                    break;
                }
            }

            while(board[coordinates[1] + j][coordinates[0]].getTile() != null) {
                score += board[coordinates[1] - j][coordinates[0]].getTile().getPoints();
                j++;
                if (coordinates[1] + j >= BOARD_SIZE)
                    break;
            }
        }

        return score;
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
                    boardString += "|   ";
                else
                    boardString += "| " + tile.getLetter() + " ";
            }
            boardString += "\n";

            verticalCoordinate++;
        }

        return boardString;
    }
}
