package GraphicalUserInterface.AssistingTools.customPanels;

import internals.round.Round;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Η κλάση RoundPrefacePanel υλοποιεί (με σύνθεση) ένα JPanel που εμφανίζει πληροφορίες για έναν γύρο, πριν αυτός αρχίσει.
 * Σε αυτό εμφανίζονται: το όνομα του γύρου και οι πληροφορίες σχετικά με τον γύρο.
 * Επίσης, περιλαμβάνει ένα κουμπί.
 *
 * @author Ioannis Baraklilis
 * @author Alexandros Tsingos
 *
 * @version 2020.12.30
 */
public class RoundPrefacePanel{
    private JPanel basePanel; // Το JPanel που "επεκτείνεται" μέσω σύνθεσης

    /**
     * Ο τυπικός κατασκευαστής που αρχικοποιεί τα δεδομένα της κλάσης.
     *
     * @param round ο γύρος στο οποίο αναφέρεται το panel
     * @param beginButtonListener ο ActionListener που θα ενεργοποιηθεί όταν πατηθεί το κουμπί
     */
    public RoundPrefacePanel(Round round, ActionListener beginButtonListener){
        basePanel = new JPanel(new BorderLayout(20, 20));

        /* Τμήμα με όνομα γύρου */
        JPanel titlePanel = new JPanel(new FlowLayout());
        titlePanel.add(new JLabel(round.getRoundName()));
        basePanel.add(titlePanel, BorderLayout.PAGE_START);
        titlePanel.setBorder(BorderFactory.createTitledBorder("Όνομα γύρου"));

        /* Τμήμα με περιγραφή γύρου */
        JTextArea roundInfo = new JTextArea(round.getRoundDescription());
        roundInfo.setEditable(false);
        roundInfo.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Περιγραφή γύρου"));
        basePanel.add(roundInfo, BorderLayout.CENTER);

        /* Κουμπί */
        JButton beginButton = new JButton("Εκκίνηση γύρου");
        beginButton.setSelected(true);
        beginButton.addActionListener(beginButtonListener);
        basePanel.add(beginButton, BorderLayout.PAGE_END);
    }

    /**
     * Επιστρέφει το JPanel που αντιστοιχεί στην κλάση
     * @return το JPanel που αντιστοιχεί στην κλάση
     */
    public JPanel getPanel(){
        return basePanel;
    }
}
