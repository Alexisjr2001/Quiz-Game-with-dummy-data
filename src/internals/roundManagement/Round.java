package internals.roundManagement;

import internals.question.Question;
import internals.question.QuestionCategory;

import java.util.ArrayList;

public interface Round {
    /**
     * Ξεκινάει το γύρο
     */
    public void beginRound();

    /**
     * Ελέγχει εάν υπάρχει επόμεη ερώτηση
     * @return true εάν υπάρχει, διαφορετικά false
     */
    public boolean hasNextQuestion();

    /**
     *
     * @return Η επόμενη ερώτηση
     */
    public Question getNextQuestion();

    /**
     *
     * @return  Έναν πίνακα με τα ονόματα που έχουν οι κατηγορίες ερωτήσεων
     */
    public String[] getQuestionCategoryChoices();

    /**
     *
     * @param choice Η επιλογή του χρήστη
     * @return true εάν η επιλογή είναι έγκυρη, διαφορετικά false
     */
    public boolean chooseQuestionCategory(String choice);

    /**
     *
     * @param categories
     */
    public QuestionCategory[] loadQuestionCategories(QuestionCategory[] categories);

    /**
     *
     * @param answer
     * @return
     */
    public int answerQuestion(String answer);

}
