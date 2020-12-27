package GraphicalUserInterface;

import GraphicalUserInterface.AssistingTools.StaticTools;
import internals.player.PlayerController;
import internals.question.QuestionLibrary;
import internals.round.RoundController;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUI {
    // Πεδία που αφορούν την λογική του παιχνιδιού
    private PlayerController playerController; // Χρησιμοποιούμε το playerController για διαχείριση των δεδομένων των παιχτών
    private QuestionLibrary questionLibrary; // Χρησιμοποιούμε το questionLibrary για διαχείριση των δεδομένων των ερωτήσεων
    private RoundController roundController; // Χρησιμοποιούμε το roundController για τυχαία επιλογή των τύπων γύρων και απόκτηση δεδομένων και διαδικασιών που υλοποιούν αντικείμενα τύπου Round

    // Πεδία που αφορούν το γραφικό περιβάλλον του παιχνιδιού
    private JFrame mainWindow;
    private JPanel mainMenuPanel;
    private JPanel playerManagementPanel;
    private CardLayout mainWindowLayout;

    private ActionListener defaultAL = actionEvent -> JOptionPane.showMessageDialog(mainWindow, "Πάτησες: " + actionEvent.getActionCommand() + "!", actionEvent.getActionCommand(), JOptionPane.INFORMATION_MESSAGE);

    //private final String[] OK_CANCEL_GREEK = {"ΟΚ", "Άκυρο"}; // TODO: REMOVE IF NOT USED
    private final String MAINMENU = "MainMenu";
    private final String PLAYERMANAGE = "PlayerManagement";

    public GUI(){
        playerController = new PlayerController();
        questionLibrary = new QuestionLibrary(true);
        roundController = new RoundController(true, questionLibrary);


        mainWindow = new JFrame("Buzz! Quiz World");
        mainWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setSize(675, 264);
        mainWindowLayout = new CardLayout();
        mainWindow.setLayout(mainWindowLayout);

        initMainMenu(); mainWindow.add(mainMenuPanel, "MainMenu");
        initPlayerManagement(); mainWindow.add(playerManagementPanel, "PlayerManagement");

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
                mainWindowLayout.show(mainWindow.getContentPane(), PLAYERMANAGE);
                mainWindow.setSize(385, 450);
            }
        });
        tempButton.setToolTipText("Εισέρχεσαι στο μενού επιλογών Παίχτη");
        buttonsPanel.add(StaticTools.wrapInFlowLayout(tempButton));

        tempButton = new JButton("Εκκίνηση Παιχνιδιού");
        tempButton.addActionListener(defaultAL);

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
        tempButton.setToolTipText("Εμφανίζεται ο πίνακας με το τρέχον και μέγιστο σκόρ κάθε παίχτη");
        playerManagementPanel.add(StaticTools.wrapInFlowLayout(tempButton));

        tempButton = new JButton("Εμφάνιση Πίνακα Σκορ");
        tempButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (playerController.getNumberOfPlayers() != 0) {
                    StaticTools.showTable(mainWindow, playerController.getScoreboard());
                } else {
                    JOptionPane.showMessageDialog(mainWindow, "Δεν υπάρχουν αποθηκευμένοι παίχτες!", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        tempButton.setToolTipText("Επιστρέφει στο κύριο μενού επιλογών του παιχνιδιού");
        playerManagementPanel.add(StaticTools.wrapInFlowLayout(tempButton));

        tempButton = new JButton("Επιστροφή στο κύριο μενού");
        tempButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                mainWindowLayout.show(mainWindow.getContentPane(), MAINMENU);
                mainWindow.setSize(675, 264);
            }
        });
        tempButton.setToolTipText("Διαγράφεται η εγγραφή ενός παίχτη απο το παιχνίδι");
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

    public static void main(String[] args) { // TODO: DEBUGGING -- REMOVE IN FINAL
        (new GUI()).begin();
    }
}

