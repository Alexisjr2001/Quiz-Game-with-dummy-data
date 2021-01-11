package internals.round;

import internals.question.Question;
import internals.question.QuestionCategory;
import internals.question.QuestionLibrary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

class RightAnswerTest extends RoundTest{
    private RightAnswer rightAnswer;

    @BeforeEach
    void setUp() {

        // Αρχείο με μόνο μία ερώτηση. Η χρήση αυτού του αρχείο προορίζεται μόνο για το testing.
        String questionFile = "TestQuestionLibrary.txt";

        // Δημιουργώ μια βιβλιοθήκη ερωτήσεων
        try {
            QuestionLibrary questionLibrary = new QuestionLibrary(true,questionFile);
            rightAnswer = new RightAnswer(questionLibrary,1);
        }
        catch (IOException e){
            e.printStackTrace();
        }


    }

    @Test
    void isOver() {
        assertEquals(false,rightAnswer.isOver());

        rightAnswer.proceed(); //Είμαι στη τρέχουσα ερώτηση που είναι και μοναδική
        rightAnswer.proceed(); //Τραβάω άλλη μία ερώτηση

        assertEquals(true, rightAnswer.isOver());

        rightAnswer.proceed(); //Τραβάω άλλη μία ερώτηση
        assertEquals(true,rightAnswer.isOver());
    }

    @Test
    void proceed() {
        // Δημιουργώ μια ερώτηση
        String aQuestion = "Σε ποια ήπειρο βρίσκονται οι Ηνωμένες Πολιτείας Αμερικής;";
        String[] answers = {"Βόρεια Αμερική","Ασία","Νότια Αμερική","Ωκεανία"};
        String rightAnsw = "Βόρεια Αμερική";
        String category = "Γεωγραφία";
        Question expected = new Question(aQuestion,answers, rightAnsw,category);

        rightAnswer.proceed(); //Είμαι στη τρέχουσα ερώτηση που είναι και μοναδική

        String actualQuestion = rightAnswer.getQuestion(); //Παίρνω την εκφώνηση

        assertNotNull(actualQuestion);
        assertEquals(expected.getQuestion(),actualQuestion); //Ελέγχω εάν ταυτίζονται οι εκφωνήσεις

        String[] actualAnswers = rightAnswer.getQuestionAnswers(); //Παίρνω τις σωστές απαντήσεις

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

        assertEquals(expected.getRightAnswer(), rightAnswer.getRightQuestionAnswer()); //Ελέγχω εάν ταυτίζονται οι σωστές απαντήσεις
        assertEquals(expected.getCategory(),rightAnswer.getQuestionCategory());

        rightAnswer.proceed(); //Τραβάω άλλη μία ερώτηση

        actualQuestion = rightAnswer.getQuestion(); //Παίρνω την εκφώνηση

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

        rightAnswer.proceed(); //Είμαι στη τρέχουσα ερώτηση που είναι και μοναδική

        // Ελέγχω εάν ταυτίζονται οι εκφωνήσεις
        String actualQuestion = rightAnswer.getQuestion(); //Παίρνω την εκφώνηση

        assertNotNull(actualQuestion);
        assertEquals(expected.getQuestion(),actualQuestion);

        // Ελέγχω εάν ταυτίζονται και τα υπόλοιπα πεδία της ερώτησης πέρα από την εκφώνηση
        String[] actualAnswers = rightAnswer.getQuestionAnswers(); //Παίρνω τις σωστές απαντήσεις

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

        assertEquals(expected.getRightAnswer(), rightAnswer.getRightQuestionAnswer()); //Ελέγχω εάν ταυτίζονται οι σωστές απαντήσεις
        assertEquals(expected.getCategory(),rightAnswer.getQuestionCategory());

        rightAnswer.proceed(); //Τραβάω άλλη μία ερώτηση

        actualQuestion = rightAnswer.getQuestion(); //Παίρνω την εκφώνηση

        assertNull(actualQuestion); //Είχα μία ερώτηση γι αυτό περιμένω να είναι null


    }



    @Test
    void getRightQuestionAnswer() {
        String expected = "Βόρεια Αμερική";
        String actual = rightAnswer.getRightQuestionAnswer();

        assertEquals(expected,actual);

    }

    @Test
    void getQuestionCategory() {
        String expected = "Γεωγραφία";
        String actual = rightAnswer.getQuestionCategory();

        assertEquals(expected,actual);
    }




    @Test
    void getRoundName() {

        assertEquals("Σωστή απάντηση",rightAnswer.getRoundName());
    }

    @Test
    void getRoundDescription() {

        assertEquals("Άν ο παίκτης απαντήσει σωστά κερδίζει 1000 πόντους.",rightAnswer.getRoundDescription());
    }

    @Test
    void answerQuestion() {
        assertEquals(1000,rightAnswer.answerQuestion("Βόρεια Αμερική"));

        assertEquals(0,rightAnswer.answerQuestion("Ασία"));

        assertEquals(0,rightAnswer.answerQuestion("Ωκεανία"));

        assertEquals(0,rightAnswer.answerQuestion("Νότια Αμερική"));

        assertEquals(0,rightAnswer.answerQuestion("Ευρώπη"));

    }

    @Override
    RightAnswer getObject() {
        return rightAnswer;
    }
}