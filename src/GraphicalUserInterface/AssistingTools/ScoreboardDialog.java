package GraphicalUserInterface.AssistingTools;

import javax.swing.*;
import java.awt.*;

/**
 * Η κλάση ScoreboardDialog υλοποιεί ένα παράθυρο διαλόγου που περιέχει τον πίνακα σκορ.
 * Ο πίνακας αυτός περιέχει το όνομα, τρέχον σκόρ (το σκόρ που έχει συγκεντρώσει απο την στιγμή που ξεκίνησε η εκτέλεση του παιχνιδιού), το μέγιστο σκορ και
 * τις νίκες παιχνιδιού πολλών παιχτών του κάθε παίχτη.
 *
 * Η πρόσβαση σε παράθυρα διαλόγου γίνεται με αποκλειστικά μέσω της χρήσης της στατικής μεθόδου {@code showTableDialog} τρέχουσας κλάσης.
 */
public class ScoreboardDialog extends JDialog {
    private ScoreboardDialog(Frame owner, Object[][] tableElements){
        super(owner, "Πίνακας με σκορ", true);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(owner);

        JTable scoreboard = new JTable(tableElements, new String[]{"Όνομα παίχτη", "Τρέχον σκόρ", "Μέγιστο σκόρ", "Νίκες παιχνιδιού πολλών παιχτών"});
        scoreboard.setEnabled(false);
        JScrollPane dialogPane = new JScrollPane(scoreboard);
        this.setContentPane(dialogPane);

        this.setSize(950, 350);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
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
