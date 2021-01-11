package internals.question;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestionCategoryTest {
    private QuestionCategory questionCategory;

    @BeforeEach
    void setUp() {
        String categoryName = "Sports";
        String aQuestion = "Who won Ballon d'Or 2020?";
        String[] answers = {"Messi","Ronaldo","Lewandowski","nobody"};
        String rightAnswer = "nobody";
        String category = "Sports";
        Question[] questions = new Question[1];
        questions[0] = new Question(aQuestion,answers,rightAnswer,category);

        questionCategory = new QuestionCategory(categoryName,questions,true);
    }

    @Test
    void getCategoryName() {
        assertEquals("Sports",questionCategory.getCategoryName());
    }

    @Test
    void getAllQuestions() {
        String aQuestion = "Who won Ballon d'Or 2020?";
        String[] answers = {"Messi","Ronaldo","Lewandowski","nobody"};
        String rightAnswer = "nobody";
        String category = "Sports";

        Question question = new Question(aQuestion,answers,rightAnswer,category);
        Question[] expected = new Question[1];

        expected[0] = question;
        Question[] actual = questionCategory.getAllQuestions();

        assertEquals(expected[0].getQuestion(),actual[0].getQuestion());
        assertArrayEquals(expected[0].getAnswers(),actual[0].getAnswers());
        assertEquals(expected[0].getRightAnswer(),actual[0].getRightAnswer());
        assertEquals(expected[0].getCategory(),actual[0].getCategory());
    }

    @Test
    void getQuestion() {
        String aQuestion = "Who won Ballon d'Or 2020?";
        String[] answers = {"Messi","Ronaldo","Lewandowski","nobody"};
        String rightAnswer = "nobody";
        String category = "Sports";

        Question expected = new Question(aQuestion,answers,rightAnswer,category);
        Question actual = questionCategory.getQuestion("Who won Ballon d'Or 2020?");
        assertNotNull(actual);

        assertEquals(expected.getQuestion(),actual.getQuestion());
        assertArrayEquals(expected.getAnswers(),actual.getAnswers());
        assertEquals(expected.getRightAnswer(),actual.getRightAnswer());
        assertEquals(expected.getCategory(),actual.getCategory());

        assertNull(questionCategory.getQuestion("Who am I?"));
        assertNull(questionCategory.getQuestion("Which team won World Cup 2014?"));
    }

    @Test
    void getRandomQuestion() {
        String aQuestion = "Who won Ballon d'Or 2020?";
        String[] answers = {"Messi","Ronaldo","Lewandowski","nobody"};
        String rightAnswer = "nobody";
        String category = "Sports";

        //Τραβάω μια τυχαία ερώτηση, αλλά επειδή έχω βάλει μέσα μόνο μία ξέρω ουσιαστικά ποια θα μου γυρίσει
        Question expected = new Question(aQuestion,answers,rightAnswer,category);
        Question actual = questionCategory.getRandomQuestion();
        assertNotNull(actual);

        assertEquals(expected.getQuestion(),actual.getQuestion());
        assertArrayEquals(expected.getAnswers(),actual.getAnswers());
        assertEquals(expected.getRightAnswer(),actual.getRightAnswer());
        assertEquals(expected.getCategory(),actual.getCategory());

        /* Αφού έχω τραβήξει μια ερώτηση δεν υπάρχουν άλλες διαθέσιμες ερωτήσεις.
           Εφόσον όμως έχει επιτραπεί το αυτόματο ανακάτεμα ερωτήσεων,
           έχουμε την δυνατότητα να τραβήξουμε ξανά ερώτηση.
         */
        actual = questionCategory.getRandomQuestion(); //Τραβάω ξανά μια ερώτηση
        assertNotNull(actual);

        assertEquals(expected.getQuestion(),actual.getQuestion());
        assertArrayEquals(expected.getAnswers(),actual.getAnswers());
        assertEquals(expected.getRightAnswer(),actual.getRightAnswer());
        assertEquals(expected.getCategory(),actual.getCategory());

    }

    @Test
    void getRemainingRandomQuestions() {
        assertEquals(1,questionCategory.getRemainingRandomQuestions()); //Εξαρχής έχω μια ερώτηση

        questionCategory.getRandomQuestion(); //Τραβάω μια ερώτηση
        assertEquals(0,questionCategory.getRemainingRandomQuestions()); //Άρα αναμένω να έχω 0 ερωτήσεις

        questionCategory.reshuffle(); //Ανακατεύω τις ερωτήσεις (Ουσιαστικά έχω ξανά μια ερώτηση διαθέσιμη)
        assertEquals(1,questionCategory.getRemainingRandomQuestions()); // Αναμένω να έχω μία ερώτηση

        questionCategory.getRandomQuestion();//Τραβάω ξανά μια ερώτηση
        assertEquals(0,questionCategory.getRemainingRandomQuestions()); //Άρα αναμένω να έχω 0 ερωτήσεις

    }

    @Test
    void automaticShuffleOff(){
        String categoryName = "Sports";
        String aQuestion = "Who won Ballon d'Or 2020?";
        String[] answers = {"Messi","Ronaldo","Lewandowski","nobody"};
        String rightAnswer = "nobody";
        String category = "Sports";
        Question[] questions = new Question[1];
        questions[0] = new Question(aQuestion,answers,rightAnswer,category);

        // automaticShuffle == false
        questionCategory = new QuestionCategory(categoryName,questions,false);

        //Τραβάω μια τυχαία ερώτηση
        Question actual = questionCategory.getRandomQuestion();
        assertNotNull(actual);


        /* Αφού έχω τραβήξει μια ερώτηση δεν υπάρχουν άλλες διαθέσιμες ερωτήσεις.
           Επιπλέον δεν έχει επιτραπεί το αυτόματο ανακάτεμα ερωτήσεων,
           οπότε δεν έχουμε την δυνατότητα να τραβήξουμε ξανά ερώτηση.
         */
        actual = questionCategory.getRandomQuestion(); //Τραβάω ξανά μια ερώτηση
        assertNull(actual);
    }


}