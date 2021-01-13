package internals.round;

import internals.question.Question;
import internals.question.QuestionLibrary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class StopChronometerTest extends RoundTest{
    private StopChronometer stopChronometer;

    @BeforeEach
    void setUp() {
        // Αρχείο με μόνο μία ερώτηση. Η χρήση αυτού του αρχείο προορίζεται μόνο για το testing.
        String questionFile = "TestQuestionLibrary.txt";

        // Δημιουργώ μια βιβλιοθήκη ερωτήσεων
        try {
            QuestionLibrary questionLibrary = new QuestionLibrary(true,questionFile);
            stopChronometer = new StopChronometer(questionLibrary,1,5);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    void isOver() {
        assertEquals(false,stopChronometer.isOver());

        stopChronometer.proceed(); //Είμαι στη τρέχουσα ερώτηση που είναι και μοναδική
        stopChronometer.proceed(); //Τραβάω άλλη μία ερώτηση

        assertEquals(true, stopChronometer.isOver());

        stopChronometer.proceed(); //Τραβάω άλλη μία ερώτηση
        assertEquals(true,stopChronometer.isOver());
    }

    @Test
    void proceed() {
        // Δημιουργώ μια ερώτηση
        String aQuestion = "Σε ποια ήπειρο βρίσκονται οι Ηνωμένες Πολιτείας Αμερικής;";
        String[] answers = {"Βόρεια Αμερική","Ασία","Νότια Αμερική","Ωκεανία"};
        String rightAnsw = "Βόρεια Αμερική";
        String category = "Γεωγραφία";
        Question expected = new Question(aQuestion,answers, rightAnsw,category);

        stopChronometer.proceed(); //Είμαι στη τρέχουσα ερώτηση που είναι και μοναδική

        String actualQuestion = stopChronometer.getQuestion(); //Παίρνω την εκφώνηση

        assertNotNull(actualQuestion);
        assertEquals(expected.getQuestion(),actualQuestion); //Ελέγχω εάν ταυτίζονται οι εκφωνήσεις

        String[] actualAnswers = stopChronometer.getQuestionAnswers(); //Παίρνω τις σωστές απαντήσεις

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

        assertEquals(expected.getRightAnswer(), stopChronometer.getRightQuestionAnswer()); //Ελέγχω εάν ταυτίζονται οι σωστές απαντήσεις
        assertEquals(expected.getCategory(),stopChronometer.getQuestionCategory());

        stopChronometer.proceed(); //Τραβάω άλλη μία ερώτηση

        actualQuestion = stopChronometer.getQuestion(); //Παίρνω την εκφώνηση

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

        stopChronometer.proceed(); //Είμαι στη τρέχουσα ερώτηση που είναι και μοναδική

        // Ελέγχω εάν ταυτίζονται οι εκφωνήσεις
        String actualQuestion = stopChronometer.getQuestion(); //Παίρνω την εκφώνηση

        assertNotNull(actualQuestion);
        assertEquals(expected.getQuestion(),actualQuestion);

        // Ελέγχω εάν ταυτίζονται και τα υπόλοιπα πεδία της ερώτησης πέρα από την εκφώνηση
        String[] actualAnswers = stopChronometer.getQuestionAnswers(); //Παίρνω τις σωστές απαντήσεις

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

        assertEquals(expected.getRightAnswer(), stopChronometer.getRightQuestionAnswer()); //Ελέγχω εάν ταυτίζονται οι σωστές απαντήσεις
        assertEquals(expected.getCategory(),stopChronometer.getQuestionCategory());

        stopChronometer.proceed(); //Τραβάω άλλη μία ερώτηση

        actualQuestion = stopChronometer.getQuestion(); //Παίρνω την εκφώνηση

        assertNull(actualQuestion); //Είχα μία ερώτηση γι αυτό περιμένω να είναι null
    }


    @Test
    void getRightQuestionAnswer() {
        String expected = "Βόρεια Αμερική";
        String actual = stopChronometer.getRightQuestionAnswer();

        assertEquals(expected,actual);
    }

    @Test
    void getQuestionCategory() {
        String expected = "Γεωγραφία";
        String actual = stopChronometer.getQuestionCategory();

        assertEquals(expected,actual);
    }


    @Test
    void getRoundName() {
        assertEquals("Σταμάτησε το χρονόμετρο",stopChronometer.getRoundName());
    }

    @Test
    void getRoundDescription() {
        assertEquals(String.format("Υπάρχει ένα χρονόμετρο που μετράει αντίστροφα 5 δευτερόλεπτα!%n" +
                "Ο κάθε παίχτης που απαντάει σωστά κερδίζει τόσους πόντους, όσους τα millisecond που απέμεναν ακόμα όταν απάντησε επί 0,2."),stopChronometer.getRoundDescription());
    }

    @Override
    StopChronometer getObject() {
        return stopChronometer;
    }
}