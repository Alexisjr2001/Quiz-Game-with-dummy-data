package internals.roundManagement;

import java.util.Stack;

public class RoundController {
    private Stack<String> roundTypeNamesStack;
    private static final Round[] roundTypes = {new RightAnswer(), new Bet()};
    private boolean automaticShuffle;
    private int minPlayers;
    private int maxPlayers;

    public RoundController(boolean automaticShuffle, int minPlayers, int maxPlayers) {
        this.automaticShuffle = automaticShuffle;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        reshuffle();
    }

    public RoundController reshuffle(){
        roundTypeNamesStack = new Stack<>();

        for (Round r : roundTypes){
            if (minPlayers<=r.getMinPlayers() && r.getMaxPlayers()<=maxPlayers) {
                roundTypeNamesStack.push(r.getRoundName());
            }
        }

        java.util.Collections.shuffle(roundTypeNamesStack);
        return this;
    }

    public int getRemainingRoundTypes(){
        return roundTypeNamesStack.size();
    }

    public String getRandomRoundType(){
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
