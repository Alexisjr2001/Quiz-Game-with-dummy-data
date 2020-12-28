package GraphicalUserInterface.AssistingTools;

import internals.question.Question;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.HashMap;

public class QuestionPanel extends JPanel {
    private HashMap<String, JLabel> playerAnswers;

    private QuestionPanel(Question question, String[] selectedPlayerNames){
        super(new BorderLayout());

        JPanel questionPanel = new JPanel(new GridLayout(2, 1));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 30));
        topPanel.setBorder( BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
        questionPanel.add(topPanel);


        JPanel bottomPanel = new JPanel(new GridLayout(1, 2)); questionPanel.add(bottomPanel);


        if (question instanceof Question){
            topPanel.add(new JLabel(question.getQuestion()));



        } else {

        }

        bottomPanel.add(new JPanel());

        JPanel answersPanel = new JPanel(new GridLayout(4, 1));
        JPanel individualAnswerPanel;

        String[] answers = question.getAnswers();

        for (int i = 0; i<answers.length; i++){
            individualAnswerPanel = new JPanel(new BorderLayout());
            individualAnswerPanel.add(new JLabel(i+1 + ": " + answers[i]));
            individualAnswerPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
            answersPanel.add(individualAnswerPanel);
        }

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

        this.add(playerAnswersPanel, BorderLayout.PAGE_END);
    }

    public void markAnswer(String playerName, int answerNumber){
        playerAnswers.get(playerName).setText(String.valueOf(answerNumber));
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