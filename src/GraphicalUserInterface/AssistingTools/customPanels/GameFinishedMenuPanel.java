package GraphicalUserInterface.AssistingTools.customPanels;

import GraphicalUserInterface.AssistingTools.StaticTools;
import internals.player.PlayerController;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Η κλάση GameFinishedMenuPanel επεκτείνει (με σύνθεση) ένα JPanel που αντιστοιχεί στην οθόνη τέλους του παιχνιδιού
 * του κεντρικού παραθύρου (περιέχει κουμπί που επιστρέφει στο μενού εκκίνησης παιχνιδιού).
 *
 * Η οθόνη τέλους παιχνιδιού περιέχει τα αποτελέσματα του παιχνιδιού δηλαδή το τελικό (άθροισμα απο κάθε γύρο) σκορ
 * κάθε παίχτη και (αν είναι παιχνίδι πολλών παιχτών) τον νικητή του παιχνιδιού (μπορεί να είναι και ισοπαλία όπου
 * θεωρούνται και οι δύο παίχτες νικητές).
 */
public class GameFinishedMenuPanel {
    private JPanel internalPanel; // Το JPanel που "επεκτείνεται" μέσω σύνθεσης

    public GameFinishedMenuPanel(String[] selectedPlayerNames, int[] totalPlayerGains, PlayerController playerController, ActionListener returnToMenuButtonListener){
        internalPanel = new JPanel(new BorderLayout());

        internalPanel.add(StaticTools.wrapInFlowLayout(new JLabel("Τέλος παιχνιδιού")), BorderLayout.PAGE_START);

        JPanel statsPanel = new JPanel(new BorderLayout(20, 20)); internalPanel.add(statsPanel, BorderLayout.CENTER);
        statsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Αποτελέσματα παιχνιδιού"));

        JPanel playerStatsPanelPanel = new JPanel(new BorderLayout()); statsPanel.add(playerStatsPanelPanel, BorderLayout.CENTER);
        playerStatsPanelPanel.add(new JSeparator(), BorderLayout.PAGE_END);

        JPanel playerStatsPanel = new JPanel(new GridLayout(0, 2)); playerStatsPanelPanel.add(playerStatsPanel, BorderLayout.CENTER);

        JPanel statsPlayerNamesPanel = new JPanel(new GridLayout(0, 1)); playerStatsPanel.add(statsPlayerNamesPanel);
        statsPlayerNamesPanel.setBorder(BorderFactory.createTitledBorder("Όνομα παίκτη"));

        JPanel statsPlayerScorePanel = new JPanel(new GridLayout(0, 1)); playerStatsPanel.add(statsPlayerScorePanel);
        statsPlayerScorePanel.setBorder(BorderFactory.createTitledBorder("Σκορ παίκτη"));


        for (int i = 0; i < selectedPlayerNames.length; i++){
            JPanel roundScoreGained = new JPanel(new BorderLayout());
            roundScoreGained.add(StaticTools.wrapInGridBagLayout(new JLabel(String.valueOf(totalPlayerGains[i]))), BorderLayout.CENTER);

            JPanel selectedPlayerName = new JPanel(new BorderLayout());
            selectedPlayerName.add(StaticTools.wrapInGridBagLayout(new JLabel(selectedPlayerNames[i])), BorderLayout.CENTER);
            if (i != selectedPlayerNames.length - 1){
                roundScoreGained.add(new JSeparator(), BorderLayout.PAGE_END);
                selectedPlayerName.add(new JSeparator(), BorderLayout.PAGE_END);
            }


            statsPlayerNamesPanel.add(selectedPlayerName);
            statsPlayerScorePanel.add(roundScoreGained);
        }


        if (selectedPlayerNames.length != 1){ // Έλεγχος για το αν είναι παιχνίδι πολλών παιχτών
            int maxPosition = 0;
            for (int i = 1; i < selectedPlayerNames.length; i++){ // Εύρεση παίχτη με μέγιστο άθροισμα σκορ όλων των γύρων
                if (totalPlayerGains[i] > totalPlayerGains[maxPosition]){
                    maxPosition = i;
                }
            }

            JPanel gameWinners = new JPanel(new GridLayout(0, 1));

            ArrayList<String> playersWithMaxPoints = new ArrayList<>();

            for (int i = 0; i < selectedPlayerNames.length; i++){ // Βρίσκω ποιοι παίκτες έχουν μέγιστο βαθμό
                if (totalPlayerGains[i] == totalPlayerGains[maxPosition]){
                    playersWithMaxPoints.add(selectedPlayerNames[i]);

                    playerController.countMultiplayerWins(selectedPlayerNames[i]); // "Σημειώνω" την νίκη του παίχτη με το μέγιστο σκορ όλων των γύρων

                    gameWinners.add(StaticTools.wrapInGridBagLayout(new JLabel(selectedPlayerNames[i])));
                }
            }

            if (selectedPlayerNames.length == playersWithMaxPoints.size()){ // Ισοπαλία
                gameWinners.removeAll(); // Είναι ισοπαλία οπότε δεν χρειάζεται να εμφανίζονται όλα τα ονόματα
                gameWinners.add(StaticTools.wrapInGridBagLayout(new JLabel("Ισοπαλία")));
            }


            JPanel gameWinnersPanel = new JPanel(new GridBagLayout()); statsPanel.add(gameWinnersPanel, BorderLayout.PAGE_END);
            gameWinnersPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Νικητής παιχνιδιού: ")); // Εμφάνιση νικητή παιχνιδιού
            gameWinnersPanel.add(gameWinners);
        }

        JButton returnToMenuButton = new JButton("Επιστροφή στο μενού"); // Κουμπί επιστροφής
        returnToMenuButton.addActionListener(returnToMenuButtonListener);
        internalPanel.add(StaticTools.wrapInFlowLayout(returnToMenuButton), BorderLayout.PAGE_END);
    }

    /**
     * Επιστρέφει το JPanel που αντιστοιχεί στην κλάση
     * @return το JPanel που αντιστοιχεί στην οθόνη τέλους του παιχνιδιού
     */
    public JPanel getPanel(){
        return internalPanel;
    }

}
