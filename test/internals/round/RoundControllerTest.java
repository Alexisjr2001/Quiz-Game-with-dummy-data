package internals.round;

import internals.question.QuestionLibrary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RoundControllerTest {
    private RoundController roundController;

    @BeforeEach
    void setUp() {

        // Αρχείο με μόνο μία ερώτηση. Η χρήση αυτού του αρχείο προορίζεται μόνο για το testing.
        String questionFile = "TestQuestionLibrary.txt";

        try {
            QuestionLibrary questionLibrary = new QuestionLibrary(true,questionFile);
            roundController = new RoundController(true,questionLibrary);
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    @Test
    void setPlayerNumber() {
        assertEquals("Επιτυχία",roundController.setPlayerNumber(1));

        assertEquals("Μη αποδεκτός αριθμός παιχτών! Θεωρείται αριθμός παιχτών ίσος με ένα!",roundController.setPlayerNumber(-1));

        assertEquals("Επιτυχία",roundController.setPlayerNumber(3));

        assertEquals("Επιτυχία",roundController.setPlayerNumber(2));

        assertEquals("Μη αποδεκτός αριθμός παιχτών! Θεωρείται αριθμός παιχτών ίσος με ένα!",roundController.setPlayerNumber(-2));

        assertEquals("Μη αποδεκτός αριθμός παιχτών! Θεωρείται αριθμός παιχτών ίσος με ένα!",roundController.setPlayerNumber(0));

    }

    @Test
    void setNumberOfQuestionsPerRound() {
        assertEquals("Επιτυχία",roundController.setNumberOfQuestionsPerRound(5));

        assertEquals("Επιτυχία", roundController.setNumberOfQuestionsPerRound(3));

        assertEquals("Μη αποδεκτός αριθμός ερωτήσεων για έναν γύρο",roundController.setNumberOfQuestionsPerRound(0));

        assertEquals("Μη αποδεκτός αριθμός ερωτήσεων για έναν γύρο", roundController.setNumberOfQuestionsPerRound(-5));
    }
    

    @Test
    void getRemainingRoundsNumber() {
        /* Λειτουργία ενός παίχτη */

        roundController.setPlayerNumber(1);
        assertEquals(3,roundController.getRemainingRoundsNumber()); //3 είναι τύποι γύρων που είναι διαθέσιμοι για Single Player

        //"Τραβάω" αυτούς τους τρεις τύπους γύρων
        roundController.getRandomRoundType();
        assertEquals(2,roundController.getRemainingRoundsNumber());

        roundController.getRandomRoundType();
        assertEquals(1,roundController.getRemainingRoundsNumber());

        roundController.getRandomRoundType();
        assertEquals(0,roundController.getRemainingRoundsNumber());

        //Αν τραβήξω ξανά γύρο τότε αναμένω να έχουν ξαναγεμίσει οι 3 τύποι γύρων
        roundController.getRandomRoundType(); //"Τραβάω" ένα γύρο
        assertEquals(2,roundController.getRemainingRoundsNumber()); //Αναμένω 3-1 = 2 γύρους

        /* Λειτουργία δύο παιχτών */
        roundController.setPlayerNumber(2);
        assertEquals(5,roundController.getRemainingRoundsNumber()); //5 είναι τύποι γύρων που είναι διαθέσιμοι για 2 παίχτες

        //"Τραβάω" αυτούς τους πέντε τύπους γύρων
        roundController.getRandomRoundType();
        assertEquals(4,roundController.getRemainingRoundsNumber());

        roundController.getRandomRoundType();
        assertEquals(3,roundController.getRemainingRoundsNumber());

        roundController.getRandomRoundType();
        assertEquals(2,roundController.getRemainingRoundsNumber());

        roundController.getRandomRoundType();
        assertEquals(1,roundController.getRemainingRoundsNumber());

        roundController.getRandomRoundType();
        assertEquals(0,roundController.getRemainingRoundsNumber());

        //Αν τραβήξω ξανά γύρο τότε αναμένω να έχουν ξαναγεμίσει οι 5 τύποι γύρων
        roundController.getRandomRoundType(); //"Τραβάω" ένα γύρο
        assertEquals(4,roundController.getRemainingRoundsNumber()); //Αναμένω 5-1 = 4 γύρους

        /* Ελέγχω εάν οι γύροι που επιστρέφονται είναι συμβατοί με την λειτουργία 3 παιχτών */
        roundController.setPlayerNumber(3);
        assertEquals(0 , roundController.getRemainingRoundsNumber());//Δεν έχω συμβατούς γύρους για 3 παίχτες
    }

    @Test
    void getRandomRoundType() {
        /* Ελέγχω εάν οι γύροι που επιστρέφονται είναι συμβατοί με την λειτουργία ενός παίχτη */

        roundController.setPlayerNumber(1);
        //1ος γύρος
        Round actual = roundController.getRandomRoundType();
        assertEquals(true , actual.playerNumberIsCompatible(1));

        //2ος γύρος
        actual = roundController.getRandomRoundType();
        assertEquals(true , actual.playerNumberIsCompatible(1));

        //3ος γύρος
        actual = roundController.getRandomRoundType();
        assertEquals(true , actual.playerNumberIsCompatible(1));

        //4ος γύρος - εδώ ελέγχεται και το automaticShuffle == true
        actual = roundController.getRandomRoundType();
        assertEquals(true , actual.playerNumberIsCompatible(1));

        /* Ελέγχω εάν οι γύροι που επιστρέφονται είναι συμβατοί με την λειτουργία δύο παιχτών */
        roundController.setPlayerNumber(2);
        actual = roundController.getRandomRoundType();

        assertEquals(true , actual.playerNumberIsCompatible(2));

    }

    @Test
    void automaticShuffleOff(){
        roundController = new RoundController(false, new QuestionLibrary(true));

        roundController.setPlayerNumber(1);

        //Αναμένω να έχω τρεις τύπους γύρους
        assertEquals(3,roundController.getRemainingRoundsNumber());
        Round actual = roundController.getRandomRoundType();
        assertNotNull(actual);

        //Αναμένω να έχω 2 τύπους γύρους
        assertEquals(2,roundController.getRemainingRoundsNumber());
        actual = roundController.getRandomRoundType();
        assertNotNull(actual);

        //Αναμένω να έχω 1 τύπο γύρου
        assertEquals(1,roundController.getRemainingRoundsNumber());
        actual = roundController.getRandomRoundType();
        assertNotNull(actual);

        //Αναμένω να μην έχω τύπο γύρου
        assertEquals(0,roundController.getRemainingRoundsNumber());
        actual = roundController.getRandomRoundType();
        assertNull(actual);


    }
}