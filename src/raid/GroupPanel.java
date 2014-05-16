package raid;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
	
	public void tierUsers(HashMap<String, String> classes){
		String[] vanqU = {
				"Rogue", "Death Knight", "Mage", "Druid"
		};
		String[] protU = {
				"Warrior", "Hunter", "Shaman", "Monk"
		};
		String[] conqU = {
				"Paladin", "Priest", "Warlock"
		};
		int vanq = 0, prot = 0, conq = 0;
		
		if (classes.isEmpty()) {
			//do nothing
		} else {
			for (String s : classes.values()) {
				if(Arrays.asList(vanqU).contains(s)){
					vanq++;
				}else if(Arrays.asList(protU).contains(s)){
					prot++;
				}else if(Arrays.asList(conqU).contains(s)){
					conq++;
				}
			}
			JLabel label = new JLabel("Vanquisher: " + vanq);
			JLabel label1 = new JLabel("Conqueror: " + conq);
			JLabel label2 = new JLabel("Protector: " + prot);
			label.setFont(new Font("times new roman", Font.PLAIN, 11));
			label1.setFont(new Font("times new roman", Font.PLAIN, 11));
			label2.setFont(new Font("times new roman", Font.PLAIN, 11));
			add(label);
			add(label1);
			add(label2);
		}
	}

    public void missingGems(ArrayList list){
        if(list.isEmpty()){
            // do nothing
        }else {
            String listPlayers = list.toString().replace("]", "");
            listPlayers = listPlayers.replace("[", "");
            JLabel label = new JLabel("Missing gems: " + listPlayers);
            label.setFont(new Font("times new roman", Font.PLAIN, 11));
            add(label);
        }
    }
    public void missingEnchants(ArrayList list){
        if(list.isEmpty()){
            // do nothing
        }else {
            String listPlayers = list.toString().replace("]", "");
            listPlayers = listPlayers.replace("[", "");
            JLabel label = new JLabel("Missing enchants: " + listPlayers);
            label.setFont(new Font("times new roman", Font.PLAIN, 11));
            add(label);
        }
    }

    public void roleCount(int tankCount, int healerCount, int dpsCount){
        if(tankCount != 0) {
            JLabel tankLabel = new JLabel("Tanks: " + tankCount);
            tankLabel.setFont(new Font("times new roman", Font.PLAIN, 11));
            add(tankLabel);
        }
        if (healerCount != 0) {
            JLabel healerLabel = new JLabel("Healers: " + healerCount);
            healerLabel.setFont(new Font("times new roman", Font.PLAIN, 11));
            add(healerLabel);
        }
        if(dpsCount != 0) {
            JLabel dpsLabel = new JLabel("Dps: " + dpsCount);
            dpsLabel.setFont(new Font("times new roman", Font.PLAIN, 11));
            add(dpsLabel);
        }
    }
}
