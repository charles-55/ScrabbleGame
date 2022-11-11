import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameMasterTest {
        GameMaster test;
    @Before
    public void setup(){
        test=new GameMaster();
    }
    @Test
    public void testPlayerSize() throws Exception {
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

        assertSame(true,test.attemptPlay("ability",new int[]{1,3}, Board.Direction.FORWARD));
    }
    @Test
    public void testChangeTurn(){
        test.setPlayerSize(3);
        test.addPlayer(new Player("leslie"));
        test.addPlayer(new Player("james"));
        test.addPlayer(new Player("meyiwa"));
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
        test.addPlayer(new Player("charles"));
        test.addPlayer(new Player("leslie"));


        assertEquals("charles",test.getPlayers()[0].getName());
        assertEquals("leslie",test.getPlayers()[1].getName());
    }




}