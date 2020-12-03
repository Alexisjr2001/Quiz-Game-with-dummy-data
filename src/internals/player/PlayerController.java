package internals.player;

import java.util.HashMap;
import java.util.HashSet;


/**
 * Η κλάση {@code PlayerController}, αποτελεί κλάση τύπου "controller" για την κλάση {@code Player}.
 * Δηλαδή, αποθηκεύει και χειρίζεται αντικείμενα τύπου {@code Player} και προσφέρει ένα απλό περιβάλλον διαχείρισης αυτών μέσω κλήσης μεθόδων.
 *
 * @version 2020.11.14
 */
public class PlayerController {
    private HashMap<String, Player> playerStore; // Αποθηκεύει τους παίχτες, δίνοντας την δυνατότητα να αντιστοιχίσουμε εύκολα τα ονόματα τους με το αντίστοιχο αντικείμενο που τούς διαχειρίζεται.

    /**
     * Τυπικός κατασκευαστής της κλάσης.
     */
    public PlayerController() {
        this.playerStore = new HashMap<>();
    }

    /**
     * Δημιουργεί έναν παίχτη και επιστρέφει την κατάσταση επιτυχίας αυτής της ενέργειας.
     *
     * Άν δεν υπάρχει ίδιος παίχτης με το ίδιο όνομα, η ενέργεια θα πετύχει και θα επιστραφεί η συμβολοσειρά "Επιτυχία".
     * Διαφορετικά, η ενέργεια δεν θα πετύχει και θα επιστραφεί συμβολοσειρά που περιγράφει το σφάλμα.
     *
     * @param playerName όνομα νέου παίχτη.
     * @return κατάσταση επιτυχίας της μεθόδου. "Επιτυχία" σε περίπτωση επιτυχίας, "Υπάρχει ήδη παίχτης με αυτό το όνομα" διαφορετικά.
     */
    public String createPlayer(String playerName){
        if (playerStore.containsKey(playerName)){
            return "Υπάρχει ήδη παίχτης με αυτό το όνομα";
        } else {
            playerStore.put(playerName, new Player(playerName));
            return "Επιτυχία";
        }
    }

    /**
     * Διαγράφει έναν παίχτη και επιστρέφει την κατάσταση επιτυχίας αυτής της ενέργειας.
     *
     * Άν υπάρχει παίχτης με αυτό το όνομα, η ενέργεια θα πετύχει και θα επιστραφεί η συμβολοσειρά "Επιτυχία".
     * Διαφορετικά, η ενέργεια θα δεν πετύχει και θα επιστραφεί συμβολοσειρά που περιγράφει το σφάλμα.
     *
     * @param playerName το όνομα του παίχτη προς διαγραφή.
     * @return η κατάσταση επιτυχίας της μεθόδου. "Επιτυχία" σε περίπτωση επιτυχίας, "Δεν υπάρχει παίχτης με αυτό το όνομα" διαφορετικά.
     */
    public String removePlayer(String playerName){
        Player temp = playerStore.remove(playerName);
        if (temp != null){
            return "Επιτυχία";
        } else {
            return "Δεν υπάρχει παίχτης με αυτό το όνομα";
        }
    }

    /**
     * Αλλάζει το όνομα ενός παίχτη και επιστρέφει την κατάσταση επιτυχίας αυτής της ενέργειας.
     *
     * Άν υπάρχει παίχτης με αυτό το όνομα και δεν υπάρχει παίχτης με ίδιο όνομα με το νέο που δίνεται σαν δεύτερο όρισμα, η ενέργεια θα πετύχει και θα επιστραφεί η συμβολοσειρά "Επιτυχία".
     * Διαφορετικά, η ενέργεια θα δεν πετύχει και θα επιστραφεί συμβολοσειρά που περιγράφει το σφάλμα.
     *
     * @param playerName όνομα παίχτη προς αλλαγή.
     * @param newName νέο όνομα παίχτη προς αλλαγή.
     * @return κατάσταση επιτυχίας. "Επιτυχία" σε περίπτωση επιτυχίας, διαφορετικά επιστρέφει "Δεν υπάρχει παίχτης με αυτό το όνομα" ή  "Υπάρχει ήδη παίχτης με αυτό το όνομα" αντιστοίχως με το πρόβλημα.
     */
    public String changePlayerName(String playerName, String newName){
        Player p = playerStore.get(playerName);

        if (p == null){
            return "Δεν υπάρχει παίχτης με αυτό το όνομα";
        } else if (!playerExists(newName).equals("Επιτυχία")){
            playerStore.remove(playerName);
            p.setName(newName);
            playerStore.put(newName, p);
            return "Επιτυχία";
        } else {
            return "Υπάρχει ήδη παίχτης με αυτό το όνομα";
        }
    }

    /**
     * Επιστρέφει δισδιάστατο πίνακα συμβολοσειρών που αναπαριστούν τον πίνακα με τα σκόρ όλων των αποθηκευμένων παιχτών.
     *
     * Συγκεκριμένα, αυτός ο δισδιάστατος πίνακας θα έχει αριθμό γραμμών ίσο με το πλήθος των παιχτών
     * και τριών στηλών: Η πρώτη στήλη ({@code getScoreboard()[i][0]}) περιέχει το όνομα του (i-ου) παίχτη,
     * η δεύτερη στήλη ({@code getScoreboard()[i][1]}) το τρέχον σκόρ του (i-ου) παίχτη
     * και η τρίτη στήλη ({@code getScoreboard()[i][2]}) το μέγιστο σκόρ (highscore) του (i-ου) παίχτη.
     *
     * @return Δισδιάστος πίνακας που αναπαριστά τον πίνακα με τα σκόρ και highscore όλων των αποθηκευμένων παιχτών.
     */
    public String[][] getScoreboard(){
        String[][] temp = new String[playerStore.size()][3];

        int i = 0;
        for (Player p : playerStore.values()){
            temp[i][0] = p.getName();
            temp[i][1] = Integer.toString(p.getScore());
            temp[i][2] = Integer.toString(p.getHighScore());
            i++;
        }

        return temp;
    }

    /**
     * Επιστρέφει το τρέχον σκόρ του παίχτη (αν υπάρχει) με το όνομα που δίνεται ως όρισμα.
     * Άν δεν υπάρχει επιστρέφεται -1.
     *
     * @param playerName όνομα παίχτη του οποίου το σκόρ θα επιστραφεί.
     * @return το σκόρ του παίχτη αν αυτός υπάρχει, διαφορετικά -1.
     */
    public int getPlayerScore(String playerName){
        Player p = playerStore.get(playerName);

        if (p == null){
            return -1;
        }
        else {
            return p.getScore();
        }
    }

    /**
     * Υπολογίζει και αποθηκεύει το νέο σκόρ του παίχτη (αν υπάρχει) του οποίου το όνομα δίνεται ως όρισμα και επιστρέφεται η κατάσταση επιτυχίας αυτής της ενέργειας.
     * Αν υπάρχει παίχτης με αυτό το όνομα, η ενέργεια επιτυγχάνει και επιστρέφεται true, διαφορετικά επιστρέφεται false.
     *
     * @param playerName όνομα του παίχτη.
     * @param gain πόντοι που θα προστεθούν στο τρέχον σύνολο πόντων (μπορεί να είναι αρνητικό).
     * @return η κατάσταση επιτυχίας της μεθόδου.
     */
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

    /**
     * Επιστρέφει το μέγιστο σκόρ (highscore) του παίχτη (αν υπάρχει) του οποίου το όνομα δίνεται ως όρισμα.
     * Αν υπάρχει παίχτης με αυτό το όνομα, η ενέργεια επιτυγχάνει και επιστρέφεται το μέγιστο σκόρ, διαφορετικά επιστρέφεται -1.
     *
     * @param playerName όνομα του παίχτη.
     * @return το μέγιστο σκόρ (highscore) του παίχτη αν αυτός υπάρχει, διαφορετικά -1.
     */
    public int getPlayerHighscore(String playerName){
        Player p = playerStore.get(playerName);

        if (p == null){
            return -1;
        }

        return p.getHighScore();
    }


    /**
     *
     * @return
     */
    public String[] listPlayers(){
        String[] playerNames = new String[playerStore.size()];
        int i=0;
        for(String aPlayerName : playerStore.keySet()){
            playerNames[i] = aPlayerName;
            i++;
        }

        return playerNames;
    }

    public String playerExists(String playerName){
        if (playerStore.containsKey(playerName)){
            return "Επιτυχία";
        }
        else{
            return "Δεν υπάρχει παίχτης με το συγκεκριμένο όνομα";
        }
    }

    public int getNumberOfPlayers(){
        return playerStore.size();
    }
}
