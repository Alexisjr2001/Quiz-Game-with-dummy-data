package GraphicalUserInterface.AssistingTools;

import internals.round.*;

import javax.swing.*;
import java.awt.*;

/**
 * Η κλάση StaticTools αποτελεί μία βοηθητική κλάση και παρέχει ένα σύνολο στατικών μεθόδων για την υποβοήθηση ανάπτυξης γραφικού περιβάλλοντος.
 *
 * @author Ioannis Baraklilis
 * @author Alexandros Tsingos
 *
 * @version 2021.01.11
 */
public class StaticTools {

    /**
     * Η κλάση αυτή περιέχει μόνο στατικές μεθόδους οπότε δεν συνίσταται η δημιουργία αντικειμένων της.
     */
    public StaticTools(){}

    /**
     * Δέχεται ένα αντικείμενο τύπου JComponent και επιστρέφει ένα JPanel με διαρρύθμιση FlowLayout που περιέχει μόνο το αντικείμενο του ορίσματος.
     * Σκοπός της είναι να επιστέφει ένα συστατικό το οποίο διατηρεί το "επιθυμητό" μέγεθος του και να έχει στοίχιση στο κέντρο (ως προς την γραμμή που βρίσκεται).
     *
     * @param component συστατικό του οποίο θα "τυλιχθεί" σε FlowLayout
     * @return panel, με μοναδικό συστατικό το συστατικό του ορίσματος, που όταν προστεθεί σε δοχείο θα διατηρεί το μέγεθος του και θα έχει στοίχιση στο κέντρο.
     */
    public static JPanel wrapInFlowLayout(JComponent component){
        JPanel temp = new JPanel(new FlowLayout());
        temp.add(component);
        return temp;
    }

    /**
     * Θέτει ως μοναδικό στοιχείο ενός JPanel, διαγράφοντας τυχόν προϋπάρχοντα περιεχόμενα, ένα JPanel που δίνεται ως παράμετρος.
     * @param basePanel το JPanel του οποίου τα περιεχόμενα θα αλλάξουν
     * @param newOnlyPanel το JPanel που θα αποτελέσει μοναδικό περιεχόμενο του JPanel της άλλης παραμέτρου
     */
    public static void switchPanelTo(JPanel basePanel, JPanel newOnlyPanel){
        basePanel.removeAll(); // Αφαιρώ τυχόν προϋπάρχοντα περιεχόμενα
        basePanel.add(newOnlyPanel); // Θέτω μοναδικό περιεχόμενο
        basePanel.revalidate(); // Καλώ για ενημέρωση κατάστασης του JPanel που έχει αλλάξει περιεχόμενα
        basePanel.repaint();
    }

    /**
     * Δέχεται ένα αντικείμενο τύπου JComponent και επιστρέφει ένα JPanel με διαρρύθμιση GridBagLayout που περιέχει μόνο το αντικείμενο του ορίσματος.
     * Σκοπός της είναι να επιστέφει ένα συστατικό το οποίο διατηρεί το "επιθυμητό" μέγεθος του και να έχει στοίχιση στο κέντρο (ως προς την γραμμή και στήλη που βρίσκεται).
     *
     * @param component συστατικό του οποίο θα "τυλιχθεί" σε GridBagLayout
     * @return panel, με μοναδικό συστατικό το συστατικό του ορίσματος, που όταν προστεθεί σε δοχείο θα διατηρεί το μέγεθος του και θα έχει στοίχιση στο κέντρο.
     */
    public static JPanel wrapInGridBagLayout(JComponent component){
        JPanel temp = new JPanel(new GridBagLayout());
        temp.add(component);
        return temp;
    }

    /**
     * Εκτελείται η απάντηση και υπολογίζεται και επιστρέφεται η βαθμολογία που (πιθανώς) κερδίζεται.
     * @param playerName Όνομα παίχτη που απαντάει
     * @param givenAnswer Η απάντηση που έδωσε ο παίχτης
     * @param round Ο γύρος, στον οποίο απαντάει ο παίχτης
     * @return βαθμολογίας που (πιθανώς) κερδίζει ο παίχτης δίνοντας την συγκεκριμένη απάντηση
     */
    public static int calculatePlayerAnswerGain(String playerName, String givenAnswer, Round round){
        if (round instanceof RightAnswer){
            return  ((RightAnswer)round).answerQuestion(givenAnswer);
        } else if (round instanceof Bet){
            return ((Bet)round).answerQuestion(givenAnswer, playerName);
        } else if (round instanceof StopChronometer){
            return ((StopChronometer) round).answerQuestion(givenAnswer);
        } else if (round instanceof QuickAnswer){
            return ((QuickAnswer) round).answerQuestion(givenAnswer);
        } else if (round instanceof Thermometer){
            return ((Thermometer) round).answerQuestion(givenAnswer, playerName);
        } else {
            return 0;
        }
    }

}
