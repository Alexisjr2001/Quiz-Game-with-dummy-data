package internals.player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class PlayerControllerTest {
    private PlayerController playerController;
    private String result;

    @BeforeEach
    void setUp() {
        playerController = new PlayerController();
    }

    @Test
    void createPlayer() {
        result = playerController.createPlayer("Alex");
        assertEquals("Επιτυχία",result);

        result = playerController.createPlayer("John");
        assertEquals("Επιτυχία",result);

        result = playerController.createPlayer("Nick");
        assertEquals("Επιτυχία",result);

        result = playerController.createPlayer("Alex");
        assertEquals("Υπάρχει ήδη παίχτης με αυτό το όνομα",result);

        result = playerController.createPlayer("Nick");
        assertEquals("Υπάρχει ήδη παίχτης με αυτό το όνομα",result);
    }

    @Test
    void removePlayer() {
        playerController.createPlayer("Alex");
        playerController.createPlayer("John");
        playerController.createPlayer("Nick");

        result = playerController.removePlayer("Alex");
        assertEquals("Επιτυχία",result);

        result = playerController.removePlayer("Alex");
        assertEquals("Δεν υπάρχει παίχτης με αυτό το όνομα",result);

        result = playerController.removePlayer("Bob");
        assertEquals("Δεν υπάρχει παίχτης με αυτό το όνομα",result);

        result = playerController.removePlayer("Nick");
        assertEquals("Επιτυχία",result);

        result = playerController.removePlayer("Mary");
        assertEquals("Δεν υπάρχει παίχτης με αυτό το όνομα",result);
    }

    @Test
    void changePlayerName() {
        playerController.createPlayer("Alex");
        playerController.createPlayer("John");
        playerController.createPlayer("Nick");

        result = playerController.changePlayerName("Alex","Alexander");
        assertEquals("Επιτυχία", result);

        result = playerController.changePlayerName("Alexander","Alexander");
        assertEquals("Υπάρχει ήδη παίχτης με το όνομα: " + "Alexander" + "! Δεν μπορούν να υπάρχουν δύο παίχτες με το ίδιο όνομα...",result);

        result = playerController.changePlayerName("John","Alexander");
        assertEquals("Υπάρχει ήδη παίχτης με το όνομα: " + "Alexander" + "! Δεν μπορούν να υπάρχουν δύο παίχτες με το ίδιο όνομα...",result);

        result = playerController.changePlayerName("Bob","Mark");
        assertEquals("Δεν υπάρχει παίχτης με το όνομα: " + "Bob" + "! Δεν μπορεί να γίνει αλλαγή όνομα παίχτη που δεν υπάρχει...",result);

        result = playerController.changePlayerName("John","Takis");
        assertEquals("Επιτυχία", result);
    }

    @Test
    void getScoreboard() {
        playerController.createPlayer("Alex");
        playerController.createPlayer("John");
        playerController.createPlayer("Nick");

        playerController.playerCalculateGain("Alex",1000);
        playerController.playerCalculateGain("Nick",250);
        playerController.playerCalculateGain("John",500);

        playerController.countMultiplayerWins("Alex");
        playerController.countMultiplayerWins("Alex");
        playerController.countMultiplayerWins("John");
        playerController.countMultiplayerWins("Alex");
        playerController.countMultiplayerWins("Alex");
        playerController.countMultiplayerWins("John");
        playerController.countMultiplayerWins("Nick");
        playerController.countMultiplayerWins("John");

        String[][] res = {{"Alex","1000","1000","4"},{"Nick","250","250","1"},{"John","500","500","3"}};
        assertArrayEquals(res,playerController.getScoreboard());

    }

    @Test
    void getPlayerScore() {
        playerController.createPlayer("Alex");

        int res = playerController.getPlayerScore("Alex");
        assertEquals(0,res);

        playerController.playerCalculateGain("Alex",1500);

        res = playerController.getPlayerScore("Alex");
        assertEquals(1500,res);

        res = playerController.getPlayerScore("John");
        assertEquals(-1,res);
    }

    @Test
    void playerCalculateGain() {
        playerController.createPlayer("Alex");
        playerController.createPlayer("John");

        Boolean res = playerController.playerCalculateGain("Alex",1500);
        assertEquals(true, res);
        assertEquals(1500, playerController.getPlayerScore("Alex"));

        res = playerController.playerCalculateGain("John",500);
        assertEquals(true,res);
        assertEquals(500, playerController.getPlayerScore("John"));

        res = playerController.playerCalculateGain("Nick",1000);
        assertEquals(false,res);
        assertEquals(-1, playerController.getPlayerScore("Nick"));
    }

    @Test
    void getPlayerHighScore() {
        playerController.createPlayer("Alex");

        int res = playerController.getPlayerHighScore("Alex");
        assertEquals(0,res);

        playerController.playerCalculateGain("Alex",1500);

        res = playerController.getPlayerHighScore("Alex");
        assertEquals(1500,res);

        playerController.playerCalculateGain("Alex",-500);

        res = playerController.getPlayerHighScore("Alex");
        assertEquals(1500,res);

        res = playerController.getPlayerScore("John");
        assertEquals(-1,res);
    }

    @Test
    void listPlayers() {
        String[] res1 = new String[0];
        assertArrayEquals(res1,playerController.listPlayers());

        playerController.createPlayer("Alex");
        playerController.createPlayer("John");
        playerController.createPlayer("Nick");

        String[] res2 = {"Alex","Nick","John"};
        assertArrayEquals(res2,playerController.listPlayers());
    }

    @Test
    void playerExists() {
        playerController.createPlayer("Alex");
        playerController.createPlayer("John");

        assertEquals("Επιτυχία",playerController.playerExists("Alex"));
        assertEquals("Επιτυχία",playerController.playerExists("John"));
        assertEquals("Δεν υπάρχει παίχτης με το συγκεκριμένο όνομα",playerController.playerExists("Bob"));
        assertEquals("Δεν υπάρχει παίχτης με το συγκεκριμένο όνομα",playerController.playerExists("Mary"));
    }

    @Test
    void getNumberOfPlayers() {
        assertEquals(0,playerController.getNumberOfPlayers());

        playerController.createPlayer("Alex");
        playerController.createPlayer("John");
        playerController.createPlayer("Nick");

        assertEquals(3,playerController.getNumberOfPlayers());

        playerController.removePlayer("Alex");
        playerController.removePlayer("John");

        assertEquals(1,playerController.getNumberOfPlayers());
    }

    @Test
    void clearPlayerScore() {
        playerController.createPlayer("Alex");
        playerController.playerCalculateGain("Alex",500);

        assertEquals("Επιτυχία",playerController.clearPlayerScore("Alex"));
        assertEquals(0,playerController.getPlayerScore("Alex"));

        assertEquals("Δεν υπάρχει παίχτης με το συγκεκριμένο όνομα",playerController.clearPlayerScore("John"));
        assertEquals(-1,playerController.getPlayerScore("John"));
    }

    @Test
    void clearAllPlayersScore() {
        playerController.createPlayer("Alex");
        playerController.playerCalculateGain("Alex",500);

        playerController.createPlayer("John");
        playerController.playerCalculateGain("John",1500);

        playerController.clearAllPlayersScore();

        assertEquals(0,playerController.getPlayerScore("Alex"));
        assertEquals(0,playerController.getPlayerScore("John"));
    }

    @Test
    void getMultiplayerWins() {
        playerController.createPlayer("Alex");
        playerController.countMultiplayerWins("Alex");
        playerController.countMultiplayerWins("Alex");

        assertEquals(2,playerController.getMultiplayerWins("Alex"));

        assertEquals(-1,playerController.getMultiplayerWins("John"));
    }

    @Test
    void countMultiplayerWins() {
        playerController.createPlayer("Alex");

        assertEquals(1,playerController.countMultiplayerWins("Alex"));
        assertEquals(2,playerController.countMultiplayerWins("Alex"));

        assertEquals(-1,playerController.countMultiplayerWins("John"));
        assertEquals(-1,playerController.countMultiplayerWins("Bob"));
    }

    @Test
    void saveGameStatistics() {
        playerController.createPlayer("Alex");
        playerController.createPlayer("John");
        playerController.createPlayer("Nick");

        playerController.playerCalculateGain("Alex",500);
        playerController.playerCalculateGain("John",1000);
        playerController.playerCalculateGain("Nick",1500);

        playerController.countMultiplayerWins("Alex");
        playerController.countMultiplayerWins("Alex");
        playerController.countMultiplayerWins("John");
        playerController.countMultiplayerWins("Nick");

        try {
            playerController.saveGameStatistics("TestStats.dat");
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    @Test
    void loadGameStatistics() {

        try {
            playerController.loadGameStatistics("TestStats.dat");
        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }

        assertEquals(500,playerController.getPlayerHighScore("Alex"));
        assertEquals(1000,playerController.getPlayerHighScore("John"));
        assertEquals(1500,playerController.getPlayerHighScore("Nick"));

        assertEquals(2,playerController.getMultiplayerWins("Alex"));
        assertEquals(1,playerController.getMultiplayerWins("John"));
        assertEquals(1,playerController.getMultiplayerWins("Nick"));

        assertEquals(0,playerController.getPlayerScore("Alex"));
        assertEquals(0,playerController.getPlayerScore("John"));
        assertEquals(0,playerController.getPlayerScore("Nick"));

        assertEquals(-1,playerController.getPlayerHighScore("Bob"));
        assertEquals(-1,playerController.getMultiplayerWins("Bob"));
        assertEquals(-1,playerController.getPlayerScore("Bob"));

        assertEquals(-1,playerController.getPlayerHighScore("Mary"));
        assertEquals(-1,playerController.getMultiplayerWins("Mary"));
        assertEquals(-1,playerController.getPlayerScore("Mary"));
    }

    @Test
    void complexRenameResultsTest(){
        assertEquals("Επιτυχία", playerController.createPlayer("John"));
        assertNotEquals("Επιτυχία", playerController.createPlayer("John"));


        assertEquals("Επιτυχία", playerController.createPlayer("Jim"));
        assertNotEquals("Επιτυχία", playerController.createPlayer("John"));
        assertNotEquals("Επιτυχία", playerController.createPlayer("Jim"));


        assertEquals("Επιτυχία", playerController.changePlayerName("Jim", "jim"));
        assertEquals("Επιτυχία", playerController.playerExists("jim"));
        assertNotEquals("Επιτυχία", playerController.playerExists("Jim"));
        assertEquals("Επιτυχία", playerController.playerExists("John"));
        assertNotEquals("Επιτυχία", playerController.playerExists("john"));


        assertEquals(1, playerController.countMultiplayerWins("jim"));
        assertEquals(-1, playerController.countMultiplayerWins("Jim"));
        assertEquals(0, playerController.getMultiplayerWins("John"));
        assertEquals(1, playerController.countMultiplayerWins("John"));


        assertEquals("Επιτυχία", playerController.changePlayerName("John", "Jim"));
        assertEquals(-1, playerController.countMultiplayerWins("John"));
        assertEquals(-1, playerController.countMultiplayerWins("john"));
        assertEquals(2, playerController.countMultiplayerWins("Jim"));
        assertEquals(2, playerController.countMultiplayerWins("jim"));


        assertEquals(3, playerController.countMultiplayerWins("Jim"));
        assertEquals(4, playerController.countMultiplayerWins("Jim"));
        assertEquals(5, playerController.countMultiplayerWins("Jim"));
        assertEquals(3, playerController.countMultiplayerWins("jim"));

        assertEquals(-1, playerController.countMultiplayerWins("John"));
        assertEquals(-1, playerController.countMultiplayerWins("john"));

        assertEquals(6, playerController.countMultiplayerWins("Jim"));
        assertEquals(7, playerController.countMultiplayerWins("Jim"));
        assertEquals(8, playerController.countMultiplayerWins("Jim"));
        assertEquals(4, playerController.countMultiplayerWins("jim"));

    }

    @Test
    void complexDeleteResultsTest(){
        assertEquals("Επιτυχία", playerController.createPlayer("John"));
        assertEquals("Επιτυχία", playerController.removePlayer("John"));
        assertNotEquals("Επιτυχία", playerController.removePlayer("John"));
        assertEquals("Επιτυχία", playerController.createPlayer("John"));


        assertEquals("Επιτυχία", playerController.createPlayer("Jim"));
        assertNotEquals("Επιτυχία", playerController.createPlayer("John"));
        assertEquals("Επιτυχία", playerController.removePlayer("Jim"));
        assertNotEquals("Επιτυχία", playerController.createPlayer("John"));
        assertNotEquals("Επιτυχία", playerController.removePlayer("Bob"));
        assertEquals("Επιτυχία", playerController.createPlayer("Jim"));
        assertNotEquals("Επιτυχία", playerController.createPlayer("Jim"));
        assertNotEquals("Επιτυχία", playerController.createPlayer("John"));


        assertEquals("Επιτυχία", playerController.createPlayer("jim"));
        assertNotEquals("Επιτυχία", playerController.createPlayer("Jim"));
        assertNotEquals("Επιτυχία", playerController.createPlayer("jim"));
        assertEquals("Επιτυχία", playerController.playerExists("jim"));
        assertEquals("Επιτυχία", playerController.playerExists("Jim"));
        assertEquals("Επιτυχία", playerController.removePlayer("Jim"));
        assertNotEquals("Επιτυχία", playerController.playerExists("Jim"));
        assertEquals("Επιτυχία", playerController.playerExists("jim"));


        assertEquals(1, playerController.countMultiplayerWins("jim"));
        assertEquals(-1, playerController.countMultiplayerWins("Jim"));
        assertEquals(0, playerController.getMultiplayerWins("John"));
        assertEquals(1, playerController.countMultiplayerWins("John"));


        assertEquals("Επιτυχία", playerController.changePlayerName("John", "Jim"));
        assertEquals(-1, playerController.countMultiplayerWins("John"));
        assertEquals(-1, playerController.countMultiplayerWins("john"));
        assertEquals(2, playerController.countMultiplayerWins("Jim"));
        assertEquals(2, playerController.countMultiplayerWins("jim"));

        assertEquals("Επιτυχία", playerController.removePlayer("Jim"));
        assertNotEquals("Επιτυχία", playerController.playerExists("Jim"));
        assertEquals("Επιτυχία", playerController.playerExists("jim"));


        assertEquals(-1, playerController.countMultiplayerWins("Jim"));
        assertEquals(-1, playerController.countMultiplayerWins("Jim"));
        assertEquals(-1, playerController.countMultiplayerWins("Jim"));
        assertEquals(3, playerController.countMultiplayerWins("jim"));

        assertEquals(-1, playerController.countMultiplayerWins("John"));
        assertEquals(-1, playerController.countMultiplayerWins("john"));

        assertEquals("Επιτυχία", playerController.createPlayer("Jim"));

        assertEquals(1, playerController.countMultiplayerWins("Jim"));
        assertEquals(2, playerController.countMultiplayerWins("Jim"));
        assertEquals(3, playerController.countMultiplayerWins("Jim"));
        assertEquals(4, playerController.countMultiplayerWins("jim"));

    }
}