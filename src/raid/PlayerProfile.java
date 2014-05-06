package raid;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.io.*;
import java.util.HashMap;



import org.json.*;


import javax.swing.*;
import javax.swing.border.TitledBorder;

public class PlayerProfile extends JPanel {
    private JSONObject object;

    public PlayerProfile() {

        /*JButton backButton = new JButton("\u22b2Back");
        backButton.addActionListener(new ActionListener() {
                                         @Override
                                         public void actionPerformed(ActionEvent e) {
                                             removeAll();
                                             setBorder(null);
                                             setLayout(new BorderLayout());
                                             add(topPanel, BorderLayout.NORTH);
                                             add(scrollPane, BorderLayout.CENTER);
                                             validate();
                                             repaint();
                                         }
                                     }
        );*/
        //add(backButton);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //setBorder(BorderFactory.createTitledBorder(null, "Character information", TitledBorder.TOP, TitledBorder.TOP, new Font("times new roman", Font.PLAIN, 12), Color.BLACK));
        setPreferredSize(new Dimension(300, 200));
    }

    public JSONObject getURL(String character, String realm) {
        String characterName = character;
        String realmName = realm.replaceAll(" ", "%20");
        try {
            URL profile = new URL("http://eu.battle.net/api/wow/character/" + realmName + "/" + characterName + "?fields=items,professions");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(profile.openStream(), "UTF-8"));

            String inputLine;
            String json = "";
            while ((inputLine = in.readLine()) != null) {
                json = inputLine + json;
            }
            in.close();
            object = new JSONObject(json);

        } catch (Exception e) {
        }
        return object;
    }

    public void getPlayerName() {
        try {
            JLabel label = new JLabel("Player name: " + object.get("name"));
            label.setFont(new Font("times new roman", Font.PLAIN, 11));
            add(label);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getProfessions() {
        try {
            if (object.getJSONObject("professions").getJSONArray("primary").length() == 0) {
                JLabel label = new JLabel("Professions: No professions");
                label.setFont(new Font("times new roman", Font.PLAIN, 11));
                add(label);
            } else if (object.getJSONObject("professions").getJSONArray("primary").get(0) != null && object.getJSONObject("professions").getJSONArray("primary").isNull(1)) {
                JLabel label = new JLabel("Professions:  " + object.getJSONObject("professions").getJSONArray("primary").getJSONObject(0).get("name") +
                        " [" + object.getJSONObject("professions").getJSONArray("primary").getJSONObject(0).get("rank") + "] ");
                label.setFont(new Font("times new roman", Font.PLAIN, 11));
                add(label);
            } else {
                JLabel label = new JLabel("Professions:  " + object.getJSONObject("professions").getJSONArray("primary").getJSONObject(0).get("name") +
                        " [" + object.getJSONObject("professions").getJSONArray("primary").getJSONObject(0).get("rank") + "] " + ", "
                        + object.getJSONObject("professions").getJSONArray("primary").getJSONObject(1).get("name") + " [" +
                        object.getJSONObject("professions").getJSONArray("primary").getJSONObject(1).get("rank") + "]");
                label.setFont(new Font("times new roman", Font.PLAIN, 11));
                add(label);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void getLevel() {
        try {
            JLabel label = new JLabel("Level: " + object.get("level"));
            label.setFont(new Font("times new roman", Font.PLAIN, 11));
            add(label);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 1 = Warrior, 2 = Paladin, 3 = Hunter, 4 = Rogue, 5 = Priest, 6 = Death Knight, 7 = Shaman, , 8 = Mage, 9 = Warlock, 10 = Monk, 11 = Druid
     */
    public void getCharacterClass() {
        try {
            HashMap classMap = new HashMap();
            classMap.put(1, "Warrior");
            classMap.put(2, "Paladin");
            classMap.put(3, "Hunter");
            classMap.put(4, "Rogue");
            classMap.put(5, "Priest");
            classMap.put(6, "Death Knight");
            classMap.put(7, "Shaman");
            classMap.put(8, "Mage");
            classMap.put(9, "Warlock");
            classMap.put(10, "Monk");
            classMap.put(11, "Druid");
            JLabel label = new JLabel("Class: " + classMap.get(object.get("class")));
            label.setFont(new Font("times new roman", Font.PLAIN, 11));
            add(label);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getItemLevel() {
        try {
            JSONObject items = object.getJSONObject("items");
            JLabel label = new JLabel("Item level: " + items.get("averageItemLevel") + " (" + items.get("averageItemLevelEquipped") + " equipped" + ")");
            label.setFont(new Font("times new roman", Font.PLAIN, 11));
            add(label);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}