package internals.round;

import internals.question.Question;
import internals.question.QuestionLibrary;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Η {@code StopChronometer} επεκτείνει την {@code Round}, μοντελοποιώντας τον τύπο γύρου Σταμάτησε το χρονόμετρο.
 * @author Ioannis Baraklilis
 * @author Alexandros Tsingos
 *
 * @version 2021.1.3
 */
public class StopChronometer extends Round{
    private int count; //Τα milliseconds που απέμεναν ακόμα, όταν απάντησε ο παίχτης
    private final int timeAllowed; //Βοηθητική μεταβλητή που αρχικοποιεί την count και περιέχει τα δευτερόλεπτα που θα μετρηθούν αντίστροφα

    /**
     * Τυπικός κατασκευαστής που αρχικοποιεί τα δεδομένα της κλάσης.
     * @param questionStore αναφορά σε αντικείμενο της κλάσης QuestionLibrary, απο την οποία θα "παρθούν" οι ερωτήσεις.
     * @param questionNumber αριθμός ερωτήσεων που θα πρέπει να απαντηθούν σε αυτό τον γύρο.
     * @param timeAllowed Τα δευτερόλεπτα που έχει στην διάθεση του ο παίχτης για να απαντήσει την ερώτηση
     */
    public StopChronometer(QuestionLibrary questionStore, int questionNumber, int timeAllowed) {
        super(questionStore, 1, 2, questionNumber);
        this.timeAllowed = timeAllowed;
    }

    /**
     * Επιστρέφει το όνομα του τύπου γύρου.
     * @return όνομα του τύπου του γύρου.
     */
    @Override
    public String getRoundName() {
        return "Σταμάτησε το χρονόμετρο";
    }

    /**
     * Επιστρέφει την περιγραφή του τύπου γύρου.
     * @return περιγραφή του τύπου του γύρου.
     */
    @Override
    public String getRoundDescription() {
        return "Υπάρχει ένα χρονόμετρο που μετράει αντίστροφα 5 δευτερόλεπτα!" +
                "Ο κάθε παίχτης που απαντάει σωστά κερδίζει τόσους πόντους, όσους τα millisecond που απέμεναν ακόμα όταν απάντησε επί 0,2.";
    }

    /**
     * Ξεκινάει το χρονόμετρο που μετράει αντίστροφα τα δευτερόλεπτα που έχει στην διάθεση του ο παίχτης για να απαντήσει την ερώτηση
     * @param actionListener Χρησιμοποιείται για την επικοινωνία με την γραφική διεπαφή χρήστη
     */
    public void beginTimer(ActionListener actionListener){
        count = timeAllowed*1000; //Μετατροπή από seconds σε milliseconds

        //Ορίζουμε ένα Timer που θα μειώνει ανά 100 τα milliseconds μέχρι να μηδενιστεί
        Timer timer =new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(count <= 0){
                    ((Timer) e.getSource()).stop();
                }
                else {
                    count-=100;
                }
            }
        });

        timer.start(); //Ξεκινάει το Timer

        timer.addActionListener(actionListener); //Προσθήκη του action listener
    }

    /**
     *
     * @return Τα milliseconds που απέμεναν ακόμα, όταν απάντησε ο παίχτης
     */
    public int getTime(){
        return count;
    }

    /**
     * Υπολογίζει και επιστρέφει τον αριθμό πόντων που (πιθανόν μπορεί να) κερδίζει ο παίχτης δεδομένης της απάντησης που έδωσε
     * και του milliseconds που απέμεναν ακόμα, όταν απάντησε ο παίχτης.
     * Αν ο παίχτης απαντήσει σωστά κερδίζει τόσους πόντους όσους τα milliseconds που απέμεναν ακόμα όταν απάντησε επί 0.2
     * Αν ο παίχτης απαντήσει λάθος ή δεν απαντήσει εντός του χρονικού περιθωρίου που ορίζει το χρονόμετρο ο παίχτης κερδίζει 0 πόντους.
     * @param answer Η απάντηση που έδωσε ο παίχτης.
     * @return αριθμός πόντων που αντιστοιχούν στην δοθείσα απάντηση του ορίσματος.
     */
    public int answerQuestion(String answer){
        Question temp = getQuestionInstance(); // Παίρνω αναφορά στην τρέχουσα ερώτηση

        if(temp!=null && temp.isRight(answer)){ // Άν υπάρχουν διαθέσιμες ερωτήσεις και ο παίχτης απάντησε σωστά
            return (int) Math.round(count*0.2);
        }
        else {
            return 0;
        }
    }
}
