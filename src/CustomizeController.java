import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomizeController implements ActionListener {

    private final ScrabbleFrame view;
    private final Square[][] squares;
    private boolean assignedOriginPoint;

    public CustomizeController(ScrabbleFrame view, Square[][] squares) {
        this.view = view;
        this.squares = squares;
        assignedOriginPoint = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String[] coordinates = e.getActionCommand().split(" ");
        int x = Integer.parseInt(coordinates[0]);
        int y = Integer.parseInt(coordinates[1]);

        if(squares[x][y].getSquareType() == Square.SquareType.ORIGIN)
            assignedOriginPoint = false;

        String customType = view.getCustomSquareType();

        switch(customType) {
            case "Blank" -> squares[x][y].setSquareType(Square.SquareType.BLANK);
            case "Origin" -> {
                if (assignedOriginPoint)
                    view.handleMessage("Origin point has already been set!");
                else {
                    squares[x][y].setSquareType(Square.SquareType.ORIGIN);
                    assignedOriginPoint = true;
                }
            }
            case "Double Letter Score" -> squares[x][y].setSquareType(Square.SquareType.DLS);
            case "Double Word Score" -> squares[x][y].setSquareType(Square.SquareType.DWS);
            case "Triple Letter Score" -> squares[x][y].setSquareType(Square.SquareType.TLS);
            case "Triple Word Score" -> squares[x][y].setSquareType(Square.SquareType.TWS);
        }
    }
}
