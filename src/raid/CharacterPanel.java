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

/**
 * This class holds a panel with character information. The panel is externally added to MainFrame to display the
 * information that the panel holds.
 */
public class CharacterPanel extends JPanel {
    private JSONObject object;

    public CharacterPanel(String name) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(300, 200));
        setName(name);
    }

    /**
     * Reads from wow api and returns a JSONObject.
     */
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

    /**
     * Add characters name to JLabel.
     */
    public void getCharacterName() {
        try {
            JLabel label = new JLabel("Player name: " + name());
            label.setFont(new Font("times new roman", Font.PLAIN, 11));
            add(label);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get characters professions and add to JLabel.
     */
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

    /**
     * Get characters level and add to JLabel.
     */
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
     * Add characters class to new JLabel.
     */
    public void getCharacterClass() {
        try {
            JLabel label = new JLabel("Class: " + wowClass());
            label.setFont(new Font("times new roman", Font.PLAIN, 11));
            add(label);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add characters item level to a new JLabel.
     */
    public void getItemLevel() {
        try {
            JLabel label = new JLabel("Item level: " + itemLevelValue() + " (" + iLvlEquipped() + " equipped" + ")");
            label.setFont(new Font("times new roman", Font.PLAIN, 11));
            add(label);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get characters item level.
     */
    public int itemLevelValue() {
        try {
            String val = object.getJSONObject("items").get("averageItemLevel").toString();
            return Integer.parseInt(val);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get characters item level equipped.
     */
    public String iLvlEquipped(){
    	try {
			return object.getJSONObject("items").get("averageItemLevelEquipped").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

    }

    /**
     * Get character name.
     */
    public String name(){
    	try {
			return object.get("name").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

    }

    /**
     * Get characters class. Values below are numbers that correspond to specified class in JSONObject.
     * 1 = Warrior, 2 = Paladin, 3 = Hunter, 4 = Rogue, 5 = Priest, 6 = Death Knight, 7 = Shaman, , 8 = Mage, 9 = Warlock, 10 = Monk, 11 = Druid
     */
    public String wowClass(){
    	try {
    		HashMap<Integer, String> classMap = new HashMap<Integer, String>();
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
       
            return classMap.get(object.get("class"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
    }
}