package internals.round;

import internals.question.QuestionLibrary;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Η κλάση {@code RoundController}, αποτελεί κλάση τύπου "controller" για τις κλάσεις {@code Round}, {@code QuestionLibrary}.
 * Δηλαδή, αποθηκεύει και χειρίζεται αντικείμενα τύπου {@code QuestionLibrary},{@code Round} και προσφέρει ένα απλό περιβάλλον διαχείρισης αυτών μέσω κλήσης μεθόδων.
 *
 * @author Ioannis Baraklilis
 * @author Alexandros Tsingos
 *
 * @version 2020.12.3
 */
public class RoundController {
    private Stack<Round> roundTypesStack; //Αποθηκεύει τους γύρους και επιτρέπει την εύκολη αφαίρεση των στοιχείων.
    private final ArrayList<Round> availableRoundTypes; //Αποθηκεύει όλους τους διαθέσιμους τύπους γύρων
    private boolean automaticShuffle; //Αποθηκεύει την επιλογή που δόθηκε ως όρισμα στον κατασκευαστή για αυτόματο "ανακάτεμα".
    private int playerNumber; // Αποθηκεύει τον αριθμό των παιχτών
    private QuestionLibrary questionStore; //Αναφορά σε QuestionLibrary, απο την οποία θα "παρθούν" οι ερωτήσεις.
    private int numberOfQuestionsPerRound; //Αποθηκεύει τον αριθμό των ερωτήσεων ανά γύρο

    /**
     * Τυπικός κατασκευαστής που αρχικοποιεί τα δεδομένα της κλάσης.
     * @param automaticShuffle κατάσταση αυτόματου "ανακατέματος". true για ενεργοποίηση, false διαφορετικά.
     * @param questionStore αναφορά σε αντικείμενο QuestionLibrary που θα επιστρέφει ερωτήσεις που θα χρησιμοποιηθούν στους διάφορους γύρους
     */
    public RoundController(boolean automaticShuffle, QuestionLibrary questionStore) {
        this.automaticShuffle = automaticShuffle;
        this.questionStore = questionStore;
        playerNumber = 1; // Η πρώτη φάση του προγράμματος ορίζει παιχνίδι ενός παίχτη
        availableRoundTypes = new ArrayList<>();
        numberOfQuestionsPerRound = 1; // Αρχικοποίηση σε ελάχιστο αριθμό ερωτήσεων

        reshuffle(); // Εισαγωγή και τυχαιοποίηση σειράς στοιχείων στην δομή roundTypesStack
    }

    /**
     * Θέτει τον αριθμό των παιχτών ίσο με το όρισμα που δέχεται.
     * @param playerNumber ο αριθμός των παιχτών
     * @return κατάσταση επιτυχίας της μεθόδου. "Επιτυχία" σε περίπτωση επιτυχίας, "Μη αποδεκτός αριθμός παιχτών! Θεωρείται αριθμός παιχτών ίσος με ένα!" διαφορετικά.
     */
    public String setPlayerNumber(int playerNumber){
        if(this.playerNumber == playerNumber){ // Έχει ήδη οριστεί αριθμός παιχτών στην επιθυμητή τιμή
            return "Επιτυχία";
        }
        else if(playerNumber<=0){ // Μή αποδεκτή τιμή αριθμού παιχτών
            if(playerNumber!=1) { // Έχει ήδη οριστεί αριθμός παιχτών στην επιθυμητή τιμή
                playerNumber = 1; // Θέτω (αυθαίρετα) σε 1
                repopulateAvailableRoundTypes(); // "Γέμισμα" λίστας με διαθέσιμους τύπους γύρων
            }

            return "Μη αποδεκτός αριθμός παιχτών! Θεωρείται αριθμός παιχτών ίσος με ένα!";
        }

        this.playerNumber = playerNumber;
        reshuffle(); // Ανανέωση δομών για νέο αριθμό παιχτών
        return "Επιτυχία";
    }

    /**
     * Θέτει τον αριθμό των ερωτήσεων ανά γύρο ίσο με το όρισμα που δέχεται.
     * Άν το όρισμα είναι θετικός αριθμός η εκτέλεση της μεθόδου επιτυγχάνει, διαφορετικά αποτυγχάνει και δεν κάνει καμία μεταβολή στον αριθμό των ερωτήσεων ανα γύρο.
     * Σε κάθε περίπτωση επιστρέφει ανάλογο μήνυμα κατάστασης επιτυχίας.
     * @param numberOfQuestionsPerRound ο αριθμός των ερωτήσεων ανά γύρο
     * @return κατάσταση επιτυχίας της μεθόδου. "Επιτυχία" σε περίπτωση επιτυχίας, "Μη αποδεκτός αριθμός ερωτήσεων για έναν γύρο" διαφορετικά.
     */
    public String setNumberOfQuestionsPerRound(int numberOfQuestionsPerRound){
        if (numberOfQuestionsPerRound>0) { // Ελέγχω αν το όρισμα είναι αποδεκτή τιμή
            this.numberOfQuestionsPerRound = numberOfQuestionsPerRound;
            reshuffle(); // Ανανέωση δομών για νέο αριθμό ερωτήσεων ανα γύρο
            return "Επιτυχία";
        }
        return "Μη αποδεκτός αριθμός ερωτήσεων για έναν γύρο";
    }

    /**
     *  Δημιουργεί τα αντικείμενα των διαθέσιμων τύπων γύρων που θα δοθούν αργότερα.
     *  Η δημιουργία αντικειμένων των γύρων εξασφαλίζει ότι οι γύροι που θα δοθούν (απο άλλη μέθοδο) έχουν τις απαραίτητες αρχικοποιήσεις και δεν έχουν ξαναχρησιμοποιηθεί.
     */
    private void repopulateAvailableRoundTypes(){
        availableRoundTypes.clear(); // Αγνοώ τους τύπους που δεν έχουν εμφανιστεί για να τους συμπεριλάβω όλους εκ νέου.
        availableRoundTypes.add(new RightAnswer(questionStore, numberOfQuestionsPerRound));
        availableRoundTypes.add(new Bet(questionStore, numberOfQuestionsPerRound));
    }

    /**
     * Υλοποιεί την λειτουργία "ανακατέματος" της κλάσης. Κάθε αναφορά σε "ανακάτεμα" εννοεί κλήση αυτής της μεθόδου.
     * Όλοι οι τύποι γύρων που έχουν ηδη εμφανιστεί ξαναγίνονται διαθέσιμοι και τυχαιοποιείται η σειρά επιστροφής τους
     * (όταν ζητηθεί επιστροφή μέσω της μεθόδου getRandomRoundType()).
     *
     * @return αναφορά στην κλάση που καλείται με σκοπό την χρήση της σε "αλυσιδωτές" κλήσεις αυτής.
     */
    public RoundController reshuffle(){
        repopulateAvailableRoundTypes(); // Δημιουργία αντικειμένων των διαθέσιμων τύπων γύρων

        roundTypesStack = new Stack<>(); // Αγνοώ τους τύπους που δεν έχουν εμφανιστεί για να τους συμπεριλάβω όλους εκ νέου.

        for (Round aRound : availableRoundTypes){ // Εισάγω κάθε συμβατό με τον ορισμένο αριθμό παιχτών τύπο γύρου στην στοίβα
            if (aRound.playerNumberIsCompatible(playerNumber)) {
                roundTypesStack.push(aRound);
            }
        }

        java.util.Collections.shuffle(roundTypesStack); //Ανακάτεμα - τυχαιοποίηση σειράς τύπων γύρων στην δομή απο την οποία θα επιστραφούν.
        return this;
    }

    /**
     * Επιστρέφει το πλήθος των τύπων των γύρων που απομένουν για επιστροφή απο την μέθοδο {@code getRandomType()}.
     *
     * Αν το automaticReshuffle == false τότε υπάρχει η περίπτωση να επιστραφεί 0 εαν έχουν "εξαντληθεί" οι τύποι των γύρων
     * (μέχρι ο διαχειριστής της κλάσης να έχει κάνει reshuffle()).
     * @return πλήθος των τύπων των γύρων που απομένουν για επιστροφή απο την μέθοδο {@code getRandomType()}
     */
    public int getRemainingRoundsNumber(){
        return roundTypesStack.size();
    }

    /**
     * Επιστρέφει γύρο τυχαίου τύπου.
     *
     * Αν έχουν ήδη επιστραφεί όλοι οι τύποι γύρων τότε:
     *
     * Αν έχει επιτραπεί το αυτόματο "ανακάτεμα" οι τύποι γύρων γύροι ξανά-ανακατεύονται αυτόματα.
     * (Οι τύποι γύρων που έχουν ηδη εμφανιστεί ξαναγίνονται διαθέσιμοι, τυχαιοποιείται η σειρά τους και επιστρέφεται ένας απο αυτούς.)
     *
     * Αν δεν έχει επιτραπεί το αυτόματο "ανακάτεμα" οι τύποι γύρων δεν ξανά-ανακατεύονται αυτόματα και επιστρέφεται null!
     * (Θα πρέπει αυτός που διαχειρίζεται την κλάση να καλέσει ο ίδιος και όχι αυτόματα όπως πριν την reshuffle(), ώστε οι τύποι γύρων που έχουν ηδη εμφανιστεί να ξαναγίνουν διαθέσιμοι)
     *
     * Παρέχεται έτσι μια προγραμματιστική ευελιξία για αυτόν που διαχειρίζεται την κλάση!
     *
     * Αν δεν έχουν ήδη επιστραφεί όλοι οι τύποι γύρων τότε:
     * Επιστρέφεται ένας γύρος τυχαίου τύπου.
     *
     * @return ένας γύρος τυχαίου τύπου ή null
     */
    public Round getRandomRoundType(){
        if (roundTypesStack.empty()) { // Αν έχουν ήδη επιστραφεί όλοι οι τύποι γύρων
            if (automaticShuffle) { // Αν έχει επιτραπεί το αυτόματο "ανακάτεμα"
                reshuffle(); // Κάνω "ανακάτεμα"
            } else { // Αν δεν έχει επιτραπεί το αυτόματο "ανακάτεμα"
                return null;
            }
        }

        return roundTypesStack.pop(); // Επιστρέφεται ένας γύρος τυχαίου τύπου.
    }

}
