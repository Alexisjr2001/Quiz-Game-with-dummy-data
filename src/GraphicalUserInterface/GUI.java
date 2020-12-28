package GraphicalUserInterface;

import GraphicalUserInterface.AssistingTools.*;
import internals.player.PlayerController;
import internals.question.Question;
import internals.question.QuestionLibrary;
import internals.round.RightAnswer;
import internals.round.Round;
import internals.round.RoundController;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

/**
 * Η κλάση που μοντελοποιεί την αλληλεπίδραση της εφαρμογής με τον χρήστη (με διεπαφή γραφικού περιβάλλοντος).
 * Ουσιαστικά, διαχειρίζεται και χρησιμοποιεί αντικείμενα κλάσεων τύπου Controller και υλοποιεί την λογική του παιχνιδιού,
 * ενω χειρίζεται την είσοδο / έξοδο  απο / προς τον χρήστη μέσω παραθύρων.
 *
 * @author Ioannis Baraklilis
 * @author Alexandros Tsingos
 *
 * @version 2020.12.28
 */
public class GUI {
    // Πεδία που αφορούν την λογική του παιχνιδιού
    private PlayerController playerController; // Χρησιμοποιούμε το playerController για διαχείριση των δεδομένων των παιχτών
    private QuestionLibrary questionLibrary; // Χρησιμοποιούμε το questionLibrary για διαχείριση των δεδομένων των ερωτήσεων
    private RoundController roundController; // Χρησιμοποιούμε το roundController για τυχαία επιλογή των τύπων γύρων και απόκτηση δεδομένων και διαδικασιών που υλοποιούν αντικείμενα τύπου Round

    // Πεδία που αφορούν το γραφικό περιβάλλον του παιχνιδιού
    private JFrame mainWindow; // Το κεντρικό παράθυρο της εφαρμογής
    private CardLayout mainWindowLayout; // Το layout κυρίου παραθύρου (για αλλαγή εύκολη αργότερη εναλλαγή των panel)

    private JPanel mainMenuPanel; // Το panel του κυρίου μενού
    private JPanel playerManagementPanel; // Το panel του μενού διαχείρισης παιχτών
    private JPanel gameLobbyPanel;  // Το panel του μενού εκκίνησης του παιχνιδιού
    private JPanel gameActionPanel; // Το panel στο οποίο διαδραματίζεται το παιχνίδι

    // Στεθερές που αντιστοιχούν στα ονόματα των panel στο CardLayout του κυρίου παραθύρου
    private final String MAIN_MENU = "MainMenu";
    private final String PLAYER_MANAGE = "PlayerManagement";
    private final String GAME_LOBBY = "GameLobby";
    private final String GAME_ACTION = "GameAction";

    // Πεδία που αφορούν την εκτέλεση του παιχνιδιού
    private Round currentRound;
    private Question currentQuestion;
    private QuestionPanel currentQuestionPanel;

    /**
     * Τυπικός (default) κατασκευαστής της κλάσης {@code GUI}. Αρχικοποιεί (ή/και φορτώνει) τα δεδομένα του παιχνιδιού και της γραφικής διεπαφής.
     * Για την εκκίνηση του γραφικού περιβάλλοντος, ακολούθως μετά την κατασκευή του αντικειμένου, απαιτείται η κλήση της μεθόδου {@code begin()}.
     * Για τα αντικείμενα των πεδίων, όταν είναι δυνατό, ενεργοποιείται η λειτουργία "αυτόματου ανακατέματος" που αποσκοπεί στο να περιοριστεί η επανεμφάνιση ίδιων
     * κατηγοριών / ερωτήσεων / γύρων στην ίδια συνεδρία παιχνιδιού (δηλαδή για παράδειγμα άν έχω αποθηκευμένες 100 ερωτήσεις στο σύνολο σε 100 ξεχωριστές κατηγορίες, αν τελειώσει ένα παιχνίδι
     * όπου έχουν χρησιμοποιηθεί 5, αυτές οι 5 δεν θα ξανά εμφανιστούν σε επόμενο παιχνίδι μέχρι να "Χρησιμοποιηθούν" οι υπόλοιπες 95).
     */
    public GUI(){
        /*TODO: DEBUGGING -- REMOVE IN FINAL*/
        JFrame sizeManager = new JFrame(); sizeManager.setVisible(true);
        JButton managerButton = new JButton("-----------PRESS FOR SIZE-----------"); sizeManager.add(managerButton);
        managerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ((JButton)actionEvent.getSource()).setText(mainWindow.getSize().toString());
            }
        });
        sizeManager.setSize(400, 200); sizeManager.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sizeManager.setLocation(0, 0);

        // Αρχικοποίηση δεδομένων παιχνιδιού
        playerController = new PlayerController();
        questionLibrary = new QuestionLibrary(true);
        roundController = new RoundController(true, questionLibrary);


        // Αρχικοποίηση δεδομένων κυρίου παραθύρου
        mainWindow = new JFrame("Buzz! Quiz World");
        mainWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Το κλείσιμο του παραθύρου διαχειρίζεται (παρακάτω) ένας WindowListener
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setSize(675, 264);
        mainWindowLayout = new CardLayout();
        mainWindow.setLayout(mainWindowLayout);

        initMainMenu(); mainWindow.add(mainMenuPanel, "MainMenu"); // Αρχικοποίηση panel κυρίου μενού.
        initPlayerManagement(); mainWindow.add(playerManagementPanel, "PlayerManagement"); // Αρχικοποίηση panel μενού διαχείρισης παιχτών.
        initPlayLobby(); mainWindow.add(gameLobbyPanel, GAME_LOBBY);
        gameActionPanel = new JPanel(new BorderLayout()); mainWindow.add(gameActionPanel, GAME_ACTION);

        mainWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitRoutine();
            }
        });
    }

    private void exitRoutine(){
        int userResponse = JOptionPane.showConfirmDialog(mainWindow, "Πριν την έξοδο, θέλεις να αποθηκευτούν τα δεδομένα παιχτών;");
        if (userResponse == 0){
            //  save data
            mainWindow.dispose();
        } else if (userResponse == 1){
            mainWindow.dispose();
        }
    }

    /**
     * Εμφανίζει το παράθυρο της γραφικής διεπαφής της εφαρμογής με τον χρήστη.
     */
    public void begin(){
        mainWindow.setVisible(true);
    }


    private void initMainMenu(){
        mainMenuPanel = new JPanel(new GridLayout(2, 1)); mainWindow.add(mainMenuPanel);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 30)); mainMenuPanel.add(titlePanel);
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 3)); mainMenuPanel.add(buttonsPanel);

        /* Κομμάτι με τίτλο */
        titlePanel.add(new JLabel("Καλωσόρισες στο παιχνίδι Buzz! Quiz World!"));


        /* Κομμάτι με κουμπιά ελέγχου */
        JButton tempButton = new JButton("Διαχείριση Παιχτών");
        tempButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                switchPanelTo(PLAYER_MANAGE);
            }
        });
        tempButton.setToolTipText("Εισέρχεσαι στο μενού επιλογών Παίχτη");
        buttonsPanel.add(StaticTools.wrapInFlowLayout(tempButton));

        tempButton = new JButton("Εκκίνηση Παιχνιδιού");
        tempButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                switchPanelTo(GAME_LOBBY);
            }
        });

        buttonsPanel.add(StaticTools.wrapInFlowLayout(tempButton));
        tempButton.setToolTipText("Ξεκινάς το παιχνίδι");
        tempButton = new JButton("Έξοδος");
        tempButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                exitRoutine();
            }
        });
        tempButton.setToolTipText("Έξοδος απο το παιχνίδι");
        buttonsPanel.add(StaticTools.wrapInFlowLayout(tempButton));


    }

    private void initPlayerManagement(){
        playerManagementPanel = new JPanel(new GridLayout(5, 1, 20, 20));

        JButton tempButton;

        tempButton = new JButton("Δημιουργία Παίχτη");
        tempButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String response = JOptionPane.showInputDialog(mainWindow, "Δώσε όνομα παίχτη για προσθήκη", "Δημιουργία Παίχτη", JOptionPane.PLAIN_MESSAGE);
                if (response!=null){
                    String result = playerController.createPlayer(response);
                    if (result.equals("Επιτυχία")){
                        JOptionPane.showMessageDialog(mainWindow, "Δημιουργήθηκε επιτυχώς νέος παίχτης με το όνομα: " + response, "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(mainWindow, result, "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        tempButton.setToolTipText("Δημιουργείται ένας νέος παίχτης στο παιχνίδι");
        playerManagementPanel.add(StaticTools.wrapInFlowLayout(tempButton));

        tempButton = new JButton("Μετονομασία Παίχτη");
        tempButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
               new RenameDialog();
            }
        });
        tempButton.setToolTipText("Αλλάζεις το όνομα του παίχτη");
        playerManagementPanel.add(StaticTools.wrapInFlowLayout(tempButton));

        tempButton = new JButton("Διαγραφή Παίχτη");
        tempButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new DeleteDialog();
            }
        });
        tempButton.setToolTipText("Διαγράφεται η εγγραφή ενός παίχτη απο το παιχνίδι");
        playerManagementPanel.add(StaticTools.wrapInFlowLayout(tempButton));

        tempButton = new JButton("Εμφάνιση Πίνακα Σκορ");
        tempButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (playerController.getNumberOfPlayers() != 0) {
                    ScoreboardDialog.showTableDialog(mainWindow, playerController.getScoreboard());
                } else {
                    JOptionPane.showMessageDialog(mainWindow, "Δεν υπάρχουν αποθηκευμένοι παίχτες!", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        tempButton.setToolTipText("Εμφανίζεται ο πίνακας με το τρέχον και μέγιστο σκόρ κάθε παίχτη");
        playerManagementPanel.add(StaticTools.wrapInFlowLayout(tempButton));

        tempButton = new JButton("Επιστροφή στο κύριο μενού");
        tempButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                switchPanelTo(MAIN_MENU);
            }
        });
        tempButton.setToolTipText("Επιστρέφει στο κύριο μενού επιλογών του παιχνιδιού");
        playerManagementPanel.add(StaticTools.wrapInFlowLayout(tempButton));
    }

    private class RenameDialog extends JDialog {
        public RenameDialog(){
            super(mainWindow, "Μετονομασία Παίχτη", true);

            if (playerController.getNumberOfPlayers() == 0){
                JOptionPane.showMessageDialog(mainWindow, "Δεν υπάρχουν αποθηκευμένοι παίχτες, οπότε δεν μπορείς να μετονομάσεις κάποιον", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                disposeSelf();
            } else {
                setLocationRelativeTo(mainWindow);
                setDefaultCloseOperation(DISPOSE_ON_CLOSE);

                JPanel mainPanel = new JPanel(new GridLayout(3, 1));
                this.setContentPane(mainPanel);

                JPanel selectionPanel = new JPanel(new BorderLayout());
                mainPanel.add(selectionPanel);
                TitledBorder border = BorderFactory.createTitledBorder("Δώσε το όνομα του παίχτη που θέλεις να μετονομάσεις");
                selectionPanel.setBorder(border);
                JComboBox<String> dropdownList = new JComboBox<>(playerController.listPlayers());
                dropdownList.setEditable(false);
                selectionPanel.add(dropdownList);


                JPanel newNamePanel = new JPanel(new BorderLayout());
                mainPanel.add(newNamePanel);
                border = BorderFactory.createTitledBorder("Δώσε νέο όνομα παίχτη");
                newNamePanel.setBorder(border);
                JTextField newNameField = new JTextField();
                newNamePanel.add(newNameField);

                JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
                mainPanel.add(buttonPanel);

                JButton button = new JButton("OK");
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        String result = playerController.changePlayerName(dropdownList.getItemAt(dropdownList.getSelectedIndex()), newNameField.getText());
                        if (result.equals("Επιτυχία")) {
                            JOptionPane.showMessageDialog(getRootDialog(), String.format("Η μετονομασία παίχτη απο %s σε %s, ήταν επιτυχής", dropdownList.getItemAt(dropdownList.getSelectedIndex()), newNameField.getText()), "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
                            disposeSelf();
                        } else {
                            JOptionPane.showMessageDialog(getRootDialog(), result, "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                buttonPanel.add(StaticTools.wrapInFlowLayout(button));

                button = new JButton("Cancel");
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        disposeSelf();
                    }
                });
                buttonPanel.add(StaticTools.wrapInFlowLayout(button));


                this.pack();
                this.setVisible(true);
            }
        }

        private void disposeSelf(){
            this.dispose();
        }

        private RenameDialog getRootDialog(){
            return this;
        }
    }

    private class DeleteDialog extends JDialog {
        public DeleteDialog(){
            super(mainWindow, "Διαγραφή", true);

            if (playerController.getNumberOfPlayers() == 0){
                JOptionPane.showMessageDialog(mainWindow, "Δεν υπάρχουν αποθηκευμένοι παίχτες, οπότε δεν μπορείς να διαγράψεις κάποιον", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                disposeSelf();
            } else {
                setLocationRelativeTo(mainWindow);
                setDefaultCloseOperation(DISPOSE_ON_CLOSE);

                JPanel mainPanel = new JPanel(new GridLayout(3, 1));
                this.setContentPane(mainPanel);

                JPanel selectionPanel = new JPanel(new BorderLayout());
                mainPanel.add(selectionPanel);
                TitledBorder border = BorderFactory.createTitledBorder("Δώσε το όνομα του παίχτη που θέλεις να διαγράψεις");
                selectionPanel.setBorder(border);
                JComboBox<String> dropdownList = new JComboBox<>(playerController.listPlayers());
                dropdownList.setEditable(false);
                selectionPanel.add(dropdownList);

                JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
                mainPanel.add(buttonPanel);

                JButton button = new JButton("OK");
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        String result = playerController.removePlayer(dropdownList.getItemAt(dropdownList.getSelectedIndex()));
                        if (result.equals("Επιτυχία")) {
                            JOptionPane.showMessageDialog(getRootDialog(), String.format("Η διαγραφή του παίχτη %s, ήταν επιτυχής", dropdownList.getItemAt(dropdownList.getSelectedIndex())), "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
                            disposeSelf();
                        } else {
                            JOptionPane.showMessageDialog(getRootDialog(), result, "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                buttonPanel.add(StaticTools.wrapInFlowLayout(button));

                button = new JButton("Cancel");
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        disposeSelf();
                    }
                });
                buttonPanel.add(StaticTools.wrapInFlowLayout(button));


                this.pack();
                this.setVisible(true);
            }
        }

        private void disposeSelf(){
            this.dispose();
        }

        private DeleteDialog getRootDialog(){
            return this;
        }
    }

    private void initPlayLobby(){
        gameLobbyPanel = new JPanel(new GridLayout(2, 1)); mainWindow.add(gameLobbyPanel, GAME_LOBBY);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 30)); gameLobbyPanel.add(titlePanel);
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 3)); gameLobbyPanel.add(buttonsPanel);

        /* Κομμάτι με τίτλο */
        titlePanel.add(new JLabel("Εκκίνηση Παιχνιδιού"));


        /* Κομμάτι με κουμπιά ελέγχου */
        JButton tempButton = new JButton("Επιστροφή στο κύριο μενού");
        tempButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                switchPanelTo(MAIN_MENU);
            }
        });
        tempButton.setToolTipText("Επιστρέφει στο κύριο μενού επιλογών του παιχνιδιού");
        buttonsPanel.add(StaticTools.wrapInFlowLayout(tempButton));

        tempButton = new JButton("Εκκίνηση Παιχνιδιού");
        tempButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
//                int[] settings = GameSelectionDialog.showGameSettingSelection(mainWindow, 2, 1, 20, 1, 20, 1);
//                if (settings != null){
//                    String[] playerNames = PlayerSelectionDialog.selectPlayers(mainWindow, settings[0], playerController.listPlayers());
//
//                    if (playerNames != null){
//                        // Εφαρμογή ρυθμίσεων σχετικά με τους γύρους
//                        roundController.setPlayerNumber(settings[0]);
//                        roundController.setNumberOfQuestionsPerRound(settings[2]);
//
//                        initGameAction(settings[0], settings[1], playerNames);
//                        switchPanelTo(GAME_ACTION);
//                    }
//                }
            }
        });

        buttonsPanel.add(StaticTools.wrapInFlowLayout(tempButton));
        tempButton.setToolTipText("Ξεκινάς το παιχνίδι");

        tempButton = new JButton("Πληροφορίες");
        tempButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(mainWindow, String.format("Το παιχνίδι αποτελείται απο έναν αριθμό γύρων όπου κάθε ένας έχει έναν αριθμό ερωτήσεων.%n" +
                        "Κάθε ερώτηση έχει 4 πιθανές απαντήσεις με την αρίθμηση 1, 2, 3, 4 όπου μόνο μία είναι σωστή.%n" +
                        "Καλείσαι να απαντήσεις σε αυτές τις ερωτήσεις δίνοντας τον αριθμό που αντιστοιχεί στην απάντηση που θέλεις να επιλέξεις %n" +
                        "(πρέπει να πατήσεις το κουμπί της απάντησης 1, 2, 3 ή 4 που αντιστοιχούν στον αριθμό 1, 2, 3 ή 4 ή το γράμμα v, b, n, m αντίστοιχα για τον πρώτο ή δεύτερο παίκτη).%n" +
                        "Σημειώνεται ότι κάθε γύρος ίσως να έχει κάποιες παραλλαγές σε σχέση με αυτές τις οδηγίες, οι οποίες εξηγούνται προτού αρχίσει ο συγκεκριμένος γύρος.%n" +
                        "%n Ανα πάσα στιγμή κατά την διάρκεια του παιχνιδιού μπορείς να γυρίσεις στο μενού εκκίνησης παιχνιδιού κάνοντας δεξί κλικ και στην συνέχεια πατώντας επιστροφή στο μενού εκκίνησης.%n" +
                        "Με την επιστροφή πριν τελειώσει κάποιος γύρος δεν προσμετράται η νίκη / ήττα για τον γύρο που μόλις διακόπηκε."),
                        "Πληροφορίες", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        tempButton.setToolTipText("Εμφανίζει πληροφορίες σχετικά με το παιχνίδι");
        buttonsPanel.add(StaticTools.wrapInFlowLayout(tempButton));


    }

    private void initGameAction(int numberOfPlayers, int numberOfRounds, String[] selectedPlayerNames) {
        gameActionPanel.setFocusable(true);
        boolean[] playerHasAnswered = new boolean[numberOfPlayers];
        int[] playerGains = new int[numberOfPlayers];
        KeyListener keyPressResponder;

        for (int i = 0;  i <numberOfRounds; i++){
            for (int j = 0; j < numberOfPlayers; j++) {
                playerHasAnswered[j] = false;
            }

            currentRound = roundController.getRandomRoundType();
            currentRound.proceed();


            while (!currentRound.isOver()){
                currentQuestion = new Question(currentRound.getQuestion(), currentRound.getQuestionAnswers(), // Κατασκευάζω "αντίγραφο" ερώτησης
                        currentRound.getRightQuestionAnswer(), currentRound.getQuestionCategory());

                gameActionPanel.removeAll();
                currentQuestionPanel = QuestionPanel.constructQuestionPanel(currentQuestion, selectedPlayerNames);
                gameActionPanel.add(currentQuestionPanel);
                gameActionPanel.revalidate();
                gameActionPanel.repaint();

                boolean allPlayersHaveAnswered = false;
                while (!allPlayersHaveAnswered) {
                    allPlayersHaveAnswered = true;
                    for (boolean b : playerHasAnswered) {
                        if (!b){
                            allPlayersHaveAnswered = false;
                            break;
                        }
                    }
                }



            }
        }

        gameActionPanel.addKeyListener(new KeyAdapter() {
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
                        playerGains[0] = playerAnswer(selectedPlayerNames[0], currentQuestion.getAnswers()[correspondingInt],
                                currentQuestion, currentRound);
                        playerHasAnswered[0] = true;
                        currentQuestionPanel.markAnswer(selectedPlayerNames[0], correspondingInt + 1);
                    }

                } else if (numberOfPlayers == 2) {
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
                            playerGains[1] = playerAnswer(selectedPlayerNames[1], currentQuestion.getAnswers()[correspondingInt],
                                    currentQuestion, currentRound);
                            playerHasAnswered[1] = true;
                            currentQuestionPanel.markAnswer(selectedPlayerNames[1], correspondingInt + 1);
                        }

                    }
                }
            }
        });


    }

    private int playerAnswer(String playerName, String givenAnswer, Question currentQuestion, Round currentRound){
        return 0;
    }

    private void switchPanelTo(String panelConstantName){
        switch (panelConstantName){
            case MAIN_MENU:
                mainWindowLayout.show(mainWindow.getContentPane(), MAIN_MENU);
                mainWindow.setSize(675, 264);
                break;
            case PLAYER_MANAGE:
                mainWindowLayout.show(mainWindow.getContentPane(), PLAYER_MANAGE);
                mainWindow.setSize(385, 450);
                break;
            case GAME_LOBBY:
                mainWindowLayout.show(mainWindow.getContentPane(), GAME_LOBBY);
                mainWindow.setSize(750, 210);
                break;
            case GAME_ACTION:
                mainWindowLayout.show(mainWindow.getContentPane(), GAME_ACTION);
                mainWindow.setSize(900, 610);
                break;
        }
    }

    public static void main(String[] args) { // TODO: DEBUGGING -- REMOVE IN FINAL
        (new GUI()).begin();
    }
}

