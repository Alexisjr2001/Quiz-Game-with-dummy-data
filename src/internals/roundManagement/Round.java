package internals.roundManagement;

import internals.question.Question;
import internals.question.QuestionLibrary;

/**
 * Η {@code Round} μοντελοποιεί το χαρακτηριστικό των γύρων της εφαρμογής.
 * Αυτή η κλάση ορίζει ένα κοινό πρότυπο (πεδία και μεθόδους) που πρέπει να ακολουθήσουν οι κλάσεις που την κληρονομούν
 * και συνεπώς δεν υπάρχει πρακτική χρησιμότητα στην δημιουργία αντικειμένων της κλάσης αυτής.
 *
 * @version 2020.11.14
 */
public class Round {
    protected QuestionLibrary questionStore; // Αντίγραφο (αναφορά σε αντικείμενο της) κλάσης QuestionLibrary, απο όπου μπορεί να λάβει άμεσα τυχαίες (ή μη τυχαίες) κατηγορίες και έμμεσα τυχαίες ερωτήσεις.

    protected Question roundQuestion; // Αποθηκεύει αναφορά στην ερώτηση σε αυτή την φάση του γύρου

    protected int requiredNumberOfPlayers; // Αποθηκεύει τον ελάχιστο αριθμό των παιχτών που απαιτούνται για τον συγκεκριμένο γύρο

    protected int questionNumber; // Αποθηκεύει τον αριθμό των ερωτήσεων που πρέπει να απαντηθούν σε αυτόν τον γύρο

    /**
     * Ο τυπικός κατασκευαστής της κλάσης Round
     * @param questionStore Μία αναφορά σε ένα αντικείμενο της κλάσης QuestionLibrary
     * @param requiredNumberOfPlayers Ο ελάχιστος απαιτούμενος αριθμός παιχτών για τον γύρο
     * @param questionNumber Ο αριθμός των ερωτήσεων που πρέπει να απαντηθούν σε αυτόν τον γύρο
     */
    public Round(QuestionLibrary questionStore, int requiredNumberOfPlayers, int questionNumber) {
        this.questionStore = questionStore;
        this.requiredNumberOfPlayers = requiredNumberOfPlayers;
        this.questionNumber = questionNumber;
    }

    /**
     * Επιστρέφει το όνομα του τύπου γύρου. Πρέπει να γίνει override απο κάθε κλάση που κληρονομεί την τρέχουσα.
     * @return όνομα του τύπου του γύρου.
     */
    public String getRoundName() {
        return "Όνομα γύρου"; // Επιστρέφει placeholder τιμή
    }

    /**
     * Επιστρέφει την περιγραφή του τύπου γύρου. Πρέπει να γίνει override απο κάθε κλάση που κληρονομεί την τρέχουσα.
     * @return περιγραφή του τύπου του γύρου.
     */
    public String getRoundDescription() {
        return "Περιγραφή τύπου γύρου"; // Επιστρέφει placeholder τιμή
    }

    /**
     * Σηματοδοτεί την έναρξη του γύρου και εκτελεί τις απαραίτητες προετοιμασίες (αν αυτές χρειάζονται) για την έναρξη του γύρου.
     * Πρέπει να εκτελείται πάντα πριν την χρήση της κλάσης (ή αυτών που την κληρονομούν).
     */
    public void beginRound(){

    }

    /**
     * @return κατάσταση εκτέλεσης του γύρου. true, αν έχει τελειώσει (έχει χρησιμοποιηθεί ο αριθμός των ερωτήσεων που δόθηκε ως όρισμα στην κατασκευή)
     * και false σε διαφορετική περίπτωση.
     */
    public boolean isOver(){
        return questionNumber == 0;
    }

    /**
     * Σηματοδοτεί την συνέχεια του γύρου σε επόμενη ερώτηση και εκτελεί τις απαραίτητες προετοιμασίες (αν αυτές χρειάζονται) για αυτή.
     * @return αναφορά στο ίδιο το αντικείμενο για χρήση σε αλυσιδωτές κλήσεις αυτού.
     */
    public Round proceed(){
        if (questionNumber > 0){
            roundQuestion = questionStore.getRandomQuestionCategory().getRandomQuestion();
            questionNumber--;
        } else {
            roundQuestion = null;
        }

        return this;
    }

    /**
     * Επιστρέφει συμβολοσειρά με τη εκφώνηση της ερώτησης.
     * Σε περίπτωση που ο αριθμός των ερωτήσεων έχουν ξεπεράσει τον αριθμό συνολικών ερωτήσεων που προβλέπονται για τον γύρο, επιστρέφει null.
     * @return την εκφώνηση της επόμενης ερώτησης ή null αν οι ερωτήσεις έχουν "εξαντληθεί".
     */
    public String getQuestion() {
        if (questionNumber > 0){
            return roundQuestion.getQuestion();
        } else {
            return null;
        }
    }

    /**
     * Επιστρέφει έναν πίνακα με τα αντίγραφα των πιθανών απαντήσεων για την ερώτηση.
     * @return πίνακα των απαντήσεων για την ερώτηση.
     */
    public String[] getQuestionAnswers(){
        return roundQuestion.getAnswers();
    }

    /**
     * Υπολογίζει και επιστρέφει την αριθμό πόντων που (πιθανόν μπορεί να) κερδίζει ο παίχτης δεδομένης της απάντησης που έδωσε.
     * Πρέπει να γίνει override απο κάθε κλάση που κληρονομεί την τρέχουσα.
     * @param answer Η απάντηση που έδωσε ο παίχτης.
     * @return αριθμός πόντων που αντιστοιχούν στην δοθείσα απάντηση του ορίσματος.
     */
    public int answerQuestion(String answer) {
        return 0; // Επιστρέφει placeholder τιμή
    }

    /**
     * Ελέγχει αν ο γύρος μπορεί να υποστηρίξει τον αριθμό παιχτών που δίνεται ως όρισμα.
     *
     * @param playerNumber αριθμός παιχτών που ελέγχεται για συμβατότητα με τον γύρο.
     * @return true αν ο γύρος μπορεί να υποστηρίξει τον αριθμό παιχτών που δίνεται, διαφορετικά false.
     */
    public boolean allowsPlayerNumber(int playerNumber){
        return playerNumber >= requiredNumberOfPlayers;
    }

}
