import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.sound.sampled.*;

public class ScrabbleFrame extends JFrame implements ScrabbleView {

    private final GameMaster model;
    private final BoardController boardController;
    private final CommandController commandController;
    private final JButton[][] board;
    private JLabel gameName;
    private JLabel currentPlayer;
    private JLabel[] playerScores;
    private JLabel[] playerRacks;
    public enum Commands {NEW_GAME, LOAD, SAVE, SAVE_AS, QUIT, HELP, ABOUT, EXCHANGE, PASS}

    private Clip clip;

    public ScrabbleFrame() {
        model = new GameMaster();
        boardController = new BoardController(this,model);
        commandController = new CommandController(model, this);
        board = new JButton[model.getBoard().getBoardSize()][model.getBoard().getBoardSize()];
        model.addView(this);

        playMusic("Audio/backgroundMusic.wav");

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenuSetup());
        menuBar.add(helpMenuSetup());
        this.setJMenuBar(menuBar);

        initializeGame();

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Scrabble Game");
        this.setMinimumSize(new Dimension(500, 500));
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void playMusic(String fileName) {
        try {
            File file = new File(fileName);
            if (file.exists())
            {
                AudioInputStream sound = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(sound);
                clip.setFramePosition(0);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            else
            {
                throw new RuntimeException("Sound: file not found: " + fileName);
            }
        }
        catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            throw new RuntimeException("Sound: Unsupported Audio File: " + e);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Sound: Input/Output Error: " + e);
        }
        catch (LineUnavailableException e) {
            e.printStackTrace();
            throw new RuntimeException("Sound: Line Unavailable Exception Error: " + e);
        }
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

        boardPanel.add(new JLabel());
        for(int i = 0; i < model.getBoard().getBoardSize(); i++) {
            JLabel label = new JLabel(String.valueOf(i));
            boardPanel.add(label);
        }
        for(int i = 0; i < model.getBoard().getBoardSize(); i++) {
            boardPanel.add(new JLabel(String.valueOf(i)));
            for(int j = 0; j < model.getBoard().getBoardSize(); j++) {
                JButton button = new JButton();
                button.setActionCommand(i + " " + j);
                board[i][j] = button;
                button.addActionListener(boardController);
                boardPanel.add(button);
            }
        }
        boardPanel.setPreferredSize(new Dimension(685, 685));
        boardPanel.setMaximumSize(new Dimension(685, 685));
        boardPanel.setMinimumSize(new Dimension(685, 685));

        return boardPanel;
    }

    private JPanel playerPanelSetup() {
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.PAGE_AXIS));
        playerPanel.add(currentPlayer);

        for(int i = 0; i < model.getPlayers().length; i++) {
            JLabel nameLabel = new JLabel("Name: " + model.getPlayers()[i].getName());
            playerScores[i] = new JLabel("Score: " + model.getPlayers()[i].getScore());
            playerRacks[i] = new JLabel(model.getPlayers()[i].getRack().getRackLetters());

            playerPanel.add(nameLabel);
            playerPanel.add(playerScores[i]);
            playerPanel.add(playerRacks[i]);
        }

        return playerPanel;
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

        playCommandsPanel.setPreferredSize(new Dimension(685, 100));
        playCommandsPanel.setMaximumSize(new Dimension(685, 100));
        playCommandsPanel.setMinimumSize(new Dimension(685, 100));

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
            playerRacks = new JLabel[Integer.parseInt(numPlayersOptions.getSelectedItem())];
            for(int i = 0; i < Integer.parseInt(numPlayersOptions.getSelectedItem()); i++) {
                String playerName = JOptionPane.showInputDialog("Player " + (i + 1) + "'s name: ");
                model.addPlayer(new Player(playerName));
            }

            gameName = new JLabel(model.getGameFileName());
            currentPlayer = new JLabel("Player turn: " + model.getPlayers()[model.getTurn()].getName());

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
            JPanel gamePanel = new JPanel();
            gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.X_AXIS));
            JPanel boardAndPlayCommandsPanel = new JPanel();
            boardAndPlayCommandsPanel.setLayout(new BoxLayout(boardAndPlayCommandsPanel, BoxLayout.PAGE_AXIS));

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

    public int[] getExchangeTileIndex() {
        JPanel exchangeNumPanel = new JPanel(new GridLayout(1, 2));
        Choice exchangeNumOptions = new Choice();
        for(int i = 1; i <= 7; i++) {
            exchangeNumOptions.add(String.valueOf(i));
        }
        exchangeNumPanel.add(new JLabel("Number of tiles to exchange:"));
        exchangeNumPanel.add(exchangeNumOptions);

        if(JOptionPane.showConfirmDialog(this, exchangeNumPanel, "Exchange Configuration", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            Character[] tiles = new Character[7];
            for(int i = 0; i < 7; i++) {
                tiles[i] = model.getPlayers()[model.getTurn()].getRack().getTiles()[i].getLetter();
            }

            JList<Character> rack = new JList<>(tiles);
            rack.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            if(JOptionPane.showConfirmDialog(this, rack, "Exchange Configuration", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                return rack.getSelectedIndices();
            else
                JOptionPane.showMessageDialog(this, "Exchange incomplete!");
        }
        else
            JOptionPane.showMessageDialog(this, "Exchange incomplete!");

        return new int[0];
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
                //board[coordinates[0]][coordinates[1] + i].setText(String.valueOf(word.charAt(i)));
                board[coordinates[0]][coordinates[1] + i].setIcon(model.getBoard().getBoard()[coordinates[0]][coordinates[1] + i].getTile().getIcon());
            }
            else if(direction == Board.Direction.DOWNWARD)
                board[coordinates[0] + i][coordinates[1]].setText(String.valueOf(word.charAt(i)));
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
        playerRacks[model.getTurn()].setText(model.getPlayers()[model.getTurn()].getRack().getRackLetters());
    }

    /**
     * Get a letter to set the blank tile.
     */
    @Override
    public char handleBlankTile() {
        return JOptionPane.showInputDialog("What letter should the blank tile represent?").charAt(0);
    }
}
