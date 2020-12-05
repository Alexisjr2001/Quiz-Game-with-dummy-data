package internals.round;

import internals.question.Question;
import internals.question.QuestionLibrary;

import java.util.HashMap;

/**
 * Η {@code Bet} επεκτείνει την {@code Round}, μοντελοποιώντας τον τύπο γύρου Ποντάρισμα.
 * @author Ioannis Baraklilis
 * @author Alexandros Tsingos
 *
 * @version 2020.12.3
 */

public class Bet extends Round {

    private HashMap<String , Integer> bets; //Αποθηκεύει το μέγεθος του στοιχήματος, παρέχοντας την δυνατότητα να αντιστοιχίσουμε εύκολα το όνομα του παίχτη με την αντίστοιχη ποσότητα στοιχήματος.

    public Bet(QuestionLibrary questionStore, int questionNumber) {
        super(questionStore, 1, 1, questionNumber);
        bets = new HashMap<>();
    }


    /**
     * Επιστρέφει το όνομα του τύπου γύρου.
     * @return όνομα του τύπου του γύρου.
     */
    @Override
    public String getRoundName(){
        return "Ποντάρισμα";
    }

    /**
     * Επιστρέφει την περιγραφή του τύπου γύρου.
     * @return περιγραφή του τύπου του γύρου.
     */
    @Override
    public String getRoundDescription(){
        return String.format("Γύρος με πονταρίσματα!%n" +
                "Ο παίκτης μπορεί να ποντάρει 250, 500, 750 και 1000 πόντους.%n" +
                "Στη συνέχεια αν απαντήσει σωστά κερδίζει τους πόντους που πόνταρε, αλλιώς τους χάνει.");
    }

    /**
     * Αποθηκεύει το ποντάρισμα του παίχτη και επιστρέφει την κατάσταση επιτυχίας αυτής της ενέργειας.
     * Ο παίκτης μπορεί να ποντάρει 250, 500, 750 και 1000 πόντους.
     *
     * Άν το ποντάρισμα του παίχτη είναι μία από τις παραπάνω τιμές, η ενέργεια θα πετύχει και θα επιστραφεί η συμβολοσειρά "Επιτυχία".
     * Διαφορετικά, η ενέργεια θα δεν πετύχει και θα επιστραφεί συμβολοσειρά που περιγράφει το σφάλμα.
     *
     * @param playerName Το όνομα του παίχτη
     * @param betAmount Η τιμή του πονταρίσματος (μία απο τις τιμές: 250, 500, 750, 1000)
     * @return Η κατάσταση επιτυχίας της μεθόδου. "Επιτυχία" για επιτυχία, "Μη έγκυρη τιμή πονταρίσματος" διαφορετικά.
     */
    public String placeBet(String playerName, int betAmount){

        switch(betAmount){
            case 250:
            case 500:
            case 750:
            case 1000:
                bets.put(playerName,betAmount);
                return "Επιτυχία";
            default:
                return "Μη έγκυρη τιμή πονταρίσματος";
        }

    }

    /**
     * Υπολογίζει και επιστρέφει τον αριθμό πόντων που (πιθανόν μπορεί να) κερδίζει ο παίχτης δεδομένης της απάντησης που έδωσε και του πονταρίσματος που έκανε πριν.
     * Αν το όνομα παίχτη που έδωσε έχει κάνει ποντάρισμα και η απάντηση είναι σωστή, τότε επιστρέφει τον αριθμό πόντων που κερδίζει.
     * Αν το όνομα παίχτη που έδωσα έχει κάνει ποντάρισμα και η απάντηση είναι λάθος, τότε επιστρέφει τον αριθμό πόντων που χάνει.
     * Διαφορετικά, επιστρέφεται 0.
     *
     * @param answer Η απάντηση που έδωσε ο παίχτης.
     * @param playerName Το όνομα του παίχτη που απαντάει.
     * @return αριθμός πόντων που αντιστοιχούν στην δοθείσα απάντηση των ορισμάτων.
     */
    public int answerQuestion(String answer, String playerName){
        Question temp = getQuestionInstance();

        if(temp!=null && bets.containsKey(playerName)){
            if(temp.isRight(answer)) {
                return bets.get(playerName);
            }
            else {
                return -1 * bets.get(playerName);
            }
        }
        else {
            return 0;
        }
    }
}
