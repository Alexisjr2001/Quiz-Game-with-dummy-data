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
     * Τυπικός κατασκευαστής της κλάσης. Αρχικοποιεί τα δεδομένα της κλάσης.
     * @param automaticShuffle κατάσταση αυτόματου "ανακατέματος". true για ενεργοποίηση, false διαφορετικά.
     */

    public QuestionLibrary(boolean automaticShuffle) {
        this.automaticShuffle = automaticShuffle;
        categoryStore = new HashMap<>();
        loadQuestions(); // "Φορτωση" ερωτήσεων στην κλάση.
        reshuffle(); // Πρέπει αμέσως μετά την δημιουργία να μπορώ να πάρω τυχαίες κατηγορίες, οπότε κάνω τυχαιοποίηση τους.
    }

    /**
     * Επιστρέφει μια τυχαία κατηγορία ερωτήσεων.
     *
     * Αν έχουν ήδη επιστραφεί όλες οι κατηγορίες τότε:
     *
     * Αν έχει επιτραπεί το αυτόματο "ανακάτεμα" οι κατηγορίες ξανά-ανακατεύονται αυτόματα.
     * (Οι κατηγορίες που έχουν ηδη εμφανιστεί ξαναγίνονται διαθέσιμες, τυχαιοποιείται η σειρά τους και επιστρέφεται μία απο αυτές.)
     *
     * Αν δεν έχει επιτραπεί το αυτόματο "ανακάτεμα" οι κατηγορίες δεν ξανά-ανακατεύονται αυτόματα και επιστρέφεται null!
     * (Θα πρέπει αυτός που διαχειρίζεται την κλάση να καλέσει ο ίδιος και όχι αυτόματα όπως πριν την reshuffle(), ώστε οι κατηγορίες που έχουν ηδη εμφανιστεί να ξαναγίνουν διαθέσιμες)
     *
     * Παρέχεται έτσι μια προγραμματιστική ευελιξία για αυτόν που διαχειρίζεται την κλάση!
     *
     * Αν δεν έχουν ήδη επιστραφεί όλες οι κατηγορίες τότε:
     *
     * Επιστρέφεται μια τυχαία κατηγορία ερωτήσεων.
     *
     * @return μια τυχαία κατηγορία ή null
     */

    public QuestionCategory getRandomQuestionCategory(){
        if (categoryStack.size()==0){ // Αν έχουν ήδη επιστραφεί όλες οι κατηγορίες
            if (automaticShuffle) { // Αν έχει επιτραπεί το αυτόματο "ανακάτεμα"
                reshuffle(); // Κάνω ανακάτεμα
            }
            else { // Αν δεν έχει επιτραπεί το αυτόματο "ανακάτεμα"
                return null; // Επιστρέφεται null
            }
        }

        return categoryStack.pop(); // Επιστρέφεται μια τυχαία κατηγορία ερωτήσεων.
    }

    /**
     * Υλοποιεί την λειτουργία "ανακατέματος" της κλάσης. Κάθε αναφορά σε "ανακάτεμα" εννοεί κλήση αυτής της μεθόδου.
     * Όλες οι κατηγορίες που έχουν ηδη εμφανιστεί ξαναγίνονται διαθέσιμες και τυχαιοποιείται η σειρά επιστροφής όλων των ερωτήσεων
     * (επιστροφή μέσω της μεθόδου getRandomQuestionCategory()).
     * @return αναφορά στην κλάση που καλείται με σκοπό την χρήση της σε "αλυσιδωτές" κλήσεις αυτής.
     */
    public QuestionLibrary reshuffle(){

        categoryStack = new Stack<>(); // Αγνοώ τις κατηγορίες που δεν έχουν εμφανιστεί για να τις συμπεριλάβω όλες εκ νέου.
        for (QuestionCategory qc : categoryStore.values()){ // Εισάγω όλες τις κατηγορίες στο stack
            categoryStack.push(qc);
        }

        java.util.Collections.shuffle(categoryStack); //Ανακάτεμα - τυχαιοποίηση σειράς κατηγοριών στην δομή απο την οποία θα επιστραφούν.
        return this;
    }

    /**
     * Επιστρέφεται το πλήθος των κατηγοριών ερωτήσεων που είναι διαθέσιμες για επιστροφή απο την μέθοδο {@code getRandomQuestionCategory()}.
     *
     * Αν το automaticReshuffle == false τότε υπάρχει η περίπτωση να επιστρέφει 0 εαν έχουν "εξαντληθεί" οι κατηγορίες
     * (μέχρι ο διαχειριστής της κλάσης να έχει κάνει reshuffle()).
     *
     * @return αριθμό των κατηγοριών που απομένουν για επιστροφή απο την μέθοδο {@code getRandomQuestionCategory()}.
     */

    public int getRemainingCategoriesNumber(){
        return categoryStack.size();
    }

    /**
     * Επιστρέφει την κατηγορία που αντιστοιχεί στην συμβολοσειρά που δίνεται ως όρισμα (αν υπάρχει τέτοια κατηγορία αντίστοιχη με αυτή την συμβολοσειρά).
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
        for (QuestionCategory qc : categoryStore.values()){ // Εισάγω στον πίνακα μία-μία τις κατηγορίες που υπάρχουν (ως values) στο HashMap
            temp[i++] = qc;
        }

        return temp;
    }

    /**
     * Φορτώνει την δομή categoryStore με ερωτήσεις (που έχουν πρότυπη μορφή) με σκοπό δοκιμής σωστής λειτουργίας, για το πρώτο μέρος της εργασίας.
     */
    private void loadQuestions(){
        Random randomGetter = new Random(); // Παραγωγός τυχαίων αριθμών
        final int questionNumber = 100; // Αριθμός κατηγοριών και ερωτήσεων ανα κατηγορία (Σταθερά που ορίζεται απο τον προγραμματιστή)
        String[] answers = {"Απάντηση 1", "Απάντηση 2", "Απάντηση 3", "Απάντηση 4"}; // Οι πρότυπες απαντήσεις κάθε κατηγορίας
        for(int categoryNumber = 0; categoryNumber <= questionNumber; categoryNumber++){ // Για κάθε κατηγορία:
            String categoryName = "Όνομα κατηγορίας " + categoryNumber; // Ορίζω όνομα κατηγορίας
            Question[] questionTempStore = new Question[questionNumber]; // Οι ερωτήσεις αυτής της κατηγορίας

            for (int questionIterator = 0; questionIterator < questionTempStore.length; questionIterator++){ // Για κάθε ερώτηση:
                // Η ερώτηση έχει εκφώνηση τύπου "Ερώτηση + αριθμός ερώτησης", απαντήσεις τις πρότυπες απαντήσεις,
                // σωστή απάντηση μία τυχαία απο τις πρότυπες και όνομα κατηγορίας το όνομα που έχει η κατηγορία σε αυτή την επανάληψη του εξωτερικού βρόχου.
                questionTempStore[questionIterator] = new Question("Ερώτηση " + questionIterator, answers, answers[randomGetter.nextInt(4)], categoryName);
            }
            // Η κατηγορία έχει όνομα αυτό που ορίστηκε παραπάνω, ερωτήσεις αυτές που δημιουργήθηκαν στον πάνω βρόχο και επιτρέπεται το αυτόματο "ανακάτεμα"
            QuestionCategory temp = new QuestionCategory(categoryName, questionTempStore, true);
            categoryStore.put(temp.getCategoryName(), temp); // Αντιστοιχίζω όνομα κατηγορίας με αντικείμενο κατηγορίας
        }
    }

}