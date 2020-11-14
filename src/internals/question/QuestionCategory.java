package internals.question;

import internals.roundManagement.Round;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

public class QuestionCategory {
    private String categoryName;
    private HashMap<String, Question> questionStore;
    private Stack<Question> questionStack;
    private boolean automaticShuffle;

    public QuestionCategory(String categoryName, boolean automaticShuffle) {
        this.categoryName = categoryName;
        this.questionStore = new HashMap<>();
        this.automaticShuffle = automaticShuffle;
        this.reshuffle();
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
        return questionStack.pop();
    }

    public int getRemainingQuestion(){
        return questionStack.size();
    }

    public QuestionCategory reshuffle(){
        questionStack=new Stack<>();
        for(Map.Entry<String,Question> q: questionStore.entrySet()){
            questionStack.push(q.getValue());
        }

        java.util.Collections.shuffle(questionStack);
        return this;

    }



}
