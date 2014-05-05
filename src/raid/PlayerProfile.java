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

public class PlayerProfile extends JPanel{
    private JSONObject object;
    public PlayerProfile(final JScrollPane scrollPane, final JPanel topPanel){

        JButton backButton = new JButton("\u22b2Back");
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
        );
        add(backButton);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder(null, "Character information", TitledBorder.TOP, TitledBorder.TOP, new Font("times new roman", Font.PLAIN, 12), Color.BLACK));
    }
    public JSONObject getURL(String character, String realm) {
        String characterName = character;
        String realmName = realm.replaceAll(" ", "%20");
        try {
            URL profile = new URL("http://eu.battle.net/api/wow/character/" + realmName + "/" + characterName + "?fields=items,professions");
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(profile.openStream()));

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
    public void getPlayerName(String character, String realm) {
        try {
            JSONObject obj = getURL(character, realm);
                add(new JLabel("Player name: " + obj.get("name")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getProfessions(String character, String realm) {
        try {
            JSONObject obj = getURL(character, realm);
            if (obj.getJSONObject("professions").getJSONArray("primary").length() == 0){
                add(new JLabel("Professions: No professions"));
            }else if(obj.getJSONObject("professions").getJSONArray("primary").get(0) != null && obj.getJSONObject("professions").getJSONArray("primary").isNull(1)){
                add(new JLabel("Professions:  " + obj.getJSONObject("professions").getJSONArray("primary").getJSONObject(0).get("name") +
                        " [" + obj.getJSONObject("professions").getJSONArray("primary").getJSONObject(0).get("rank") + "] "));
            }else {
                add(new JLabel("Professions:  " + obj.getJSONObject("professions").getJSONArray("primary").getJSONObject(0).get("name") +
                        " [" + obj.getJSONObject("professions").getJSONArray("primary").getJSONObject(0).get("rank") + "] " + ", "
                        + obj.getJSONObject("professions").getJSONArray("primary").getJSONObject(1).get("name") + " [" +
                        obj.getJSONObject("professions").getJSONArray("primary").getJSONObject(1).get("rank") + "]"));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    public void getLevel(String character, String realm) {
        try {
            JSONObject obj = getURL(character, realm);
            add(new JLabel("Level: " + obj.get("level")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 1 = Warrior, 2 = Paladin, 3 = Hunter, 4 = Rogue, 5 = Priest, 6 = Death Knight, 7 = Shaman, , 8 = Mage, 9 = Warlock, 10 = Monk, 11 = Druid
     */
    public void getClass(String character, String realm){
        try{
        JSONObject obj = getURL(character, realm);
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
        add(new JLabel("Class: " + classMap.get(obj.get("class"))));
        } catch (Exception e) {
        e.printStackTrace();
    }
    }

    public void getItemLevel(String character, String realm) {
        try {
            JSONObject obj = getURL(character, realm);
            add(new JLabel("Item level: " + obj.getJSONObject("items").get("averageItemLevelEquipped")));
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}