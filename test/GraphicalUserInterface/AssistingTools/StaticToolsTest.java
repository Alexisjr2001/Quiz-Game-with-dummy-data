package GraphicalUserInterface.AssistingTools;

import internals.question.QuestionLibrary;
import internals.round.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StaticToolsTest {

    @Test
    void calculatePlayerAnswerGain() {
        // Αρχείο με μόνο μία ερώτηση. Η χρήση αυτού του αρχείο προορίζεται μόνο για το testing.
        String questionFile = "TestQuestionLibrary.txt";
        QuestionLibrary questionLibrary;

        try {
            questionLibrary = new QuestionLibrary(true,questionFile);

            Round testRound;

            testRound = new RightAnswer(questionLibrary, 1);
            testRound.proceed();

            assertEquals(1000, StaticTools.calculatePlayerAnswerGain("player", testRound.getRightQuestionAnswer(), testRound));
            assertEquals(0, StaticTools.calculatePlayerAnswerGain("player", "Ασία", testRound));

            testRound = new Bet(questionLibrary, 1);
            testRound.proceed();

            ((Bet)testRound).placeBet("player", 250);

            assertEquals(250, StaticTools.calculatePlayerAnswerGain("player", testRound.getRightQuestionAnswer(), testRound));
            assertEquals(-250, StaticTools.calculatePlayerAnswerGain("player", "Ασία", testRound));

            testRound = new QuickAnswer(questionLibrary, 1);
            testRound.proceed();

            assertEquals(1000, StaticTools.calculatePlayerAnswerGain("player1", testRound.getRightQuestionAnswer(), testRound));
            assertEquals(500, StaticTools.calculatePlayerAnswerGain("player2",  testRound.getRightQuestionAnswer(), testRound));


            testRound = new Thermometer(questionLibrary, 1);
            testRound.proceed();

            assertEquals(0, StaticTools.calculatePlayerAnswerGain("player", testRound.getRightQuestionAnswer(), testRound));
            assertEquals(0, StaticTools.calculatePlayerAnswerGain("player",  testRound.getRightQuestionAnswer(), testRound));
            assertEquals(0, StaticTools.calculatePlayerAnswerGain("player",  testRound.getRightQuestionAnswer(), testRound));
            assertEquals(0, StaticTools.calculatePlayerAnswerGain("player",  testRound.getRightQuestionAnswer(), testRound));
            assertEquals(5000, StaticTools.calculatePlayerAnswerGain("player",  testRound.getRightQuestionAnswer(), testRound));

            testRound = null;
            assertEquals(0, StaticTools.calculatePlayerAnswerGain("player", "Answer", testRound));

            testRound = new QuickAnswer(questionLibrary, 1);
            testRound.proceed();

            assertEquals(0, StaticTools.calculatePlayerAnswerGain("player1", "Ασία", testRound));
            assertEquals(1000, StaticTools.calculatePlayerAnswerGain("player2",  testRound.getRightQuestionAnswer(), testRound));

            testRound = new RightAnswer(questionLibrary, 1);
            testRound.proceed();

            assertEquals(0, StaticTools.calculatePlayerAnswerGain("player", "Ασία", testRound));
            assertEquals(1000, StaticTools.calculatePlayerAnswerGain("player", testRound.getRightQuestionAnswer(), testRound));

            testRound = null;
            assertEquals(0, StaticTools.calculatePlayerAnswerGain("player", "Answer", testRound));

            testRound = new Bet(questionLibrary, 1);
            testRound.proceed();

            ((Bet)testRound).placeBet("player", 500);

            assertEquals(500, StaticTools.calculatePlayerAnswerGain("player", testRound.getRightQuestionAnswer(), testRound));
            assertEquals(-500, StaticTools.calculatePlayerAnswerGain("player", "Ασία", testRound));


        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}