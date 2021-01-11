package GraphicalUserInterface.AssistingTools;

import GraphicalUserInterface.AssistingTools.customDialogs.BetInitialisationDialog;
import GraphicalUserInterface.AssistingTools.customPanels.GameFinishedMenuPanel;
import GraphicalUserInterface.AssistingTools.customPanels.QuestionPanel;
import GraphicalUserInterface.AssistingTools.customPanels.RoundPrefacePanel;
import internals.player.PlayerController;
import internals.round.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Η κλάση GameSequenceHandler αποτελεί την μονάδα που χειρίζεται το "λειτουργικό" μέρος του παιχνιδιού.
 * Δηλαδή αρμοδιότητα της είναι να εμφανίζει τις ερωτήσεις και δέχεται απαντήσεις και να εμφανίζει σχετικά αποτελέσματα.
 */
public class GameSequenceHandler {
    private final RoundController roundController; // Χρησιμοποιούμε το playerController για διαχείριση των δεδομένων των παιχτών
    private int numberOfRounds; // Αριθμός γύρων που θα παιχτούν
    private final String[] selectedPlayerNames; // Ονόματα παιχτών που έχουν επιλεγεί να παίξουν
    private PlayerController playerController; // Χρησιμοποιούμε το roundController για τυχαία επιλογή των τύπων γύρων και απόκτηση δεδομένων και διαδικασιών που υλοποιούν αντικείμενα τύπου Round
    private final ActionListener returnToMenuButtonListener; // Ο ActionListener που πρέπει να ενεργοποιηθεί απο το στοιχείο το οποίο επιστρέφει σε προηγούμενο μενού.

    private final JPanel basePanel; // Το "βασικό" JPanel πάνω στο οποίο διαδραματίζεται το παιχνίδι

    private QuestionPanel currentQuestionPanel; // Το JPanel της τρέχουσας ερώτησης
    private Round currentRound; // Ο τρέχον γύρος

    private boolean[] playerHasAnswered; // Ο πίνακας που αποθηκεύει την κατάστασης απάντησης (αν έχει απαντήσει ή όχι)
    private int[] playerGains;  // Ο πίνακας που αποθηκεύει τον αριθμό των πόντων που έχει συγκεντρώσει ο παίχτης στην διάρκεια ενός γύρου
    private int[] totalPlayerGains; // Ο πίνακας που αποθηκεύει τον αριθμό των πόντων που έχει συγκεντρώσει ο παίχτης στην διάρκεια του παιχνιδιού (όλων των γύρων)

    private JDialog timeRemainingDialog; // Ο διάλογος χρονομέτρου

    /**
     * Ο τυπικός κατασκευαστής που αρχικοποιεί τα δεδομένα της κλάσης.
     * Για να ξεκινήσει το παιχνίδι αρκεί να εκκινηθεί με πρώτη ερώτηση και για να γίνει αυτό αρκεί να κληθεί μια φόρα η μέθοδος proceedToNextQuestion.
     *
     * @param basePanel το JPanel πάνω στο οποίο διαδραματίζεται το παιχνίδι
     * @param returnToMenuButtonListener ο ActionListener που πρέπει να ενεργοποιηθεί απο το στοιχείο το οποίο επιστρέφει σε προηγούμενο μενού
     * @param roundController ο RoundController του παιχνιδιού
     * @param playerController ο PlayerController του παιχνιδιού
     * @param numberOfRounds ο αριθμός γύρων που θα παιχτούν
     * @param selectedPlayerNames τα ονόματα των παιχτών που θα παίξουν
     */
    public GameSequenceHandler(JPanel basePanel, ActionListener returnToMenuButtonListener, RoundController roundController, PlayerController playerController, int numberOfRounds, String[] selectedPlayerNames) {
        this.basePanel = basePanel;

        this.basePanel.setFocusable(true);

        this.roundController = roundController;
        this.playerController = playerController;
        this.numberOfRounds = numberOfRounds;
        this.selectedPlayerNames = selectedPlayerNames;
        this.returnToMenuButtonListener = returnToMenuButtonListener;

        currentRound = null;

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

    /**
     * Θέτει ως εμφανιζόμενο αντικείμενο στο JFrame του παιχνιδιού στο κεντρικό παράθυρο την οθόνη τέλους του παιχνιδιού
     * (που περιέχει κουμπί που επιστρέφει στο μενού εκκίνησης παιχνιδιού).
     *
     * Η οθόνη τέλους παιχνιδιού περιέχει τα αποτελέσματα του παιχνιδιού δηλαδή το τελικό (άθροισμα απο κάθε γύρο) σκορ
     * κάθε παίχτη και (αν είναι παιχνίδι πολλών παιχτών) τον νικητή του παιχνιδιού (μπορεί να είναι και ισοπαλία όπου
     * θεωρούνται και οι δύο παίχτες νικητές).
     */
    public void gameFinishedMenu() {
        if (currentRound instanceof StopChronometer){
            timeRemainingDialog.dispose(); // Αν έχω γύρο σταμάτησε το χρονόμετρο, κλείνω τον διάλογο χρονομέτρου.
        }

        for (KeyListener kl : basePanel.getKeyListeners()){
            basePanel.removeKeyListener(kl);
        }

        JPanel endGamePanel = (new GameFinishedMenuPanel(selectedPlayerNames, totalPlayerGains, playerController, returnToMenuButtonListener)).getPanel();

        StaticTools.switchPanelTo(basePanel, endGamePanel);
    }

    /**
     * Υλοποιεί την λειτουργία της απάντησης του παίχτη που εξαρτάται ανάλογα με τον γύρο και επιστρέφει τον αριθμό
     * των πόντων που απέκτησε απαντώντας.
     *
     * @param playerName το όνομα του παίχτη που απάντησε
     * @param givenAnswer η απάντηση που επέλεξε ο παίχτης
     * @return ο αριθμός των πόντων που απέκτησε ο παίκτης απο την απάντηση που έδωσε
     */
    public int playerAnswer(String playerName, String givenAnswer){
        // Εκτέλεση της απάντησης και υπολογισμός βαθμολογίας που (πιθανώς) κερδίζεται
        int gains = StaticTools.calculatePlayerAnswerGain(playerName, givenAnswer, currentRound);

        if (selectedPlayerNames.length == 1){ // Ελέγχω αν είναι παιχνίδι ενός παίχτη
            playerController.playerCalculateGain(playerName, gains); // Αποθήκευση βαθμολογίας σε παιχνίδι ενός παίχτη
        }

        return gains;
    }

    /**
     * "Προχωράει" το παιχνίδι στην επόμενη ερώτηση του γύρου.
     * Άν έχουν "τελειώσει" οι ερωτήσεις του γύρου, προχωράει στον επόμενο γύρο.
     * Άν έχουν τελειώσει και οι γύροι μεταβαίνει το παιχνίδι στην εμφάνιση την "οθόνης" τέλους παιχνιδιού.
     */
    public void proceedToNextQuestion(){
        if (currentRound != null){ // Έλεγχος για το αν είναι ο πρώτος γύρος
            for (int i = 0; i < selectedPlayerNames.length; i++){ // Αρχικοποίηση της κατάστασης απαντήσεων
                playerHasAnswered[i] = false;
            }
            currentRound.proceed(); // Μετάβαση στην πρώτη ερώτηση

            if (!currentRound.isOver()) { // Υπάρχει πρώτος γύρος
                currentQuestionPanel = QuestionPanel.constructQuestionPanel(currentRound.getQuestionWithRandomizedAnswers(), selectedPlayerNames);
            }
        }

        if (currentRound == null || currentRound.isOver()) { // Τέλος γύρου ή πρώτος γύρος, πρέπει να εισαχθεί νέος γύρος.
            /* "Φόρτωση" επόμενου γύρου */
            for (int i = 0; i < selectedPlayerNames.length; i++) { // Αρχικοποίηση / Επεξεργασία των πινάκων αριθμών πόντων
                totalPlayerGains[i] += playerGains[i];
                playerGains[i] = 0;
            }

            if (numberOfRounds <= 0){ // Ο αριθμός των γύρων τελείωσε
                gameFinishedMenu(); // Μετάβαση στην "οθόνη" τερματισμού παιχνιδιού
                return;
            }

            currentRound = roundController.getRandomRoundType(); // Μετάβαση στον επόμενο γύρο
            numberOfRounds--; // "Μέτρηση" αριθμού γύρων που έχουν παιχτεί


            for (int i = 0; i < selectedPlayerNames.length; i++){ // Αρχικοποίηση της κατάστασης απαντήσεων
                playerHasAnswered[i] = false;
            }
            /* Μετάβαση στην επόμενη ερώτηση */
            currentRound.proceed();
            currentQuestionPanel = QuestionPanel.constructQuestionPanel(currentRound.getQuestionWithRandomizedAnswers(), selectedPlayerNames);

            StaticTools.switchPanelTo(basePanel, new RoundPrefacePanel(currentRound, new ActionListener() { // Εμφάνιση οδηγιών γύρου
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    /* Προετοιμασία και εμφάνιση νέας ερώτησης */
                    basePanel.removeAll();
                    basePanel.add(new JPanel());
                    basePanel.revalidate();
                    basePanel.repaint();

                    if (nextQuestionInitialisation()){ // Η προετοιμασία τερμάτισε με επιτυχία
                        StaticTools.switchPanelTo(basePanel, currentQuestionPanel.getPanel());
                    } else {
                        gameFinishedMenu();
                    }
                }
            }).getPanel());
        } else {
            for (int i = 0; i < selectedPlayerNames.length; i++) { // Αρχικοποίηση / Επεξεργασία των πινάκων αριθμών πόντων
                totalPlayerGains[i] += playerGains[i];
                playerGains[i] = 0;
            }


            /* Προετοιμασία και εμφάνιση νέας ερώτησης */
            basePanel.removeAll();
            basePanel.add(new JPanel());
            basePanel.revalidate();
            basePanel.repaint();

            if (nextQuestionInitialisation()){  // Η προετοιμασία τερμάτισε με επιτυχία
                StaticTools.switchPanelTo(basePanel, currentQuestionPanel.getPanel());
            } else {
                gameFinishedMenu(); // Υπάρχει νέα ερώτηση
            }
        }
    }

    /**
     * Εκτελεί "προετοιμασίες" απαραίτητες για την επόμενη ερώτηση του γύρου που δίνεται ως όρισμα και επιστρέφει την επιτυχία της προετοιμασίας.
     * Η επιτυχία εξαρτάται απο τα δεδομένα που παρέχει ο χρήστης.
     *
     * @return την επιτυχία της αρχικοποίησης.
     */
    private boolean nextQuestionInitialisation() {
        if (currentRound instanceof RightAnswer){ // Περίπτωση γύρου RightAnswer
            return true; // Καμία αρχικοποίηση δεν είναι απαραίτητη
        } else if (currentRound instanceof Bet){ // Περίπτωση γύρου Bet
            return (new BetInitialisationDialog(basePanel, (Bet) currentRound, selectedPlayerNames)).getDialogSuccessResult();
        } else if (currentRound instanceof StopChronometer){ // Περίπτωση γύρου StopChronometer
            JOptionPane.showConfirmDialog(basePanel, "Πάτησε ΟΚ για να αρχίσει ο γύρος", "Εκκίνηση γύρου", JOptionPane.DEFAULT_OPTION);

            JFrame baseFrame = (JFrame) basePanel.getTopLevelAncestor();

            timeRemainingDialog = new JDialog(baseFrame, "Απομείναντας χρόνος", false);
            timeRemainingDialog.setFocusable(false);
            timeRemainingDialog.setFocusableWindowState(false);
            timeRemainingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            timeRemainingDialog.setSize(200, 200);
            timeRemainingDialog.setLocation(baseFrame.getX()+baseFrame.getWidth(), baseFrame.getY() + 100);


            JLabel time = new JLabel("00000");
            timeRemainingDialog.setLayout(new GridBagLayout());
            timeRemainingDialog.add(time);

            ((StopChronometer) currentRound).beginTimer(new ActionListener() {
                JLabel timeLabel = time;
                StopChronometer timeController = (StopChronometer) currentRound;

                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    timeLabel.setText(String.valueOf(timeController.getTime()));
                }
            });

            timeRemainingDialog.setVisible(true);
            //timeRemainingDialog.toBack();
            return true;
        } else if (currentRound instanceof QuickAnswer){ // Περίπτωση γύρου QuickAnswer
            return true; // Καμία αρχικοποίηση δεν είναι απαραίτητη
        } else { // Περίπτωση γύρου Thermometer
            return true; // Καμία αρχικοποίηση δεν είναι απαραίτητη
        }
    }

    /**
     * Ο KeyAdapter που ανιχνεύει και αντιδρά στο πάτημα των κουμπιών που αποτελεί απάντηση των παιχτών (ή παίχτη).
     * Υποστηρίζει παιχνίδι μέχρι δύο παιχτών.
     * Οι απαντήσεις του πρώτου παίχτη αντιστοιχούν στα πλήκτρα 1, 2, 3 και 4 για τις αντίστοιχες απαντήσεις.
     * Οι απαντήσεις του δεύτερου παίχτη αντιστοιχούν στα πλήκτρα v, b, n και m (ή ω, β, ν και μ σε ελληνικό πληκτρολόγιο) για τις απαντήσεις 1, 2, 3 και 4 αντίστοιχα
     * μη διακρίνοντας πεζά / κεφαλαία.
     */
    public class PressResponder extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            char pressed = Character.toLowerCase(e.getKeyChar()); // Ανεξαρτησία απο πεζά / κεφαλαία
            int correspondingInt = 0;

            if ("1234".indexOf(pressed) != -1) { // Απάντησε ο πρώτος παίχτης
                switch (pressed) { // Αντιστοίχηση σε απάντηση
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
                if (!playerHasAnswered[0]) { // Έχει απαντήσει ξανά, αγνοώ την νέα απάντηση
                    playerHasAnswered[0] = true; // "Σημειώνω" το γεγονός ότι απάντησε
                    playerGains[0] = playerAnswer(selectedPlayerNames[0], currentRound.getQuestionAnswers()[correspondingInt]); // Εκτελώ την απάντηση
                    currentQuestionPanel.markAnswer(selectedPlayerNames[0], correspondingInt + 1); // Σημειώνω στην οθόνη την απάντηση
                }

            } else if (selectedPlayerNames.length == 2) { // Απάντησε ο δεύτερος παίχτης
                /* Ομοίως με τον πρώτο παίχτη γίνεται ανάλογη διαδικασία */
                if ("vbnmωβνμ".indexOf(pressed) != -1) {
                    switch (pressed) {
                        case 'v':
                        case 'ω':
                            correspondingInt = 0; break;
                        case 'b':
                        case 'β':
                            correspondingInt = 1; break;
                        case 'n':
                        case 'ν':
                            correspondingInt = 2; break;
                        case 'm':
                        case 'μ':
                            correspondingInt = 3; break;
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
                if (currentRound instanceof StopChronometer){
                    timeRemainingDialog.dispose(); // Αν έχω γύρο σταμάτησε το χρονόμετρο, κλείνω τον διάλογο χρονομέτρου.
                }

                JOptionPane.showMessageDialog(basePanel, "Η σωστή απάντηση ήταν: " + currentRound.getRightQuestionAnswer(), "Σωστή απάντηση", JOptionPane.INFORMATION_MESSAGE);
                proceedToNextQuestion();
            }

        }
    }

}
