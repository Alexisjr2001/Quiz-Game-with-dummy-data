package internals.question;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Η κλάση {@code QuestionCategory} μοντελοποιεί την έννοια της κατηγορίας ερωτήσεων.
 * Συνεπώς, αποθηκεύει όλες τις ερωτήσεις που ανήκουν σε αυτή την κατηγορία και παρέχει ένα απλό περιβάλλον για πρόσβαση σε αυτές μέσω των εκφωνήσεων τους.
 * Παράλληλα, παρέχει και μεθόδους για επιλογή τυχαίων ερωτήσεων αυτής της κατηγορίας.
 *
 * @version 2020.11.14
 */
public class QuestionCategory {
    private String categoryName; // Όνομα κατηγορίας.
    private HashMap<String, Question> questionStore; // Αποθηκεύει τις ερωτήσεις, δίνοντας την δυνατότητα να αντιστοιχίσουμε εύκολα εκφώνηση τους με το αντίστοιχο αντικείμενο που τις διαχειρίζεται.
    private Stack<Question> questionStack; // Αποθηκεύει αντίγραφο των ερωτήσεων που επιτρέπει την εύκολη αφαίρεση των στοιχείων.
    private boolean automaticShuffle; // Αποθηκεύει την επιλογή που δόθηκε ως όρισμα στον κατασκευαστή για αυτόματο "ανακάτεμα".

    /**
     * Τυπικός κατασκευαστής της κλάσης.
     * @param categoryName Όνομα κατηγορίας
     * @param inputQuestions Πίνακας με τις ερωτήσεις που πρέπει να αποθηκευτούν στην κατηγορία
     * @param automaticShuffle κατάσταση αυτόματου "ανακατέματος". true για ενεργοποίηση, false διαφορετικά
     */
    public QuestionCategory(String categoryName, Question[] inputQuestions, boolean automaticShuffle) {
        this.categoryName = categoryName;

        this.questionStore = new HashMap<>();
        for (Question q : inputQuestions){
            questionStore.put(q.getQuestion(), q);
        }

        this.automaticShuffle = automaticShuffle;
        reshuffle();
    }

    /**
     * Επιστρέφει όνομα της κατηγορίας
     * @return όνομα της κατηγορίας.
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Επιστέφει πίνακα με τα αντίγραφα όλων των ερωτήσεων της κατηγορίας.
     * @return πίνακα με όλες τις αποθηκευμένες ερωτήσεις.
     */
    public Question[] getAllQuestions(){
        Question[] temp = new Question[questionStore.size()];

        int i = 0; // Μετρητής θέσης στον πίνακα temp
        for (Question q : questionStore.values()){
            temp[i++] = q;
        }

        return temp;
    }

    /**
     * Επιστρέφει την ερώτηση που αντιστοιχεί στην εκφώνηση που δίνεται ως όρισμα αν υπάρχει.
     * Αν δεν υπάρχει επιστρέφει null.
     * @param question Εκφώνηση της ερώτησης.
     * @return Αναφορά σε αντικείμενο ερώτησης που αντιστοιχεί, ή null αν δεν υπάρχει αντίστοιχη αποθηκευμένη.
     */
    public Question getQuestion(String question){
        return questionStore.get(question);
    }

    /**
     * Επιστρέφει τυχαία ερώτηση της κατηγορίας.
     * Αν έχουν ήδη επιστραφεί όλες οι ερωτήσεις τότε άν έχει επιτραπεί το αυτόματο "ανακάτεμα" οι ερωτήσεις ξανά-ανακατεύονται
     * (οι ερωτήσεις που έχουν ηδη εμφανιστεί ξαναγίνονται διαθέσιμες και τυχαιοποιείται η σειρά τους στην δομή) και επιστρέφεται μία τυχαία απο αυτές.
     *
     * Διαφορετικά, στην περίπτωση που έχουν ήδη επιστραφεί όλες οι ερωτήσεις και δεν έχει επιτραπεί το αυτόματο "ανακάτεμα", επιστρέφεται null.
     * @return μία τυχαία ερώτηση
     */
    public Question getRandomQuestion(){
        if (questionStack.size()==0){
            if (automaticShuffle) {
                reshuffle();
            } else {
                return null;
            }
        }

        return questionStack.pop();
    }

    /**
     * Επιστρέφει τον αριθμό των ερωτήσεων που απομένουν για επιστροφή απο την μέθοδο {@code getRandomQuestion()},
     * πριν χρειαστεί να γίνει reshuffle() ή να αρχίσει να επιστρέφεται {@code null}.
     * @return αριθμό των ερωτήσεων που απομένουν για επιστροφή απο την μέθοδο {@code getRandomQuestion()}.
     */
    public int getRemainingRandomQuestions(){
        return questionStack.size();
    }

    /**
     * Υλοποιεί την λειτουργία "ανακατέματος" της κλάσης:
     * "Ανακατεύει" τα στοιχεία της δομής με τις ερωτήσεις που επιστρέφονται απο την μέθοδο {@code getRandomQuestion()}.
     * Συγκεκριμένα, όλες οι ερωτήσεις της συγκεκριμένης κατηγορίας γίνονται διαθέσιμες προς επιστροφή απο την μέθοδο {@code getRandomQuestion()} (ανεξάρτητα απο τον αριθμό το φορών που έχουν επιστραφεί)
     * και τυχαιοποιείται η σειρά με την οποία θα επιστραφούν.
     * @return αναφορά στην κλάση που καλείται με σκοπό την χρήση της σε "αλυσιδωτές" κλήσεις αυτής.
     */
    public QuestionCategory reshuffle(){
        questionStack=new Stack<>();
        for(Map.Entry<String,Question> q: questionStore.entrySet()){
            questionStack.push(q.getValue());
        }

        java.util.Collections.shuffle(questionStack);
        return this;

    }

}
