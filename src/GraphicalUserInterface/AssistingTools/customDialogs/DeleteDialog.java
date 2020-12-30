package GraphicalUserInterface.AssistingTools.customDialogs;

import GraphicalUserInterface.AssistingTools.StaticTools;
import internals.player.PlayerController;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Η κλάση DeleteDialog επεκτείνει (μέσω σύνθεσης) την κλάση JDialog και υλοποιεί την λειτουργία του να ζητάει
 * απο τον χρήστη να επιλέξει το όνομα του παίκτη του οποίου την εγγραφή απο το παιχνίδι θέλει να διαγράψει
 * και εκτελεί την ενέργεια αυτή.
 */
public class DeleteDialog {
    private JDialog baseDialog; // Το JDialog που "επεκτείνεται" μέσω σύνθεσης

    /**
     * Ο τυπικός κατασκευαστής της κλάσης που κατασκευάζει και εμφανίζει το παράθυρο διαλόγου.
     * @param mainWindow αναφορά στο κύριο παράθυρο της εφαρμογής που προκάλεσε την εμφάνιση του παρόντος διαλόγου
     * @param playerController αναφορά στο αντικείμενο τύπου PlayerController του παιχνιδιού
     */
    public DeleteDialog(JFrame mainWindow, PlayerController playerController) {
        baseDialog = new JDialog(mainWindow, "Διαγραφή", true);

        if (playerController.getNumberOfPlayers() == 0) { // Έλεγχος για την ύπαρξη παιχτών
            JOptionPane.showMessageDialog(mainWindow, "Δεν υπάρχουν αποθηκευμένοι παίχτες, οπότε δεν μπορείς να διαγράψεις κάποιον", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            dispose();
        } else {
            baseDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            JPanel mainPanel = new JPanel(new GridLayout(0, 1));
            baseDialog.setContentPane(mainPanel);

            /* Τμήμα με επιλογή παίχτη για διαγραφή */
            JPanel selectionPanel = new JPanel(new BorderLayout());
            mainPanel.add(selectionPanel);
            TitledBorder border = BorderFactory.createTitledBorder("Επίλεξε το όνομα του παίχτη που θέλεις να διαγράψεις");

            /* Τμήμα με λίστα επιλογής παίχτη προς διαγραφή */
            selectionPanel.setBorder(border);
            JComboBox<String> dropdownList = new JComboBox<>(playerController.listPlayers());
            dropdownList.setEditable(false);
            selectionPanel.add(dropdownList);

            /* Τμήμα με κουμπιά */
            JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
            mainPanel.add(buttonPanel);

            JButton button = new JButton("OK");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    String result = playerController.removePlayer(dropdownList.getItemAt(dropdownList.getSelectedIndex()));
                    if (result.equals("Επιτυχία")) {
                        JOptionPane.showMessageDialog(baseDialog, String.format("Η διαγραφή του παίχτη %s, ήταν επιτυχής", dropdownList.getItemAt(dropdownList.getSelectedIndex())), "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(baseDialog, result, "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            buttonPanel.add(StaticTools.wrapInFlowLayout(button));

            button = new JButton("Cancel");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    dispose();
                }
            });
            buttonPanel.add(StaticTools.wrapInFlowLayout(button));


            baseDialog.setSize(420, 135);
            baseDialog.setLocationRelativeTo(mainWindow);
            baseDialog.setVisible(true);
        }
    }

    /**
     * Καταστρέφει το παράθυρο διαλόγου.
     */
    public void dispose() {
        baseDialog.dispose();
    }
}
