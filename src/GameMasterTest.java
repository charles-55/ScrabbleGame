import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameMasterTest {

    GameMaster test;

    @Before
    public void setup(){
        test = new GameMaster();
        test.setPlayerSize(2);
    }

    @After
    public void tearDown() {
        test = null;
    }

    @Test
    public void testPlayerSize() {
        test.setPlayerSize(2);
        assertEquals(2,test.getPlayers().length);
        test.setPlayerSize(4);
        assertEquals(4,test.getPlayers().length);
        assertSame(false,test.setPlayerSize(100));
        assertSame(false,test.setPlayerSize(0));
    }

    @Test
    public void testGameFileName(){
        test.setGameFileName("game");
        assertEquals("game",test.getGameFileName());
    }

    @Test
    public void testAttemptPlay(){
        Tile[] tiles=new Tile[7];
        tiles[0]=new Tile('L',2);
        tiles[1]=new Tile('A',2);
        tiles[2]=new Tile('T',2);
        tiles[3]=new Tile('E',2);

        test.addPlayer(new Player("charles", false));
        test.addPlayer(new Player("leslie", false));
        test.getPlayers()[test.getTurn()].getRack().setTiles(tiles);
        assertSame(true,test.attemptPlay(new PlayEvent("LATE",new int[]{7,7}, Board.Direction.DOWNWARD)));
    }

    @Test
    public void testExchangeTile(){
        Tile[] tiles=new Tile[7];
        tiles[0]=new Tile('L',2);
        tiles[1]=new Tile('A',2);
        tiles[2]=new Tile('T',2);
        tiles[3]=new Tile('E',2);

        test.addPlayer(new Player("charles", false));
        test.addPlayer(new Player("leslie", false));
        test.getPlayers()[test.getTurn()].getRack().setTiles(tiles);
        assertSame(true,test.exchangeTile(new int[]{1,2}));
    }

    @Test
    public void testChangeTurn(){
        test.setPlayerSize(3);
        test.addPlayer(new Player("leslie", false));
        test.addPlayer(new Player("james", false));
        test.addPlayer(new Player("meyiwa", false));
        assertEquals(0,test.getTurn());
        test.changeTurn();
        assertEquals(1,test.getTurn());
        test.changeTurn();
        test.changeTurn();
        assertEquals(0,test.getTurn());
    }

    @Test
    public void testAddPlayer(){
        test.setPlayerSize(2);
        test.addPlayer(new Player("charles", false));
        test.addPlayer(new Player("leslie", false));

        assertEquals("charles",test.getPlayers()[0].getName());
        assertEquals("leslie",test.getPlayers()[1].getName());

        assertFalse(test.addPlayer(new Player("jjThompson",false)));
    }

    @Test
    public void testAddViews(){
        try {
            ScrabbleView v = new ScrabbleFrame();
            ScrabbleView h = new ScrabbleFrame();
            ScrabbleView m = new ScrabbleFrame();
            test.addView(v);
            test.addView(h);
            test.addView(m);
            assertEquals(v, test.getView(0));
            assertEquals(h, test.getView(1));
            assertEquals(m, test.getView(2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void TestAi(){
        test.addPlayer(new Player("charles", true));
        test.addPlayer(new Player("leslie", false));
        assertTrue(test.getPlayers()[test.getTurn()].isAI());
        test.changeTurn();
        assertFalse(test.getPlayers()[test.getTurn()].isAI());
    }
}