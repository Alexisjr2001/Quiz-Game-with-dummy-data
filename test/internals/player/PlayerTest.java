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
    void setName() {

        String result = player.setName("Alex");

        assertEquals("Bob",result);
        assertEquals("Alex", player.getName());
    }

    @Test
    void clearCurrentScore() {

        player.scoreGain(1500);
        player.clearCurrentScore();
        assertEquals(0,player.getScore());
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
}