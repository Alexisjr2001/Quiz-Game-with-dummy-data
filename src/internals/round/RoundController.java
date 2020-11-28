package internals.round;

import internals.question.QuestionLibrary;

import java.util.ArrayList;
import java.util.Stack;

public class RoundController {
    private Stack<Round> roundTypesStack;
    private final ArrayList<Round> availableRoundTypes;
    private boolean automaticShuffle;
    private int playerNumber;
    private QuestionLibrary questionStore;
    private int numberOfQuestionsPerRound;

    public RoundController(boolean automaticShuffle, QuestionLibrary questionStore) {
        this.automaticShuffle = automaticShuffle;
        this.questionStore = questionStore;
        playerNumber = 1;
        availableRoundTypes = new ArrayList<>();
        numberOfQuestionsPerRound = 1;

        reshuffle(); // Εισαγωγή και τυχαιοποίηση σειράς στοιχείων στην δομή roundTypesStack;
    }

    public String setPlayerNumber(int playerNumber){
        if(this.playerNumber == playerNumber){
            return "Επιτυχία";
        }
        else if(playerNumber<=0){
            if(playerNumber!=1) {
                playerNumber = 1;
                repopulateAvailableRoundTypes();
            }

            return "Μη αποδεκτός αριθμός παιχτών! Θεωρείται αριθμός παιχτών ίσος με ένα!";
        }

        this.playerNumber = playerNumber;
        reshuffle();
        return "Επιτυχία";
    }

    public String setNumberOfQuestionsPerRound(int numberOfQuestionsPerRound){
        if (numberOfQuestionsPerRound>0) {
            this.numberOfQuestionsPerRound = numberOfQuestionsPerRound;
            reshuffle();
            return "Επιτυχία";
        }
        return "Μη αποδεκτός αριθμός ερωτήσεων για έναν γύρο";
    }

    private void repopulateAvailableRoundTypes(){
        availableRoundTypes.clear();
        availableRoundTypes.add(new RightAnswer(questionStore, numberOfQuestionsPerRound));
        availableRoundTypes.add(new Bet(questionStore, numberOfQuestionsPerRound));
    }

    public RoundController reshuffle(){
        repopulateAvailableRoundTypes();

        roundTypesStack = new Stack<>();

        for (Round aRound : availableRoundTypes){
            if (aRound.playerNumberIsCompatible(playerNumber)) {
                roundTypesStack.push(aRound);
            }
        }

        java.util.Collections.shuffle(roundTypesStack);
        return this;
    }

    public int getRemainingRoundsNumber(){
        return roundTypesStack.size();
    }

    public Round getRandomRoundType(){
        if (roundTypesStack.empty()) {
            if (automaticShuffle) {
                reshuffle();
            } else {
                return null;
            }
        }

        return roundTypesStack.pop();
    }

}
