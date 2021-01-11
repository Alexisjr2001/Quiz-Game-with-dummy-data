package GraphicalUserInterface.AssistingTools.customPanels;

import GraphicalUserInterface.AssistingTools.StaticTools;
import internals.question.ImageQuestion;
import internals.question.Question;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;

/**
 * Η κλάση QuestionPanel επεκτείνει (με σύνθεση) ένα JPanel που αντιστοιχεί σε μία ερώτηση.
 * Σε αυτό εμφανίζονται: η εκφώνηση της ερώτησης, οι πιθανές απαντήσεις και η απάντηση που επέλεξε ο κάθε παίχτης.
 * (η απάντηση που έδωσε κάποιος παίχτης μπορεί να αλλάξει με την χρήση της κατάλληλης μεθόδου).
 *
 * @author Ioannis Baraklilis
 * @author Alexandros Tsingos
 *
 * @version 2021.01.11
 */
public class QuestionPanel{
    private HashMap<String, JLabel> playerAnswers; // Αντιστοίχηση ονόματος παίχτη και JLabel που εμφανίζει την απάντηση του

    private JPanel basePanel; // Το JPanel που "επεκτείνεται" μέσω σύνθεσης

    /**
     * Ο τυπικός κατασκευαστής που αρχικοποιεί τα δεδομένα της κλάσης.
     * Δέχεται ερωτήσεις κειμένου ή εικόνας μέσω κληρονομικότητας.
     *
     * @param question η ερώτηση που αντιστοιχεί στο JPanel
     * @param selectedPlayerNames τα ονόματα των παιχτών που απαντούν σε αυτή την ερώτηση
     */
    public QuestionPanel(Question question, String[] selectedPlayerNames){
        basePanel = new JPanel(new BorderLayout());

        JPanel questionPanel = new JPanel(new BorderLayout()); basePanel.add(questionPanel);
        questionPanel.setFocusable(true);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 30));
        topPanel.setBorder( BorderFactory.createTitledBorder( // Εκφώνηση ερώτησης
                BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()),
                "Κατηγορία: " + question.getCategory() + "."));
        questionPanel.add(topPanel, BorderLayout.PAGE_START);


        JPanel bottomPanel = new JPanel(new GridLayout(1, 1)); questionPanel.add(bottomPanel);

        topPanel.add(new JLabel(question.getQuestion()));

        if (question instanceof ImageQuestion){ // Δυναμική κατασκευή αντικειμένου ανάλογα με το αν είναι ερώτηση κειμένου ή εικόνας
            ImageIcon tempImageIcon = ((ImageQuestion)question).getImageIcon();


            int newWidth, newHeight;

            if (tempImageIcon.getIconWidth() > tempImageIcon.getIconHeight()){
                double widthFactor = tempImageIcon.getIconWidth()/450.0; // Βρίσκω κατά πόσο πρέπει να διαιρεθεί το πλάτος για να "χωρέσει" ομοιόμορφα στο κενό που υπάρχει
                if (widthFactor <= 0){
                    widthFactor = 1;
                }

                newWidth = (int)(tempImageIcon.getIconWidth()/widthFactor);
                newHeight = (int)( (double)tempImageIcon.getIconHeight() / tempImageIcon.getIconWidth() * newWidth );
            } else {
                double heightFactor = tempImageIcon.getIconHeight()/400.0; // Βρίσκω κατά πόσο πρέπει να διαιρεθεί το "ύψος" για να "χωρέσει" ομοιόμορφα στο κενό που υπάρχει
                if (heightFactor <= 0){
                    heightFactor = 1;
                }

                newHeight = (int)(tempImageIcon.getIconHeight()/heightFactor);
                newWidth = (int) ( tempImageIcon.getIconWidth() / (double)tempImageIcon.getIconHeight() * newHeight);
            }

            ImageIcon imageIcon = new ImageIcon(tempImageIcon.getImage().getScaledInstance(newWidth, // Δημιουργώ την επεξεργασμένη εικόνα
                    newHeight, Image.SCALE_SMOOTH));
            bottomPanel.add(new JLabel(imageIcon));
        }

        /* Δημιουργία τμήματος με πιθανές απαντήσεις */
        JPanel answersPanel = new JPanel(new GridLayout(4, 1)); bottomPanel.add(answersPanel);
        JPanel individualAnswerPanel;

        String[] answers = question.getAnswers();

        for (int i = 0; i<answers.length; i++){
            individualAnswerPanel = new JPanel(new GridBagLayout());
            individualAnswerPanel.add(new JLabel(i+1 + ": " + answers[i]));
            individualAnswerPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
            answersPanel.add(individualAnswerPanel);
        }

        /* Δημιουργία τμήματος όπου φαίνονται οι απαντήσεις κάθε παίχτη που παίζει */
        playerAnswers = new HashMap<>(selectedPlayerNames.length);

        JPanel playerAnswersPanel = new JPanel(new GridLayout(0, 1));
        TitledBorder playerAnswersBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Απαντήσεις που δόθηκαν:");
        playerAnswersBorder.setTitleJustification(TitledBorder.RIGHT);
        playerAnswersPanel.setBorder(playerAnswersBorder);


        for (String s : selectedPlayerNames){
            JLabel tempLab = new JLabel("Καμία απάντηση");
            playerAnswers.put(s, tempLab);
            JPanel individualAnswer = StaticTools.wrapInFlowLayout(tempLab);
            individualAnswer.setBorder(BorderFactory.createTitledBorder("Η απάντηση του " + s));
            playerAnswersPanel.add(individualAnswer);
        }

        basePanel.add(playerAnswersPanel, BorderLayout.PAGE_END);
    }

    /**
     * Σημειώνεται (εμφανίζεται) στο panel η απάντηση που έδωσε ο χρήστης.
     * @param playerName ο χρήστης που απάντησε
     * @param answerNumber ο αριθμός της απάντησης που έδωσε
     */
    public void markAnswer(String playerName, int answerNumber){
        playerAnswers.get(playerName).setText(String.valueOf(answerNumber));
    }

    /**
     * Επιστρέφει το JPanel που αντιστοιχεί στην ερώτηση
     * @return το JPanel που αντιστοιχεί στην ερώτηση
     */
    public JPanel getPanel(){
        return basePanel;
    }

    /**
     * Κατασκευάζει και επιστέφει JPanel που αντιστοιχεί σε μία ερώτηση.
     * @param question ερώτηση σύμφωνα με την οποία θα κατασκευαστεί JPanel
     * @return το JPanel που αντιστοιχεί στην ερώτηση του ορίσματος
     */
    public static QuestionPanel constructQuestionPanel(Question question, String[] selectedPlayerNames){
        return new QuestionPanel(question, selectedPlayerNames);
    }
}