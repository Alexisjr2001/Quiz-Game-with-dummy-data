package internals.player;

/**
 * Η κλάση {@code Player}, μοντελοποιεί την οντότητα του παίχτη στην εφαρμογή.
 *
 * @version 2020.11.14
 */
public class Player {
    private String name; // Το όνομα του παίχτη.
    private int score; // Το σύνολο των πόντων που έχει συγκεντρώσει ο παίχτης.
    private int highScore; // Το μέγιστο σύνολο πόντων που έχει συγκεντρώσει μέχρι στιγμής ο παίχτης.

    /**
     * Ο τυπικός κατασκευαστής της κλάσης.
     * @param name όνομα του παίχτη.
     */
    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.highScore = 0;
    }

    /**
     * Επιστρέφει το όνομα του παίχτη.
     * @return όνομα του παίχτη.
     */
    public String getName() {
        return name;
    }

    /**
     * Επιστρέφει το σύνολο των πόντων που έχει συγκεντρώσει ο παίχτης.
     * @return το τρέχον σκόρ του παίχτη
     */
    public int getScore() {
        return score;
    }

    /**
     * Επιστρέφει το μέγιστο σύνολο πόντων που έχει συγκεντρώσει μέχρι στιγμής ο παίχτης.
     * @return το μέγιστο (μέχρι τώρα) σκόρ του παίχτη.
     */
    public int getHighScore() {
        return highScore;
    }

    /**
     * Υπολογίζει και αποθηκεύει το νέο σκόρ του παίχτη και το επιστρέφει.
     * @param gain πόντοι που θα προστεθούν στο τρέχον σύνολο πόντων (μπορεί να είναι αρνητικό).
     * @return το νέο σύνολο πόντων του παίχτη.
     */
    public int scoreGain(int gain){
        score += gain;

        if (score > highScore){ // Υπολογισμός του νέου (εφόσον χρειάζεται) highscore.
            highScore = score;
        }

        return score;
    }

    /**
     * Αλλάζει το όνομα του παίχτη σε αυτό που δίνεται σαν όρισμα και επιστρέφει το παλαιό όνομα.
     * @param newName νέο όνομα παίχτη.
     * @return παλαιό όνομα παίχτη.
     */
    public String setName(String newName){
        String temp = name;

        name = newName;

        return temp;
    }
}
