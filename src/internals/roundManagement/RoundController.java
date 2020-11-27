package internals.roundManagement;

import internals.question.QuestionLibrary;

import java.util.ArrayList;
import java.util.Stack;

public class RoundController {
    private Stack<Round> roundTypeNamesStack;
    private final ArrayList<Round> availableRoundTypes;
    private boolean automaticShuffle;
    private int playerNumber;
    private QuestionLibrary questionStore;

    public RoundController(boolean automaticShuffle, int playerNumber, QuestionLibrary questionStore, int numberOfQuestionsPerRound) {
        this.automaticShuffle = automaticShuffle;
        this.playerNumber = playerNumber;
        this.questionStore = questionStore;
        availableRoundTypes = new ArrayList<>();

        availableRoundTypes.add(new RightAnswer(questionStore, numberOfQuestionsPerRound));
        availableRoundTypes.add(new Bet(questionStore, numberOfQuestionsPerRound));

        reshuffle(); // Εισαγωγή και τυχαιοποίηση σειράς στοιχείων στην δομή roundTypeNamesStack;
    }


    public RoundController reshuffle(){
        roundTypeNamesStack = new Stack<>();

        for (Round aRound : availableRoundTypes){
            if (aRound.playerNumberIsCompatible(playerNumber)) {
                roundTypeNamesStack.push(aRound);
            }
        }

        java.util.Collections.shuffle(roundTypeNamesStack);
        return this;
    }

    public int getRemainingRoundsNumber(){
        return roundTypeNamesStack.size();
    }

    public Round getRandomRoundType(){
        if (roundTypeNamesStack.empty()) {
            if (automaticShuffle) {
                reshuffle();
            } else {
                return null;
            }
        }

        return roundTypeNamesStack.pop();
    }

}
