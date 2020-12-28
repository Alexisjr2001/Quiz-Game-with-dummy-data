package GraphicalUserInterface.AssistingTools;

import internals.question.Question;
import internals.round.Round;
import internals.round.RoundController;

public class GameSequenceHandler {
    private RoundController roundController;
    private int numberOfRounds;

    private QuestionPanel currentQuestionPanel;
    private Round currentRound;
    private Question currentQuestion;
    private int currentQuestionNumber;
    private int currentRoundNumber;


    public GameSequenceHandler(RoundController roundController, int numberOfRounds) {
        this.roundController = roundController;
        this.numberOfRounds = numberOfRounds;


    }
}
