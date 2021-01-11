package internals.round;

import internals.question.Question;
import internals.question.QuestionLibrary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BetTest extends RoundTest{
    private Bet bet;

    @BeforeEach
    void setUp() {

        // Αρχείο με μόνο μία ερώτηση. Η χρήση αυτού του αρχείο προορίζεται μόνο για το testing.
        String questionFile = "TestQuestionLibrary.txt";

        // Δημιουργώ μια βιβλιοθήκη ερωτήσεων
        try {
            QuestionLibrary questionLibrary = new QuestionLibrary(true,questionFile);
            bet = new Bet(questionLibrary,1);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    void isOver() {
        assertEquals(false,bet.isOver());

        bet.proceed(); //Είμαι στη τρέχουσα ερώτηση που είναι και μοναδική
        bet.proceed(); //Τραβάω άλλη μία ερώτηση

        assertEquals(true, bet.isOver());

        bet.proceed(); //Τραβάω άλλη μία ερώτηση
        assertEquals(true,bet.isOver());
    }

    @Test
    void proceed() {
        // Δημιουργώ μια ερώτηση
        String aQuestion = "Σε ποια ήπειρο βρίσκονται οι Ηνωμένες Πολιτείας Αμερικής;";
        String[] answers = {"Βόρεια Αμερική","Ασία","Νότια Αμερική","Ωκεανία"};
        String rightAnsw = "Βόρεια Αμερική";
        String category = "Γεωγραφία";
        Question expected = new Question(aQuestion,answers, rightAnsw,category);

        bet.proceed(); //Είμαι στη τρέχουσα ερώτηση που είναι και μοναδική

        String actualQuestion = bet.getQuestion(); //Παίρνω την εκφώνηση

        assertNotNull(actualQuestion);
        assertEquals(expected.getQuestion(),actualQuestion); //Ελέγχω εάν ταυτίζονται οι εκφωνήσεις

        String[] actualAnswers = bet.getQuestionAnswers(); //Παίρνω τις σωστές απαντήσεις

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

        assertEquals(expected.getRightAnswer(), bet.getRightQuestionAnswer()); //Ελέγχω εάν ταυτίζονται οι σωστές απαντήσεις
        assertEquals(expected.getCategory(),bet.getQuestionCategory());

        bet.proceed(); //Τραβάω άλλη μία ερώτηση

        actualQuestion = bet.getQuestion(); //Παίρνω την εκφώνηση

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

        bet.proceed(); //Είμαι στη τρέχουσα ερώτηση που είναι και μοναδική

        // Ελέγχω εάν ταυτίζονται οι εκφωνήσεις
        String actualQuestion = bet.getQuestion(); //Παίρνω την εκφώνηση

        assertNotNull(actualQuestion);
        assertEquals(expected.getQuestion(),actualQuestion);

        // Ελέγχω εάν ταυτίζονται και τα υπόλοιπα πεδία της ερώτησης πέρα από την εκφώνηση
        String[] actualAnswers = bet.getQuestionAnswers(); //Παίρνω τις σωστές απαντήσεις

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

        assertEquals(expected.getRightAnswer(), bet.getRightQuestionAnswer()); //Ελέγχω εάν ταυτίζονται οι σωστές απαντήσεις
        assertEquals(expected.getCategory(),bet.getQuestionCategory());

        bet.proceed(); //Τραβάω άλλη μία ερώτηση

        actualQuestion = bet.getQuestion(); //Παίρνω την εκφώνηση

        assertNull(actualQuestion); //Είχα μία ερώτηση γι αυτό περιμένω να είναι null
    }




    @Test
    void getRightQuestionAnswer() {
        String expected = "Βόρεια Αμερική";
        String actual = bet.getRightQuestionAnswer();

        assertEquals(expected,actual);
    }

    @Test
    void getQuestionCategory() {
        String expected = "Γεωγραφία";
        String actual = bet.getQuestionCategory();

        assertEquals(expected,actual);
    }




    @Test
    void getRoundName() {
        assertEquals("Ποντάρισμα",bet.getRoundName());
    }

    @Test
    void getRoundDescription() {
        assertEquals(String.format("Γύρος με πονταρίσματα!%n" +
                "Ο παίκτης μπορεί να ποντάρει 250, 500, 750 και 1000 πόντους.%n" +
                "Στη συνέχεια αν απαντήσει σωστά κερδίζει τους πόντους που πόνταρε, αλλιώς τους χάνει."),bet.getRoundDescription());
    }

    @Test
    void placeBet() {
        assertEquals("Επιτυχία",bet.placeBet("Alex",250));

        assertEquals("Επιτυχία",bet.placeBet("Alex",500));

        assertEquals("Επιτυχία",bet.placeBet("Alex",750));

        assertEquals("Επιτυχία",bet.placeBet("Alex",1000));

        assertEquals("Μη έγκυρη τιμή πονταρίσματος",bet.placeBet("Alex",0));

        assertEquals("Μη έγκυρη τιμή πονταρίσματος",bet.placeBet("Alex",-250));

        assertEquals("Μη έγκυρη τιμή πονταρίσματος",bet.placeBet("Alex",800));

        assertEquals("Μη έγκυρη τιμή πονταρίσματος",bet.placeBet("Alex",1250));
    }

    @Test
    void additionalPlaceBetTest() {
        assertEquals("Επιτυχία",bet.placeBet("John",250));

        assertEquals("Επιτυχία",bet.placeBet("Alex",500));

        assertNotEquals("Επιτυχία",bet.placeBet("Alex",752));

        assertEquals("Επιτυχία",bet.placeBet("John",1000));

        assertEquals("Επιτυχία",bet.placeBet("Alex",750));

        assertNotEquals("Επιτυχία",bet.placeBet("John",800));

        assertNotEquals("Επιτυχία",bet.placeBet("Ανδρέας",-1000));

        assertNotEquals("Επιτυχία",bet.placeBet("John",-250));

        assertNotEquals("Επιτυχία",bet.placeBet("Alex",-500));

        assertNotEquals("Επιτυχία",bet.placeBet("Alex",-10));

        assertNotEquals("Επιτυχία",bet.placeBet("John",10));

        assertEquals("Επιτυχία",bet.placeBet("Γιάννης",750));

        assertNotEquals("Επιτυχία",bet.placeBet("John",25));

        assertEquals("Επιτυχία",bet.placeBet("Δημήτρης",500));

    }



    @Test
    void answerQuestion() {
        int result;
        bet.placeBet("Alex",250);
        result = bet.answerQuestion("Βόρεια Αμερική","Alex");

        assertEquals(250,result);

        bet.placeBet("Alex",1000);
        result = bet.answerQuestion("Βόρεια Αμερική","Alex");

        assertEquals(1000,result);

        bet.placeBet("John",500);
        result = bet.answerQuestion("Βόρεια Αμερική","John");

        assertEquals(500,result);

        bet.placeBet("John",750);
        result = bet.answerQuestion("Βόρεια Αμερική","John");

        assertEquals(750,result);

        bet.placeBet("Alex",250);
        result = bet.answerQuestion("Ασία","Alex");

        assertEquals(-250,result);

        bet.placeBet("Alex",1000);
        result = bet.answerQuestion("Νότια Αμερική","Alex");

        assertEquals(-1000,result);

        bet.placeBet("John",500);
        result = bet.answerQuestion("Ωκεανία","John");

        assertEquals(-500,result);

        bet.placeBet("John",750);
        result = bet.answerQuestion("Νότια Αμερική","John");

        assertEquals(-750,result);

    }

    @Override
    Bet getObject() {
        return bet;
    }
}