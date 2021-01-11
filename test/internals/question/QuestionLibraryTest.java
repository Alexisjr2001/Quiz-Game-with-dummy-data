package internals.question;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class QuestionLibraryTest {
    private QuestionLibrary questionLibrary;

    @BeforeEach
    void setUp() {
        // Αρχείο με μόνο μία ερώτηση. Η χρήση αυτού του αρχείο προορίζεται μόνο για το testing.
        String questionFile = "TestQuestionLibrary.txt";

        try {
            questionLibrary = new QuestionLibrary(true,questionFile);
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    @Test
    void getRandomQuestionCategory() {
        // Δημιουργώ μια ερώτηση
        String aQuestion = "Σε ποια ήπειρο βρίσκονται οι Ηνωμένες Πολιτείας Αμερικής;";
        String[] answers = {"Βόρεια Αμερική","Ασία","Νότια Αμερική","Ωκεανία"};
        String rightAnswer = "Βόρεια Αμερική";
        String category = "Γεωγραφία";
        Question expectedQuestion = new Question(aQuestion,answers,rightAnswer,category);

        // Δημιουργώ μια κατηγορία ερωτήσεων
        Question[] expectedQuestions = new Question[1];
        expectedQuestions[0] = expectedQuestion;

        QuestionCategory expectedCategory = new QuestionCategory(category,expectedQuestions,true);
        QuestionCategory actualCategory = questionLibrary.getRandomQuestionCategory();
        assertNotNull(actualCategory); //Ελέγχω εάν επιστράφηκε μια κατηγορία

        // Ελέγχω εάν είναι η ίδια κατηγορία
        assertEquals(expectedCategory.getCategoryName(),actualCategory.getCategoryName());

        // Ελέγχω πέρα από το εάν έχω την ίδια κατηγορία, εάν έχω και την αναμενόμενη ερώτηση
        Question actualQuestion = questionLibrary.getRandomQuestionCategory().getRandomQuestion();
        assertNotNull(actualQuestion); //Ελέγχω εάν έχει επιστραφεί ερώτηση

        assertEquals(expectedQuestion.getQuestion(),actualQuestion.getQuestion());
        assertArrayEquals(expectedQuestion.getAnswers(),actualQuestion.getAnswers());
        assertEquals(expectedQuestion.getRightAnswer(),actualQuestion.getRightAnswer());
        assertEquals(expectedQuestion.getCategory(),actualQuestion.getCategory());

        /* Αφού έχω τραβήξει μια κατηγορία δεν υπάρχουν άλλες διαθέσιμες κατηγορίες.
           Εφόσον όμως έχει επιτραπεί το αυτόματο ανακάτεμα κατηγοριών,
           έχουμε την δυνατότητα να τραβήξουμε ξανά μια κατηγορία.
         */
        actualCategory = questionLibrary.getRandomQuestionCategory(); // Τραβάω ξανά μια τυχαία κατηγορία
        assertNotNull(actualCategory); //Ελέγχω εάν επιστράφηκε μια κατηγορία

        // Ελέγχω πέρα από το εάν έχω την ίδια κατηγορία, εάν έχω και την αναμενόμενη ερώτηση
        actualQuestion = questionLibrary.getRandomQuestionCategory().getRandomQuestion();
        assertNotNull(actualQuestion);

        assertEquals(expectedQuestion.getQuestion(),actualQuestion.getQuestion());
        assertArrayEquals(expectedQuestion.getAnswers(),actualQuestion.getAnswers());
        assertEquals(expectedQuestion.getRightAnswer(),actualQuestion.getRightAnswer());
        assertEquals(expectedQuestion.getCategory(),actualQuestion.getCategory());
    }


    @Test
    void getRemainingCategoriesNumber() {
        assertEquals(1,questionLibrary.getRemainingCategoriesNumber()); //Εξαρχής μια κατηγορία ερωτήσεων

        questionLibrary.getRandomQuestionCategory(); //Τραβάω μια κατηγορία
        assertEquals(0,questionLibrary.getRemainingCategoriesNumber()); //Άρα αναμένω να έχω 0 κατηγορίες

        questionLibrary.reshuffle(); //Ανακατεύω τις κατηγορίες (Ουσιαστικά έχω ξανά μια κατηγορία ερωτήσεων διαθέσιμη)
        assertEquals(1,questionLibrary.getRemainingCategoriesNumber()); // Αναμένω να έχω μία κατηγορία ερωτήσεων

        questionLibrary.getRandomQuestionCategory();//Τραβάω ξανά μια κατηγορία
        assertEquals(0,questionLibrary.getRemainingCategoriesNumber()); //Άρα αναμένω να έχω 0 κατηγορίες ερωτήσεων

    }

    @Test
    void getQuestionCategory() {
        assertNotNull(questionLibrary.getQuestionCategory("Γεωγραφία"));

        assertNull(questionLibrary.getQuestionCategory("Αθλητικά"));

        assertNull(questionLibrary.getQuestionCategory("History"));
    }

    @Test
    void getAllQuestionCategories() {
        // Δημιουργώ μια ερώτηση
        String aQuestion = "Σε ποια ήπειρο βρίσκονται οι Ηνωμένες Πολιτείας Αμερικής;";
        String[] answers = {"Βόρεια Αμερική","Ασία","Νότια Αμερική","Ωκεανία"};
        String rightAnswer = "Βόρεια Αμερική";
        String category = "Γεωγραφία";
        Question expectedQuestion = new Question(aQuestion,answers,rightAnswer,category);

        // Δημιουργώ μια κατηγορία ερωτήσεων
        Question[] expectedQuestions = new Question[1];
        expectedQuestions[0] = expectedQuestion;
        QuestionCategory expectedCategory = new QuestionCategory(category,expectedQuestions,true);

        //Δημιουργώ έναν πίνακα με κατηγορίες ερωτήσεων
        QuestionCategory[] expected = new QuestionCategory[1];
        expected[0] = expectedCategory;

        QuestionCategory[] actual = questionLibrary.getAllQuestionCategories();

        // Ελέγχω εάν έχω την ίδια κατηγορία ερωτήσεων
        assertEquals(expected[0].getCategoryName(), actual[0].getCategoryName());

        // Ελέγχω πέρα από το εάν έχω την ίδια κατηγορία, εάν έχω και την αναμενόμενη ερώτηση
        Question actualQuestion = questionLibrary.getRandomQuestionCategory().getRandomQuestion();
        assertNotNull(actualQuestion); //Ελέγχω εάν έχει επιστραφεί ερώτηση

        assertEquals(expectedQuestion.getQuestion(),actualQuestion.getQuestion());
        assertArrayEquals(expectedQuestion.getAnswers(),actualQuestion.getAnswers());
        assertEquals(expectedQuestion.getRightAnswer(),actualQuestion.getRightAnswer());
        assertEquals(expectedQuestion.getCategory(),actualQuestion.getCategory());

    }

    @Test
    void automaticShuffleOff(){
        // automaticShuffle == false
        questionLibrary = new QuestionLibrary(false);

        for (int i = 0; i <= 100; i++) {
            assertNotNull(questionLibrary.getRandomQuestionCategory());
        }

        /* Αφού έχω τραβήξει 101 κατηγορίες ερωτήσεων δεν υπάρχουν άλλες διαθέσιμες κατηγορίες.
           Επιπλέον,δεν έχει επιτραπεί και το αυτόματο ανακάτεμα κατηγοριών,
           οπότε δεν έχουμε την δυνατότητα να τραβήξουμε ξανά μια κατηγορία.
         */

        assertNull(questionLibrary.getRandomQuestionCategory());
    }

    @Test
    void loadQuestionFromFile(){

        // Εδώ ελέγχεται η περίπτωση που το αρχείο κειμένου έχει μη έγκυρη μορφή
        try {
            questionLibrary.loadQuestionFromFile("TestLoadFile.txt");
            fail("Θα έπρεπε να έχει επιστραφεί ένα IOException");
        }
        catch (IOException e){

        }

        // Εδώ ελέγχεται η περίπτωση που υπάρχει αδυναμία στο άνοιγμα του αρχείου
        try {
            questionLibrary.loadQuestionFromFile("InvalidFile.txt");
            fail("Θα έπρεπε να έχει επιστραφεί ένα IOException");
        }
        catch (IOException e){

        }
    }
}