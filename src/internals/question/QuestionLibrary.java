package internals.question;

import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

public class QuestionLibrary {
    private HashMap<String, QuestionCategory> categoryStore;
    private Stack<QuestionCategory> categoryStack;
    private boolean automaticShuffle;

    public QuestionLibrary(boolean automaticShuffle) {
        this.automaticShuffle = automaticShuffle;
        loadQuestions();
    }

    public QuestionCategory getRandomQuestionCategory(){
        if (categoryStack.size()==0){
            if (automaticShuffle) {
                reshuffle();
            }
            else {
                return null;
            }
        }

        return categoryStack.pop();
    }

    /**
     * Επαναφέρει την κλάση στην αρχική κατάσταση.
     * @return
     */
    public QuestionLibrary reshuffle(){
        categoryStack = new Stack<>();
        for (QuestionCategory qc : categoryStore.values()){
            categoryStack.push(qc);
        }

        java.util.Collections.shuffle(categoryStack);
        return this;
    }

    public int getRemainingCategoriesNumber(){
        return categoryStack.size();
    }

    public QuestionCategory getQuestionCategory(String category){
       return categoryStore.get(category);
    }

    public QuestionCategory[] getAllQuestionCategories(){
        QuestionCategory[] temp = new QuestionCategory[categoryStore.size()];

        int i = 0; // Μετρητής στοιχείων του πίνακα αντιγράφων.
        for (QuestionCategory qc : categoryStore.values()){
            temp[i++] = qc;
        }

        return temp;
    }

    /**
     * Φορτώνει την δομή categoryStore με ερωτήσεις (που έχουν πρότυπη μορφή) με σκοπό δοκιμής σωστής λειτουργίας κλάσης.
     */
    private void loadQuestions(){
        Random randomGetter = new Random();
        final int questionNumber = 100;
        String[] answers = {"Ερώτηση 1", "Ερώτηση 2", "Ερώτηση 3", "Ερώτηση 4"};
        for(int categoryNumber = 0; categoryNumber <= questionNumber; categoryNumber++){
            String categoryName = "Όνομα κατηγορίας " + categoryNumber;
            Question[] questionTempStore = new Question[questionNumber];

            for (int questionIterator = 0; questionIterator < questionTempStore.length; questionIterator++){
                questionTempStore[questionIterator] = new Question("Ερώτηση " + questionIterator, answers, answers[randomGetter.nextInt(4)], categoryName);
            }
            QuestionCategory temp = new QuestionCategory(categoryName, questionTempStore, true);
        }
    }

}