package GraphicalUserInterface.AssistingTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Η κλάση GameSelectionDialog υλοποιεί ένα παράθυρο διαλόγου που ζητάει στον χρήστη να κάνει επιλογές σχετικά με παραμέτρους του παιχνιδιού, πριν αυτό ξεκινήσει.
 * Οι ρυθμίσεις αφορούν τον αριθμό παιχτών, αριθμό γύρων και αριθμό ερωτήσεων ανα γύρο.
 */
public class GameSelectionDialog extends JDialog {
    private int[] settings;

    private WindowAdapter windowClosingReaction;

    private final SpinnerNumberModel playerNumberModel;
    private final SpinnerNumberModel roundNumberModel;
    private final SpinnerNumberModel questionsPerRoundNumberModel;

    private GameSelectionDialog(Frame owner, int maxPlayers, int minPlayers, int maxRounds, int minRounds, int maxQuestionsPerRound, int minQuestionsPerRound){
        super(owner, "Ρυθμίσεις παιχνιδιού", true);

        settings = null;

        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(owner);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                settings = null;
                disposeSelf();
            }
        });

        JPanel mainPanel = new JPanel(new GridLayout(0, 1));
        this.setContentPane(mainPanel);

        mainPanel.add(StaticTools.wrapInFlowLayout(new JLabel("Επίλεξε τις ρυθμίσεις για το παιχνίδι:")));

        JPanel playerNumberPanel = new JPanel(new BorderLayout()); mainPanel.add(playerNumberPanel);
        playerNumberPanel.setBorder(BorderFactory.createTitledBorder("Αριθμός παιχτών"));
        playerNumberModel = new SpinnerNumberModel(minPlayers, minPlayers, maxPlayers, 1);
        JSpinner playerNumberSpinner = new JSpinner(playerNumberModel);
        playerNumberPanel.add(playerNumberSpinner);

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

        JPanel buttonsPanel = new JPanel(new FlowLayout()); mainPanel.add(buttonsPanel);

        JButton jButton = new JButton("OK");
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                settings = new int[3];
                settings[0] = playerNumberModel.getNumber().intValue();
                settings[1] = roundNumberModel.getNumber().intValue();
                settings[2] = questionsPerRoundNumberModel.getNumber().intValue();
                disposeSelf();
            }
        });
        buttonsPanel.add(jButton);

        jButton = new JButton("Cancel");
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                settings = null;
                disposeSelf();
            }
        });
        buttonsPanel.add(jButton);

        this.setSize(590, 220);
        this.setVisible(true);
    }

    private void disposeSelf(){
        this.dispose();
    }

    public int[] getSettings(){
        return settings;
    }

    public static int[] showGameSettingSelection(Frame owner, int maxPlayers, int minPlayers, int maxRounds, int minRounds, int maxQuestionsPerRound, int minQuestionsPerRound){
        GameSelectionDialog gsd = new GameSelectionDialog(owner, maxPlayers, minPlayers, maxRounds, minRounds, maxQuestionsPerRound, minQuestionsPerRound);

        return gsd.settings;
    }
}
