package internals.roundManagement;

import internals.question.QuestionLibrary;

public class Bet extends Round {

    public Bet(QuestionLibrary questionStore, int questionNumber) {
        super(questionStore, 1, 1, questionNumber);
    }
}
