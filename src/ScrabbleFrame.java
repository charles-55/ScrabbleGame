import javax.swing.*;
import java.awt.*;

public class ScrabbleFrame extends JFrame implements ScrabbleView {

    private final GameMaster model;
    private final BoardController boardController;
    private final CommandController commandController;
    private final JButton[][] board;
    private JLabel gameName;
    private JLabel[] playerScores;
    private JPanel[] playerRacks;
    public enum Commands {NEW_GAME, LOAD, SAVE, SAVE_AS, QUIT, HELP, ABOUT, EXCHANGE, PASS}

    public ScrabbleFrame() {
        model = new GameMaster();
        boardController = new BoardController(this,model);
        commandController = new CommandController(model);
        board = new JButton[model.getBoard().getBoardSize()][model.getBoard().getBoardSize()];
        model.addView(this);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenuSetup());
        menuBar.add(helpMenuSetup());
        this.setJMenuBar(menuBar);

        initializeGame();

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Scrabble Game");
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private JMenu fileMenuSetup() {
        JMenu fileMenu = new JMenu("File");

        fileMenu.add(newGameMenuItemSetup());
        fileMenu.add(loadMenuItemSetup());
        fileMenu.add(saveMenuItemSetup());
        fileMenu.add(saveAsMenuItemSetup());
        fileMenu.add(quitMenuItemSetup());

        return fileMenu;
    }

    private JMenu helpMenuSetup() {
        JMenu help = new JMenu("Help");

        help.add(helpMenuItemSetup());
        help.add(aboutMenuItemSetup());

        return help;
    }

    private JMenuItem newGameMenuItemSetup() {
        JMenuItem newGame = new JMenuItem("New");
        newGame.setActionCommand(Commands.NEW_GAME.toString());
        newGame.addActionListener(commandController);
        return newGame;
    }

    private JMenuItem loadMenuItemSetup() {
        JMenuItem load = new JMenuItem("Open");
        load.setActionCommand(Commands.LOAD.toString());
        load.addActionListener(commandController);
        return load;
    }

    private JMenuItem saveMenuItemSetup() {
        JMenuItem save = new JMenuItem("Save");
        save.setActionCommand(Commands.SAVE.toString());
        save.addActionListener(commandController);
        return save;
    }

    private JMenuItem saveAsMenuItemSetup() {
        JMenuItem saveAs = new JMenuItem("Save As");
        saveAs.setActionCommand(Commands.SAVE_AS.toString());
        saveAs.addActionListener(commandController);
        return saveAs;
    }

    private JMenuItem quitMenuItemSetup() {
        JMenuItem quit = new JMenuItem("Quit");
        quit.setActionCommand(Commands.QUIT.toString());
        quit.addActionListener(commandController);
        return quit;
    }

    private JMenuItem helpMenuItemSetup() {
        JMenuItem help = new JMenuItem("Help");
        help.setActionCommand(Commands.HELP.toString());
        help.addActionListener(commandController);
        return help;
    }

    private JMenuItem aboutMenuItemSetup() {
        JMenuItem about = new JMenuItem("About");
        about.setActionCommand(Commands.ABOUT.toString());
        about.addActionListener(commandController);
        return about;
    }

    private JPanel boardPanelSetup() {
        JPanel boardPanel = new JPanel(new GridLayout(model.getBoard().getBoardSize() + 1, model.getBoard().getBoardSize() + 1));

        for(int i = 0; i < model.getBoard().getBoardSize(); i++) {
            JLabel label = new JLabel(String.valueOf(i));
            boardPanel.add(label);
        }
        boardPanel.add(new JLabel());
        for(int i = 0; i < model.getBoard().getBoardSize(); i++) {
            for(int j = 0; j < model.getBoard().getBoardSize(); j++) {
                JButton button = new JButton();
                button.setActionCommand(i + " " + j);
                board[i][j] = button;
                button.addActionListener(boardController);
                boardPanel.add(button);
            }
            boardPanel.add(new JLabel(String.valueOf(i)));
        }
        boardPanel.setPreferredSize(new Dimension(600, 600));
        boardPanel.setMaximumSize(new Dimension(600, 600));
        boardPanel.setMinimumSize(new Dimension(600, 600));

        return boardPanel;
    }

    private JPanel playerPanelSetup() {
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.PAGE_AXIS));

        for(int i = 0; i < model.getPlayers().length; i++) {
            JLabel nameLabel = new JLabel("Name: " + model.getPlayers()[i].getName());
            playerScores[i] = new JLabel("Score: " + model.getPlayers()[i].getScore());
            nameLabel.setAlignmentX(CENTER_ALIGNMENT);
            playerScores[i].setAlignmentX(CENTER_ALIGNMENT);
            playerRacks[i] = getPlayerRackPanel(model.getPlayers()[i]);

            playerPanel.add(nameLabel);
            playerPanel.add(playerScores[i]);
            playerPanel.add(playerRacks[i]);
        }

        return playerPanel;
    }

    private JPanel getPlayerRackPanel(Player player) {
        JPanel rackPanel = new JPanel(new GridLayout(2, 7, 1, 0));

        for(Tile tile : player.getRack().getTiles()) {
            JLabel label = new JLabel(String.valueOf(tile.getLetter()));
            label.setAlignmentX(LEFT_ALIGNMENT);
            rackPanel.add(label);
        }

        for(Tile tile : player.getRack().getTiles()) {
            JLabel label = new JLabel(String.valueOf(tile.getPoints()));
            label.setAlignmentX(RIGHT_ALIGNMENT);
            rackPanel.add(label);
        }

        return rackPanel;
    }

    private JPanel playCommandsPanel() {
        JPanel playCommandsPanel = new JPanel(new GridLayout(1, 2));

        JButton exchangeButton = new JButton("Exchange");
        exchangeButton.setActionCommand(Commands.EXCHANGE.toString());
        exchangeButton.addActionListener(commandController);

        JButton passButton = new JButton("Pass");
        passButton.setActionCommand(Commands.PASS.toString());
        passButton.addActionListener(commandController);

        playCommandsPanel.add(exchangeButton);
        playCommandsPanel.add(passButton);

        return playCommandsPanel;
    }

    private void initializeGame() {
        JPanel initPanel = new JPanel(new GridLayout(2, 2));

        JTextField gameNameTextField = new JTextField(20);
        initPanel.add(new JLabel("Game Name: "));
        initPanel.add(gameNameTextField);

        Choice numPlayersOptions = new Choice();
        for(int i = model.getMinPlayers(); i <= model.getMaxPlayers(); i++) {
            numPlayersOptions.add(String.valueOf(i));
        }
        initPanel.add(new JLabel("Number of players: "));
        initPanel.add(numPlayersOptions);

        if((JOptionPane.showConfirmDialog(this, initPanel, "Game Configuration", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) && (model.setPlayerSize(Integer.parseInt(numPlayersOptions.getSelectedItem())))) {
            model.setGameFileName(gameNameTextField.getText());
            playerScores = new JLabel[Integer.parseInt(numPlayersOptions.getSelectedItem())];
            playerRacks = new JPanel[Integer.parseInt(numPlayersOptions.getSelectedItem())];
            for(int i = 0; i < Integer.parseInt(numPlayersOptions.getSelectedItem()); i++) {
                String playerName = JOptionPane.showInputDialog("Player " + (i + 1) + "'s name: ");
                model.addPlayer(new Player(playerName));
            }

            gameName = new JLabel(model.getGameFileName());

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
            JPanel gamePanel = new JPanel();
            gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.X_AXIS));
            JPanel boardAndPlayCommandsPanel = new JPanel(new GridLayout(2, 1));

            boardAndPlayCommandsPanel.add(boardPanelSetup());
            boardAndPlayCommandsPanel.add(playCommandsPanel());
            gamePanel.add(boardAndPlayCommandsPanel);
            gamePanel.add(playerPanelSetup());
            mainPanel.add(gameName);
            mainPanel.add(gamePanel);
            this.add(mainPanel);
        }
        else
            JOptionPane.showMessageDialog(this, "Game setup incomplete!");
    }

    public String[] attemptPlay() {
        JPanel attemptPlayPanel = new JPanel(new GridLayout(2, 2));

        JTextField wordTextField = new JTextField(20);
        attemptPlayPanel.add(new JLabel("Enter word to play: "));
        attemptPlayPanel.add(wordTextField);

        Choice directionOption = new Choice();
        directionOption.add(Board.Direction.FORWARD.toString());
        directionOption.add(Board.Direction.DOWNWARD.toString());

        attemptPlayPanel.add(new JLabel("Direction: "));
        attemptPlayPanel.add(directionOption);

        if(JOptionPane.showConfirmDialog(this, attemptPlayPanel, "Game Configuration", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            String[] returnArray = new String[2];
            returnArray[0] = wordTextField.getText();
            returnArray[1] = directionOption.getSelectedItem();

            return returnArray;
        }
        else
            JOptionPane.showMessageDialog(this, "Play incomplete!");
        return null;
    }

    /**
     * Handle new game update to the view.
     */
    @Override
    public void handleNewGameUpdate() {
        this.dispose();
        new ScrabbleFrame();
    }

    /**
     * Handle quit update to the view.
     */
    @Override
    public void handleQuitUpdate() {
        JOptionPane.showMessageDialog(this, "Thank you for playing. Goodbye...");
        this.dispose();
    }

    /**
     * Handle help call in the view.
     *
     * @param message Help message to display.
     */
    @Override
    public void handleHelpCall(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    /**
     * Handle about call in the view.
     *
     * @param message About message to display.
     */
    @Override
    public void handleAboutCall(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    /**
     * Handle the score update.
     */
    @Override
    public void handleScoreUpdate() {
        playerScores[model.getTurn()].setText("Score: " + model.getPlayers()[model.getTurn()].getScore());
    }

    /**
     * Handle the rack update();
     */
    @Override
    public void handleRackUpdate() {
        playerRacks[model.getTurn()] = getPlayerRackPanel(model.getPlayers()[model.getTurn()]);
    }
}
