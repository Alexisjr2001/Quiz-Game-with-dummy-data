package internals.round;

import internals.question.Question;
import internals.question.QuestionLibrary;

import java.util.HashMap;

/**
 * Η {@code Thermometer} επεκτείνει την {@code Round}, μοντελοποιώντας τον τύπο γύρου Θερμόμετρο.
 * @author Ioannis Baraklilis
 * @author Alexandros Tsingos
 *
 * @version 2021.1.2
 */

public class Thermometer extends Round{
    private HashMap<String,Integer> rightAnswerCounter; //Αποθηκεύει το πλήθος των σωστών απαντήσεων κάθε παίχτη, παρέχοντας την δυνατότητα να αντιστοιχίσουμε εύκολα το όνομα του παίχτη με το αντίστοιχο πλήθος σωστών απαντήσεων
    private boolean gameIsOver; //Δείχνει εάν ο γύρος έχει ολοκληρωθεί, δηλαδή εάν κάποιος από τους παίχτες έχει απαντήσει σωστά 5 ερωτήσεις και είναι μάλιστα ο πρώτος που το κατάφερε

    /**
     * Τυπικός κατασκευαστής που αρχικοποιεί τα δεδομένα της κλάσης.
     * @param questionStore αναφορά σε αντικείμενο της κλάσης QuestionLibrary, απο την οποία θα "παρθούν" οι ερωτήσεις.
     * @param questionNumber αριθμός ερωτήσεων που θα πρέπει να απαντηθούν σε αυτό τον γύρο.
     */
    public Thermometer(QuestionLibrary questionStore, int questionNumber){
        super(questionStore,2,2, Math.max(questionNumber, 5));
        rightAnswerCounter = new HashMap<>();
        gameIsOver = false;
    }

    /**
     * Επιστρέφει το όνομα του τύπου γύρου.
     * @return όνομα του τύπου του γύρου.
     */
    @Override
    public String getRoundName() {
        return "Θερμόμετρο";
    }

    /**
     * Επιστρέφει την περιγραφή του τύπου γύρου.
     * @return περιγραφή του τύπου του γύρου.
     */
    @Override
    public String getRoundDescription() {
        return "Ο πρώτος παίχτης που θα απαντήσει σωστά 5 ερωτήσεις κερδίζει 5000 πόντους.";
    }

    /**
     * Επιστρέφει true, αν έχει ολοκληρωθεί ο γύρος
     * (κάποιος από τους παίχτες έχει απαντήσει σωστά 5 ερωτήσεις ή
     * έχει χρησιμοποιηθεί ο αριθμός των ερωτήσεων που δόθηκε ως όρισμα στην κατασκευή)
     * Επιστρέφει false, σε διαφορετική περίπτωση.
     * @return boolean τιμή ανάλογα με το αν έχει τελειώσει ο γύρος.
     */
    @Override
    public boolean isOver() {
        return gameIsOver || super.isOver();
    }


    /**
     * Σηματοδοτεί την συνέχεια του γύρου σε επόμενη ερώτηση και εκτελεί τις απαραίτητες προετοιμασίες (αν αυτές χρειάζονται) για αυτή.
     * @return αναφορά στο ίδιο το αντικείμενο για χρήση σε "αλυσιδωτές" κλήσεις αυτού ή null εάν κάποιος από τους παίχτες έχει απαντήσει σωστά 5 ερωτήσεις.
     */
    @Override
    public Round proceed() {
        if(!gameIsOver){
            return super.proceed();
        }

        return null;
    }


   /**
    * Επιστρέφει συμβολοσειρά με τη εκφώνηση της τρέχουσας ερώτησης.
    * Σε περίπτωση που έχει ολοκληρωθεί ο γύρος (κάποιος από τους παίχτες έχει απαντήσει σωστά 5 ερωτήσεις ή
    * ο αριθμός των ερωτήσεων έχουν ξεπεράσει τον αριθμό συνολικών ερωτήσεων που προβλέπονται για αυτόν τον γύρο), επιστρέφει null.
    * @return την εκφώνηση της ερώτησης ή null αν οι ερωτήσεις έχουν "εξαντληθεί".
    */
    @Override
    public String getQuestion() {
        if(gameIsOver){
            return null;
        }
        return super.getQuestion();
    }

    /**
     * Επιστρέφει αντικείμενο της ερώτησης σε αυτό το στάδιο του γύρου.
     * @return αντικείμενο της ερώτησης σε αυτό το στάδιο του γύρου ή null εάν κάποιος από τους παίχτες έχει απαντήσει σωστά 5 ερωτήσεις.
     */
    @Override
    protected Question getQuestionInstance() {
        if(gameIsOver){
            return null;
        }
        return super.getQuestionInstance();
    }

    /**
     * Επιστρέφεται η σωστή απάντηση, η οποία αντιστοιχεί στην τρέχουσα ερώτηση.
     * Σε περίπτωση που έχει ολοκληρωθεί ο γύρος (κάποιος από τους παίχτες έχει απαντήσει σωστά 5 ερωτήσεις ή
     * ο αριθμός των ερωτήσεων έχουν ξεπεράσει τον αριθμό συνολικών ερωτήσεων που προβλέπονται για αυτόν τον γύρο), επιστρέφει null.
     * @return η σωστή απάντηση στην τρέχουσα ερώτηση ή null αν έχει ολοκληρωθεί ο γύρος.
     */
    @Override
    public String getRightQuestionAnswer() {
        if(gameIsOver){
            return null;
        }
        return super.getRightQuestionAnswer();
    }

    /**
     * Επιστρέφεται η κατηγορία της ερώτησης στο τρέχον στάδιο του γύρου.
     * Σε περίπτωση που έχει ολοκληρωθεί ο γύρος (κάποιος από τους παίχτες έχει απαντήσει σωστά 5 ερωτήσεις ή
     * ο αριθμός των ερωτήσεων έχουν ξεπεράσει τον αριθμό συνολικών ερωτήσεων που προβλέπονται για αυτόν τον γύρο), επιστρέφει null.
     * @return η κατηγορία της ερώτησης ή null αν έχει ολοκληρωθεί ο γύρος.
     */
    @Override
    public String getQuestionCategory() {
        if(gameIsOver){
            return null;
        }
        return super.getQuestionCategory();
    }


    /**
     * Υπολογίζει και επιστρέφει τον αριθμό πόντων που (πιθανόν μπορεί να) κερδίζει ο παίχτης.
     * Όσο το πλήθος των σωστών απαντήσεων είναι κάτω από 5 επιστρέφονται μηδέν πόντοι,
     * ενώ όταν ο παίχτης φτάσει στις 5 σωστές απαντήσεις κερδίζει 5000 πόντους.
     * @param answer Η απάντηση που έδωσε ο παίχτης.
     * @param playerName Το όνομα του παίχτη που απαντάει.
     * @return αριθμός πόντων που αντιστοιχούν στην δοθείσα απάντηση του ορίσματος.
     */
    public int answerQuestion(String answer, String playerName){
        Question temp = getQuestionInstance(); // Παίρνω αναφορά στην τρέχουσα ερώτηση

        if(!rightAnswerCounter.containsKey(playerName)){ // Αν είναι η πρώτη φορά που απαντάει ο παίχτης κάνω κατάλληλη αρχικοποίηση στην δομή
            rightAnswerCounter.put(playerName,0);
        }

        if(temp!=null) { // Άν υπάρχουν διαθέσιμες ερωτήσεις
            if (temp.isRight(answer)) { //Αν ο παίχτης απάντησε σωστά
                rightAnswerCounter.put(playerName, rightAnswerCounter.get(playerName) + 1); //Αυξάνω το πλήθος των νικών του παίχτη κατά ένα
            }
        }
        else {
            return 0;
        }

        if(rightAnswerCounter.get(playerName) == 5){ //Αν απάντησε ο παίχτης 5 σωστές ερωτήσεις
            gameIsOver = true;  // Ο γύρος ολοκληρώνεται
            return 5000;
        }
        else{
            return 0;
        }
    }
}
