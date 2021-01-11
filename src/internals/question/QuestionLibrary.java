package internals.question;

import java.io.*;
import java.util.*;

/**
 * Η κλάση {@code QuestionLibrary} μοντελοποιεί την έννοια της "βιβλιοθήκης" ερωτήσεων.
 * Πιο συγκεκριμένα, διαχειρίζεται τις ερωτήσεις όλων των κατηγοριών, παρέχοντας διάφορες λειτουργίες επί των κατηγοριών.
 *
 * @author Ioannis Baraklilis
 * @author Alexandros Tsingos
 *
 * @version 2020.12.31
 */

public class QuestionLibrary {
    private HashMap<String, QuestionCategory> categoryStore; // Αποθηκεύει τις κατηγορίες ερωτήσεων, δίνοντας την δυνατότητα να αντιστοιχίσουμε εύκολα την κατηγορία με το αντίστοιχο αντικείμενο που την διαχειρίζεται.
    private Stack<QuestionCategory> categoryStack; // Αποθηκεύει αντίγραφο των κατηγοριών των ερωτήσεων που επιτρέπει την εύκολη αφαίρεση των στοιχείων.
    private boolean automaticShuffle;   // Αποθηκεύει την επιλογή που δόθηκε ως όρισμα στον κατασκευαστή για αυτόματο "ανακάτεμα".

    /**
     * Κατασκευαστής της κλάσης, για τυχαία δεδομένα. Αρχικοποιεί τα δεδομένα της κλάσης με "τυχαία" δεδομένα (ερωτήσεις με πρότυπη μορφή με σκοπό τον έλεγχο της κλάσης).
     * @param automaticShuffle κατάσταση αυτόματου "ανακατέματος". true για ενεργοποίηση, false διαφορετικά.
     */

    public QuestionLibrary(boolean automaticShuffle) {
        this.automaticShuffle = automaticShuffle;
        categoryStore = new HashMap<>();
        loadQuestions(); // "Φόρτωση" ερωτήσεων στην κλάση.
        reshuffle(); // Πρέπει αμέσως μετά την δημιουργία να μπορώ να πάρω τυχαίες κατηγορίες, οπότε κάνω τυχαιοποίηση τους.
    }

    /**
     * Κατασκευαστής της κλάσης, για ανάγνωση δεδομένων απο αρχείο. Αρχικοποιεί τα δεδομένα της κλάσης φορτώνοντας ερωτήσεις απο αρχείο χρησιμοποιώντας την μέθοδο loadQuestionFromFile.
     * @param automaticShuffle κατάσταση αυτόματου "ανακατέματος". true για ενεργοποίηση, false διαφορετικά.
     * @param questionFile Όνομα αρχείου απο όπου θα φορτωθούν οι ερωτήσεις
     * @throws IOException Σε περίπτωση που υπάρχει πρόβλημα κατά το άνοιγμα και διάβασμα από το αρχείο κειμένου
     */

    public QuestionLibrary(boolean automaticShuffle, String questionFile) throws IOException {
        this.automaticShuffle = automaticShuffle;
        categoryStore = new HashMap<>();
        loadQuestionFromFile(questionFile); // Φόρτωση ερωτήσεων στην κλάση.
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


    /**
     * Διαβάζει τις ερωτήσεις καθώς και τα σχετικά τους δεδομένα.
     * Η κάθε γραμμή του αρχείου έχει την εξής γραμμογράφηση:
     * Όνομα_Κατηγορίας, Εκφώνηση_Ερώτησης, Απάντηση1, Απάντηση2, Απάντηση3, Απάντηση4, Σωστή_Απάντηση, ImageFileName/Null
     * χωρισμένα με ένα tab.
     * Παρατήρηση: Το ImageFileName πρέπει να είναι Null εάν πρόκειται για ερώτηση χωρίς εικόνα. Επίσης, το ImageFileName αποτελεί πλήρες όνομα της εικόνας στον φάκελο Images (σε επίπεδο project).
     * @param fileName Το όνομα του αρχείου κειμένου, από όπου θα διαβάσουμε όλες τις ερωτήσεις.
     * @throws IOException Σε περίπτωση που υπάρχει πρόβλημα κατά το άνοιγμα και διάβασμα από το αρχείο κειμένου.
     */
    public void loadQuestionFromFile(String fileName) throws IOException {
        //Η δομή αυτή αποθηκεύει όλες τις ερωτήσεις της κάθε κατηγορίας
        //Το String αναπαριστά το όνομα της κατηγορίας
        //Το ArrayList αποθηκεύει τις ερωτήσεις της κατηγορίας
        HashMap<String, ArrayList<Question>> temp = new HashMap<>();
        String[] answers = new String[4]; //Πίνακας συμβολοσειρών που περιέχει τις απαντήσεις της κάθε ερώτησης
        Question question;

        try {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String aLine;
                while ((aLine = br.readLine()) != null) {

                    String[] parts = aLine.split("\t"); //Διαχωρισμός της γραμμής σε κομμάτια με βάση το tab

                    answers = new String[4];
                    for (int i = 2; i < 6; i++) {//Αποθηκεύω τις απαντήσεις της ερώτησης
                        answers[i - 2] = parts[i];
                    }

                    //Δημιουργία ερώτησης
                    if (parts[7].equals("Null")) { //ερώτηση χωρίς εικόνα
                        question = new Question(parts[1], answers, parts[6], parts[0]);
                    } else { //ερώτηση με εικόνα
                        question = new ImageQuestion(parts[1], answers, parts[6], parts[0], parts[7]);
                    }

                    ArrayList<Question> questions = temp.get(parts[0]);

                    if (questions == null) {  //Έλεγχος εάν είναι η πρώτη φορά που έχουμε αυτή την κατηγορία και εκτέλεση των απαιτούμενων ενεργειών
                        questions = new ArrayList<>();
                        temp.put(parts[0], questions);
                    }

                    questions.add(question); //Προσθέτω την ερώτηση στην κατηγορία που ανήκει
                }

                for (Map.Entry<String, ArrayList<Question>> e : temp.entrySet()) { //Διατρέχουμε το HashMap με όνομα temp
                    /* Μετατροπή του ArrayList της κάθε κατηγορίας σε array */
                    Question[] questionsOfCategory = new Question[e.getValue().size()];

                    questionsOfCategory = e.getValue().toArray(questionsOfCategory);

                    //Δημιουργία της κατηγορίας
                    QuestionCategory aQuestionCategory = new QuestionCategory(e.getKey(), questionsOfCategory, true);
                    // Αποθήκευση της κατηγορίας στην δομή που περιέχει όλες τις κατηγορίες
                    categoryStore.put(aQuestionCategory.getCategoryName(), aQuestionCategory);
                }

            }
        } catch (IOException e){
            throw new IOException(e.getMessage());
        } catch (Exception e){
            throw new IOException("Σφάλμα κατά την αποκωδικοποίηση του αρχείου");
        }
    }

}