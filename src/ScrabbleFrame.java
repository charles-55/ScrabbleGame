import javax.swing.*;
import java.awt.*;

public class ScrabbleFrame extends JFrame implements ScrabbleView {

    private GameMaster model;
    private BoardController boardController;
    private CommandController commandController;
    private JButton[][] board;
    private JLabel gameName;
    public enum Commands {NEW_GAME, LOAD, SAVE, SAVE_AS, QUIT, HELP, ABOUT, PLAY, EXCHANGE, PASS}

    public ScrabbleFrame() {
        model = new GameMaster();
        boardController = new BoardController();
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
        this.setSize(getMaximumSize());
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
        JPanel boardPanel = new JPanel(new GridLayout(model.getBoard().getBoardSize(), model.getBoard().getBoardSize()));

        for(int i = 0; i < model.getBoard().getBoardSize(); i++) {
            for(int j = 0; j < model.getBoard().getBoardSize(); j++) {
                JButton button = new JButton();
                button.setActionCommand(i + " " + j);
                board[i][j] = button;
                button.addActionListener(boardController);
                boardPanel.add(button);
            }
        }
        boardPanel.setPreferredSize(new Dimension(600, 600));
        boardPanel.setMaximumSize(new Dimension(900, 900));
        boardPanel.setMinimumSize(new Dimension(450, 450));

        return boardPanel;
    }

    private JPanel playerPanelSetup() {
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.PAGE_AXIS));

        for(Player player : model.getPlayers()) {
            JLabel nameLabel = new JLabel("Name: " + player.getName());
            JLabel scoreLabel = new JLabel("Score: " + player.getScore());
            nameLabel.setAlignmentX(CENTER_ALIGNMENT);
            scoreLabel.setAlignmentX(CENTER_ALIGNMENT);

            playerPanel.add(nameLabel);
            playerPanel.add(scoreLabel);
            playerPanel.add(getPlayerRackPanel(player));
        }

        playerPanel.setPreferredSize(new Dimension(200, 600));
        playerPanel.setMaximumSize(new Dimension(200, 900));
        playerPanel.setMinimumSize(new Dimension(200, 450));

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
        JPanel playCommandsPanel = new JPanel(new GridLayout(1, 3));

        JButton playButton = new JButton("Play");
        playButton.setActionCommand(Commands.PLAY.toString());
        playButton.addActionListener(commandController);

        JButton exchangeButton = new JButton("Exchange");
        exchangeButton.setActionCommand(Commands.EXCHANGE.toString());
        exchangeButton.addActionListener(commandController);

        JButton passButton = new JButton("Pass");
        passButton.setActionCommand(Commands.PASS.toString());
        passButton.addActionListener(commandController);

        playCommandsPanel.add(playButton);
        playCommandsPanel.add(exchangeButton);
        playCommandsPanel.add(passButton);

        playCommandsPanel.setPreferredSize(new Dimension(600, 100));
        playCommandsPanel.setMaximumSize(new Dimension(900, 100));
        playCommandsPanel.setMinimumSize(new Dimension(450, 100));

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

        if(JOptionPane.showConfirmDialog(this, initPanel, "Game Configuration", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            model.setGameFileName(gameNameTextField.getText());
            model.setPlayerSize(Integer.parseInt(numPlayersOptions.getSelectedItem()));
            for(int i = 0; i < Integer.parseInt(numPlayersOptions.getSelectedItem()); i++) {
                String playerName = JOptionPane.showInputDialog("Player " + (i + 1) + "'s name: ");
                model.addPlayer(new Player(playerName), i);
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
}
