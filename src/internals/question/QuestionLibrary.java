package internals.question;

import java.util.HashMap;
import java.util.Stack;

public class QuestionLibrary {
    private HashMap<String, QuestionCategory> categoryStore;
    private Stack<QuestionCategory> categoryStack;
    private boolean automaticShuffle;

    public QuestionLibrary(QuestionCategory[] categoryStore, boolean automaticShuffle) {

    }


    public QuestionCategory getRandomQuestionCategory(){

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

    public int getRemainingCategories(){
        return categoryStack.size();
    }

    private void loadCategories(){

    }

    public QuestionCategory getQuestionCategory(String category){

    }

    public QuestionCategory[] getAllQuestionCategories(){
        QuestionCategory[] temp = new QuestionCategory[categoryStore.size()];

        int i = 0; // Μετρητής στοιχείων του πίνακα αντιγράφων.
        for (QuestionCategory qc : categoryStore.values()){
            temp[i++] = qc;
        }

        return temp;
    }

}
