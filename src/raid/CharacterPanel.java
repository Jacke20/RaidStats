package raid;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * This class holds a panel with character information. The panel is externally added to MainFrame to display the
 * information that the panel holds.
 */
public class CharacterPanel extends JPanel {
    private JSONObject object;
    private JSONObject obj;
    private HashMap<String, String> items = new HashMap<String, String>();
    private HashMap<String, Integer> numberOfSockets = new HashMap<String, Integer>();
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
            BufferedReader in = new BufferedReader(new InputStreamReader(profile.openStream(), "UTF-8"));

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

    public JSONObject getItemAPI(String id) {
        try {
            URL profile = new URL("http://eu.battle.net/api/wow/item/" + id);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(profile.openStream(), "UTF-8"));

            String inputLine;
            String json = "";
            while ((inputLine = in.readLine()) != null) {
                json = inputLine + json;
            }
            in.close();
            obj = new JSONObject(json);
        } catch (Exception e) {
        }
        return obj;
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

    public void getAchievementPoints(){
        try{
            JLabel label = new JLabel("Achievement points: " + object.get("achievementPoints"));
            label.setFont(new Font("times new roman", Font.PLAIN, 11));
            add(label);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void getArmoryLink(String character, String realm) throws URISyntaxException {
        String characterName = character;
        String realmName = realm.replaceAll(" ", "-");
    	final URI uri = new URI("http://eu.battle.net/wow/en/character/" + realmName + "/" + characterName + "/advanced");
    	final JLabel label = new JLabel();
        label.setText("<HTML><FONT face =\"times new roman\" size = \"2.5\" color=\"#000099\">Armory link</FONT></HTML>");
        label.setToolTipText(uri.toString());
    	class OpenUrlAction implements MouseListener {
    	      @Override public void mouseClicked(MouseEvent e) {
    	        open(uri);
    	      }

			@Override
			public void mouseEntered(MouseEvent e) {
				label.setText("<HTML><FONT face =\"times new roman\" size = \"2.5\" color=\"#000099\"><U>Armory link</U></FONT></HTML>");
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				label.setText("<HTML><FONT face =\"times new roman\" size = \"2.5\" color=\"#000099\">Armory link</FONT></HTML>");
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
    	    }
    	label.addMouseListener(new OpenUrlAction());
        add(label);
    }
    
    private static void open(URI uri) {
        if (Desktop.isDesktopSupported()) {
          try {
            Desktop.getDesktop().browse(uri);
          } catch (IOException e) { /* TODO: error handling */ }
        } else { /* TODO: error handling */ }
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

    /**
     * Check if the arraylist returned from getSocketsArray() is filled with true only.
     * If it is, character if fully gemmed, else the character is not fully gemmed.
     */
    public void checkGems(){
        fillItems();
        getNumberOfSockets();
        ArrayList list = getSocketsArray();
        for (int i = 0; i < list.size(); i++){
            boolean hasGem = ((Boolean) list.get(i)).booleanValue();
            if (!hasGem){
                JLabel label = new JLabel("Fully gemmed: No");
                label.setFont(new Font("times new roman", Font.PLAIN, 11));
                add(label);
                return;
            }
        }
        JLabel label = new JLabel("Fully gemmed: Yes");
        label.setFont(new Font("times new roman", Font.PLAIN, 11));
        add(label);
    }

    /**
     * Go through the gem slots for each of the characters items if there are any and fill an arraylist with true if the
     * slots are filled, else fill it with false.
     * @return the arraylist that holds true or false values
     */
   private ArrayList getSocketsArray(){
       Iterator it = numberOfSockets.entrySet().iterator();
       ArrayList <Boolean> hasSockets = new ArrayList<Boolean>();
       while (it.hasNext()) {
           Map.Entry pairs = (Map.Entry) it.next();
           try {
               for(int i = 0; i < Integer.parseInt(pairs.getValue().toString()); i ++){ // Go through each gem slot
                   if(object.getJSONObject("items").getJSONObject(pairs.getKey().toString()).getJSONObject("tooltipParams").has("gem" + i)){ // Check if there is a gem
                       hasSockets.add(true);
                   }else{
                       hasSockets.add(false);
                   }
               }
           }catch(Exception e){
               e.printStackTrace();
           }
       }
       return hasSockets;
    }

    /**
     * Goes through the items ids with the wow item api to find out how many sockets they can hold.
     */
    private void getNumberOfSockets() {
        Iterator it = items.entrySet().iterator();
        while (it.hasNext()) {
            int socketCounter = 0;
            Map.Entry pairs = (Map.Entry) it.next();
            try {
                obj = getItemAPI(pairs.getValue().toString());
                if (obj.getBoolean("hasSockets")){
                for (int i = 0; i < 4; i++) {
                     if(!obj.getJSONObject("socketInfo").getJSONArray("sockets").isNull(i)) {
                        socketCounter ++;
                    }
                }
                numberOfSockets.put(pairs.getKey().toString(), socketCounter);
                }
        }catch(Exception e){
                e.printStackTrace();
        }
    }
    }

    /**
     * Add characters items as keys for the items ids to a hashmap
     */
    private void fillItems(){
        try {
            if (object.getJSONObject("items").has("trinket1")){
                items.put("trinket1", object.getJSONObject("items").getJSONObject("trinket1").get("id").toString());
            }
            if (object.getJSONObject("items").has("trinket2")){
                items.put("trinket2", object.getJSONObject("items").getJSONObject("trinket2").get("id").toString());
            }
            if (object.getJSONObject("items").has("feet")){
                items.put("feet", object.getJSONObject("items").getJSONObject("feet").get("id").toString());
            }
            if (object.getJSONObject("items").has("chest")){
                items.put("chest", object.getJSONObject("items").getJSONObject("chest").get("id").toString());
            }
            if (object.getJSONObject("items").has("hands")){
                items.put("hands", object.getJSONObject("items").getJSONObject("hands").get("id").toString());
            }
            if (object.getJSONObject("items").has("back")){
                items.put("back", object.getJSONObject("items").getJSONObject("back").get("id").toString());
            }
            if (object.getJSONObject("items").has("neck")){
                items.put("neck", object.getJSONObject("items").getJSONObject("neck").get("id").toString());
            }
            if (object.getJSONObject("items").has("wrist")){
                items.put("wrist", object.getJSONObject("items").getJSONObject("wrist").get("id").toString());
            }
            if (object.getJSONObject("items").has("finger1")){
                items.put("finger1", object.getJSONObject("items").getJSONObject("finger1").get("id").toString());
            }
            if (object.getJSONObject("items").has("finger2")){
                items.put("finger2", object.getJSONObject("items").getJSONObject("finger2").get("id").toString());
            }
            if (object.getJSONObject("items").has("head")){
                items.put("head", object.getJSONObject("items").getJSONObject("head").get("id").toString());
            }
            if (object.getJSONObject("items").has("mainHand")){
                items.put("mainHand", object.getJSONObject("items").getJSONObject("mainHand").get("id").toString());
            }
            if (object.getJSONObject("items").has("legs")){
                items.put("legs", object.getJSONObject("items").getJSONObject("legs").get("id").toString());
            }
            if (object.getJSONObject("items").has("waist")){
                items.put("waist", object.getJSONObject("items").getJSONObject("waist").get("id").toString());
            }
            if (object.getJSONObject("items").has("shoulder")){
                items.put("shoulder", object.getJSONObject("items").getJSONObject("shoulder").get("id").toString());
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}