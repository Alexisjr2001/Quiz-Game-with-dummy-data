package internals.roundManagement;

import internals.question.Question;
import internals.question.QuestionCategory;
import internals.question.QuestionLibrary;

public class Round {
    protected QuestionLibrary questionStore;
    protected Question roundQuestion;
    private final int minPlayers = 0; // =0 placeholder
    private final int maxPlayers = 0; // =0 placeholder
    protected int questionNumber;
    protected String roundDescription;
    protected String roundName;

    public Round(QuestionLibrary questionStore, Question roundQuestion,
                  int questionNumber, String roundDescription) {
        this.questionStore = questionStore;
        this.roundQuestion = roundQuestion;
        this.questionNumber = questionNumber;
        this.roundDescription = roundDescription;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public String getRoundName() {
        return roundName;
    }

    public String getRoundDescription() {
        return roundDescription;
    }

    public String getRoundType() {
        return roundType;
    }


    public String getRoundCategory() {
        return roundQuestion.getCategory();
    }

    /**
     * Ξεκινάει το γύρο
     */
    public void beginRound(){

    }

    /**
     * Ε
     *
     * @return true εάν υπάρχει, διαφορετικά false
     */
    public int getQuestionsRemaining(){
        return 0;
    }

    /**
     * Προχωράει στην επόμενη ερώτηση
     * @return reference στο ίδιο το αντικείμενο για αλυσιδωτές κλήσεις.
     */
    public Round proceed(){
        if (questionNumber-- > 0){
            roundQuestion = questionStore.getRandomQuestionCategory().getRandomQuestion();
        } else {
            roundQuestion = null;
        }

        return this;
    }

    /**
     *
     * @return Η επόμενη ερώτηση, αν οι δοθείσες ερωτήσεις εχουν ξεπεράσεις τις συνολικές ερωτήσεις που προβλέπονται για τον γύρο, επιστρέφει null.
     */
    public String getQuestion() {
        if (questionNumber > 0){
            return roundQuestion.getQuestion();
        } else {
            return null;
        }
    }

    public String[] getQuestionAnswers(){
        return roundQuestion.getAnswers();
    }

    public int getRemainingQuestionsInRound(){

    }


    /**
     *
     * @param choice Η επιλογή του χρήστη
     * @return true εάν η επιλογή είναι έγκυρη, διαφορετικά false
     */
    public boolean chooseQuestionCategory(String choice) {
        return false;
    }

    /**
     *
     * @param categories
     */
    public QuestionCategory[] loadQuestionCategories(QuestionCategory[] categories){
        return null;
    }

    /**
     *
     * @param answer
     * @return
     */
    public int answerQuestion(String answer) {
        return 0;
    }

}
