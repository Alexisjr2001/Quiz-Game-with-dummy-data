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
    private Stack<Round> roundTypesStack; //Αποθηκεύει αντίγραφο των τύπων των γύρων που επιτρέπει την εύκολη αφαίρεση των στοιχείων.
    private final ArrayList<Round> availableRoundTypes; //Αποθηκεύει όλους τους διαθέσιμους τύπους γύρων
    private boolean automaticShuffle; //Αποθηκεύει την επιλογή που δόθηκε ως όρισμα στον κατασκευαστή για αυτόματο "ανακάτεμα".
    private int playerNumber; // Αποθηκεύει τον αριθμό των παιχτών
    private QuestionLibrary questionStore; //Αναφορά σε QuestionLibrary
    private int numberOfQuestionsPerRound; //ΑΠοθηκεύει τον αριθμό των ερωτήσεων ανά γύρο

    /**
     *
     * @param automaticShuffle κατάσταση αυτόματου "ανακατέματος". true για ενεργοποίηση, false διαφορετικά.
     * @param questionStore οι ερωτήσεις που θα χρησιμοποιηθούν στους διάφορους γύρους
     */
    public RoundController(boolean automaticShuffle, QuestionLibrary questionStore) {
        this.automaticShuffle = automaticShuffle;
        this.questionStore = questionStore;
        playerNumber = 1;
        availableRoundTypes = new ArrayList<>();
        numberOfQuestionsPerRound = 1;

        reshuffle(); // Εισαγωγή και τυχαιοποίηση σειράς στοιχείων στην δομή roundTypesStack;
    }

    /**
     * Θέτει τον αριθμό των παιχτών ίσο με το όρισμα που δέχεται.
     * @param playerNumber ο αριθμός των παιχτών
     * @return κατάσταση επιτυχίας της μεθόδου. "Επιτυχία" σε περίπτωση επιτυχίας, "Μη αποδεκτός αριθμός παιχτών! Θεωρείται αριθμός παιχτών ίσος με ένα!" διαφορετικά.
     */
    public String setPlayerNumber(int playerNumber){
        if(this.playerNumber == playerNumber){
            return "Επιτυχία";
        }
        else if(playerNumber<=0){
            if(playerNumber!=1) {
                playerNumber = 1;
                repopulateAvailableRoundTypes();
            }

            return "Μη αποδεκτός αριθμός παιχτών! Θεωρείται αριθμός παιχτών ίσος με ένα!";
        }

        this.playerNumber = playerNumber;
        reshuffle();
        return "Επιτυχία";
    }

    /**
     * Θέτει τον αριθμό των ερωτήσεων ανά γύρο ίσο με το όρισμα που δέχεται.
     * @param numberOfQuestionsPerRound ο αριθμός των ερωτήσεων ανά γύρο
     * @return κατάσταση επιτυχίας της μεθόδου. "Επιτυχία" σε περίπτωση επιτυχίας, "Μη αποδεκτός αριθμός ερωτήσεων για έναν γύρο" διαφορετικά.
     */
    public String setNumberOfQuestionsPerRound(int numberOfQuestionsPerRound){
        if (numberOfQuestionsPerRound>0) {
            this.numberOfQuestionsPerRound = numberOfQuestionsPerRound;
            reshuffle();
            return "Επιτυχία";
        }
        return "Μη αποδεκτός αριθμός ερωτήσεων για έναν γύρο";
    }

    /**
     *  Δημιουργεί τα αντικείμενα των γύρων που θα δοθούν αργότερα.
     *  Η δημιουργία αντικειμένων των γύρων εξασφαλίζει ότι οι γύροι που θα δοθούν (απο άλλη μέθοδο) έχουν τις απαραίτητες αρχικοποιήσεις και δεν έχουν ξαναχρησιμοποιηθεί.
     */
    private void repopulateAvailableRoundTypes(){
        availableRoundTypes.clear();
        availableRoundTypes.add(new RightAnswer(questionStore, numberOfQuestionsPerRound));
        availableRoundTypes.add(new Bet(questionStore, numberOfQuestionsPerRound));
    }

    /**
     * Υλοποιεί την λειτουργία "ανακατέματος" της κλάσης:
     * Ανακατεύει με τυχαίο τρόπο τους τύπους των γύρων που υπάρχουν.
     * @return αναφορά στην κλάση που καλείται με σκοπό την χρήση της σε "αλυσιδωτές" κλήσεις αυτής.
     */
    public RoundController reshuffle(){
        repopulateAvailableRoundTypes();

        roundTypesStack = new Stack<>();

        for (Round aRound : availableRoundTypes){
            if (aRound.playerNumberIsCompatible(playerNumber)) {
                roundTypesStack.push(aRound);
            }
        }

        java.util.Collections.shuffle(roundTypesStack);
        return this;
    }

    /**
     * Επιστρέφει το πλήθος των τύπων των γύρων που απομένουν για επιστροφή απο την μέθοδο {@code getRandomType()}.
     *
     * Αν το automaticReshuffle == false τότε υπάρχει η περίπτωση να επιστραφεί null εαν έχουν "εξαντληθεί" οι τύποι των γύρων
     * και ο διαχειριστής της κλάσης δεν έχει κάνει reshuffle().
     * @return πλήθος των τύπων των γύρων που απομένουν για επιστροφή απο την μέθοδο {@code getRandomType()}
     */
    public int getRemainingRoundsNumber(){
        return roundTypesStack.size();
    }

    /**
     * Επιστρέφει τυχαίο τύπο γύρου.
     * Αν έχουν ήδη επιστραφεί όλοι οι τύποι γύρων τότε:
     *      Αν έχει επιτραπεί το αυτόματο "ανακάτεμα" οι τύποι γύρων ξανά-ανακατεύονται αυτόματα.
     *      (Οι τύποι γύρων που έχουν ηδη εμφανιστεί ξαναγίνονται διαθέσιμες, τυχαιοποιείται η σειρά τους μέσα στην δομή και επιστρέφεται ένας από αυτούς.)
     *
     *      Αν δεν έχει επιτραπεί το αυτόματο "ανακάτεμα" οι τύποι γύρων δεν ξανά-ανακατεύονται αυτόματα και επιστρέφεται null!
     *      (Θα πρέπει αυτός που διαχειρίζεται την κλάση να καλέσει ο ίδιος και όχι αυτόματα όπως πριν την reshuffle(), ώστε οι τύποι γύρων που έχουν ήδη εμφανιστεί να ξαναγίνουν διαθέσιμοι, να τυχαιοποιηθεί η σειρά τους μέσα στην δομή και να επιστραφεί ένας από αυτούς.)
     *
     *      Παρέχεται έτσι μια προγραμματιστική ευελιξία για αυτόν που διαχειρίζεται την κλάση!
     *
     * Αν δεν έχουν ήδη επιστραφεί όλες οι ερωτήσεις τότε:
     *      Επιστρέφεται ένας τυχαίος τύπος γύρου μέσα από την στοίβα.
     *
     * @return ένας τυχαίος τύπος γύρου ή null
     */
    public Round getRandomRoundType(){
        if (roundTypesStack.empty()) {
            if (automaticShuffle) {
                reshuffle();
            } else {
                return null;
            }
        }

        return roundTypesStack.pop();
    }

}
