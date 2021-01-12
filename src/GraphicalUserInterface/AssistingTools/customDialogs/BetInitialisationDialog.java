package GraphicalUserInterface.AssistingTools.customDialogs;

import GraphicalUserInterface.AssistingTools.StaticTools;
import internals.round.Bet;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * Η κλάση BetInitialisationDialog επεκτείνει (μέσω σύνθεσης) την κλάση JDialog και υλοποιεί την λειτουργία του να ζητάει
 * απο τον χρήστη τα απαραίτητα δεδομένα για αρχικοποίηση του γύρου Bet.
 * Συγκεκριμένα ζητάει απο κάθε χρήστη που παίζει να ποντάρει έναν προκαθορισμένο αριθμό πόντων και θέτει αυτόν τον αριθμό
 * στο αντικείμενο γύρου που δέχεται.
 *
 * @author Ioannis Baraklilis
 * @author Alexandros Tsingos
 *
 * @version 2020.12.30
 */
public class BetInitialisationDialog{
    private boolean result; // Αποτέλεσμα επιτυχίας αρχικοποίησης

    private JDialog baseDialog; // Το JDialog που "επεκτείνεται" μέσω σύνθεσης

    /**
     * Ο τυπικός κατασκευαστής της κλάσης που αρχικοποιεί τον διάλογο σύμφωνα με τα ορίσματα.
     * @param owner το JPanel το οποίο προκάλεσε την δημιουργία του διαλόγου
     * @param round ο γύρος για τον οποίο πρέπει να γίνει προετοιμασία
     * @param selectedPlayerNames τα ονόματα των παιχτών που παίζουν σε αυτό τον γύρο
     */
    public BetInitialisationDialog(JPanel owner, Bet round, String[] selectedPlayerNames) {
        baseDialog = new JDialog((JFrame) owner.getTopLevelAncestor(), "Στοιχήματα", true);

        baseDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridLayout(0, 1));
        baseDialog.setContentPane(mainPanel);

        /* Τμήμα με εμφάνιση κατηγορίας επόμενης ερώτησης */
        JPanel infoPanel = new JPanel(new GridLayout(2, 1)); baseDialog.add(infoPanel);
        infoPanel.add(StaticTools.wrapInFlowLayout(new JLabel("Η κατηγορία της επόμενης ερώτησης είναι: " + round.getQuestionCategory())));
        infoPanel.add(StaticTools.wrapInFlowLayout(new JLabel("Τοποθετήστε τα στοιχήματα σας:")));

        /* Τμήμα με πονταρίσματα */
        Integer[] betOptions = { 250, 500, 750, 1000 };
        ArrayList<JComboBox<Integer>> dropdownListList = new ArrayList<>(selectedPlayerNames.length);

        for (String name : selectedPlayerNames) {
            JPanel selectionPanel = new JPanel(new BorderLayout());
            mainPanel.add(selectionPanel);
            TitledBorder border = BorderFactory.createTitledBorder("Ποντάρισμα για " + name);
            selectionPanel.setBorder(border);
            JComboBox<Integer> dropdownList = new JComboBox<>(betOptions);
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
                for (int i = 0; i < selectedPlayerNames.length; i++){
                    JComboBox<Integer> currentList = dropdownListList.get(i);
                    round.placeBet(selectedPlayerNames[i], currentList.getItemAt(currentList.getSelectedIndex()));
                }

                disposeSelf(true);
            }
        });
        buttonPanel.add(StaticTools.wrapInFlowLayout(button));

        baseDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(baseDialog, "Είσαι σίγουρος ότι θέλεις να γυρίσεις στο μενού;", "Έξοδος", JOptionPane.YES_NO_OPTION) == 0) {
                    disposeSelf(false); // Ο χρήστης επιλέγει να μην συνεχίσει με ποντάρισμα και πρέπει να γίνει έξοδος.
                }
            }
        });

        /* Ρυθμίσεις παραθύρου διαλόγου */
        baseDialog.setSize(630, 330);
        baseDialog.setLocationRelativeTo(owner);
        baseDialog.setVisible(true);
    }

    /**
     * Καταστρέφει το παράθυρο διαλόγου αφού αποθηκεύσει την κατάσταση τερματισμού.
     * @param dialogueSuccess κατάσταση τερματισμού
     */
    private void disposeSelf(boolean dialogueSuccess){
        result = dialogueSuccess;
        baseDialog.dispose();
    }

    /**
     * Επιστρέφει την κατάσταση τερματισμού.
     * @return Επιστρέφει την κατάσταση τερματισμού, true αν έγινε έξοδος μέσω πατήματος κουμπιού "ΟΚ" και false διαφορετικά
     */
    public boolean getDialogSuccessResult(){
        return result;
    }
}
