import javax.swing.*;
import java.io.File;
import java.io.Serializable;

/**
 * The Board class.
 * This class is responsible for implementing the scrabble board.
 *
 * @author Osamudiamen Nwoko 101152520
 * @version 2.0
 */
public class Board implements Serializable {

    public enum Direction {FORWARD, DOWNWARD}
    private final Square[][] board;
    private final int[] BOARD_SIZE;
    private final int[] ORIGIN_POINT;

    /**
     * Create the board.
     */
    public Board() {
        BOARD_SIZE = new int[] {15, 15};
        ORIGIN_POINT = new int[] {BOARD_SIZE[0] / 2, BOARD_SIZE[1] / 2};
        board = new Square[BOARD_SIZE[0]][BOARD_SIZE[1]];
        boardSetUp();
    }

    public Board(Square[][] squares) {
        board = squares;
        BOARD_SIZE = new int[] {squares.length, squares[0].length};

        for(int i = 0; i < BOARD_SIZE[0]; i++) {
            for(int j = 0; j < BOARD_SIZE[1]; j++) {
                if(board[i][j].getSquareType() == Square.SquareType.ORIGIN) {
                    ORIGIN_POINT = new int[]{i, j};
                    return;
                }
            }
        }
        throw new RuntimeException("No Origin point found");
    }

    private void boardSetUp(){
        for(int i = 0; i < BOARD_SIZE[0]; i++) {
            for(int j = 0; j < BOARD_SIZE[1]; j++)
                board[i][j] = new Square();
        }

        board[ORIGIN_POINT[0]][ORIGIN_POINT[1]].setSquareType(Square.SquareType.ORIGIN);
        for(int k=0;k<BOARD_SIZE[0];k++){
            for(int g=0;g<BOARD_SIZE[1];g++){
                if((k==0&&g==0)||(k==5 && g==5)||(k==9 && g==9)||(k==14 && g==14)){
                    board[k][g].setSquareType(Square.SquareType.TWS);
                } else if ((k==14&&g==0)||(k==9 && g==5)||(k==5 && g==9)||(k==0 && g==14)) {
                    board[k][g].setSquareType(Square.SquareType.TWS);
                } else if (!((k==0&&g==0)||(k==5 && g==5)||(k==9 && g==9)||(k==14 && g==14)&&(k==14&&g==0)||(k==9 && g==5)||(k==5 && g==9)||(k==0 && g==14))) {
                    if(k==g && (k!=7 && g!=7) && (k!=8 && g!=8) && (k!=6 && g!=6) ){
                        board[k][g].setSquareType(Square.SquareType.DWS);
                    } else if ((k==13 && g==1 )||(k==12&&g==2) ||(k==11&&g==3)||(k==10&&g==4)||(k==8&&g==6)||(k==6&&g==8)||(k==4&&g==10)||(k==3&&g==11)||(k==2&&g==12)||(k==1&&g==13)) {
                        board[k][g].setSquareType(Square.SquareType.DWS);
                    } else if (k==8 &&g==8 || k==6 && g== 6) {
                        board[k][g].setSquareType(Square.SquareType.DLS);
                    }else if((k==3 && g==0) || (k==6 && g==3)|| (k==7 && g==4)|| (k==8 && g==3)|| (k==11 && g==0)||(k==3 && g==14)|| (k==6 && g==11)|| (k==7 && g==10)|| (k==8 && g==11)|| (k==11 && g==14) ){
                        board[k][g].setSquareType(Square.SquareType.DLS);

                    }else if((k==5 && g==2)|| (k==9 && g==2)||(k==5 && g==12) || (k==9 && g==12)){
                        board[k][g].setSquareType(Square.SquareType.TLS);
                    }else if((k==0&&g==7)||(k==7 && g==14)||(k==7 && g==0)||(k==14 && g==7) ){
                        board[k][g].setSquareType(Square.SquareType.TWS);
                    } else if((k==0 && g==3) || (k==3 && g==6)|| (k==4 && g==7)|| (k==3 && g==8)|| (k==0 && g==11)||(k==14 && g==3)|| (k==11 && g==6)|| (k==10 && g==7)|| (k==11 && g==8)|| (k==14 && g==11) ){
                        board[k][g].setSquareType(Square.SquareType.DLS);
                    }else if((k==12 && g==5)|| (k==2 && g==9)||(k==12 && g==5) || (k==12 && g==9)){
                        board[k][g].setSquareType(Square.SquareType.TLS);
                    }
                }
            }
        }
    }

    /**
     * Return the board in form of Square arrays.
     * @return The board in form of Square arrays.
     */
    public Square[][] getBoard() {
        return board;
    }

    /**
     * Return the size of the board.
     * @return The size of the board.
     */
    public int[] getBoardSize() {
        return BOARD_SIZE;
    }

    /**
     * Return if the board is empty or not.
     * @return true if the board is empty, false otherwise.
     */
    public boolean isEmpty() {
        for(int i = 0; i < BOARD_SIZE[0]; i++) {
            for(int j = 0; j < BOARD_SIZE[1]; j++) {
                if(board[i][j].getTile() != null)
                    return false;
            }
        }
        return true;
    }

    public boolean checkFirstPlayConditions(int wordLength, int[] coordinates, Direction direction) {
        if(isEmpty()) {
            if(direction == Direction.FORWARD)
                return (coordinates[1] == ORIGIN_POINT[1]) && (coordinates[0] <= ORIGIN_POINT[0]) && (coordinates[0] + wordLength - 1 >= ORIGIN_POINT[0]);
            else if(direction == Direction.DOWNWARD)
                return (coordinates[0] == ORIGIN_POINT[0]) && (coordinates[1] <= ORIGIN_POINT[1]) && (coordinates[1] + wordLength - 1 >= ORIGIN_POINT[1]);
        }

        return false;
    }

    /**
     * Attempts to play a particular word on the board.
     * @param wordTiles An array of tiles that spells the word.
     * @param coordinates The coordinate of the starting
     *                    point of the word on the board.
     * @param direction The direction in which the word is
     *                  spelt on the board.
     * @return true if the attempt was successful, false
     * otherwise.
     */
    public boolean attemptPlay(Tile[] wordTiles, int[] coordinates, Direction direction) {
        if((coordinates[0] >= BOARD_SIZE[0]) || (coordinates[1] >= BOARD_SIZE[1]))
            return false;

        if(!checkFirstPlayConditions(wordTiles.length, coordinates, direction) && isEmpty())
            return false;

        if(direction == Direction.FORWARD) {
            if(coordinates[0] + wordTiles.length - 1 >= BOARD_SIZE[0])
                return false;

            for(int i = 0; i < wordTiles.length; i++) {
                if(wordTiles[i] != null) {
                    boolean placed = board[coordinates[1]][coordinates[0] + i].placeTile(wordTiles[i]);
                    if(!placed) {
                        for(int j = i - 1; j >= 0; j--) {
                            board[coordinates[1]][coordinates[0] + j].removeTile();
                        }
                        return false;
                    }
                }
            }
        }
        else if(direction == Direction.DOWNWARD) {
            if(coordinates[1] + wordTiles.length - 1 >= BOARD_SIZE[1])
                return false;

            for(int i = 0; i < wordTiles.length; i++) {
                if(wordTiles[i] != null) {
                    boolean placed = board[coordinates[0] + i][coordinates[1]].placeTile(wordTiles[i]);
                    if(!placed) {
                        for(int j = i - 1; j >= 0; j--) {
                            board[coordinates[0] + j][coordinates[1]].removeTile();
                        }
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Return the score of the play made.
     * @return Score of the play made.
     */
    public int getScore(int[] coordinates, Direction direction) {
        int score = 0;
        int multiplier = 1;

        if(direction == Direction.FORWARD) {
            int i = 0;
            while(board[coordinates[1]][coordinates[0] - i].getTile() != null) {
                int j = 0;
                while(board[coordinates[1] - j][coordinates[0] - i].getTile() != null) {
                    if(board[coordinates[1] - j][coordinates[0] - i].getSquareType() == Square.SquareType.TWS) {
                        multiplier *= 3;
                        score += board[coordinates[1] - j][coordinates[0] - i].getTile().getPoints();
                    }
                    else if(board[coordinates[1] - j][coordinates[0] - i].getSquareType() == Square.SquareType.DWS) {
                        multiplier *= 2;
                        score += board[coordinates[1] - j][coordinates[0] - i].getTile().getPoints();
                    }
                    else if(board[coordinates[1] - j][coordinates[0] - i].getSquareType() == Square.SquareType.TLS)
                        score += board[coordinates[1] - j][coordinates[0] - i].getTile().getPoints() * 3;
                    else if(board[coordinates[1] - j][coordinates[0] - i].getSquareType() == Square.SquareType.DLS)
                        score += board[coordinates[1] - j][coordinates[0] - i].getTile().getPoints() * 2;
                    else
                        score += board[coordinates[1] - j][coordinates[0] - i].getTile().getPoints();
                    j++;
                    if(coordinates[1] - j < 0) {
                        j = 0;
                        break;
                    }
                }

                while(board[coordinates[1] + j][coordinates[0] - i].getTile() != null) {
                    if(board[coordinates[1] + j][coordinates[0] - i].getSquareType() == Square.SquareType.TWS) {
                        multiplier *= 3;
                        score += board[coordinates[1] + j][coordinates[0] - i].getTile().getPoints();
                    }
                    else if(board[coordinates[1] + j][coordinates[0] - i].getSquareType() == Square.SquareType.DWS) {
                        multiplier *= 2;
                        score += board[coordinates[1] + j][coordinates[0] - i].getTile().getPoints();
                    }
                    else if(board[coordinates[1] + j][coordinates[0] - i].getSquareType() == Square.SquareType.TLS)
                        score += board[coordinates[1] + j][coordinates[0] - i].getTile().getPoints() * 3;
                    else if(board[coordinates[1] + j][coordinates[0] - i].getSquareType() == Square.SquareType.DLS)
                        score += board[coordinates[1] + j][coordinates[0] - i].getTile().getPoints() * 2;
                    else
                        score += board[coordinates[1] + j][coordinates[0] - i].getTile().getPoints();
                    j++;
                    if(coordinates[1] + j  >= BOARD_SIZE[1])
                        break;
                }

                i++;
                if(coordinates[0] - i < 0) {
                    i = 0;
                    break;
                }
            }

            while(board[coordinates[1]][coordinates[0] + i].getTile() != null) {
                if(board[coordinates[1]][coordinates[0] + i].getSquareType() == Square.SquareType.TWS) {
                    multiplier *= 3;
                    score += board[coordinates[1]][coordinates[0] + i].getTile().getPoints();
                }
                else if(board[coordinates[1]][coordinates[0] + i].getSquareType() == Square.SquareType.DWS) {
                    multiplier *= 2;
                    score += board[coordinates[1]][coordinates[0] + i].getTile().getPoints();
                }
                else if(board[coordinates[1]][coordinates[0] + i].getSquareType() == Square.SquareType.TLS)
                    score += board[coordinates[1]][coordinates[0] + i].getTile().getPoints() * 3;
                else if(board[coordinates[1]][coordinates[0] + i].getSquareType() == Square.SquareType.DLS)
                    score += board[coordinates[1]][coordinates[0] + i].getTile().getPoints() * 2;
                else
                    score += board[coordinates[1]][coordinates[0] + i].getTile().getPoints();
                i++;
                if (coordinates[1] + i >= BOARD_SIZE[1])
                    break;
            }
        }
        else if(direction == Direction.DOWNWARD) {
            int j = 0;
            while(board[coordinates[1] - j][coordinates[0]].getTile() != null) {
                int i = 0;
                while(board[coordinates[1] - j][coordinates[0] - i].getTile() != null) {
                    if(board[coordinates[1] - j][coordinates[0] - i].getSquareType() == Square.SquareType.TWS) {
                        multiplier *= 3;
                        score += board[coordinates[1] - j][coordinates[0] - i].getTile().getPoints();
                    }
                    else if(board[coordinates[1] - j][coordinates[0] - i].getSquareType() == Square.SquareType.DWS) {
                        multiplier *= 2;
                        score += board[coordinates[1] - j][coordinates[0] - i].getTile().getPoints();
                    }
                    else if(board[coordinates[1] - j][coordinates[0] - i].getSquareType() == Square.SquareType.TLS)
                        score += board[coordinates[1] - j][coordinates[0] - i].getTile().getPoints() * 3;
                    else if(board[coordinates[1] - j][coordinates[0] - i].getSquareType() == Square.SquareType.DLS)
                        score += board[coordinates[1] - j][coordinates[0] - i].getTile().getPoints() * 2;
                    else
                        score += board[coordinates[1] - j][coordinates[0] - i].getTile().getPoints();
                    i++;
                    if(coordinates[1] - i  < 0) {
                        i = 0;
                        break;
                    }
                }

                while(board[coordinates[1] - j][coordinates[0] + i].getTile() != null) {
                    if(board[coordinates[1] - j][coordinates[0] + i].getSquareType() == Square.SquareType.TWS) {
                        multiplier *= 3;
                        score += board[coordinates[1] - j][coordinates[0] + i].getTile().getPoints();
                    }
                    else if(board[coordinates[1] - j][coordinates[0] + i].getSquareType() == Square.SquareType.DWS) {
                        multiplier *= 2;
                        score += board[coordinates[1] - j][coordinates[0] + i].getTile().getPoints();
                    }
                    else if(board[coordinates[1] - j][coordinates[0] + i].getSquareType() == Square.SquareType.TLS)
                        score += board[coordinates[1] - j][coordinates[0] + i].getTile().getPoints() * 3;
                    else if(board[coordinates[1] - j][coordinates[0] + i].getSquareType() == Square.SquareType.DLS)
                        score += board[coordinates[1] - j][coordinates[0] + i].getTile().getPoints() * 2;
                    else
                        score += board[coordinates[1] - j][coordinates[0] + i].getTile().getPoints();
                    i++;
                    if(coordinates[1] + i >= BOARD_SIZE[1])
                        break;
                }

                j++;
                if(coordinates[0] - j < 0) {
                    j = 0;
                    break;
                }
            }

            while(board[coordinates[1] + j][coordinates[0]].getTile() != null) {
                if(board[coordinates[1] + j][coordinates[0]].getSquareType() == Square.SquareType.TWS) {
                    multiplier *= 3;
                    score += board[coordinates[1] + j][coordinates[0]].getTile().getPoints();
                }
                else if(board[coordinates[1] + j][coordinates[0]].getSquareType() == Square.SquareType.DWS) {
                    multiplier *= 2;
                    score += board[coordinates[1] + j][coordinates[0]].getTile().getPoints();
                }
                else if(board[coordinates[1] + j][coordinates[0]].getSquareType() == Square.SquareType.TLS)
                    score += board[coordinates[1] + j][coordinates[0]].getTile().getPoints() * 3;
                else if(board[coordinates[1] + j][coordinates[0]].getSquareType() == Square.SquareType.DLS)
                    score += board[coordinates[1] + j][coordinates[0]].getTile().getPoints() * 2;
                else
                    score += board[coordinates[1] + j][coordinates[0]].getTile().getPoints();
                j++;
                if (coordinates[1] + j >= BOARD_SIZE[1])
                    break;
            }
        }

        return score * multiplier;
    }

    /**
     * Return a string representation of the board.
     * @return A string representation of the board.
     */
    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder("  || 0");

        for(int i = 1; i < BOARD_SIZE[0]; i++) {
            if(i <= 10)
                boardString.append(" | ").append(i);
            else
                boardString.append("| ").append(i);
        }
        boardString.append("""

                ---------------------------------------------------------------
                """);

        int verticalCoordinate = 0;
        for(Square[] squares : board) {
            if(verticalCoordinate < 10)
                boardString.append(" ").append(verticalCoordinate).append("|");
            else
                boardString.append(verticalCoordinate).append("|");

            for(Square square : squares) {
                Tile tile = square.getTile();
                if(tile == null)
                    boardString.append("|   ");
                else
                    boardString.append("| ").append(tile.getLetter()).append(" ");
            }
            boardString.append("\n");

            verticalCoordinate++;
        }

        return boardString.toString();
    }
}
