import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BoardController implements ActionListener {
    private final ScrabbleFrame frame;
    private final GameMaster model;

    public BoardController(ScrabbleFrame frame, GameMaster game) {
        this.frame = frame;
        this.model = game;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String[] input = e.getActionCommand().split(" ");
        int x = Integer.parseInt(input[0]);
        int y = Integer.parseInt(input[1]);

        String[] strings = frame.attemptPlay();
        if(strings[1].equals(Board.Direction.FORWARD.toString()))
            model.attemptPlay(strings[0], new int[]{x, y}, Board.Direction.FORWARD);
        else if(strings[1].equals(Board.Direction.DOWNWARD.toString()))
            model.attemptPlay(strings[0], new int[]{x, y}, Board.Direction.DOWNWARD);
    }
}
