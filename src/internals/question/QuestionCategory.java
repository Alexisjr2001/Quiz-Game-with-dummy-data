package internals.question;

import java.util.HashMap;
import java.util.Stack;

public class QuestionCategory {
    private String categoryName;
    private HashMap<String, Question> questionStore;
    private Stack<Question> questionStack;
    private boolean automaticShuffle;

    public QuestionCategory() {

    }

    public String getCategoryName() {
        return categoryName;
    }

    public Question[] getAllQuestions(){
        return questionStore.values().toArray(new Question[0]);
    }

    public Question getQuestion(String question){
        return questionStore.get(question);
    }

    public Question getRandomQuestion(){

    }

    public int getRemainingQuestion(){

    }

    public QuestionCategory reshuffle(){

    }



}
