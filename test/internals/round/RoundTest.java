package internals.round;

import internals.question.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

abstract class RoundTest {
    private Round round;

    @Test
    abstract void getRoundName();

    @Test
    abstract void getRoundDescription();

    @Test
    abstract void isOver();

    @Test
    abstract void proceed();

    @Test
    abstract void getQuestion();


    @Test
    void getQuestionAnswers() {

        String[] expected = {"Βόρεια Αμερική","Ασία","Νότια Αμερική","Ωκεανία"};
        String[] actual = getObject().getQuestionAnswers();

        //Ελέγχω εάν ταυτίζονται οι απαντήσεις
        int counter = 0;
        for (String aString : actual){
            for (String answer : expected){
                if(aString.equals(answer)){
                    counter++;
                }
            }

        }

        if(counter != 4){ //δεν ταυτίζονται οι απαντήσεις, άρα αποτυχία του test
            fail("Αποτυχία test");
        }
    }

    @Test
    abstract void getRightQuestionAnswer();

    @Test
    abstract void getQuestionCategory();

    @Test
    void getQuestionWithRandomizedAnswers() {

        // Δημιουργώ μια ερώτηση
        String aQuestion = "Σε ποια ήπειρο βρίσκονται οι Ηνωμένες Πολιτείας Αμερικής;";
        String[] answers = {"Βόρεια Αμερική","Ασία","Νότια Αμερική","Ωκεανία"};
        String rightAnsw = "Βόρεια Αμερική";
        String category = "Γεωγραφία";
        Question expected = new Question(aQuestion,answers, rightAnsw,category);

        Question actual = getObject().getQuestionWithRandomizedAnswers();
        //Ελέγχω εάν ταυτίζονται οι ερωτήσεις
        assertNotNull(actual);

        assertEquals(expected.getQuestion(),actual.getQuestion());
        assertEquals(expected.getRightAnswer(),actual.getRightAnswer());
        assertEquals(expected.getCategory(),actual.getCategory());

        String[] actualAnswers = actual.getAnswers();
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
    }

    @Test
    void playerNumberIsCompatible(){
        assertEquals(true,getObject().playerNumberIsCompatible(1));

        assertEquals(true, getObject().playerNumberIsCompatible(2));

        assertEquals(false, getObject().playerNumberIsCompatible(0));

        assertEquals(false, getObject().playerNumberIsCompatible(3));
    }

    abstract Round getObject();
}