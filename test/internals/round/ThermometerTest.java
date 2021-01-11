package internals.round;

import internals.question.Question;
import internals.question.QuestionLibrary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ThermometerTest extends RoundTest{
    private Thermometer thermometer;

    @BeforeEach
    void setUp() {
        // Αρχείο με 20 ακριβώς ίδιες ερωτήσεις. Η χρήση αυτού του αρχείο προορίζεται μόνο για το testing.
        String questionFile = "TestThermometer.txt";

        // Δημιουργώ μια βιβλιοθήκη ερωτήσεων
        try {
            QuestionLibrary questionLibrary = new QuestionLibrary(true,questionFile);
            thermometer = new Thermometer(questionLibrary,20);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    void isOver() {
         /* Ελέγχω την περίπτωση του τι γίνεται όταν κανένας από τους παίχτες δεν μπορέσει να απαντήσει 5 σωστές απαντήσεις,
        πριν τελειώσει ο αριθμός των διαθέσιμων ερωτήσεων που υπάρχουν */

        assertEquals(false,thermometer.isOver());

        thermometer.proceed(); //Είμαι στη τρέχουσα ερώτηση
        for(int i=0; i<20; i++){
            thermometer.proceed(); //Τραβάω άλλη μία ερώτηση
        }

        //έχω τραβήξει όλες τις ερωτήσεις
        assertEquals(true, thermometer.isOver());


    }

    @Test
    void IsOver2() {
        /* Ελέγχω την περίπτωση του τι γίνεται όταν ένας παίχτης καταφέρει πρώτος να απαντήσει σωστά 5 ερωτήσεις,
           πριν τελειώσουν οι διαθέσιμες ερωτήσεις που υπάρχουν
         */

        /* Απαντάω σωστά πέντε ερωτήσεις */

        //1η σωστή απάντηση
        thermometer.answerQuestion("Βόρεια Αμερική","Alex");

        assertEquals(false,thermometer.isOver());

        //2η σωστή απάντηση
        thermometer.answerQuestion("Βόρεια Αμερική","Alex");

        assertEquals(false,thermometer.isOver());

        //3η σωστή απάντηση
        thermometer.answerQuestion("Βόρεια Αμερική","Alex");

        assertEquals(false,thermometer.isOver());

        //4η σωστή απάντηση
        thermometer.answerQuestion("Βόρεια Αμερική","Alex");

        assertEquals(false,thermometer.isOver());

        //5η σωστή απάντηση
        thermometer.answerQuestion("Βόρεια Αμερική","Alex");

        assertEquals(true,thermometer.isOver());

        //Ελέγχω εάν μόνο ο πρώτος που απαντάει σωστά 5 ερωτήσεις, κερδίζει τον γύρο και άρα τελειώνει και ο γύρος
        thermometer.answerQuestion("Βόρεια Αμερική","John");//Μετά από 5 σωστές απαντήσεις ο άλλος παίχτης δεν έχει δικαίωμα να απαντήσει

        assertEquals(true,thermometer.isOver());
    }

    @Test
    void proceed() {
        /* Ελέγχω την περίπτωση του τι γίνεται όταν κανένας από τους παίχτες δεν μπορέσει να απαντήσει 5 σωστές απαντήσεις,
        πριν τελειώσει ο αριθμός των διαθέσιμων ερωτήσεων που υπάρχουν */

        // Δημιουργώ μια ερώτηση
        String aQuestion = "Σε ποια ήπειρο βρίσκονται οι Ηνωμένες Πολιτείας Αμερικής;";
        String[] answers = {"Βόρεια Αμερική","Ασία","Νότια Αμερική","Ωκεανία"};
        String rightAnsw = "Βόρεια Αμερική";
        String category = "Γεωγραφία";
        Question expected = new Question(aQuestion,answers, rightAnsw,category);

        thermometer.proceed(); //Είμαι στη τρέχουσα ερώτηση που επειδή στο αρχείο έχω βάλει 20 φορές την ίδια ερώτηση ξέρω ότι ταυτίζεται με την από επάνω

        String actualQuestion = thermometer.getQuestion(); //Παίρνω την εκφώνηση

        assertNotNull(actualQuestion);
        assertEquals(expected.getQuestion(),actualQuestion); //Ελέγχω εάν ταυτίζονται οι εκφωνήσεις

        String[] actualAnswers = thermometer.getQuestionAnswers(); //Παίρνω τις σωστές απαντήσεις

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

        assertEquals(expected.getRightAnswer(), thermometer.getRightQuestionAnswer()); //Ελέγχω εάν ταυτίζονται οι σωστές απαντήσεις
        assertEquals(expected.getCategory(),thermometer.getQuestionCategory());

        for(int i=0; i<20; i++){//Τραβάω όλες τις διαθέσιμες ερωτήσεις
            thermometer.proceed();
        }

        actualQuestion = thermometer.getQuestion(); //Παίρνω την εκφώνηση

        assertNull(actualQuestion);
    }

    @Test
    void proceed2() {
        /* Ελέγχω την περίπτωση του τι γίνεται όταν ένας παίχτης καταφέρει πρώτος να απαντήσει σωστά 5 ερωτήσεις,
           πριν τελειώσουν οι διαθέσιμες ερωτήσεις που υπάρχουν
         */

        /* Απαντάω σωστά πέντε ερωτήσεις */

        //1η σωστή απάντηση
        assertNotNull( thermometer.proceed());
        thermometer.answerQuestion("Βόρεια Αμερική","Alex");


        //2η σωστή απάντηση
        assertNotNull( thermometer.proceed());
        thermometer.answerQuestion("Βόρεια Αμερική","Alex");


        //3η σωστή απάντηση
        assertNotNull( thermometer.proceed());
        thermometer.answerQuestion("Βόρεια Αμερική","Alex");


        //4η σωστή απάντηση
        assertNotNull( thermometer.proceed());
        thermometer.answerQuestion("Βόρεια Αμερική","Alex");


        //5η σωστή απάντηση
        assertNotNull( thermometer.proceed());
        thermometer.answerQuestion("Βόρεια Αμερική","Alex");

        //6η απάντηση (έχει ολοκληρωθεί ο γύρος)
        assertNull( thermometer.proceed());
    }

    @Test
    void getQuestion() {
        /* Ελέγχω την περίπτωση του τι γίνεται όταν κανένας από τους παίχτες δεν μπορέσει να απαντήσει 5 σωστές απαντήσεις,
        πριν τελειώσει ο αριθμός των διαθέσιμων ερωτήσεων που υπάρχουν */

        // Δημιουργώ μια ερώτηση
        String aQuestion = "Σε ποια ήπειρο βρίσκονται οι Ηνωμένες Πολιτείας Αμερικής;";
        String[] answers = {"Βόρεια Αμερική","Ασία","Νότια Αμερική","Ωκεανία"};
        String rightAnsw = "Βόρεια Αμερική";
        String category = "Γεωγραφία";
        Question expected = new Question(aQuestion,answers, rightAnsw,category);

        thermometer.proceed(); //Είμαι στη τρέχουσα ερώτηση που επειδή στο αρχείο έχω βάλει 20 φορές την ίδια ερώτηση ξέρω ότι ταυτίζεται με την από επάνω

        // Ελέγχω εάν ταυτίζονται οι εκφωνήσεις
        String actualQuestion = thermometer.getQuestion(); //Παίρνω την εκφώνηση

        assertNotNull(actualQuestion);
        assertEquals(expected.getQuestion(),actualQuestion);

        // Ελέγχω εάν ταυτίζονται και τα υπόλοιπα πεδία της ερώτησης πέρα από την εκφώνηση
        String[] actualAnswers = thermometer.getQuestionAnswers(); //Παίρνω τις σωστές απαντήσεις

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

        assertEquals(expected.getRightAnswer(), thermometer.getRightQuestionAnswer()); //Ελέγχω εάν ταυτίζονται οι σωστές απαντήσεις
        assertEquals(expected.getCategory(),thermometer.getQuestionCategory());

        for(int i=0; i<20; i++){//Τραβάω όλες τις διαθέσιμες ερωτήσεις
            thermometer.proceed();
        }

        actualQuestion = thermometer.getQuestion(); //Παίρνω την εκφώνηση

        assertNull(actualQuestion); //Έχουν τελειώσει οι διαθέσιμες ερωτήσεις, οπότε περιμένω να είναι null
    }


    @Test
    void getQuestion2() {
        /* Ελέγχω την περίπτωση του τι γίνεται όταν ένας παίχτης καταφέρει πρώτος να απαντήσει σωστά 5 ερωτήσεις,
           πριν τελειώσουν οι διαθέσιμες ερωτήσεις που υπάρχουν
         */

        /* Απαντάω σωστά πέντε ερωτήσεις */
        String expected = "Σε ποια ήπειρο βρίσκονται οι Ηνωμένες Πολιτείας Αμερικής;";
        String actual;

        //1η σωστή απάντηση
        thermometer.answerQuestion("Βόρεια Αμερική","John");
        actual = thermometer.getQuestion();
        assertNotNull(actual);
        assertEquals(expected,actual);


        //2η σωστή απάντηση
        thermometer.answerQuestion("Βόρεια Αμερική","John");
        actual = thermometer.getQuestion();
        assertNotNull(actual);
        assertEquals(expected,actual);


        //3η σωστή απάντηση
        thermometer.answerQuestion("Βόρεια Αμερική","John");
        actual = thermometer.getQuestion();
        assertNotNull(actual);
        assertEquals(expected,actual);


        //4η σωστή απάντηση
        thermometer.answerQuestion("Βόρεια Αμερική","John");
        actual = thermometer.getQuestion();
        assertNotNull(actual);
        assertEquals(expected,actual);


        //5η σωστή απάντηση
        thermometer.answerQuestion("Βόρεια Αμερική","John");
        assertNull( thermometer.getQuestion());

    }




    @Test
    void getRightQuestionAnswer() {
        String expected = "Βόρεια Αμερική";
        String actual;
        /* Απαντάω σωστά πέντε ερωτήσεις */

        //1η σωστή απάντηση
        thermometer.answerQuestion("Βόρεια Αμερική","Alex");
        actual = thermometer.getRightQuestionAnswer();
        assertNotNull(actual);
        assertEquals(expected,actual);


        //2η σωστή απάντηση
        thermometer.answerQuestion("Βόρεια Αμερική","Alex");
        actual = thermometer.getRightQuestionAnswer();
        assertNotNull(actual);
        assertEquals(expected,actual);


        //3η σωστή απάντηση
        thermometer.answerQuestion("Βόρεια Αμερική","Alex");
        actual = thermometer.getRightQuestionAnswer();
        assertNotNull(actual);
        assertEquals(expected,actual);


        //4η σωστή απάντηση
        thermometer.answerQuestion("Βόρεια Αμερική","Alex");
        actual = thermometer.getRightQuestionAnswer();
        assertNotNull(actual);
        assertEquals(expected,actual);


        //5η σωστή απάντηση
        thermometer.answerQuestion("Βόρεια Αμερική","Alex");
        actual = thermometer.getRightQuestionAnswer();
        assertNull(actual); //Ολοκληρώθηκε ο γύρος

    }


    @Test
    void getQuestionCategory() {

        String expected = "Γεωγραφία";
        String actual;
        /* Απαντάω σωστά πέντε ερωτήσεις */

        //1η σωστή απάντηση
        thermometer.answerQuestion("Βόρεια Αμερική","Alex");
        actual = thermometer.getQuestionCategory();
        assertNotNull(actual);
        assertEquals(expected,actual);


        //2η σωστή απάντηση
        thermometer.answerQuestion("Βόρεια Αμερική","Alex");
        actual = thermometer.getQuestionCategory();
        assertNotNull(actual);
        assertEquals(expected,actual);


        //3η σωστή απάντηση
        thermometer.answerQuestion("Βόρεια Αμερική","Alex");
        actual = thermometer.getQuestionCategory();
        assertNotNull(actual);
        assertEquals(expected,actual);


        //4η σωστή απάντηση
        thermometer.answerQuestion("Βόρεια Αμερική","Alex");
        actual = thermometer.getQuestionCategory();
        assertNotNull(actual);
        assertEquals(expected,actual);


        //5η σωστή απάντηση
        thermometer.answerQuestion("Βόρεια Αμερική","Alex");
        actual = thermometer.getQuestionCategory();
        assertNull(actual); //Ολοκληρώθηκε ο γύρος
    }



    @Test
    void playerNumberIsCompatible() {
        assertEquals(false,thermometer.playerNumberIsCompatible(1));

        assertEquals(true, thermometer.playerNumberIsCompatible(2)); // Μόνο για 2 παίχτες

        assertEquals(false, thermometer.playerNumberIsCompatible(0));

        assertEquals(false, thermometer.playerNumberIsCompatible(3));
    }

    @Test
    void getRoundName() {
        assertEquals("Θερμόμετρο",thermometer.getRoundName());
    }

    @Test
    void getRoundDescription() {
        assertEquals("Ο πρώτος παίχτης που θα απαντήσει σωστά 5 ερωτήσεις κερδίζει 5000 πόντους.",thermometer.getRoundDescription());
    }


    @Test
    void answerQuestion() {
        /* Παίχτης1: 0 σωστές 0 λάθος απαντήσεις
           Παίχτης2: 0 σωστές 0 λάθος απαντήσεις
         */

        //1η απάντηση
        assertEquals(0,thermometer.answerQuestion("Βόρεια Αμερική","Alex"));
        assertEquals(0,thermometer.answerQuestion("Βόρεια Αμερική","John"));

        /* Παίχτης1: 1 σωστή 0 λάθος απαντήσεις
           Παίχτης2: 1 σωστή 0 λάθος απαντήσεις
         */

        //2η απάντηση
        assertEquals(0,thermometer.answerQuestion("Βόρεια Αμερική","Alex"));
        assertEquals(0,thermometer.answerQuestion("Ασία","John"));
         /* Παίχτης1: 2 σωστές 0 λάθος απαντήσεις
           Παίχτης2: 1 σωστή 1 λάθος απαντήσεις
         */

        //3η απάντηση
        assertEquals(0,thermometer.answerQuestion("Ωκεανία","Alex"));
        assertEquals(0, thermometer.answerQuestion("Βόρεια Αμερική","John"));

        /* Παίχτης1: 2 σωστές 1 λάθος απαντήσεις
           Παίχτης2: 2 σωστές 1 λάθος απαντήσεις
         */

        //4η απάντηση
        assertEquals(0,thermometer.answerQuestion("Ωκεανία","Alex"));
        assertEquals(0,thermometer.answerQuestion("Ασία","John"));
         /* Παίχτης1: 2 σωστές 2 λάθος απαντήσεις
           Παίχτης2: 2 σωστές 2 λάθος απαντήσεις
         */


        //5η απάντηση
        assertEquals(0,thermometer.answerQuestion("Βόρεια Αμερική","Alex"));
        assertEquals(0,thermometer.answerQuestion("Βόρεια Αμερική","John"));
         /* Παίχτης1: 3 σωστές 2 λάθος απαντήσεις
           Παίχτης2: 3 σωστές 2 λάθος απαντήσεις
         */

        //6η απάντηση
        assertEquals(0,thermometer.answerQuestion("Βόρεια Αμερική","Alex"));
        assertEquals(0,thermometer.answerQuestion("Βόρεια Αμερική","John"));
         /* Παίχτης1: 4 σωστές 2 λάθος απαντήσεις
           Παίχτης2: 4 σωστές 2 λάθος απαντήσεις
         */

        //7η απάντηση
        assertEquals(0,thermometer.answerQuestion("Νότια Αμερική","Alex"));
        assertEquals(5000,thermometer.answerQuestion("Βόρεια Αμερική","John"));
         /* Παίχτης1: 4 σωστές 2 λάθος απαντήσεις
           Παίχτης2: 5 σωστές 2 λάθος απαντήσεις
         */

        //------- Τέλος γύρου ---------

        assertEquals(0,thermometer.answerQuestion("Βόρεια Αμερική","Alex"));
    }

    @Override
    Thermometer getObject() {
        return thermometer;
    }
}