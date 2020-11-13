package internals.player;

public class Player {
    private String name;
    private int score;
    private int highScore;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.highScore = 0;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int getHighScore() {
        return highScore;
    }

    public Player scoreGain(int gain){
        score += gain;

        return this;
    }

    public Player setName(String newName){
        name = newName;

        return this;
    }
}
