package GraphicalUserInterface.AssistingTools.customDialogs;

import javax.swing.*;
import java.awt.*;

/**
 * Η κλάση ScoreboardDialog υλοποιεί ένα παράθυρο διαλόγου που περιέχει τον πίνακα σκορ.
 * Ο πίνακας αυτός περιέχει το όνομα, τρέχον σκορ (το σκορ που έχει συγκεντρώσει στο παιχνίδι ενός παίχτη απο την στιγμή που ξεκίνησε η εκτέλεση του παιχνιδιού),
 * το μέγιστο σκορ και τις νίκες παιχνιδιού πολλών παιχτών του κάθε παίχτη.
 *
 * Η πρόσβαση σε παράθυρα διαλόγου γίνεται και μέσω της χρήσης της στατικής μεθόδου {@code showTableDialog} τρέχουσας κλάσης,
 * όπου δεν απαιτείται η δημιουργία αντικειμένου.
 */
public class ScoreboardDialog{
    private JDialog baseDialog; // Το JDialog που "επεκτείνεται" μέσω σύνθεσης

    /**
     * Ο τυπικός κατασκευαστής της κλάσης που κατασκευάζει και εμφανίζει το παράθυρο διαλόγου.
     * Ο πίνακας που δέχεται ως παράμετρο πρέπει να είναι n x 4, όπου n ο επιθυμητός αριθμός παιχτών. Η πρώτη στήλη του
     * θα πρέπει να περιέχει τα ονόματα των παιχτών, η δεύτερη το τρέχον σκόρ του, η τρίτη το μέγιστο σκορ (highscore) και
     * η τέταρτη τον αριθμό νικών σε παιχνίδι πολλών παιχτών.
     *
     * @param owner αναφορά στο παράθυρο της εφαρμογής που προκάλεσε την εμφάνιση του παρόντος διαλόγου
     * @param tableElements ο πίνακας που περιέχει τα ονόματα και σκορ κάθε παίχτη και τα στοιχεία του οποίου
     *                      θα εμφανιστούν ως μέρος του διαλόγου.
     */
    public ScoreboardDialog(Frame owner, Object[][] tableElements){
        baseDialog = new JDialog(owner, "Πίνακας με σκορ", true);

        baseDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        baseDialog.setLocationRelativeTo(owner);

        /* Τμήμα με πίνακα */
        JTable scoreboard = new JTable(tableElements, new String[]{"Όνομα παίχτη", "Τρέχον σκορ σε ατομικό παιχνίδι",
                "Μέγιστο σκορ σε ατομικό παιχνίδι", "Νίκες παιχνιδιού πολλών παιχτών"});
        scoreboard.setEnabled(false);
        JScrollPane dialogPane = new JScrollPane(scoreboard);
        baseDialog.setContentPane(dialogPane);

        /* Τμήμα ρυθμίσεων παραθύρου διαλόγου */
        baseDialog.setSize(970, 350);
        baseDialog.setLocationRelativeTo(null);
        baseDialog.setVisible(true);
    }

    /**
     * Καταστρέφει το παράθυρο διαλόγου.
     */
    public void dispose(){
        baseDialog.dispose();
    }

    /**
     * Δημιουργεί και εμφανίζει παράθυρο διαλόγου με τον πίνακα των σκορ.
     * @param owner το Frame το οποίο προκάλεσε την δημιουργία του διαλόγου
     * @param tableElements στοιχεία του πίνακα
     */
    public static void showTableDialog(Frame owner, Object[][] tableElements){
        new ScoreboardDialog(owner, tableElements);
    }
}
