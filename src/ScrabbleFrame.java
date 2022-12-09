import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import javax.sound.sampled.*;

/**
 * The Frame Class.
 *
 * @author Osamudiamen 101152520
 * @author Meyiwa Temile
 * @version 2.0
 */
public class ScrabbleFrame extends JFrame implements ScrabbleView {

    private final GameMaster model;
    private final BoardController boardController;
    private final CommandController commandController;
    private final JButton[][] board;
    private JLabel currentPlayer;
    private JLabel[] playerScores;
    private ArrayList<JLabel[]> playerRacks;
    private static Clip clip;
    private final static Color BORDER_COLOR = Color.RED;
    private final static Font FONT = new Font(Font.SERIF, Font.PLAIN|Font.BOLD,  30);
    private final static String AUDIO = "src/Audio/backgroundMusic.wav";
    public enum Commands {NEW_GAME, LOAD, SAVE, SAVE_AS, QUIT, HELP, ABOUT, EXCHANGE, PASS}

    /**
     * Create a scrabble frame
     */
    public ScrabbleFrame() {
        super("Scrabble Game");
        model = new GameMaster();
        boardController = new BoardController(this,model);
        commandController = new CommandController(model, this);
        board = new JButton[model.getBoard().getBoardSize()[0]][model.getBoard().getBoardSize()[1]];
        model.addView(this);

        playMusic();

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenuSetup());
        menuBar.add(helpMenuSetup());
        this.setJMenuBar(menuBar);

        if(initializeGame()) {
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.setMinimumSize(new Dimension(500, 500));
            this.pack();
            this.setLocationRelativeTo(null);
            this.setVisible(true);

            if (model.getPlayers()[model.getTurn()].isAI())
                model.attemptPlay(((AIPlayer) model.getPlayers()[model.getTurn()]).play(model.getBoard()));
        }
        else
            model.quit();
    }

    /**
     * Play music in an infinite loop
     */
    private void playMusic() {
        try {
            File file = new File(AUDIO);
            AudioInputStream sound = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(sound);
            clip.setFramePosition(0);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private boolean initializeGame() {
        JPanel startPanel = new JPanel(new GridLayout(4, 1));
        AtomicInteger option = new AtomicInteger(-1);

        JButton newGame = new JButton("New Game");
        JButton customizeGame = new JButton("Customize Game");
        JButton loadGame = new JButton("Load Game");
        JButton quit = new JButton("Quit");

        newGame.addActionListener(e -> {
            option.set(0);
            newGame.setBackground(Color.GREEN);
            customizeGame.setBackground(null);
            loadGame.setBackground(null);
            quit.setBackground(null);
        });
        customizeGame.addActionListener(e -> {
            option.set(1);
            newGame.setBackground(null);
            customizeGame.setBackground(Color.GREEN);
            loadGame.setBackground(null);
            quit.setBackground(null);
        });
        loadGame.addActionListener(e -> {
            option.set(2);
            newGame.setBackground(null);
            customizeGame.setBackground(null);
            loadGame.setBackground(Color.GREEN);
            quit.setBackground(null);
        });
        quit.addActionListener(e -> {
            option.set(3);
            newGame.setBackground(null);
            customizeGame.setBackground(null);
            loadGame.setBackground(null);
            quit.setBackground(Color.GREEN);
        });

        startPanel.add(newGame);
        startPanel.add(customizeGame);
        startPanel.add(loadGame);
        startPanel.add(quit);

        JOptionPane.showMessageDialog(this, startPanel);

        if(option.get() == 0)
            return newGame();
        else if(option.get() == 1)
            return customizeGame();
        else if(option.get() == 2)
            return loadGame();

        return false;
    }

    private boolean newGame() {
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
            playerRacks = new ArrayList<>();
            for(int i = 0; i < Integer.parseInt(numPlayersOptions.getSelectedItem()); i++) {
                JPanel playerPanel = new JPanel(new GridLayout(1, 4));
                JTextField playerName = new JTextField();
                Choice isAiOptions = new Choice();
                isAiOptions.add("Yes");
                isAiOptions.add("No");

                playerPanel.add(new JLabel("Player " + (i + 1) + "'s name: "));
                playerPanel.add(playerName);
                playerPanel.add(new JLabel("Is AI: "));
                playerPanel.add(isAiOptions);

                if(JOptionPane.showConfirmDialog(this, playerPanel, "Player Configuration", JOptionPane.OK_OPTION) == JOptionPane.YES_OPTION) {
                    if(isAiOptions.getSelectedItem().equals("Yes"))
                        model.addPlayer(new AIPlayer(playerName.getText()));
                    else if(isAiOptions.getSelectedItem().equals("No"))
                        model.addPlayer(new Player(playerName.getText(), false));
                }
                else {
                    JOptionPane.showMessageDialog(this, "Player setup incomplete!");
                    return false;
                }
            }
            frameContentSetup();

            return true;
        }
        else {
            JOptionPane.showMessageDialog(this, "Game setup incomplete!");
            return false;
        }
    }

    private boolean loadGame() {
        boolean load = model.load(getFilename());
        frameContentSetup();
        return load;
    }

    private boolean customizeGame() {
        JPanel sizePanel = new JPanel(new GridLayout(2, 2));
        JTextField widthTextField = new JTextField();
        JTextField heightTextField = new JTextField();

        sizePanel.add(new JLabel("Width of the board: "));
        sizePanel.add(widthTextField);
        sizePanel.add(new JLabel("Height of the board: "));
        sizePanel.add(heightTextField);

        JOptionPane.showMessageDialog(this, sizePanel);
        try {
            int width = Integer.parseInt(widthTextField.getText());
            int height = Integer.parseInt(heightTextField.getText());

            JPanel customBoardPanel = new JPanel(new GridLayout(height, width));
            Square[][] squares = new Square[height][width];
            JButton[][] customBoard = new JButton[height][width];
            CustomizeController customizeController = new CustomizeController(this, squares);

            for(int i = 0; i < height; i++) {
                for(int j = 0; j < width; j++) {
                    squares[i][j] = new Square();
                    customBoard[i][j] = new JButton(squares[i][j].getIcon());
                    customBoard[i][j].setActionCommand(i + " " + j);
                    customBoard[i][j].addActionListener(customizeController);
                    customBoardPanel.add(customBoard[i][j]);
                }
            }

            customBoardPanel.setMaximumSize(new Dimension(1000, 1000));
            customBoardPanel.setMinimumSize(new Dimension(1000, 1000));
            customBoardPanel.setPreferredSize(new Dimension(1000, 1000));

            JOptionPane.showMessageDialog(this, customBoardPanel);
            try {
                model.setBoard(new Board(squares));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
                return false;
            }
            newGame();

            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input! Customization failed!");
            return false;
        }
    }

    public String getCustomSquareType() {
        JPanel jPanel = new JPanel(new GridLayout(2, 1));
        JLabel label = new JLabel("Select the square type you want for this square...");
        Choice choice = new Choice();

        choice.add("Blank");
        choice.add("Origin");
        choice.add("Double Letter Score");
        choice.add("Double Word Score");
        choice.add("Triple Letter Score");
        choice.add("Triple Word Score");

        jPanel.add(label);
        jPanel.add(choice);

        JOptionPane.showMessageDialog(this, jPanel);
        return choice.getSelectedItem();
    }

    private void frameContentSetup() {
        this.setTitle(model.getGameFileName());
        currentPlayer = new JLabel("Player turn: " + model.getPlayers()[model.getTurn()].getName());
        currentPlayer.setFont(FONT);

        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.X_AXIS));
        JPanel boardAndPlayCommandsPanel = new JPanel();
        boardAndPlayCommandsPanel.setLayout(new BoxLayout(boardAndPlayCommandsPanel, BoxLayout.PAGE_AXIS));

        boardAndPlayCommandsPanel.add(boardPanelSetup());
        boardAndPlayCommandsPanel.add(playCommandsPanel());
        gamePanel.add(boardAndPlayCommandsPanel);
        gamePanel.add(playerPanelSetup());
        this.add(gamePanel);
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
        JPanel boardPanel = new JPanel(new GridLayout(model.getBoard().getBoardSize()[0], model.getBoard().getBoardSize()[1]));

        for(int i = 0; i < model.getBoard().getBoardSize()[0]; i++) {
            for(int j = 0; j < model.getBoard().getBoardSize()[1]; j++) {
                JButton button = new JButton();
                button.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
                button.setIcon(model.getBoard().getBoard()[i][j].getIcon());
                button.setActionCommand(i + " " + j);
                button.addActionListener(boardController);
                board[i][j] = button;
                boardPanel.add(board[i][j]);
            }
        }
        boardPanel.setPreferredSize(new Dimension(670, 670));
        boardPanel.setMaximumSize(new Dimension(670, 670));
        boardPanel.setMinimumSize(new Dimension(670, 670));

        return boardPanel;
    }

    private JPanel playerPanelSetup() {
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.PAGE_AXIS));
        playerPanel.add(currentPlayer);

        for(int i = 0; i < model.getPlayers().length; i++) {
            JLabel nameLabel = new JLabel("Name: " + model.getPlayers()[i].getName());
            nameLabel.setFont(FONT);
            playerScores[i] = new JLabel("Score: " + model.getPlayers()[i].getScore());
            playerScores[i].setFont(FONT);
            playerRacks.add(new JLabel[7]);
            JPanel rackPanel = new JPanel(new GridLayout(1, 7));

            for(int j = 0; j < 7; j++) {
                Tile tile = model.getPlayers()[i].getRack().getTiles()[j];
                if(tile.getIcon() == null)
                    playerRacks.get(i)[j] = new JLabel(String.valueOf(tile.getLetter()));
                else
                    playerRacks.get(i)[j] = new JLabel(tile.getIcon());
                rackPanel.add(playerRacks.get(i)[j]);
            }

            playerPanel.add(nameLabel);
            playerPanel.add(playerScores[i]);
            playerPanel.add(rackPanel);
        }

        return playerPanel;
    }

    private JPanel playCommandsPanel() {
        JPanel playCommandsPanel = new JPanel(new GridLayout(1, 2));
        File image;

        JButton exchangeButton = new JButton();
        exchangeButton.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        image = new File("src/Graphics/EXCHANGE.png");
        if(image.exists())
            exchangeButton.setIcon(new ImageIcon(image.toString()));
        else
            exchangeButton.setText("EXCHANGE");
        exchangeButton.setActionCommand(Commands.EXCHANGE.toString());
        exchangeButton.addActionListener(commandController);

        JButton passButton = new JButton();
        passButton.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        image = new File("src/Graphics/PASS.png");
        if(image.exists())
            passButton.setIcon(new ImageIcon(image.toString()));
        else
            passButton.setText("PASS");
        passButton.setActionCommand(Commands.PASS.toString());
        passButton.addActionListener(commandController);

        playCommandsPanel.add(exchangeButton);
        playCommandsPanel.add(passButton);

        playCommandsPanel.setPreferredSize(new Dimension(670, 100));
        playCommandsPanel.setMaximumSize(new Dimension(670, 100));
        playCommandsPanel.setMinimumSize(new Dimension(670, 50));

        return playCommandsPanel;
    }

    /**
     * Attemps a play on the board, if the move is valid the play will be made else return null
     * @return returnArray[] array of the word and direction to be played
     */
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

        if(JOptionPane.showConfirmDialog(this, attemptPlayPanel, "Play Configuration", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            String[] returnArray = new String[2];
            returnArray[0] = wordTextField.getText().toUpperCase();
            returnArray[1] = directionOption.getSelectedItem();

            return returnArray;
        }
        else
            JOptionPane.showMessageDialog(this, "Play incomplete!");
        return null;
    }

    /**
     * Handles the exchange action if a player want to exchange their current
     * tile(s) on the rack.
     * @return int[] array of exchanged tiles
     */
    public int[] getExchangeTileIndex() {
        JPanel panel = new JPanel(new GridLayout(2, 7));
        JCheckBox[] checkBoxes = new JCheckBox[7];
        ArrayList<Integer> tileIndexes = new ArrayList<>();

        for(int i = 0; i < 7; i++) {
            Icon image = model.getPlayers()[model.getTurn()].getRack().getTiles()[i].getIcon();
            if(image == null)
                panel.add(new JLabel(String.valueOf(model.getPlayers()[model.getTurn()].getRack().getTiles()[i].getLetter())));
            else
                panel.add(new JLabel(image));
        }

        for(int i = 0; i < 7; i++) {
            checkBoxes[i] = new JCheckBox();
            panel.add(checkBoxes[i]);
        }

        if(JOptionPane.showConfirmDialog(this, panel, "Exchange Configuration", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            for(int i = 0; i < 7; i++) {
                if(checkBoxes[i].isSelected())
                    tileIndexes.add(i);
            }

            return tileIndexes.stream().mapToInt(i -> i).toArray();
        }
        else
            JOptionPane.showMessageDialog(this, "Exchange incomplete!");

        return new int[0];
    }

    public String getFilename() {
        return JOptionPane.showInputDialog("Enter filename: ");
    }

    /**
     * Handle new game update to the view.
     */
    @Override
    public void handleNewGameUpdate() {
        clip.stop();
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
     * Handle the change turn update.
     * @param playerName The name of the player with the updated turn.
     */
    @Override
    public void handleChangeTurn(String playerName) {
        JOptionPane.showMessageDialog(this, "It is " + playerName + "'s turn!");
        currentPlayer.setText("Player turn: " + playerName);
    }

    /**
     * Handle the board update.
     */
    @Override
    public void handleBoardUpdate(String word, int[] coordinates, Board.Direction direction) {
        for(int i = 0; i < word.length(); i++) {
            if(direction == Board.Direction.FORWARD) {
                if(model.getBoard().getBoard()[coordinates[0]][coordinates[1] + i].getTile().getIcon() == null)
                    board[coordinates[0]][coordinates[1] + i].setText(String.valueOf(word.charAt(i)));
                else
                    board[coordinates[0]][coordinates[1] + i].setIcon(model.getBoard().getBoard()[coordinates[0]][coordinates[1] + i].getTile().getIcon());
            }
            else if(direction == Board.Direction.DOWNWARD) {
                if(model.getBoard().getBoard()[coordinates[0] + i][coordinates[1]].getTile().getIcon() == null)
                    board[coordinates[0] + i][coordinates[1]].setText(String.valueOf(word.charAt(i)));
                else
                    board[coordinates[0] + i][coordinates[1]].setIcon(model.getBoard().getBoard()[coordinates[0] + i][coordinates[1]].getTile().getIcon());
            }
        }
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
        for(int i = 0; i < 7; i++) {
            try {
                playerRacks.get(model.getTurn())[i].setIcon(model.getPlayers()[model.getTurn()].getRack().getTiles()[i].getIcon());
            } catch (Exception e) {
                playerRacks.get(model.getTurn())[i].setText(String.valueOf(model.getPlayers()[model.getTurn()].getRack().getTiles()[i].getLetter()));
            }
        }
    }

    /**
     * Set the letter of a blank tile.
     * @param tile Tile to set the letter.
     * @return true if the letter was changed, false otherwise.
     */
    @Override
    public boolean handleBlankTile(Tile tile) {
        if(JOptionPane.showConfirmDialog(this, "Do you want to use your blank tile?") == JOptionPane.YES_OPTION) {
            if(tile.setBlankTileLetter(JOptionPane.showInputDialog("What letter should the blank tile represent?").charAt(0))) {
                handleRackUpdate();
                return true;
            }
        }
        return false;
    }

    /**
     * Handle a message from the model.
     * @param message The message from the model.
     */
    @Override
    public void handleMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
