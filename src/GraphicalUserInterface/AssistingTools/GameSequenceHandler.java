package GraphicalUserInterface.AssistingTools;

import internals.player.PlayerController;
import internals.question.Question;
import internals.round.*;
import internals.round.Round;
import internals.round.RoundController;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GameSequenceHandler {
    private final RoundController roundController;
    private int numberOfRounds;
    private final String[] selectedPlayerNames;
    private PlayerController playerController;
    private final ActionListener returnToMenuButtonListener;

    private final JPanel basePanel;

    private QuestionPanel currentQuestionPanel;
    private Round currentRound;
    private Question currentQuestion;
    private int currentQuestionNumber;
    private int currentRoundNumber;

    private boolean[] playerHasAnswered;
    private int[] playerGains;
    private int[] totalPlayerGains;


    public GameSequenceHandler(JPanel basePanel, ActionListener returnToMenuButtonListener, RoundController roundController, PlayerController playerController, int numberOfRounds, String[] selectedPlayerNames) {
        this.basePanel = basePanel;

        this.roundController = roundController;
        this.playerController = playerController;
        this.numberOfRounds = numberOfRounds;
        this.selectedPlayerNames = selectedPlayerNames;
        this.returnToMenuButtonListener = returnToMenuButtonListener;

        currentRound = null;
        currentQuestion = null;

        this.playerHasAnswered = new boolean[selectedPlayerNames.length];
        this.playerGains = new int[selectedPlayerNames.length];
        for (int i = 0; i < selectedPlayerNames.length; i++) {
            playerGains[i] = 0;
        }

        this.totalPlayerGains = new int[selectedPlayerNames.length];
        for (int i = 0; i < selectedPlayerNames.length; i++) {
            totalPlayerGains[i] = 0;
        }

        basePanel.addKeyListener(new PressResponder());
    }

    public void begin(){
        proceedToNextQuestion();
    }

    private boolean proceedToNextRound(){
        for (int i = 0; i < selectedPlayerNames.length; i++) {
            totalPlayerGains[i] += playerGains[i];
            playerGains[i] = 0;
        }

        if (numberOfRounds-- <= 0){ // Ο αριθμός των γύρων τελείωσε
            gameFinishedMenu();
            return false;
        }

        currentRound = roundController.getRandomRoundType();

        return true;
    }

    private void gameFinishedMenu() {
        JPanel gameFinishedScreen = new JPanel(new BorderLayout());

        gameFinishedScreen.add(StaticTools.wrapInFlowLayout(new JLabel("Τέλος παιχνιδιού")));

        JPanel midPanel = new JPanel(new GridLayout(2, 1)); gameFinishedScreen.add(midPanel);

        JPanel statsPanel = new JPanel(new GridLayout(0, 2));
        statsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Αποτελέσματα παιχνιδιού"));
        midPanel.add(statsPanel);

        statsPanel.add(StaticTools.wrapInFlowLayout(new JLabel("Όνομα παίκτη")));
        statsPanel.add(StaticTools.wrapInFlowLayout(new JLabel("Σκορ παίκτη")));

        for (int i = 0; i < selectedPlayerNames.length; i++){
            JTextField roundScoreGained = new JTextField(String.valueOf(totalPlayerGains[i]));
            roundScoreGained.setEnabled(false);

            statsPanel.add(new JLabel(selectedPlayerNames[i]));
            statsPanel.add(roundScoreGained);
        }

        JButton returnToMenuButton = new JButton("Επιστροφή στο μενού");
        returnToMenuButton.addActionListener(returnToMenuButtonListener);
        midPanel.add(StaticTools.wrapInFlowLayout(returnToMenuButton));

        basePanel.removeAll();
        switchPanelTo(gameFinishedScreen);
    }

    private QuestionPanel nextQuestionPanel(){
        for (int i = 0; i < selectedPlayerNames.length; i++){
            playerHasAnswered[i] = false;
        }
        currentRound.proceed();

        if (currentRound.isOver()) return null;

        return QuestionPanel.constructQuestionPanel(new Question(currentRound.getQuestion(), currentRound.getQuestionAnswers(), currentRound.getRightQuestionAnswer(), currentRound.getQuestionCategory()), selectedPlayerNames);
    }

    private int playerAnswer(String playerName, String givenAnswer){
        int gains = 0;

        // Εκτέλεση της απάντησης και υπολογισμός βαθμολογίας που (πιθανώς) κερδίζεται
        if (currentRound instanceof RightAnswer){
            gains = ((RightAnswer)currentRound).answerQuestion(givenAnswer);
        } else if (currentRound instanceof Bet){
            gains = ((Bet)currentRound).answerQuestion(givenAnswer, playerName);
        }

        playerController.playerCalculateGain(playerName, gains); // Αποθήκευση βαθμολογίας

        return gains;
    }

    private void proceedToNextQuestion(){
        if (currentRound != null){
            currentQuestionPanel = this.nextQuestionPanel();
        }

        if (currentRound == null || currentQuestionPanel == null){ // Τέλος γύρου ή πρώτος γύρος, πρέπει να εισαχθεί νέος γύρος.
            boolean gameContinues = proceedToNextRound();
            if (!gameContinues){ // Τέλος παιχνιδιού
                return;
            }

            currentQuestionPanel = this.nextQuestionPanel(); // TODO: null check

            switchPanelTo(new RoundPrefacePanel(currentRound, new ActionListener() { // Εμφάνιση οδηγιών γύρου
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    basePanel.removeAll();
                    basePanel.add(new JPanel());
                    basePanel.revalidate();
                    basePanel.repaint();

                    if (nextQuestionInitialisation(currentRound, currentQuestion)){
                        switchPanelTo(currentQuestionPanel);
                    } else {
                        gameFinishedMenu();
                    }
                }
            }));
        } else {
            switchPanelTo(currentQuestionPanel);
        }



    }

    /**
     * Εκτελεί "προετοιμασίες" απαραίτητες για την επόμενη ερώτηση του γύρου που δίνεται ως όρισμα και επιστρέφει την επιτυχία της προετοιμασίας.
     * Η επιτυχία εξαρτάται απο τα δεδομένα που παρέχει ο χρήστης.
     *
     * @param round ο γύρος για τον οποίο πρέπει να γίνει προετοιμασία
     * @param question η ερώτηση πριν απο την οποία γίνεται προετοιμασία
     * @return την επιτυχία της αρχικοποίησης.
     */
    private boolean nextQuestionInitialisation(Round round, Question question) {

        if (round instanceof RightAnswer){
            return true; // Καμία αρχικοποίηση δεν είναι απαραίτητη
        } else {
            return (new BetInitialisationDialog(basePanel, (Bet) round)).getDialogSuccessResult();
        }
    }

    private class BetInitialisationDialog extends JDialog{
        private boolean result;

        private BetInitialisationDialog(JPanel owner, Bet round) {
            super((JFrame) owner.getTopLevelAncestor(), "Στοιχήματα", true);

            setLocationRelativeTo(owner);
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

            JPanel mainPanel = new JPanel(new GridLayout(0, 1));
            this.setContentPane(mainPanel);

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

            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    if (JOptionPane.showConfirmDialog(getDialogReference(), "Είσαι σίγουρος ότι θέλεις να γυρίσεις στο μενού;", "Έξοδος", JOptionPane.YES_NO_OPTION) == 0) {
                        disposeSelf(false); // Ο χρήστης επιλέγει να μην συνεχίσει με ποντάρισμα και πρέπει να γίνει έξοδος.
                    }
                }
            });

            this.pack();
            this.setSize(300, this.getSize().height);
            this.setVisible(true);
        }

        private void disposeSelf(boolean dialogueSuccess){
            result = dialogueSuccess;
            this.dispose();
        }

        private BetInitialisationDialog getDialogReference(){
            return this;
        }

        public boolean getDialogSuccessResult(){
            return result;
        }
    }

    private void switchPanelTo(JPanel newPanel){
        basePanel.removeAll();
        basePanel.add(newPanel);
        basePanel.revalidate();
        basePanel.repaint();
    }

    private class PressResponder extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            char pressed = Character.toLowerCase(e.getKeyChar());
            int correspondingInt = 0;

            if ("1234".indexOf(pressed) != -1) {
                switch (pressed) {
                    case '1':
                        correspondingInt = 0;
                        break;
                    case '2':
                        correspondingInt = 1;
                        break;
                    case '3':
                        correspondingInt = 2;
                        break;
                    case '4':
                        correspondingInt = 3;
                        break;
                }
                if (!playerHasAnswered[0]) {
                    playerHasAnswered[0] = true;
                    playerGains[0] = playerAnswer(selectedPlayerNames[0], currentRound.getQuestionAnswers()[correspondingInt]);
                    currentQuestionPanel.markAnswer(selectedPlayerNames[0], correspondingInt + 1);
                }

            } else if (selectedPlayerNames.length == 2) {
                if ("vbnmωβνμ".indexOf(pressed) != -1) {
                    switch (pressed) {
                        case 'v':
                        case 'ω':
                            correspondingInt = 0;
                        case 'b':
                        case 'β':
                            correspondingInt = 1;
                        case 'n':
                        case 'ν':
                            correspondingInt = 2;
                        case 'm':
                        case 'μ':
                            correspondingInt = 3;
                    }
                    if (!playerHasAnswered[1]) {
                        playerHasAnswered[1] = true;
                        playerGains[1] = playerAnswer(selectedPlayerNames[1], currentRound.getQuestionAnswers()[correspondingInt]);
                        currentQuestionPanel.markAnswer(selectedPlayerNames[1], correspondingInt + 1);
                    }

                }
            }

            boolean everyoneAnswered = true;
            for (boolean b : playerHasAnswered){ // Έλεγχος για το αν απάντησαν όλοι
                if (!b){
                    everyoneAnswered = false;
                    break;
                }
            }
            if (everyoneAnswered){
                proceedToNextQuestion();
            }

        }
    }

}
