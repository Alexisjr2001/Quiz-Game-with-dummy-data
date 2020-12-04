package internals.question;

/**
 * Η κλάση {@code Question} μοντελοποιεί την οντότητα μίας ερώτησης που πρέπει να απαντηθεί κατά την διάρκεια ενός γύρου.
 *
 *  @author Ioannis Baraklilis
 *  @author Alexandros Tsingos
 *
 * @version 2020.11.17
 */
public class Question {
    private String question; // Η εκφώνηση της ερώτησης.
    private String[] answers; // Οι πιθανές απαντήσεις της ερώτησης.
    private String rightAnswer; // Η σωστή απάντηση.
    private String category; // Το όνομα της κατηγορίας στην οποία ανήκει η ερώτηση.

    /**
     * Ο τυπικός κατασκευαστής της κλάσης
     * @param question Η εκφώνηση της ερώτησης.
     * @param answers Οι πιθανές απαντήσεις της ερώτησης.
     * @param rightAnswer Η σωστή απάντηση.
     * @param category Το όνομα της κατηγορίας στην οποία ανήκει η ερώτηση.
     */
    public Question(String question, String[] answers, String rightAnswer, String category) {
        this.question = question;
        this.answers = answers;
        this.rightAnswer = rightAnswer;
        this.category = category;
    }

    /**
     * @return την εκφώνηση της ερώτησης
     */
    public String getQuestion(){
        return question;
    }

    /**
     * Επιστρέφει έναν πίνακα με τα αντίγραφα των πιθανών απαντήσεων στην ερώτηση.
     * @return πίνακα με τις πιθανές απαντήσεις.
     */
    public String[] getAnswers() {
        String[] temp = new String[answers.length];

        for (int i = 0; i < answers.length; i++){
            temp[i] = answers[i];
        }

        return temp;
    }

    /**
     * @return την σωστή απάντηση.
     */
    public String getRightAnswer() {
        return rightAnswer;
    }


    /**
     * @return το όνομα της κατηγορίας που ανήκει η ερώτηση.
     */
    public String getCategory() {
        return category;
    }


    /**
     * Ελέγχει αν η συμβολοσειρά που δίνεται ως είσοδος είναι η σωστή απάντηση.
     * Αν είναι επιστρέφει true, διαφορετικά επιστρέφει false.
     * @param answerToCheck συμβολοσειρά που θα ελεγχθεί αν είναι η σωστή απάντηση.
     * @return τιμή που αντιστοιχεί στην ταύτιση του ορίσματος με την σωστή απάντηση.
     */
    public boolean isRight(String answerToCheck){
        return rightAnswer.equals(answerToCheck);
    }
}
