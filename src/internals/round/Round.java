package internals.round;

import internals.question.ImageQuestion;
import internals.question.Question;
import internals.question.QuestionLibrary;

import java.util.Random;

/**
 * Η {@code Round} μοντελοποιεί το χαρακτηριστικό των γύρων της εφαρμογής.
 * Αυτή η κλάση ορίζει ένα κοινό πρότυπο (πεδία και μεθόδους) που πρέπει να ακολουθήσουν οι κλάσεις που την κληρονομούν
 * και συνεπώς δεν υπάρχει πρακτική χρησιμότητα στην δημιουργία αντικειμένων της κλάσης αυτής.
 *
 * @author Ioannis Baraklilis
 * @author Alexandros Tsingos
 *
 * @version 2020.11.29
 */
public abstract class Round {
    private QuestionLibrary questionStore; // Aναφορά σε αντικείμενο της κλάσης QuestionLibrary, από όπου μπορεί να λάβει άμεσα τυχαίες (ή μη τυχαίες) κατηγορίες και έμμεσα τυχαίες ερωτήσεις.

    private Question roundQuestion; // Αποθηκεύει αναφορά στην τρέχουσα ερώτηση του γύρου

    private int minPlayers; // Αποθηκεύει τον ελάχιστο αριθμό των παιχτών που απαιτούνται για τον συγκεκριμένο γύρο
    private int maxPlayers; // Αποθηκεύει τον μέγιστο αριθμό των παιχτών που επιτρέπονται για τον συγκεκριμένο γύρο

    private int questionNumber; // Αποθηκεύει τον αριθμό των ερωτήσεων που πρέπει να απαντηθούν σε αυτόν τον γύρο

    private String[] questionAnswers; // Αποθηκεύει ένα αντίγραφο του πίνακα των πιθανών απαντήσεων της τρέχουσας ερώτησης.

    /**
     * Ο τυπικός κατασκευαστής της κλάσης Round. Αρχικοποιεί τα δεδομένα της κλάσης.
     * @param questionStore Μία αναφορά σε ένα αντικείμενο της κλάσης QuestionLibrary
     * @param minPlayers  Ο ελάχιστος απαιτούμενος αριθμός παιχτών για τον γύρο
     * @param maxPlayers Ο μέγιστος επιτρεπόμενος αριθμός παιχτών για τον γύρο
     * @param questionNumber Ο αριθμός των ερωτήσεων που πρέπει να απαντηθούν σε αυτόν τον γύρο
     */
    public Round(QuestionLibrary questionStore, int minPlayers, int maxPlayers, int questionNumber) { // Ανάθεση τιμής αντίστοιχων μεταβλητών.
        this.questionStore = questionStore;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.questionNumber = questionNumber+1; // Το +1 υπάρχει γιατί: αν "φτάσω" στην τελευταία ερώτηση θα γίνει 0, ενώ υπάρχει ερώτηση που πρέπει να απαντηθεί: Η τρέχουσα.
                                                // Αν γίνει 0 ενώ υπάρχει ερώτηση που πρέπει να απαντηθεί, δημιουργεί πρόβλημα καθώς "χάνεται" μία ερώτηση.
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
     * Επιστρέφει true, αν έχει τελειώσει ο γύρος(έχει χρησιμοποιηθεί ο αριθμός των ερωτήσεων που δόθηκε ως όρισμα στην κατασκευή)
     * Επιστρέφει false, σε διαφορετική περίπτωση.
     * @return boolean τιμή ανάλογα με το αν έχει τελειώσει ο γύρος.
     */
    public boolean isOver(){
        return questionNumber == 0;
    }

    /**
     * Σηματοδοτεί την συνέχεια του γύρου σε επόμενη ερώτηση και εκτελεί τις απαραίτητες προετοιμασίες (αν αυτές χρειάζονται) για αυτή.
     * @return αναφορά στο ίδιο το αντικείμενο για χρήση σε "αλυσιδωτές" κλήσεις αυτού.
     */
    public Round proceed(){
        if (questionNumber > 0){ // Άν υπάρχουν ερωτήσεις που δεν έχουν χρησιμοποιηθεί ακόμη
            roundQuestion = questionStore.getRandomQuestionCategory().getRandomQuestion(); // "Φορτώνω" τυχαία ερώτηση τυχαίας κατηγορίας
            questionNumber--; // Μία ερώτηση έχει φορτωθεί για απάντηση.
        } else { // Έχουν χρησιμοποιηθεί όλες οι ερωτήσεις
            roundQuestion = null; //Δεν έχουμε άλλες ερωτήσεις σε αυτόν το γύρο
        }

        questionAnswers = null; /* Την συγκεκριμένη μεταβλητή την θέτουμε ίση με null,
        ώστε να είναι σε θέση να υποδεχτεί τις πιθανές απαντήσεις της νέας ερώτησης.
        Επίσης, σηματοδοτεί ότι δεν έχει γίνει αρχικοποίηση του πίνακα με τις απαντήσεις της νέας ερώτησης. */

        return this;
    }

    /**
     * Επιστρέφει συμβολοσειρά με τη εκφώνηση της τρέχουσας ερώτησης.
     * Σε περίπτωση που ο αριθμός των ερωτήσεων έχουν ξεπεράσει τον αριθμό συνολικών ερωτήσεων που προβλέπονται για τον γύρο, επιστρέφει null.
     * @return την εκφώνηση της ερώτησης ή null αν οι ερωτήσεις έχουν "εξαντληθεί".
     */
    public String getQuestion() {
        if (questionNumber > 0){ // Άν υπάρχουν ερωτήσεις που δεν έχουν χρησιμοποιηθεί ακόμη
            if (roundQuestion == null){ // Είναι η πρώτη ερώτηση που επιστρέφεται, άρα πρέπει να γίνει ένα proceed για σωστή λειτουργία.
                proceed();
            }

            return roundQuestion.getQuestion();
        } else { // Ο αριθμός των ερωτήσεων έχουν ξεπεράσει τον αριθμό συνολικών ερωτήσεων που προβλέπονται για τον γύρο
            return null;
        }
    }

    /**
     * Επιστρέφει αντικείμενο της ερώτησης σε αυτό το στάδιο του γύρου.
     * @return αντικείμενο της ερώτησης σε αυτό το στάδιο του γύρου.
     */
    protected Question getQuestionInstance(){
        if (questionNumber > 0 && roundQuestion == null){ // Είναι η πρώτη ερώτηση που επιστρέφεται, άρα πρέπει να γίνει ένα proceed για σωστή λειτουργία.
            proceed();
        }
        return roundQuestion;
    }

    /**
     * Επιστρέφει έναν πίνακα με τα αντίγραφα (σε τυχαία σειρά) των πιθανών απαντήσεων για την τρέχουσα ερώτηση.
     * Διαδοχικές κλήσεις της μεθόδου για την τρέχουσα ερώτηση (πριν την κλήση της proceed()) επιστρέφουν τις ίδιες απαντήσεις στην ίδια σειρά.
     * @return πίνακα των απαντήσεων για την τρέχουσα ερώτηση.
     */
    public String[] getQuestionAnswers(){
        if (questionNumber > 0 && roundQuestion == null){ // Είναι η πρώτη ερώτηση που επιστρέφεται, άρα πρέπει να γίνει ένα proceed για σωστή λειτουργία.
             proceed();
        }


        if (questionAnswers == null){ // Δεν έχει γίνει αρχικοποίηση του πίνακα με τις απαντήσεις της νέας ερώτησης
            String[] tempArray = roundQuestion.getAnswers();
            Random randomGenerator = new Random();


            for (int i = 0; i < tempArray.length; i++){ // Τυχαιοποιεί τις θέσεις των στοιχείων του πίνακα
                int tempPosition = randomGenerator.nextInt(tempArray.length);

                // Αλλαγή στοιχείων των θέσεων του μετρητή επανάληψης (i) και τυχαίας θέσης (tempPosition)
                String tempElement = tempArray[tempPosition];
                tempArray[tempPosition] = tempArray[i];
                tempArray[i] = tempElement;
            }

            questionAnswers = tempArray; // Έχει γίνει αρχικοποίηση του πίνακα με τις απαντήσεις της νέας ερώτησης
        }

        // Δημιουργώ πίνακα με αντίγραφα απαντήσεων
        String[] temp = new String[4];
        temp[0] = questionAnswers[0];
        temp[1] = questionAnswers[1];
        temp[2] = questionAnswers[2];
        temp[3] = questionAnswers[3];

        return temp;
    }

    /**
     * Επιστρέφεται η σωστή απάντηση, η οποία αντιστοιχεί στην τρέχουσα ερώτηση.
     * Σε περίπτωση που ο αριθμός των ερωτήσεων έχουν ξεπεράσει τον αριθμό συνολικών ερωτήσεων που προβλέπονται για τον γύρο, επιστρέφει null.
     * @return η σωστή απάντηση στην τρέχουσα ερώτηση ή null αν οι ερωτήσεις έχουν "εξαντληθεί".
     */

    public String getRightQuestionAnswer(){
        if (questionNumber > 0){
            if (roundQuestion == null){ // Είναι η πρώτη ερώτηση που επιστρέφεται, άρα πρέπει να γίνει ένα proceed για σωστή λειτουργία.
                proceed();
            }

            return roundQuestion.getRightAnswer();
        } else {
            return null;
        }

    }

    /**
     * Επιστρέφεται η κατηγορία της ερώτησης στο τρέχον στάδιο του γύρου.
     * Σε περίπτωση που ο αριθμός των ερωτήσεων έχουν ξεπεράσει τον αριθμό συνολικών ερωτήσεων που προβλέπονται για τον γύρο, επιστρέφει null.
     * @return η κατηγορία της ερώτησης ή null αν οι ερωτήσεις έχουν "εξαντληθεί".
     */

    public String getQuestionCategory(){
        if (questionNumber > 0){ // Άν υπάρχουν ερωτήσεις που δεν έχουν χρησιμοποιηθεί ακόμη
            if (roundQuestion == null){ // Είναι η πρώτη ερώτηση που επιστρέφεται, άρα πρέπει να γίνει ένα proceed για σωστή λειτουργία.
                proceed();
            }

            return roundQuestion.getCategory();
        } else {
            return null;
        }
    }

    /**
     * Επιστρέφει αντίγραφο αντικειμένου της ερώτησης σε αυτό το στάδιο του γύρου της οποίας οι απαντήσεις έχουν τυχαία
     * σειρά, σύμφωνα με την μέθοδο getQuestionAnswers της παρούσας κλάσης
     * @return Επιστρέφει αντίγραφο αντικειμένου της ερώτησης σε αυτό το στάδιο του γύρου της οποίας οι απαντήσεις έχουν τυχαία σειρά ή null σε περίπτωση που έχουν τελειώσει οι ερωτήσεις
     */
    public Question getQuestionWithRandomizedAnswers(){
        if (questionNumber > 0){ // Δεν έχουν τελειώσει οι ερωτήσεις
            if (roundQuestion instanceof ImageQuestion){
                return new ImageQuestion(getQuestion(), getQuestionAnswers(), getRightQuestionAnswer(), getQuestionCategory(), ((ImageQuestion)roundQuestion).getImageIcon());
            } else {
                return new Question(getQuestion(), getQuestionAnswers(), getRightQuestionAnswer(), getQuestionCategory());
            }
        } else {
            return null;
        }
    }

    /**
     * Ελέγχει αν ο γύρος μπορεί να υποστηρίξει τον αριθμό παιχτών που δίνεται ως όρισμα.
     *
     * @param playerNumber αριθμός παιχτών που ελέγχεται εάν είναι συμβατός με τον τύπο του γύρου.
     * @return true αν ο γύρος μπορεί να υποστηρίξει τον αριθμό παιχτών που δίνεται, διαφορετικά false.
     */
    public boolean playerNumberIsCompatible(int playerNumber){
        return playerNumber>=minPlayers && playerNumber<=maxPlayers;
    }

}
