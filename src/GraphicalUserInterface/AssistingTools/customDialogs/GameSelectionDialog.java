package GraphicalUserInterface.AssistingTools.customDialogs;

import GraphicalUserInterface.AssistingTools.StaticTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Η κλάση GameSelectionDialog υλοποιεί ένα παράθυρο διαλόγου (μέσω σύνθεσης) που ζητάει στον χρήστη να κάνει επιλογές σχετικά με παραμέτρους του παιχνιδιού, πριν αυτό ξεκινήσει.
 * Οι ρυθμίσεις αφορούν τον αριθμό παιχτών, αριθμό γύρων και αριθμό ερωτήσεων ανα γύρο.
 *
 * @author Ioannis Baraklilis
 * @author Alexandros Tsingos
 *
 * @version 2020.12.30
 */
public class GameSelectionDialog{
    private int[] settings; // Πίνακας με ρυθμίσεις που έχει επιλέξει ο χρήστης

    private JDialog baseDialog; // Το JDialog που "επεκτείνεται" μέσω σύνθεσης

    private final SpinnerNumberModel playerNumberModel; // Αριθμός παιχτών
    private final SpinnerNumberModel roundNumberModel; // Αριθμός γύρων
    private final SpinnerNumberModel questionsPerRoundNumberModel; // Αριθμός ερωτήσεων ανα γύρο

    /**
     * Ο τυπικός κατασκευαστής της κλάσης που κατασκευάζει και εμφανίζει το παράθυρο διαλόγου.
     * @param owner το Frame το οποίο προκάλεσε την δημιουργία του διαλόγου
     * @param maxPlayers ο μέγιστος επιτρεπόμενος αριθμός παιχτών
     * @param minPlayers ο ελάχιστος επιτρεπόμενος αριθμός παιχτών
     * @param maxRounds ο μέγιστος επιτρεπόμενος αριθμός γύρων
     * @param minRounds ο ελάχιστος επιτρεπόμενος αριθμός γύρων
     * @param maxQuestionsPerRound ο μέγιστος επιτρεπόμενος αριθμός ερωτήσεων ανα γύρο
     * @param minQuestionsPerRound ο ελάχιστος επιτρεπόμενος αριθμός ερωτήσεων ανα γύρο
     */
    public GameSelectionDialog(Frame owner, int maxPlayers, int minPlayers, int maxRounds, int minRounds, int maxQuestionsPerRound, int minQuestionsPerRound){
        baseDialog = new JDialog(owner, "Ρυθμίσεις παιχνιδιού", true);

        settings = null;

        baseDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        baseDialog.addWindowListener(new WindowAdapter() { // Θέλουμε να ενημερώνεται ο πίνακας setting σε περίπτωση κλεισίματος διαλόγου με τρόπο εκτός των κουμπιών
            @Override
            public void windowClosing(WindowEvent e) {
                settings = null;
                dispose();
            }
        });

        JPanel mainPanel = new JPanel(new GridLayout(0, 1));
        baseDialog.setContentPane(mainPanel);

        /* Τμήμα με οδηγίες */
        mainPanel.add(StaticTools.wrapInFlowLayout(new JLabel("Επίλεξε τις ρυθμίσεις για το παιχνίδι:")));

        /* Τμήμα με αριθμό παιχτών */
        JPanel playerNumberPanel = new JPanel(new BorderLayout()); mainPanel.add(playerNumberPanel);
        playerNumberPanel.setBorder(BorderFactory.createTitledBorder("Αριθμός παιχτών"));
        playerNumberModel = new SpinnerNumberModel(minPlayers, minPlayers, maxPlayers, 1);
        JSpinner playerNumberSpinner = new JSpinner(playerNumberModel);
        playerNumberPanel.add(playerNumberSpinner);

        /* Τμήμα με αριθμό γύρων και ερωτήσεων ανα γύρο */
        JPanel roundNumbers = new JPanel(new GridLayout(1, 2)); mainPanel.add(roundNumbers);

        JPanel tempPanel = new JPanel(new BorderLayout()); roundNumbers.add(tempPanel);
        tempPanel.setBorder(BorderFactory.createTitledBorder("Αριθμός γύρων"));
        roundNumberModel = new SpinnerNumberModel(minRounds, minRounds, maxRounds, 1);
        JSpinner jSpinner = new JSpinner(roundNumberModel);
        tempPanel.add(jSpinner);

        tempPanel = new JPanel(new BorderLayout()); roundNumbers.add(tempPanel);
        tempPanel.setBorder(BorderFactory.createTitledBorder("Αριθμός ερωτήσεων ανα γύρο"));
        questionsPerRoundNumberModel = new SpinnerNumberModel(minQuestionsPerRound, minQuestionsPerRound, maxQuestionsPerRound, 1);
        jSpinner = new JSpinner(questionsPerRoundNumberModel);
        tempPanel.add(jSpinner);

        /* Τμήμα με κουμπιά */
        JPanel buttonsPanel = new JPanel(new FlowLayout()); mainPanel.add(buttonsPanel);

        JButton jButton = new JButton("OK");
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                settings = new int[3];
                settings[0] = playerNumberModel.getNumber().intValue();
                settings[1] = roundNumberModel.getNumber().intValue();
                settings[2] = questionsPerRoundNumberModel.getNumber().intValue();
                dispose();
            }
        });
        buttonsPanel.add(jButton);

        jButton = new JButton("Cancel");
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                settings = null;
                dispose();
            }
        });
        buttonsPanel.add(jButton);

        /* Τμήμα με ρυθμίσεις παραθύρου διαλόγου */
        baseDialog.setSize(590, 220);
        baseDialog.setLocationRelativeTo(owner);
        baseDialog.setVisible(true);
    }

    /**
     * Καταστρέφει το παράθυρο διαλόγου.
     */
    public void dispose(){
        baseDialog.dispose();
    }

    /**
     * Επιστρέφει τις ρυθμίσεις που επέλεξε ο χρήστης πριν τον τερματισμό του διαλόγου.
     * Ο πίνακας ρυθμίσεων είναι ένας πίνακας απο int 3 θέσεων με: Τον αριθμό των παιχτών στην πρώτη θέση ([0]),
     * τον αριθμό γύρων στην δεύτερη θέση ([1]) και
     * τον αριθμό ερωτήσεων ανα γύρο στην τρίτη θέση ([2]).
     *
     * @return πίνακας με τις ρυθμίσεις που επέλεξε ο χρήστης πριν τον τερματισμό του διαλόγου ή null σε περίπτωση που δεν επέλεξε ρυθμίσεις και έκλεισε το παράθυρο
     */
    public int[] getSettings(){
        return settings;
    }

    /**
     * Εμφανίζει το παράθυρο διαλόγου καλώντας τον κατάλληλο κατασκευαστή και επιστρέφει τις ρυθμίσεις που επέλεξε ο χρήστης.
     * Ο πίνακας ρυθμίσεων είναι ένας πίνακας απο int 3 θέσεων με: Τον αριθμό των παιχτών στην πρώτη θέση ([0]),
     * τον αριθμό γύρων στην δεύτερη θέση ([1]) και
     * τον αριθμό ερωτήσεων ανα γύρο στην τρίτη θέση ([2]).
     *
     * @param owner το Frame το οποίο προκάλεσε την δημιουργία του διαλόγου
     * @param maxPlayers ο μέγιστος επιτρεπόμενος αριθμός παιχτών
     * @param minPlayers ο ελάχιστος επιτρεπόμενος αριθμός παιχτών
     * @param maxRounds ο μέγιστος επιτρεπόμενος αριθμός γύρων
     * @param minRounds ο ελάχιστος επιτρεπόμενος αριθμός γύρων
     * @param maxQuestionsPerRound ο μέγιστος επιτρεπόμενος αριθμός ερωτήσεων ανα γύρο
     * @param minQuestionsPerRound ο ελάχιστος επιτρεπόμενος αριθμός ερωτήσεων ανα γύρο
     *
     * @return πίνακας με τις ρυθμίσεις που επέλεξε ο χρήστης πριν τον τερματισμό του διαλόγου ή null σε περίπτωση που δεν επέλεξε ρυθμίσεις και έκλεισε το παράθυρο
     */
    public static int[] showGameSettingSelection(Frame owner, int maxPlayers, int minPlayers, int maxRounds, int minRounds, int maxQuestionsPerRound, int minQuestionsPerRound){
        GameSelectionDialog gsd = new GameSelectionDialog(owner, maxPlayers, minPlayers, maxRounds, minRounds, maxQuestionsPerRound, minQuestionsPerRound);

        return gsd.settings;
    }
}
