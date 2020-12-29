package GraphicalUserInterface.AssistingTools;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Η κλάση PlayerSelectionDialog υλοποιεί ένα παράθυρο διαλόγου που ζητάει στον χρήστη να επιλέξει τους παίκτες που πρόκειται να παίξουν στο παιχνίδι.
 */
public class PlayerSelectionDialog extends JDialog {
    private String[] selectedPlayerNames;

    private PlayerSelectionDialog(Frame owner, int numberOfPlayers, String[] playerNames) {
        super(owner, "Επιλογή Παίχτη", true);
        selectedPlayerNames = null;

        if (playerNames.length == 0) {
            JOptionPane.showMessageDialog(owner, "Δεν υπάρχουν αποθηκευμένοι παίχτες, οπότε δεν μπορείς να διαγράψεις κάποιον", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(this, String.format("Δεν υπάρχει ο απαραίτητος αριθμός παικτών!%n " +
                    "Πρέπει πρώτα να δημιουργήσεις παίκτη απο το μενού διαχείρισης παίκτη!", "Σφάλμα", JOptionPane.ERROR_MESSAGE));
            selectedPlayerNames = null;
            this.dispose();
        } else {
            setLocationRelativeTo(owner);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            JPanel mainPanel = new JPanel(new GridLayout(0, 1));
            this.setContentPane(mainPanel);

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
                        disposeSelf();
                    } else { // Υπάρχει όνομα σε πολλαπλές θέσεις
                        JOptionPane.showMessageDialog(getThis(), "Δεν μπορούν δύο παίκτες να ταυτίζονται!", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            buttonPanel.add(StaticTools.wrapInFlowLayout(button));

            button = new JButton("Cancel");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    selectedPlayerNames = null;
                    disposeSelf();
                }
            });
            buttonPanel.add(StaticTools.wrapInFlowLayout(button));

            this.pack();
            this.setSize(300, this.getSize().height);
            this.setVisible(true);
        }
    }

    private void disposeSelf(){
        this.dispose();
    }

    private PlayerSelectionDialog getThis(){
        return this;
    }

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
