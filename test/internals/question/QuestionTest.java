package internals.question;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestionTest{
    private Question question;

    @BeforeEach
    void setUp() {
        question = getObject();
    }

    @Test
    void getQuestion() {
        assertEquals("Who won Ballon d'Or 2020?",question.getQuestion());
    }

    @Test
    void getAnswers() {
        String[] answers = {"Messi","Ronaldo","Lewandowski","nobody"};

        assertArrayEquals(answers,question.getAnswers());
    }

    @Test
    void getRightAnswer() {
        String rightAnswer = "nobody";

        assertEquals(rightAnswer,question.getRightAnswer());
    }

    @Test
    void getCategory() {
        String category = "Sports";

        assertEquals(category,question.getCategory());
    }

    @Test
    void isRight() {
        assertEquals(true,question.isRight("nobody"));

        assertEquals(false,question.isRight("Ronaldo"));

        assertEquals(false,question.isRight("Maradona"));

        assertEquals(false,question.isRight("Lewandowski"));
    }

    @Test
    Question getObject(){
        String aQuestion = "Who won Ballon d'Or 2020?";
        String[] answers = {"Messi","Ronaldo","Lewandowski","nobody"};
        String rightAnswer = "nobody";
        String category = "Sports";

        return new Question(aQuestion,answers,rightAnswer,category);
    }
}