package raid;

import java.awt.Dimension;
import java.awt.Font;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class holds a panel with group information. The panel is externally added to MainFrame to display the
 * information that the panel holds.
 */
public class GroupPanel extends JPanel {
	public GroupPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(300, 200));
	}

	public void avgItemLevelGroup(HashMap<String, Integer> itemLevel) {
		int value = 0;
		if (itemLevel.isEmpty()) {
			//do nothing
		} else {
			for (Integer i : itemLevel.values()) {
				value += i;
			}
			value = value / itemLevel.size();
			JLabel label = new JLabel("Average item level: " + value);
			label.setFont(new Font("times new roman", Font.PLAIN, 11));
			add(label);
		}
	}
}
