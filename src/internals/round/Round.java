package internals.round;

import internals.question.Question;
import internals.question.QuestionLibrary;

import java.util.Random;

/**
 * Η {@code Round} μοντελοποιεί το χαρακτηριστικό των γύρων της εφαρμογής.
 * Αυτή η κλάση ορίζει ένα κοινό πρότυπο (πεδία και μεθόδους) που πρέπει να ακολουθήσουν οι κλάσεις που την κληρονομούν
 * και συνεπώς δεν υπάρχει πρακτική χρησιμότητα στην δημιουργία αντικειμένων της κλάσης αυτής.
 *
 * @version 2020.11.27
 */
public abstract class Round {
    private QuestionLibrary questionStore; // Aναφορά σε αντικείμενο της κλάσης QuestionLibrary, απο όπου μπορεί να λάβει άμεσα τυχαίες (ή μη τυχαίες) κατηγορίες και έμμεσα τυχαίες ερωτήσεις.

    private Question roundQuestion; // Αποθηκεύει αναφορά στην ερώτηση σε αυτό το στάδιο του γύρου

    private int minPlayers; // Αποθηκεύει τον ελάχιστο αριθμό των παιχτών που απαιτούνται για τον συγκεκριμένο γύρο
    private int maxPlayers; // Αποθηκεύει τον μέγιστο αριθμό των παιχτών που επιτρέπονται για τον συγκεκριμένο γύρο

    private int questionNumber; // Αποθηκεύει τον αριθμό των ερωτήσεων που πρέπει να απαντηθούν σε αυτόν τον γύρο

    private String[] questionAnswers; // Αποθηκεύει ένα αντίγραφο του πίνακα των πιθανών απαντήσεων της τρέχουσας ερώτησης.

    /**
     * Ο τυπικός κατασκευαστής της κλάσης Round
     * @param questionStore Μία αναφορά σε ένα αντικείμενο της κλάσης QuestionLibrary
     * @param minPlayers  Ο ελάχιστος απαιτούμενος αριθμός παιχτών για τον γύρο
     * @param maxPlayers Ο μέγιστος επιτρεπόμενος αριθμός παιχτών για τον γύρο
     * @param questionNumber Ο αριθμός των ερωτήσεων που πρέπει να απαντηθούν σε αυτόν τον γύρο
     */
    public Round(QuestionLibrary questionStore, int minPlayers, int maxPlayers, int questionNumber) {
        this.questionStore = questionStore;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.questionNumber = questionNumber;
    }

    /**
     * Επιστρέφει το όνομα του τύπου γύρου. Πρέπει να γίνει override απο κάθε κλάση που κληρονομεί την τρέχουσα.
     * @return όνομα του τύπου του γύρου.
     */
    public abstract String getRoundName();

    /**
     * Επιστρέφει την περιγραφή του τύπου γύρου. Πρέπει να γίνει override απο κάθε κλάση που κληρονομεί την τρέχουσα.
     * @return περιγραφή του τύπου του γύρου.
     */
    public abstract String getRoundDescription();

    /**
     * Σηματοδοτεί την έναρξη του γύρου και εκτελεί τις απαραίτητες προετοιμασίες (αν αυτές χρειάζονται) για την έναρξη του γύρου.
     * Πρέπει να εκτελείται πάντα πριν την χρήση της κλάσης (ή αυτών που την κληρονομούν).
     */
    public abstract void beginRound();

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

        questionAnswers = null;

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
     * Επιστρέφει αντικείμενο της ερώτησης σε αυτό το στάδιο του γύρου για χρήση κλάσεων που κληρονομούν την {@code Round}.
     * @return αντικείμενο της ερώτησης σε αυτό το στάδιο του γύρου
     */
    protected Question getQuestionInstance(){
        return roundQuestion;
    }

    /**
     * Επιστρέφει έναν πίνακα με τα αντίγραφα (σε τυχαία σειρά) των πιθανών απαντήσεων για την ερώτηση.
     * @return πίνακα των απαντήσεων για την ερώτηση.
     */
    public String[] getQuestionAnswers(){
        if (questionAnswers == null){
            String[] tempArray = roundQuestion.getAnswers();
            Random randomGenerator = new Random();


            for (int i = 0; i < tempArray.length; i++){ // Τυχαιοποιεί τις θέσεις των στοιχείων του πίνακα
                int tempPosition = randomGenerator.nextInt(tempArray.length);
                String tempElement = tempArray[tempPosition];
                tempArray[tempPosition] = tempArray[i];
                tempArray[i] = tempElement;
            }

            questionAnswers = tempArray;
        }

        String[] temp = new String[4];
        temp[0] = questionAnswers[0];
        temp[1] = questionAnswers[1];
        temp[2] = questionAnswers[2];
        temp[3] = questionAnswers[3];

        return temp;
    }

    public String getQuestionCategory(){
        if (questionNumber > 0){
            return roundQuestion.getCategory();
        } else {
            return null;
        }
    }

    /**
     * Ελέγχει αν ο γύρος μπορεί να υποστηρίξει τον αριθμό παιχτών που δίνεται ως όρισμα.
     *
     * @param playerNumber αριθμός παιχτών που ελέγχεται για συμβατότητα με τον γύρο.
     * @return true αν ο γύρος μπορεί να υποστηρίξει τον αριθμό παιχτών που δίνεται, διαφορετικά false.
     */
    public boolean playerNumberIsCompatible(int playerNumber){
        return playerNumber>=minPlayers && playerNumber<=maxPlayers;
    }

}
