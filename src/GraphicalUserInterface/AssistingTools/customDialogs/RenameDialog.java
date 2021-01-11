package GraphicalUserInterface.AssistingTools.customDialogs;

import GraphicalUserInterface.AssistingTools.StaticTools;
import internals.player.PlayerController;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Η κλάση RenameDialog επεκτείνει (μέσω σύνθεσης) την κλάση JDialog και υλοποιεί την λειτουργία του να ζητάει
 * απο τον χρήστη να επιλέξει το όνομα του παίκτη του οποίου την το όνομα θέλει να αλλάξει και το νέο όνομα και εκτελεί
 * την ενέργεια αυτή εφόσον το νέο όνομα δεν είναι όνομα ήδη υπάρχον παίχτη.
 *
 * @author Ioannis Baraklilis
 * @author Alexandros Tsingos
 *
 * @version 2020.12.30
 */
public class RenameDialog {
    private JDialog baseDialog; // Το JDialog που "επεκτείνεται" μέσω σύνθεσης

    /**
     * Ο τυπικός κατασκευαστής της κλάσης που κατασκευάζει και εμφανίζει το παράθυρο διαλόγου.
     * @param mainWindow αναφορά στο κύριο παράθυρο της εφαρμογής που προκάλεσε την εμφάνιση του παρόντος διαλόγου
     * @param playerController αναφορά στο αντικείμενο τύπου PlayerController του παιχνιδιού
     */
    public RenameDialog(JFrame mainWindow, PlayerController playerController) {
        baseDialog = new JDialog(mainWindow, "Μετονομασία Παίχτη", true);

        if (playerController.getNumberOfPlayers() == 0) { // Έλεγχος για την ύπαρξη παιχτών
            JOptionPane.showMessageDialog(mainWindow, "Δεν υπάρχουν αποθηκευμένοι παίχτες, οπότε δεν μπορείς να μετονομάσεις κάποιον", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            dispose();
        } else {
            baseDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            JPanel mainPanel = new JPanel(new GridLayout(0, 1));
            baseDialog.setContentPane(mainPanel);

            /* Τμήμα με επιλογή παίχτη για μετονομασία */
            JPanel selectionPanel = new JPanel(new BorderLayout());
            mainPanel.add(selectionPanel);
            TitledBorder border = BorderFactory.createTitledBorder("Επίλεξε το όνομα του παίχτη που θέλεις να μετονομάσεις");
            selectionPanel.setBorder(border);
            JComboBox<String> dropdownList = new JComboBox<>(playerController.listPlayers());
            dropdownList.setEditable(false);
            selectionPanel.add(dropdownList);


            /* Τμήμα με εισαγωγή νέου ονόματος */
            JPanel newNamePanel = new JPanel(new BorderLayout());
            mainPanel.add(newNamePanel);
            border = BorderFactory.createTitledBorder("Δώσε νέο όνομα παίχτη");
            newNamePanel.setBorder(border);
            JTextField newNameField = new JTextField();
            newNamePanel.add(newNameField);

            /* Τμήμα με κουμπιά */
            JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
            mainPanel.add(buttonPanel);

            JButton button = new JButton("OK");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    String result = playerController.changePlayerName(dropdownList.getItemAt(dropdownList.getSelectedIndex()), newNameField.getText());
                    if (result.equals("Επιτυχία")) { // Έλεγχος αν η μετονομασία ήταν επιτυχής: ενημερώνω τον χρήστη και γίνεται τερματισμός
                        JOptionPane.showMessageDialog(baseDialog, String.format("Η μετονομασία παίχτη απο %s σε %s, ήταν επιτυχής", dropdownList.getItemAt(dropdownList.getSelectedIndex()), newNameField.getText()), "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else { // Σφάλμα: δεν ήταν επιτυχής και ενημερώνω τον χρήστη για αυτό και δεν γίνεται τερματισμός
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


            /* Τμήμα με ρυθμίσεις παραθύρου διαλόγου */
            baseDialog.setSize(445, 180);
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
