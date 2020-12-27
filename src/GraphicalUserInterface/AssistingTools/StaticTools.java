package GraphicalUserInterface.AssistingTools;

import javax.swing.*;
import java.awt.*;

public class StaticTools {

    public static JPanel wrapInFlowLayout(JComponent component){
        JPanel temp = new JPanel(new FlowLayout());
        temp.add(component);
        return temp;
    }

    public static void showTable(Frame owner, Object[][] tableElements){
        new ScoreboardDialog(owner, tableElements);
    }


}
