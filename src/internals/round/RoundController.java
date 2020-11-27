package internals.round;

import internals.question.QuestionLibrary;

import java.util.ArrayList;
import java.util.Stack;

public class RoundController {
    private Stack<Round> roundTypeNamesStack;
    private final ArrayList<Round> availableRoundTypes;
    private boolean automaticShuffle;
    private int playerNumber;
    private QuestionLibrary questionStore;

    public RoundController(boolean automaticShuffle, QuestionLibrary questionStore) {
        this.automaticShuffle = automaticShuffle;
        this.questionStore = questionStore;
        playerNumber = 1;
        availableRoundTypes = new ArrayList<>();

        reshuffle(); // Εισαγωγή και τυχαιοποίηση σειράς στοιχείων στην δομή roundTypeNamesStack;
    }

    public String setPlayerNumber(int playerNumber){
        if(this.playerNumber == playerNumber){
            return "Επιτυχία";
        }
        else if(playerNumber<=0){
            if(playerNumber!=1) {
                playerNumber = 1;
                reshuffle();
            }

            return "Μη αποδεκτός αριθμός παιχτών! Θεωρείται αριθμός παιχτών ίσος με ένα!";
        }

        this.playerNumber = playerNumber;
        reshuffle();
        return "Επιτυχία";
    }

    public String setNumberOfQuestionsPerRound(int numberOfQuestionsPerRound){
        if (numberOfQuestionsPerRound>0) {
            availableRoundTypes.clear();
            availableRoundTypes.add(new RightAnswer(questionStore, numberOfQuestionsPerRound));
            availableRoundTypes.add(new Bet(questionStore, numberOfQuestionsPerRound));
            return "Επιτυχία";
        }
        return "Μη αποδεκτός αριθμός ερωτήσεων για έναν γύρο";
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
