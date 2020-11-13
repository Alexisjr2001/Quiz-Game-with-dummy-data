package internals.question;

public class Question {
    private String question;
    private String[] answers;
    private String rightAnswer;
    private String category;

    public Question(String question, String[] answers, String rightAnswer, String category) {
        this.question = question;
        this.answers = answers;
        this.rightAnswer = rightAnswer;
        this.category = category;
    }

    public String getQuestion(){
        return question;
    }

    public String[] getAnswers() {
        return answers;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public String getCategory() {
        return category;
    }

    public boolean isRight(String answerToCheck){
        return question.equals(answerToCheck);
    }
}
