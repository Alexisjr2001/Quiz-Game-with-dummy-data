package internals.question;

import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

/**
 * Η κλάση {@code QuestionLibrary} μοντελοποιεί την έννοια της "βιβλιοθήκης" ερωτήσεων.
 * Πιο συγκεκριμένα, διαχειρίζεται τις ερωτήσεις όλων των κατηγοριών, παρέχοντας διάφορες λειτουργίες επί των κατηγοριών.
 *
 * @author Ioannis Baraklilis
 * @author Alexandros Tsingos
 *
 * @version 2020.11.29
 */

public class QuestionLibrary {
    private HashMap<String, QuestionCategory> categoryStore; // Αποθηκεύει τις κατηγορίες ερωτήσεων, δίνοντας την δυνατότητα να αντιστοιχίσουμε εύκολα την κατηγορία με το αντίστοιχο αντικείμενο που την διαχειρίζεται.
    private Stack<QuestionCategory> categoryStack; // Αποθηκεύει αντίγραφο των κατηγοριών των ερωτήσεων που επιτρέπει την εύκολη αφαίρεση των στοιχείων.
    private boolean automaticShuffle;   // Αποθηκεύει την επιλογή που δόθηκε ως όρισμα στον κατασκευαστή για αυτόματο "ανακάτεμα".

    /**
     * Τυπικός κατασκευαστής της κλάσης.
     * @param automaticShuffle κατάσταση αυτόματου "ανακατέματος". true για ενεργοποίηση, false διαφορετικά.
     */

    public QuestionLibrary(boolean automaticShuffle) {
        this.automaticShuffle = automaticShuffle;
        categoryStore = new HashMap<>();
        loadQuestions();
        reshuffle();
    }

    /**
     * Επιστρέφει μια τυχαία κατηγορία της ερώτησης.
     *
     * Αν έχουν ήδη επιστραφεί όλες οι κατηγορίες τότε:
     *
     *   Αν έχει επιτραπεί το αυτόματο "ανακάτεμα" οι κατηγορίες ξανά-ανακατεύονται αυτόματα.
     *   (Οι κατηγορίες των ερωτήσεων που έχουν ηδη εμφανιστεί ξαναγίνονται διαθέσιμες, τυχαιοποιείται η σειρά τους μέσα στην δομή και επιστρέφεται μία απο αυτές.)
     *
     *   Αν δεν έχει επιτραπεί το αυτόματο "ανακάτεμα" οι κατηγορίες δεν ξανά-ανακατεύονται αυτόματα και επιστρέφεται null!
     *   (Θα πρέπει αυτός που διαχειρίζεται την κλάση να καλέσει ο ίδιος και όχι αυτόματα όπως πριν την reshuffle(), ώστε οι κατηγορίες που έχουν ηδη εμφανιστεί να ξαναγίνουν διαθέσιμες, να τυχαιοποιηθεί η σειρά τους μέσα στην δομή και να επιστραφεί μία απο αυτές.)
     *
     *   Παρέχεται έτσι μια προγραμματιστική ευελιξία για αυτόν που διαχειρίζεται την κλάση!
     *
     * Αν δεν έχουν ήδη επιστραφεί όλες οι κατηγορίες τότε:
     *
     *   Επιστρέφεται μια τυχαία κατηγορία ερωτήσεων μέσα από την στοίβα.
     *
     * @return μια τυχαία κατηγορία ή null
     */

    public QuestionCategory getRandomQuestionCategory(){
        if (categoryStack.size()==0){
            if (automaticShuffle) {
                reshuffle();
            }
            else {
                return null;
            }
        }

        return categoryStack.pop();
    }

    /**
     * Υλοποιεί την λειτουργία "ανακατέματος" της κλάσης:
     * Ανακατεύει τις κατηγορίες των ερωτήσεων που υπάρχουν.
     *
     * @return αναφορά στην κλάση που καλείται με σκοπό την χρήση της σε "αλυσιδωτές" κλήσεις αυτής.
     */
    public QuestionLibrary reshuffle(){

        categoryStack = new Stack<>();
        for (QuestionCategory qc : categoryStore.values()){
            categoryStack.push(qc);
        }

        java.util.Collections.shuffle(categoryStack);
        return this;
    }

    /**
     * Επιστρέφεται το πλήθος των κατηγοριών των ερωτήσεων που είναι διαθέσιμες για επιστροφή απο την μέθοδο {@code getRandomQuestionCategory()}.
     *
     *  Αν το automaticReshuffle == false τότε υπάρχει η περίπτωση να επιστραφεί null εαν έχουν "εξαντληθεί" οι κατηγορίες
     *  και ο διαχειριστής της κλάσης δεν έχει κάνει reshuffle().
     *
     * @return αριθμό των κατηγοριών που απομένουν για επιστροφή απο την μέθοδο {@code getRandomQuestionCategory()}.
     */

    public int getRemainingCategoriesNumber(){
        return categoryStack.size();
    }

    /**
     * Επιστρέφει την κατηγορία που αντιστοιχεί στην συμβολοσειρά που δίνεται ως όρισμα (αν υπάρχει τέτοια κατηγορία αντιστοιχισμένη με αυτή την συμβολοσειρά).
     * @param category το όνομα της κατηγορίας
     * @return Αναφορά σε αντικείμενο κατηγορίας ερώτησης στην οποία αντιστοιχεί το όρισμα, ή null αν δεν υπάρχει αντίστοιχη αποθηκευμένη.
     */

    public QuestionCategory getQuestionCategory(String category){
       return categoryStore.get(category);
    }

    /**
     * Επιστέφει πίνακα με τα αντίγραφα όλων των κατηγοριών των ερωτήσεων.
     * @return πίνακα με όλες τις αποθηκευμένες κατηγορίες.
     */

    public QuestionCategory[] getAllQuestionCategories(){
        QuestionCategory[] temp = new QuestionCategory[categoryStore.size()];

        int i = 0; // Μετρητής στοιχείων του πίνακα αντιγράφων.
        for (QuestionCategory qc : categoryStore.values()){
            temp[i++] = qc;
        }

        return temp;
    }

    /**
     * Φορτώνει την δομή categoryStore με ερωτήσεις (που έχουν πρότυπη μορφή) με σκοπό δοκιμής σωστής λειτουργίας κλάσης για το πρώτο μέρος της εργασίας.
     */
    private void loadQuestions(){
        Random randomGetter = new Random();
        final int questionNumber = 100;
        String[] answers = {"Απάντηση 1", "Απάντηση 2", "Απάντηση 3", "Απάντηση 4"};
        for(int categoryNumber = 0; categoryNumber <= questionNumber; categoryNumber++){
            String categoryName = "Όνομα κατηγορίας " + categoryNumber;
            Question[] questionTempStore = new Question[questionNumber];

            for (int questionIterator = 0; questionIterator < questionTempStore.length; questionIterator++){
                questionTempStore[questionIterator] = new Question("Ερώτηση " + questionIterator, answers, answers[randomGetter.nextInt(4)], categoryName);
            }
            QuestionCategory temp = new QuestionCategory(categoryName, questionTempStore, true);
            categoryStore.put(temp.getCategoryName(), temp);
        }
    }

}