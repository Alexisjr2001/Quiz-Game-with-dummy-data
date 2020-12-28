package GraphicalUserInterface.AssistingTools;

import internals.question.Question;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * Η κλάση StaticTools αποτελεί μία βοηθητική κλάση και παρέχει ένα σύνολο στατικών μεθόδων για την υποβοήθηση ανάπτυξης γραφικού περιβάλλοντος.
 */
public class StaticTools {

    /**
     * Η κλάση αυτή περιέχει μόνο στατικές μεθόδους οπότε δεν συνίσταται η δημιουργία αντικειμένων της.
     */
    public StaticTools(){}

    /**
     * Δέχεται ένα αντικείμενο τύπου JComponent και επιστρέφει ένα JPanel με διαρρύθμιση FlowLayout που περιέχει μόνο το αντικείμενο του ορίσματος.
     * Σκοπός της είναι να επιστέφει ένα συστατικό το οποίο διατηρεί το "επιθυμητό" μέγεθος του και να έχει στοίχιση στο κέντρο.
     *
     * @param component συστατικό του οποίο θα "τυλιχθεί" σε FlowLayout
     * @return panel, με μοναδικό συστατικό το συστατικό του ορίσματος, που όταν προστεθεί σε δοχείο θα διατηρεί το μέγεθος του και θα έχει στοίχηση στο κέντρο.
     */
    public static JPanel wrapInFlowLayout(JComponent component){
        JPanel temp = new JPanel(new FlowLayout());
        temp.add(component);
        return temp;
    }





}
