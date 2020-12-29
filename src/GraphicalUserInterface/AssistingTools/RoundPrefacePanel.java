package GraphicalUserInterface.AssistingTools;

import internals.round.RightAnswer;
import internals.round.Round;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class RoundPrefacePanel extends JPanel {
    public RoundPrefacePanel(Round round, ActionListener beginButtonListener){
        super(new BorderLayout(20, 20));

        JPanel titlePanel = new JPanel(new FlowLayout());
        titlePanel.add(new JLabel(round.getRoundName()));
        this.add(titlePanel, BorderLayout.PAGE_START);
        titlePanel.setBorder(BorderFactory.createTitledBorder("Όνομα γύρου"));

        JTextArea roundInfo = new JTextArea(round.getRoundDescription());
        roundInfo.setEditable(false);
        JPanel roundInfoBox = StaticTools.wrapInFlowLayout(roundInfo);
        roundInfoBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Περιγραφή γύρου"));
        this.add(roundInfoBox, BorderLayout.CENTER);


        JButton beginButton = new JButton("Εκκίνηση γύρου");
        beginButton.setSelected(true);
        beginButton.addActionListener(beginButtonListener);
        this.add(beginButton, BorderLayout.PAGE_END);
    }
}
