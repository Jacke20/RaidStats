package raid;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Jacke on 2014-05-06.
 */
public class GroupInfo extends JPanel{
    public GroupInfo(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(300, 200));
    }
    public void itemLevelGroup(ArrayList<Integer> itemLevel){
        int value = 0;
        for(Integer i: itemLevel){
          value += i;
        }
        value = value/itemLevel.size();
        JLabel label = new JLabel("Average item level: " + value);
        label.setFont(new Font("times new roman", Font.PLAIN, 11));
        add(label);
    }

}
