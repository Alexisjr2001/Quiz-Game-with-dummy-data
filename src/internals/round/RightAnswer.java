package internals.round;

import internals.question.Question;
import internals.question.QuestionLibrary;

public class RightAnswer extends Round {

    public RightAnswer(QuestionLibrary questionStore, int questionNumber) {
        super(questionStore,1, 1, questionNumber);
    }

    /**
     * Επιστρέφει το όνομα του τύπου γύρου. Πρέπει να γίνει override απο κάθε κλάση που κληρονομεί την τρέχουσα.
     * @return όνομα του τύπου του γύρου.
     */
    @Override
    public String getRoundName(){
        return "Σωστή απάντηση";
    }

    /**
     * Επιστρέφει την περιγραφή του τύπου γύρου.
     * @return περιγραφή του τύπου του γύρου.
     */
    @Override
    public String getRoundDescription(){
        return "Κάθε παίχτης που απαντάει σωστά κερδίζει 1000 πόντους";
    }

    /**
     * Σηματοδοτεί την έναρξη του γύρου και εκτελεί τις απαραίτητες προετοιμασίες (αν αυτές χρειάζονται) για την έναρξη του γύρου.
     * Πρέπει να εκτελείται πάντα πριν την χρήση της κλάσης.
     */
    @Override
    public void beginRound(){}

    /**
     * Υπολογίζει και επιστρέφει την αριθμό πόντων που (πιθανόν μπορεί να) κερδίζει ο παίχτης δεδομένης της απάντησης που έδωσε.
     * @param answer Η απάντηση που έδωσε ο παίχτης.
     * @return αριθμός πόντων που αντιστοιχούν στην δοθείσα απάντηση του ορίσματος.
     */
    public int answerQuestion(String answer){
        Question temp = getQuestionInstance();

       if(temp!=null && temp.isRight(answer)){
           return 1000;
       }
       else {
           return 0;
       }

    }
}
