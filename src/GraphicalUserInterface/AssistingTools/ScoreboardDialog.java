package GraphicalUserInterface.AssistingTools;

import javax.swing.*;
import java.awt.*;

class ScoreboardDialog extends JDialog {
    public ScoreboardDialog(Frame owner, Object[][] tableElements){
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
}
