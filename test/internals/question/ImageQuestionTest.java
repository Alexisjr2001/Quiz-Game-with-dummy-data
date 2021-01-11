package internals.question;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class ImageQuestionTest extends QuestionTest{
    private ImageQuestion imageQuestion;


    @Override
    ImageQuestion getObject(){
        String aQuestion = "Who won Ballon d'Or 2020?";
        String[] answers = {"Messi","Ronaldo","Lewandowski","nobody"};
        String rightAnswer = "nobody";
        String category = "Sports";

        return new ImageQuestion(aQuestion,answers,rightAnswer,category,"cyclops.png");
    }
}