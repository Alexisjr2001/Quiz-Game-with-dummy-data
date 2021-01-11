package internals.player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player player;

    @BeforeEach
    void setUp() {

        player = new Player("Bob");
    }

    @Test
    void getName() {

        assertEquals("Bob", player.getName());
    }

    @Test
    void getScore() {

        assertEquals(0,player.getScore());
    }

    @Test
    void getHighScore() {

        assertEquals(0,player.getHighScore());
    }


    @Test
    void complexHighScoreTest(){
        assertEquals(0, player.getHighScore());

        player.scoreGain(15);
        assertEquals(15, player.getHighScore());

        player.scoreGain(-14);
        assertEquals(15, player.getHighScore());

        player.scoreGain(-1*player.getScore());
        assertEquals(15, player.getHighScore());
        player.scoreGain(10);
        player.scoreGain(15);
        player.scoreGain(105);
        player.scoreGain(-5);
        assertEquals(130, player.getHighScore());

        player.scoreGain(-125);
        assertEquals(130, player.getHighScore());

        player.scoreGain(180);
        assertEquals(180, player.getHighScore());
    }


    @Test
    void scoreGain() {

        player.scoreGain(250);
        assertEquals(250,player.getScore());
        assertEquals(250,player.getHighScore());

        player.scoreGain(-500);
        assertEquals(-250,player.getScore());
        assertEquals(250,player.getHighScore());

        player.scoreGain(1000);
        assertEquals(750,player.getScore());
        assertEquals(750,player.getHighScore());
    }


    @Test
    void complexScoreTest(){
        assertEquals(0, player.getScore());

        player.scoreGain(15);
        assertEquals(15, player.getScore());

        player.scoreGain(-14);
        assertEquals(1, player.getScore());

        player.scoreGain(-1*player.getScore());
        assertEquals(0, player.getScore());
        player.scoreGain(10);
        player.scoreGain(15);
        player.scoreGain(105);
        player.scoreGain(-5);
        assertEquals(125, player.getScore());

        player.scoreGain(-125);
        assertEquals(0, player.getScore());
    }


    @Test
    void setName() {

        String result = player.setName("Alex");

        assertEquals("Bob",result);
        assertEquals("Alex", player.getName());
    }

    @Test
    void setNameEffectsTest() {
        assertEquals(0, player.getScore());
        assertEquals(0, player.getHighScore());
        assertEquals(0, player.getMultiplayerWins());

        assertEquals("Bob", player.getName());
        player.countMultiplayerWin();
        player.scoreGain(100);

        String result = player.setName("Alex");
        assertEquals("Alex", player.getName());
        assertNotEquals("John", player.getName());
        assertNotEquals(result, player.getName());
        player.setName("John");
        assertNotEquals("Alex", player.getName());
        assertNotEquals(result, player.getName());
        assertEquals("John", player.getName());

        assertEquals(100, player.getScore());
        assertEquals(100, player.getHighScore());
        assertEquals(1, player.getMultiplayerWins());

        Player p2 = new Player("Bob");

        assertEquals(0, p2.getScore());
        assertEquals(0, p2.getHighScore());
        assertEquals(0, p2.getMultiplayerWins());

        p2.scoreGain(-50);

        player.setName("Bob");
        p2.setName("John");

        assertEquals(100, player.getScore());
        assertEquals(100, player.getHighScore());
        assertEquals(1, player.getMultiplayerWins());

        assertEquals(-50, p2.getScore());
        assertEquals(0, p2.getHighScore());
        assertEquals(0, p2.getMultiplayerWins());

    }


    @Test
    void clearCurrentScore() {

        player.scoreGain(1500);
        player.clearCurrentScore();
        assertEquals(0,player.getScore());
    }


    @Test
    void complexClearCurrentScoreTest(){
        assertEquals(0, player.getScore());

        player.scoreGain(15);
        assertEquals(15, player.getScore());
        player.clearCurrentScore();
        assertEquals(0, player.getScore());
        assertEquals(15, player.getHighScore());

        player.scoreGain(-14);
        assertEquals(-14, player.getScore());
        player.clearCurrentScore();
        assertEquals(0, player.getScore());
        assertEquals(15, player.getHighScore());


        player.clearCurrentScore();
        assertEquals(0, player.getScore());
        player.scoreGain(10);
        player.scoreGain(15);
        player.scoreGain(105);
        player.scoreGain(-5);
        assertEquals(125, player.getScore());
        player.clearCurrentScore();
        assertEquals(0, player.getScore());
        assertEquals(130, player.getHighScore());

        player.scoreGain(-125);
        assertEquals(-125, player.getScore());
        player.clearCurrentScore();
        assertEquals(0, player.getScore());
        assertEquals(130, player.getHighScore());

    }


    @Test
    void getMultiplayerWins() {

        assertEquals(0,player.getMultiplayerWins());
    }

    @Test
    void countMultiplayerWin() {

        player.countMultiplayerWin();
        assertEquals(1,player.getMultiplayerWins());

        player.countMultiplayerWin();
        player.countMultiplayerWin();
        assertEquals(3,player.getMultiplayerWins());
    }

    @Test
    void multipleCountMultiplayerWinTest() {
        assertEquals(0, player.getMultiplayerWins());

        player.countMultiplayerWin();
        assertEquals(1,player.getMultiplayerWins());

        player.countMultiplayerWin();
        player.countMultiplayerWin();
        assertEquals(3,player.getMultiplayerWins());

        for (int i = 0; i < 100; i++){
            player.countMultiplayerWin();
        }
        assertEquals(103, player.getMultiplayerWins());

        Player p2 = new Player("player2");

        player.countMultiplayerWin();
        assertEquals(0, p2.getMultiplayerWins());

        player.countMultiplayerWin();
        player.countMultiplayerWin();
        assertEquals(0, p2.getMultiplayerWins());

        p2.countMultiplayerWin();
        p2.countMultiplayerWin();

        assertEquals(2, p2.getMultiplayerWins());
        assertEquals(106, player.getMultiplayerWins());
    }

}