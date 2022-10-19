public class GameMaster {

    private Board board;
    private Bag bag;
    private Player[] players;
    private int turn;
    private Parser parser;

    public GameMaster() {
        board = new Board();
        bag = new Bag();
        turn = 0;
        parser = new Parser();
    }

    private void initializePlayers(int numPlayers) {
        players = new Player[numPlayers];

        for(Player player : players) {
            player = new Player();  // needs to initialize each player
        }
    }

    public boolean processCommand(Command command) {
        return false;
    }

    public boolean attemptPlay(String word) {
        return false;
    }

    public void playGame() {

    }
}
