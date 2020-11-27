package internals.roundManagement;

import internals.question.QuestionLibrary;

public class RightAnswer extends Round {

    public RightAnswer(QuestionLibrary questionStore, int questionNumber) {
        super(questionStore,1, 1, questionNumber);
    }
}
