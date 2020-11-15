package internals.player;

import java.util.HashMap;
import java.util.HashSet;

public class PlayerController {
    private HashMap<String, Player> playerStore;
    private HashSet<String> activePlayers;

    public PlayerController() {
        this.playerStore = new HashMap<>();
        this.activePlayers = new HashSet<>();
    }

    public boolean createPlayer(String playerName){
        if (playerStore.containsKey(playerName)){
            return false;
        } else {
            playerStore.put(playerName, new Player(playerName));
            return true;
        }
    }

    public boolean removePlayer(String playerName){
        return playerStore.remove(playerName) != null;
    }

    public boolean changePlayerName(String playerName, String newName){
        Player p = playerStore.get(playerName);

        if (p == null){
            return false;
        }
        else{
            p.setName(newName);
            return true;
        }
    }

    /**
     *
     * @return Δισδιάστος πίνακας που αναπαριστά τον πίνακα με τα σκόρ όλων των παιχτών(ενεργών και μη).
     * Έχει γραμμές πλήθους ίσο με το πλήθος των παιχτών
     * και 2 στήλες: η πρώτη για το όνομα το παίχτη και η δεύτερη για το σκόρ του.
     */
    public String[][] getScoreboard(){
        String[][] temp = new String[playerStore.size()][2];

        int i = 0;
        for (Player p : playerStore.values()){
            temp[i][0] = p.getName();
            temp[i][1] = Integer.toString(p.getScore());
            i++;
        }

        return temp;
    }

    public int getPlayerScore(String playerName){
        Player p = playerStore.get(playerName);

        if (p == null){
            return 0;
        }
        else {
            return p.getScore();
        }
    }

    public boolean playerCalculateGain(String playerName, int gain){
        Player p = playerStore.get(playerName);

        if (p == null){
            return false;
        }
        else{
            p.scoreGain(gain);
            return true;
        }


    }

    public int getPlayerHighscore(String playerName){
        Player p = playerStore.get(playerName);

        if (p == null){
            return 0;
        }

        return p.getHighScore();
    }

}
