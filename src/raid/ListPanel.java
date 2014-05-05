package raid;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by Jacke on 2014-04-29.
 */
public class ListPanel extends JPanel {
    public ListPanel() {
        GridLayout g = new GridLayout(10, 10);
        g.setHgap(5);
        g.setVgap(5);
        setLayout(g);
        setBorder(BorderFactory.createTitledBorder(null, "Added Players", TitledBorder.TOP, TitledBorder.TOP, new Font("times new roman", Font.PLAIN, 12), Color.BLACK));
        setForeground(Color.WHITE);
        setPreferredSize(new Dimension(400, 1000));
    }
}
