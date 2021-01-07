package GraphicalUserInterface;

import GraphicalUserInterface.AssistingTools.*;
import GraphicalUserInterface.AssistingTools.customDialogs.*;
import internals.player.PlayerController;
import internals.question.QuestionLibrary;
import internals.round.RoundController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

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
    private GameSequenceHandler gameplayManager;

    /**
     * Τυπικός (default) κατασκευαστής της κλάσης {@code GUI}. Αρχικοποιεί (ή/και φορτώνει) τα δεδομένα του παιχνιδιού και της γραφικής διεπαφής.
     * Για την εκκίνηση του γραφικού περιβάλλοντος, ακολούθως μετά την κατασκευή του αντικειμένου, απαιτείται η κλήση της μεθόδου {@code begin()}.
     * Για τα αντικείμενα των πεδίων, όταν είναι δυνατό, ενεργοποιείται η λειτουργία "αυτόματου ανακατέματος" που αποσκοπεί στο να περιοριστεί η επανεμφάνιση ίδιων
     * κατηγοριών / ερωτήσεων / γύρων στην ίδια συνεδρία παιχνιδιού (δηλαδή για παράδειγμα άν έχω αποθηκευμένες 100 ερωτήσεις στο σύνολο σε 100 ξεχωριστές κατηγορίες, αν τελειώσει ένα παιχνίδι
     * όπου έχουν χρησιμοποιηθεί 5, αυτές οι 5 δεν θα ξανά εμφανιστούν σε επόμενο παιχνίδι μέχρι να "Χρησιμοποιηθούν" οι υπόλοιπες 95).
     */
    public GUI(){
        // Αρχικοποίηση δεδομένων παιχνιδιού
        playerController = new PlayerController();

        // Αρχικοποίηση δεδομένων κυρίου παραθύρου
        mainWindow = new JFrame("Buzz! Quiz World");
        mainWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Το κλείσιμο του παραθύρου διαχειρίζεται (παρακάτω) ένας WindowListener
        mainWindow.setSize(675, 264);
        mainWindow.setLocationRelativeTo(null);
        mainWindowLayout = new CardLayout();
        mainWindow.setLayout(mainWindowLayout);

        initMainMenu(); mainWindow.add(mainMenuPanel, "MainMenu"); // Αρχικοποίηση panel κυρίου μενού.
        initPlayerManagement(); mainWindow.add(playerManagementPanel, "PlayerManagement"); // Αρχικοποίηση panel μενού διαχείρισης παιχτών.
        initPlayLobby(); mainWindow.add(gameLobbyPanel, GAME_LOBBY);
        gameActionPanel = new JPanel(new BorderLayout()); mainWindow.add(gameActionPanel, GAME_ACTION);

        // Προσθέτω WindowListener ώστε αν ο χρήστης επιθυμεί να κάνει έξοδο μέσω του Χ και όχι του κουμπιού "Έξοδος" να μπορεί να γίνει αποθήκευση δεδομένων
        mainWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitRoutine();
            }
        });

        do { // Ρωτάμε συνεχώς τον χρήστη αν θέλει να συνεχίζει τις προσπάθειες
            try {
                questionLibrary = new QuestionLibrary(true, "Ερωτήσεις.txt");
                break;
            } catch (IOException e) {
                if (0 != JOptionPane.showConfirmDialog(mainWindow, "Υπήρχε πρόβλημα με το άνοιγμα αρχείου με ερωτήσεις. Θέλεις να ξαναγίνει προσπάθεια;", "Σφάλμα", JOptionPane.YES_NO_CANCEL_OPTION)){
                    System.exit(-1);
                }
            }
        } while (true);
        roundController = new RoundController(true, questionLibrary);

        do{// Ρωτάμε συνεχώς τον χρήστη αν θέλει να συνεχίζει τις προσπάθειες
            try {
                playerController.loadGameStatistics("Στατιστικά Παιχνιδιού.bin");
                break;
            } catch (IOException e) {
                int response = JOptionPane.showConfirmDialog(mainWindow, "Δεν υπάρχει αρχείο με στατιστικά.\n Πάτησε Yes για να δημιουργηθεί νέο αρχείο,NO για νέα προσπάθεια ανοίγματος αρχείου (αν υπάρχει) για ακύρωση ή Cancel για έξοδο απο το πρόγραμμα.", "Σφάλμα", JOptionPane.YES_NO_CANCEL_OPTION);
                if (response == 0){ // Δημιουργία νέων στατιστικών
                    break;
                } else if (response == 1) { // Έξοδος
                    System.exit(-1);
                }
                // Δημιουργία νέου αρχείο στο τέλος εκτέλεσης
            } catch (ClassNotFoundException e){
                int response = JOptionPane.showConfirmDialog(mainWindow, "Υπήρχε πρόβλημα με το άνοιγμα αρχείου με στατιστικά.\n Πάτησε ΟΚ για να δημιουργηθεί νέο αρχείο ή Cancel για νέα προσπάθεια ανοίγματος αρχείου.", "Σφάλμα", JOptionPane.DEFAULT_OPTION);
                if (response == 0){ // Δημιουργία νέων στατιστικών
                    break;
                } else { // Έξοδος
                    System.exit(-1);
                }
            }
        } while (true);
    }

    /**
     * Η μέθοδος που καλείται για να διαχειριστεί την αποθήκευση των δεδομένων πριν την έξοδο.
     */
    private void exitRoutine(){
        int userResponse = JOptionPane.showConfirmDialog(mainWindow, "Πριν την έξοδο, θέλεις να αποθηκευτούν τα δεδομένα παιχτών;");
        if (userResponse == 0){
            do {
                try {
                    playerController.saveGameStatistics("Στατιστικά Παιχνιδιού.bin");
                    break;
                } catch (IOException e) {
                    if (0 != JOptionPane.showConfirmDialog(mainWindow, "Υπήρχε πρόβλημα κατά την αποθήκευση δεδομένων. Θέλεις να ξαναγίνει προσπάθεια;", "Σφάλμα", JOptionPane.YES_NO_CANCEL_OPTION)){
                        break;
                    }
                }
            } while (true);
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


    /**
     * Η μέθοδος που αρχικοποιεί το JPanel που αποτελεί το κύριο μενού της εφαρμογής.
     */
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

    /**
     * Η μέθοδος που αρχικοποιεί το JPanel που αποτελεί το μενού διαχείρισης παιχτών της εφαρμογής.
     */
    private void initPlayerManagement(){
        playerManagementPanel = new JPanel(new GridLayout(5, 1, 20, 20));

        JButton tempButton;

        /* Κουμπί "Δημιουργία Παίχτη" */
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

        /* Κουμπί "Μετονομασία Παίχτη" */
        tempButton = new JButton("Μετονομασία Παίχτη");
        tempButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
               new RenameDialog(mainWindow, playerController);
            }
        });
        tempButton.setToolTipText("Αλλάζεις το όνομα του παίχτη");
        playerManagementPanel.add(StaticTools.wrapInFlowLayout(tempButton));

        /* Κουμπί "Διαγραφή Παίχτη" */
        tempButton = new JButton("Διαγραφή Παίχτη");
        tempButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new DeleteDialog(mainWindow, playerController);
            }
        });
        tempButton.setToolTipText("Διαγράφεται η εγγραφή ενός παίχτη απο το παιχνίδι");
        playerManagementPanel.add(StaticTools.wrapInFlowLayout(tempButton));

        /* Κουμπί "Εμφάνιση Πίνακα Σκορ" */
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

        /* Κουμπί "Επιστροφή στο κύριο μενού" */
        tempButton = new JButton("Επιστροφή στο κύριο μενού");
        tempButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                switchPanelTo(MAIN_MENU); // Αλλαγή κυρίου JPanel σε αυτό του κυρίου μενού
            }
        });
        tempButton.setToolTipText("Επιστρέφει στο κύριο μενού επιλογών του παιχνιδιού");
        playerManagementPanel.add(StaticTools.wrapInFlowLayout(tempButton));
    }

    /**
     * Η μέθοδος που αρχικοποιεί το JPanel που αποτελεί το μενού εκκίνησης του παιχνιδιού.
     */
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
                if (playerController.getNumberOfPlayers() == 0){ // Έλεγχος για μη δημιουργία παιχτών
                    JOptionPane.showMessageDialog(mainWindow, "Δεν έχουν δημιουργηθεί ακόμα παίκτες", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int[] settings = GameSelectionDialog.showGameSettingSelection(mainWindow, 2, 1, 20, 1, 20, 1); // Επιλογή ρυθμίσεων για το παιχνίδι

                if (settings != null){ // Έλεγχος για το αν ο χρήστης επέλεξε όντως ρυθμίσεις και δεν πάτησε το κουμπί "Cancel" ή το Χ.
                    try {
                        String[] playerNames = PlayerSelectionDialog.selectPlayers(mainWindow, settings[0], playerController.listPlayers()); // Επιλογή παιχτών

                        if (playerNames != null) { //  Υπάρχουν παίκτες που ταυτίζονται
                            // Εφαρμογή ρυθμίσεων σχετικά με τους γύρους
                            roundController.setPlayerNumber(settings[0]);
                            roundController.setNumberOfQuestionsPerRound(settings[2]);

                            initGameAction(settings[1], playerNames); // Αρχικοποιώ το JPanel "πάνω" στο οποίο διαδραματίζεται το παιχνίδι
                            switchPanelTo(GAME_ACTION); // Αλλαγή κυρίου JPanel σε αυτό του παιχνιδιού
                        } else {
                            return; // Υπάρχουν παίχτες που ταυτίζονται: Επιστροφή στο μενού
                        }
                    } catch (IllegalArgumentException e){ // Σφάλμα επιλογών ρυθμίσεων χρήστη
                        JOptionPane.showMessageDialog(mainWindow, e.getMessage(), "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                    }
                }
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

    /**
     * Αρχικοποίηση του JPanel "πάνω" στο οποίο διαδραματίζεται το παιχνίδι.
     * @param numberOfRounds αριθμός γύρων που θα παιχτούν
     * @param selectedPlayerNames ονόματα των παιχτών που έχουν επιλεγεί να παίξουν
     */
    private void initGameAction(int numberOfRounds, String[] selectedPlayerNames) {
        gameActionPanel.setFocusable(true); // Επιτρέπω την εστίαση για να είναι δυνατή η δημιουργία popup μενού με δεξί κλίκ

        ActionListener returnListener = new ActionListener() { // Όταν κληθεί έχει σηματοδοτηθεί η λήξη του παιχνιδιού και το κύριο JPanel αλλάζει σε αυτό του μενού
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gameplayManager.killAllAssistingWindows();
                gameActionPanel.removeAll(); // Αφαιρώ τυχόν στοιχεία του JPanel του παιχνιδιού
                switchPanelTo(GAME_LOBBY); // Μετάβαση σε JPanel μενού εκκίνησης παιχνιδιού
            }
        };

        /* Δημιουργία μενού με δεξί κλικ για άμεση επιστροφή στο μενού */
        JPopupMenu rightClickMenu = new JPopupMenu();
        JMenuItem exitItem = new JMenuItem("Επιστροφή στο μενού"); rightClickMenu.add(exitItem);
        exitItem.addActionListener(returnListener);

        gameActionPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    rightClickMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    rightClickMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        // Δημιουργία αντικειμένου που "ελέγχει" την ροή του παιχνιδιού
        gameplayManager = new GameSequenceHandler(gameActionPanel, returnListener, roundController, playerController, numberOfRounds, selectedPlayerNames);
    }

    /**
     * Εναλλάσσει το κύριο (αυτό που φαίνεται στο παράθυρο) JPanel σε ένα απο αυτά που έχουν προκαθοριστεί σύμφωνα με την παράμετρο.
     * Παράλληλα, αλλάζει το μέγεθος του παραθύρου στο προκαθορισμένο και κατάλληλο μέγεθος και τοποθετεί το παράθυρο στο κέντρο της οθόνης.
     * Έχουν προκαθοριστεί: κύριο μενού με σταθερά MAIN_MENU, μενού διαχείρισης παιχτών με σταθερά PLAYER_MANAGE, μενού εκκίνησης παιχνιδιού με σταθερά GAME_LOBBY και
     * JPanel διαδραμάτισης παιχνιδιού με σταθερά GAME_ACTION.
     * @param panelConstantName η σταθερά που ορίζει το JPanel στο οποίο θα μεταβεί το κεντρικό παράθυρο
     */
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
                gameplayManager.proceedToNextQuestion();
                break;
        }

        mainWindow.setLocationRelativeTo(null);
    }


    public static void main(String[] args) { // TODO: DEBUGGING -- REMOVE IN FINAL
        (new GUI()).begin();
    }
}

