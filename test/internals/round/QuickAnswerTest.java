package internals.round;

import internals.question.Question;
import internals.question.QuestionLibrary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class QuickAnswerTest extends RoundTest{
    private QuickAnswer quickAnswer;

    @BeforeEach
    void setUp() {

        // Αρχείο με μόνο μία ερώτηση. Η χρήση αυτού του αρχείο προορίζεται μόνο για το testing.
        String questionFile = "TestQuestionLibrary.txt";

        // Δημιουργώ μια βιβλιοθήκη ερωτήσεων
        try {
            QuestionLibrary questionLibrary = new QuestionLibrary(true,questionFile);
            quickAnswer = new QuickAnswer(questionLibrary,1);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    void isOver() {
        assertEquals(false,quickAnswer.isOver());

        quickAnswer.proceed(); //Είμαι στη τρέχουσα ερώτηση που είναι και μοναδική
        quickAnswer.proceed(); //Τραβάω άλλη μία ερώτηση

        assertEquals(true, quickAnswer.isOver());

        quickAnswer.proceed(); //Τραβάω άλλη μία ερώτηση
        assertEquals(true,quickAnswer.isOver());
    }

    @Test
    void proceed() {
        // Δημιουργώ μια ερώτηση
        String aQuestion = "Σε ποια ήπειρο βρίσκονται οι Ηνωμένες Πολιτείας Αμερικής;";
        String[] answers = {"Βόρεια Αμερική","Ασία","Νότια Αμερική","Ωκεανία"};
        String rightAnsw = "Βόρεια Αμερική";
        String category = "Γεωγραφία";
        Question expected = new Question(aQuestion,answers, rightAnsw,category);

        quickAnswer.proceed(); //Είμαι στη τρέχουσα ερώτηση που είναι και μοναδική

        String actualQuestion = quickAnswer.getQuestion(); //Παίρνω την εκφώνηση

        assertNotNull(actualQuestion);
        assertEquals(expected.getQuestion(),actualQuestion); //Ελέγχω εάν ταυτίζονται οι εκφωνήσεις

        String[] actualAnswers = quickAnswer.getQuestionAnswers(); //Παίρνω τις σωστές απαντήσεις

        //Ελέγχω εάν ταυτίζονται οι απαντήσεις
        int counter = 0;
        for (String aString : actualAnswers){
            for (String answer : answers){
                if(aString.equals(answer)){
                    counter++;
                }
            }

        }

        if(counter != 4){ //δεν ταυτίζονται οι απαντήσεις, άρα αποτυχία του test
            fail("Αποτυχία test");
        }

        assertEquals(expected.getRightAnswer(), quickAnswer.getRightQuestionAnswer()); //Ελέγχω εάν ταυτίζονται οι σωστές απαντήσεις
        assertEquals(expected.getCategory(),quickAnswer.getQuestionCategory());

        quickAnswer.proceed(); //Τραβάω άλλη μία ερώτηση

        actualQuestion = quickAnswer.getQuestion(); //Παίρνω την εκφώνηση

        assertNull(actualQuestion);
    }

    @Test
    void getQuestion() {
        // Δημιουργώ μια ερώτηση
        String aQuestion = "Σε ποια ήπειρο βρίσκονται οι Ηνωμένες Πολιτείας Αμερικής;";
        String[] answers = {"Βόρεια Αμερική","Ασία","Νότια Αμερική","Ωκεανία"};
        String rightAnsw = "Βόρεια Αμερική";
        String category = "Γεωγραφία";
        Question expected = new Question(aQuestion,answers, rightAnsw,category);

        quickAnswer.proceed(); //Είμαι στη τρέχουσα ερώτηση που είναι και μοναδική

        // Ελέγχω εάν ταυτίζονται οι εκφωνήσεις
        String actualQuestion = quickAnswer.getQuestion(); //Παίρνω την εκφώνηση

        assertNotNull(actualQuestion);
        assertEquals(expected.getQuestion(),actualQuestion);

        // Ελέγχω εάν ταυτίζονται και τα υπόλοιπα πεδία της ερώτησης πέρα από την εκφώνηση
        String[] actualAnswers = quickAnswer.getQuestionAnswers(); //Παίρνω τις σωστές απαντήσεις

        //Ελέγχω εάν ταυτίζονται οι απαντήσεις
        int counter = 0;
        for (String aString : actualAnswers){
            for (String answer : answers){
                if(aString.equals(answer)){
                    counter++;
                }
            }

        }

        if(counter != 4){ //δεν ταυτίζονται οι απαντήσεις, άρα αποτυχία του test
            fail("Αποτυχία test");
        }

        assertEquals(expected.getRightAnswer(), quickAnswer.getRightQuestionAnswer()); //Ελέγχω εάν ταυτίζονται οι σωστές απαντήσεις
        assertEquals(expected.getCategory(),quickAnswer.getQuestionCategory());

        quickAnswer.proceed(); //Τραβάω άλλη μία ερώτηση

        actualQuestion = quickAnswer.getQuestion(); //Παίρνω την εκφώνηση

        assertNull(actualQuestion); //Είχα μία ερώτηση γι αυτό περιμένω να είναι null
    }



    @Test
    void getRightQuestionAnswer() {
        String expected = "Βόρεια Αμερική";
        String actual = quickAnswer.getRightQuestionAnswer();

        assertEquals(expected,actual);
    }

    @Test
    void getQuestionCategory() {
        String expected = "Γεωγραφία";
        String actual = quickAnswer.getQuestionCategory();

        assertEquals(expected,actual);
    }



    @Test
    void playerNumberIsCompatible() {
        assertEquals(false,quickAnswer.playerNumberIsCompatible(1));

        assertEquals(true, quickAnswer.playerNumberIsCompatible(2)); // Μόνο για 2 παίχτες

        assertEquals(false, quickAnswer.playerNumberIsCompatible(0));

        assertEquals(false, quickAnswer.playerNumberIsCompatible(3));
    }

    @Test
    void getRoundName() {
        assertEquals("Γρήγορη Απάντηση",quickAnswer.getRoundName());
    }

    @Test
    void getRoundDescription() {
        assertEquals("Ο πρώτος που απαντάει σωστά κερδίζει 1000 πόντους και ο δεύτερος που απαντάει σωστά κερδίζει 500 πόντους.",quickAnswer.getRoundDescription());
    }


    @Test
    void answerQuestion1() {
        String[] answers = {"Βόρεια Αμερική","Ασία","Νότια Αμερική","Ωκεανία"};

        assertEquals(1000,quickAnswer.answerQuestion("Βόρεια Αμερική"));

        assertEquals(500,quickAnswer.answerQuestion("Βόρεια Αμερική"));

    }

    @Test
    void answerQuestion2() {
        assertEquals(1000,quickAnswer.answerQuestion("Βόρεια Αμερική"));

        assertEquals(0,quickAnswer.answerQuestion("Ασία"));

    }


    @Test
    void answerQuestion3() {
        assertEquals(0,quickAnswer.answerQuestion("Νότια Αμερική"));

        assertEquals(1000,quickAnswer.answerQuestion("Βόρεια Αμερική"));

    }

    @Test
    void answerQuestion4() {
        assertEquals(0,quickAnswer.answerQuestion("Ωκεανία"));

        assertEquals(0,quickAnswer.answerQuestion("Νότια Αμερική"));

    }

    @Override
    QuickAnswer getObject() {
        return quickAnswer;
    }
}