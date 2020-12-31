package GraphicalUserInterface.AssistingTools.customDialogs;

import GraphicalUserInterface.AssistingTools.StaticTools;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Η κλάση PlayerSelectionDialog υλοποιεί ένα παράθυρο διαλόγου (μέσω σύνθεσης) που ζητάει στον χρήστη να επιλέξει τους
 * παίκτες που πρόκειται να παίξουν στο παιχνίδι.
 *
 * Αν τα επιλεγμένα ονόματα δεν είναι όλα διαφορετικά μεταξύ τους θα εμφανιστεί παράθυρο σφάλματος και δεν θα τερματιστεί αυτόματα η επιλογή.
 *
 * Αν το παράθυρο κλείσει χωρίς να πατηθεί το κουμπί με την περιγραφή "ΟΚ" επιστρέφεται null.
 */
public class PlayerSelectionDialog{
    private String[] selectedPlayerNames; // Τα ονόματα των παιχτών που παίζουν

    private JDialog baseDialog; // Το JDialog που "επεκτείνεται" μέσω σύνθεσης

    /**
     * Ο τυπικός κατασκευαστής της κλάσης που αρχικοποιεί τον διάλογο σύμφωνα με τα ορίσματα.
     * @param owner το Frame το οποίο προκάλεσε την δημιουργία του διαλόγου
     * @param numberOfPlayers ο αριθμός παιχτών που παίζουν
     * @param playerNames τα ονόματα των παιχτών που είναι διαθέσιμοι προς επιλογή
     */
    public PlayerSelectionDialog(Frame owner, int numberOfPlayers, String[] playerNames) {
        baseDialog = new JDialog(owner, "Επιλογή Παίχτη", true);
        selectedPlayerNames = null;

        if (playerNames.length == 0) { // Έλεγχος για το αν υπάρχουν (αποθηκευμένοι) παίχτες
            JOptionPane.showMessageDialog(owner, "Δεν υπάρχουν αποθηκευμένοι παίχτες, οπότε δεν μπορείς να διαγράψεις κάποιον", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(baseDialog, String.format("Δεν υπάρχει ο απαραίτητος αριθμός παικτών!%n " +
                    "Πρέπει πρώτα να δημιουργήσεις παίκτη απο το μενού διαχείρισης παίκτη!", "Σφάλμα", JOptionPane.ERROR_MESSAGE));
            selectedPlayerNames = null; // Καμία επιλογή
            baseDialog.dispose();
        } else {
            baseDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            /* Τμήμα με επιλογή κάθε παίχτη */
            JPanel mainPanel = new JPanel(new GridLayout(0, 1));
            baseDialog.setContentPane(mainPanel);

            ArrayList<JComboBox<String>> dropdownListList = new ArrayList<>(numberOfPlayers);

            for (int i = 0; i<numberOfPlayers; i++){
                JPanel selectionPanel = new JPanel(new BorderLayout());
                mainPanel.add(selectionPanel);
                TitledBorder border = BorderFactory.createTitledBorder(String.format("%s%s %s","Επίλεξε το όνομα του",  (numberOfPlayers!=1)?(String.format(" %dου", i+1)):(""), "παίχτη"));
                selectionPanel.setBorder(border);
                JComboBox<String> dropdownList = new JComboBox<>(playerNames);
                dropdownList.setEditable(false);
                selectionPanel.add(dropdownList);
                dropdownListList.add(dropdownList);
            }

            JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
            mainPanel.add(buttonPanel);

            /* Τμήμα με κουμπιά */
            JButton button = new JButton("OK");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    selectedPlayerNames = new String[numberOfPlayers];

                    HashSet<String> hs = new HashSet<>();

                    int counter = 0;
                    for (JComboBox<String> jcb : dropdownListList){
                        selectedPlayerNames[counter++] = jcb.getItemAt(jcb.getSelectedIndex());
                        hs.add(jcb.getItemAt(jcb.getSelectedIndex()));
                    }

                    if ( hs.size() == numberOfPlayers ){ // Κάθε όνομα είναι μοναδικό
                        dispose();
                    } else { // Υπάρχει όνομα σε πολλαπλές θέσεις
                        JOptionPane.showMessageDialog(baseDialog, "Δεν μπορούν δύο παίκτες να ταυτίζονται!", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            buttonPanel.add(StaticTools.wrapInFlowLayout(button));

            button = new JButton("Cancel");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    selectedPlayerNames = null;
                    dispose();
                }
            });
            buttonPanel.add(StaticTools.wrapInFlowLayout(button));

            /* Τμήμα ρυθμίσεων παραθύρου διαλόγου */
            baseDialog.pack();
            baseDialog.setSize(300, baseDialog.getSize().height);
            baseDialog.setLocationRelativeTo(owner);
            baseDialog.setVisible(true);
        }
    }

    /**
     * Καταστρέφει το παράθυρο διαλόγου.
     */
    public void dispose(){
        baseDialog.dispose();
    }

    /**
     * Επιστρέφει τα ονόματα των παιχτών που επελέγησαν
     * @return τα ονόματα των παιχτών που επελέγησαν ή null αν η επιλογή δεν ήταν επιτυχής (ο χρήστης έκλεισε το παράθυρο χωρίς να επιλέξει)
     */
    public String[] getSelectedPlayerNames(){
        return selectedPlayerNames;
    }

    /**
     * Εμφανίζει ένα παράθυρο διαλόγου που ζητάει στον χρήστη να επιλέξει τους παίκτες που πρόκειται να παίξουν στο παιχνίδι και επιστρέφει
     * πίνακα συμβολοσειρών με τα ονόματα των παικτών που επιλέχθηκαν, εφόσον αυτά είναι διαφορετικά, και τερματίζεται η επιλογή παιχτών.
     * Αν τα επιλεγμένα ονόματα δεν είναι όλα διαφορετικά μεταξύ τους θα εμφανιστεί παράθυρο σφάλματος και δεν θα τερματιστεί η επιλογή.
     *
     * Αν το παράθυρο κλείσει χωρίς να πατηθεί το κουμπί με την περιγραφή "ΟΚ" επιστρέφεται null.
     *
     * Αν ο αριθμός των παικτών είναι μεγαλύτερος απο το μήκος του πίνακα ή ο αριθμός είναι μη θετικός επιστρέφεται IllegalArgumentException.
     *
     * @param owner το Frame το οποίο προκάλεσε την δημιουργία του διαλόγου
     * @param numberOfPlayers ο αριθμός των παιχτών προς επιλογή
     * @param playerNames τα ονόματα των παιχτών προς επιλογή
     * @return πίνακα συμβολοσειρών με τα ονόματα επιλεγμένων παιχτών ή null σε περίπτωση εξόδου χωρίς επιλογή
     */
    public static String[] selectPlayers(Frame owner, int numberOfPlayers, String[] playerNames){
        if (numberOfPlayers > playerNames.length){
            throw new IllegalArgumentException(String.format("Δεν μπορεί να γίνει η επιλογή %d διαφορετικών παικτών με %d δημιουργημένους παίκτες", numberOfPlayers, playerNames.length));
        } else if (numberOfPlayers<=0){
            throw new IllegalArgumentException("Μη επιτρεπτός αριθμός παικτών");
        }

        PlayerSelectionDialog psd = new PlayerSelectionDialog(owner, numberOfPlayers, playerNames);

        return psd.getSelectedPlayerNames();
    }
}
