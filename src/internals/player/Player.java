package internals.player;

import java.io.Serializable;

/**
 * Η κλάση {@code Player}, μοντελοποιεί την οντότητα του παίχτη στην εφαρμογή.
 *
 * @author Ioannis Baraklilis
 * @author Alexandros Tsingos
 *
 * @version 2020.11.17
 */
public class Player implements Serializable {
    private String name; // Το όνομα του παίχτη.
    transient private int score; // Το σύνολο των πόντων που έχει συγκεντρώσει ο παίχτης στο παιχνίδι ενός παίχτη απο την στιγμή εκκίνησης της εφαρμογής.
    private int highScore; // Το μέγιστο σύνολο πόντων που έχει συγκεντρώσει μέχρι στιγμής ο παίχτης.
    private int multiplayerWins; // Ο αριθμός των νικών του παίχτη σε παιχνίδι πολλών παιχτών.
    private static final long SerialVersionUID = 7L; //ορίζουμε ένα SerialVersionUID

    /**
     * Ο τυπικός κατασκευαστής της κλάσης. Αρχικοποιεί τα δεδομένα του αντικειμένου.
     * @param name όνομα του παίχτη.
     */
    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.highScore = 0;
        this.multiplayerWins = 0;
    }

    /**
     * Επιστρέφει το όνομα του παίχτη.
     * @return όνομα του παίχτη.
     */
    public String getName() {
        return name;
    }

    /**
     * Επιστρέφει το σύνολο των πόντων που έχει συγκεντρώσει ο παίχτης στο παιχνίδι ενός παίχτη απο την στιγμή εκκίνησης της εφαρμογής ή μηδενισμού του σκορ του.
     * @return το τρέχον (στο παιχνίδι ενός παίχτη απο την στιγμή εκκίνησης της εφαρμογής ή μηδενισμού του σκορ του) σκορ του παίχτη
     */
    public int getScore() {
        return score;
    }

    /**
     * Επιστρέφει το μέγιστο σύνολο πόντων που έχει συγκεντρώσει μέχρι στιγμής ο παίχτης (στο παιχνίδι ενός παίχτη).
     * @return το μέγιστο (μέχρι τώρα) σκορ του παίχτη (στο παιχνίδι ενός παίχτη).
     */
    public int getHighScore() {
        return highScore;
    }

    /**
     * Υπολογίζει και αποθηκεύει το νέο σκορ του παίχτη (στο παιχνίδι ενός παίχτη) και το επιστρέφει.
     * Άν το νέο σκορ είναι μεγαλύτερο απο το παλιό, το νέο τίθεται ως το μέγιστο σκορ παίχτη.
     * @param gain πόντοι που θα προστεθούν στο τρέχον σύνολο πόντων (μπορεί να είναι και αρνητικό).
     * @return το νέο σύνολο πόντων του παίχτη.
     */
    public int scoreGain(int gain){
        score += gain; // Υπολογίζω και αποθηκεύω το νέο σκορ

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
        String temp = name; // Αποθήκευση παλαιού ονόματος

        name = newName;

        return temp;
    }

    /**
     * Μηδενίζει το τρέχον σκορ του παίχτη (στο παιχνίδι ενός παίχτη).
     */
    public void clearCurrentScore(){
        score = 0;
    }

    /**
     * Επιστρέφει τον αριθμό των νικών του παίχτη σε παιχνίδι πολλών παιχτών.
     * @return αριθμός νικών παίχτη σε παιχνίδι πολλών παιχτών.
     */
    public int getMultiplayerWins(){
        return multiplayerWins;
    }

    /**
     * Αυξάνει τον αποθηκευμένο αριθμό των νικών του παίχτη σε παιχνίδι πολλών παιχτών κατά 1 και επιστρέφει τον νέο αριθμό.
     * @return νέος αριθμός νικών του παίχτη σε παιχνίδι πολλών παιχτών.
     */
    public int countMultiplayerWin(){
        return ++multiplayerWins;
    }
}
