package raid;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executor;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputAdapter;

import org.json.JSONObject;

public class TextReader {
    //private String fileName;
    int tankCounter;
    int healerCounter;
    int dpsCounter;

    public TextReader() {
    }

    public void search(final HashMap<String, String> characters,
                       final GroupPanel groupPanel,
                       final HashMap<String, Integer> itemLevel, final JPanel groupWindow,
                       final JPanel listPanel, final JPanel charWindow, final JFrame frame,
                       final ArrayList<String> missingGems, final ArrayList<String> missingEnchants, final JProgressBar prog,
                       final JLabel namePL, int tankCount, int healerCount, int dpsCount, File selectedFile)
           throws IOException{
        BufferedReader file = new BufferedReader(new InputStreamReader(new FileInputStream(selectedFile), "ISO-8859-1"));
        String line;
        tankCounter = tankCount;
        healerCounter = healerCount;
        dpsCounter = dpsCount;
        while ((line = file.readLine()) != null) {
            if (line.matches("\\D*\\w*-\\w*.\\w*")) {
                final String[] s = line.split("-");
                namePL.setText("<HTML><FONT size =\"2\">Adding " + s[0] + "</FONT></HTML>");
                CharacterPanel characterPanel = new CharacterPanel(s[0].toLowerCase());
                JSONObject obj = characterPanel.getURL(s[0], s[1]);
                characterPanel.getAuditAPI(s[0], s[1]);
                characterPanel.getTalentsAPI(s[0], s[1]);
                if (!s[0].equals("") && obj != null
                        && !characters.containsKey((s[0].toLowerCase())) && obj.length() != 0) {

                    String name = s[0].toLowerCase();
                    final String nameDummy = name;
                    characters.put(nameDummy, characterPanel.wowClass());
                    name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                    final String capitalName = name;
                    final JPanel profileButton = new JPanel(new BorderLayout());
                    final JPanel buttonCross = new JPanel(new FlowLayout());
                    final JButton rButton = new JButton();
                    final JPanel iconPanel = new JPanel(new FlowLayout());
                    final JLabel nameL = new JLabel(name, JLabel.CENTER);

                    // Add role icon to iconPanel
                    final String role = characterPanel.getRole();
                    // Go through jsonobject to check which class the player is
                    // and choose icon and colour based on that.
                    // Using default class colours from:
                    // http://www.wowwiki.com/Class_colors
                    // Go through jsonobject to check which class the player is
                    // and choose icon and colour based on that.
                    // Using default class colours from:
                    // http://www.wowwiki.com/Class_colors
                    try {
                        String playerClass = obj.get("class").toString();
                        switch (Integer.parseInt(playerClass)) {
                            case 1:
                                JLabel picLabelWarrior = new JLabel(new ImageIcon("images/warrior.png"));
                                iconPanel.add(picLabelWarrior);
                                float[] colorWarrior = Color.RGBtoHSB(199, 156, 110, null);
                                profileButton.setBackground(Color.getHSBColor(colorWarrior[0], colorWarrior[1], colorWarrior[2]));
                                break;
                            case 2:
                                JLabel picLabelPaladin = new JLabel(new ImageIcon("images/paladin.png"));
                                iconPanel.add(picLabelPaladin);
                                float[] colorPaladin = Color.RGBtoHSB(245, 140, 186, null);
                                profileButton.setBackground(Color.getHSBColor(colorPaladin[0], colorPaladin[1], colorPaladin[2]));
                                ;
                                break;
                            case 3:
                                JLabel picLabelHunter = new JLabel(new ImageIcon("images/hunter.png"));
                                iconPanel.add(picLabelHunter);
                                float[] colorHunter = Color.RGBtoHSB(171, 212, 115, null);
                                profileButton.setBackground(Color.getHSBColor(colorHunter[0], colorHunter[1], colorHunter[2]));
                                break;
                            case 4:
                                JLabel picLabelRogue = new JLabel(new ImageIcon("images/rogue.png"));
                                iconPanel.add(picLabelRogue);
                                float[] colorRogue = Color.RGBtoHSB(255, 245, 105, null);
                                profileButton.setBackground(Color.getHSBColor(colorRogue[0], colorRogue[1], colorRogue[2]));
                                break;
                            case 5:
                                JLabel picLabelPriest = new JLabel(new ImageIcon("images/priest.png"));
                                iconPanel.add(picLabelPriest);
                                float[] colorPriest = Color.RGBtoHSB(255, 255, 255, null);
                                profileButton.setBackground(Color.getHSBColor(colorPriest[0], colorPriest[1], colorPriest[2]));
                                buttonCross.setBackground(Color.getHSBColor(colorPriest[0], colorPriest[1], colorPriest[2]));
                                break;
                            case 6:
                                JLabel picLabelDeathknight = new JLabel(new ImageIcon("images/deathknight.png"));
                                iconPanel.add(picLabelDeathknight);
                                float[] colorDeathKnight = Color.RGBtoHSB(196, 30, 59, null);
                                profileButton.setBackground(Color.getHSBColor(colorDeathKnight[0], colorDeathKnight[1], colorDeathKnight[2]));
                                break;
                            case 7:
                                JLabel picLabelShaman = new JLabel(new ImageIcon("images/shaman.png"));
                                iconPanel.add(picLabelShaman);
                                float[] colorShaman = Color.RGBtoHSB(0, 112, 222, null);
                                profileButton.setBackground(Color.getHSBColor(colorShaman[0], colorShaman[1], colorShaman[2]));
                                break;
                            case 8:
                                JLabel picLabelMage = new JLabel(new ImageIcon("images/mage.png"));
                                iconPanel.add(picLabelMage);
                                float[] colorMage = Color.RGBtoHSB(105, 204, 240, null);
                                profileButton.setBackground(Color.getHSBColor(colorMage[0], colorMage[1], colorMage[2]));
                                break;
                            case 9:
                                JLabel picLabelWarlock = new JLabel(new ImageIcon("images/warlock.png"));
                                iconPanel.add(picLabelWarlock);
                                float[] colorWarlock = Color.RGBtoHSB(148, 130, 201, null);
                                profileButton.setBackground(Color.getHSBColor(colorWarlock[0], colorWarlock[1], colorWarlock[2]));
                                break;
                            case 10:
                                JLabel picLabelMonk = new JLabel(new ImageIcon("images/monk.png"));
                                iconPanel.add(picLabelMonk);
                                float[] colorMonk = Color.RGBtoHSB(0, 255, 150, null);
                                profileButton.setBackground(Color.getHSBColor(colorMonk[0], colorMonk[1], colorMonk[2]));
                                break;
                            case 11:
                                JLabel picLabelDruid = new JLabel(new ImageIcon("images/druid.png"));
                                iconPanel.add(picLabelDruid);
                                float[] colorDruid = Color.RGBtoHSB(255, 125, 10, null);
                                profileButton.setBackground(Color.getHSBColor(colorDruid[0], colorDruid[1], colorDruid[2]));
                                break;
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    if (role.equals("TANK")){
                        tankCounter ++;
                        ImageIcon img = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/tank.png").getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
                        JLabel roleLabel = new JLabel(img);
                        iconPanel.add(roleLabel);
                    } else if (role.equals("HEALING")){
                        healerCounter ++;
                        ImageIcon img = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/healer.png").getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
                        JLabel roleLabel = new JLabel(img);
                        iconPanel.add(roleLabel);
                    } else if (role.equals("DPS")){
                        dpsCounter ++;
                        ImageIcon img = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/dps.png").getScaledInstance(20,20, java.awt.Image.SCALE_SMOOTH));
                        JLabel roleLabel = new JLabel(img);
                        iconPanel.add(roleLabel);
                    }else{
                        //do nothing
                    }

                    // Update group panel
                    groupPanel.removeAll();
                    itemLevel.put(nameDummy, characterPanel.itemLevelValue());
                    groupPanel.avgItemLevelGroup(itemLevel);
                    groupPanel.tierUsers(characters); // ...and the tierUsers
                    if(characterPanel.missingGems()){
                        missingGems.add(name);
                        groupPanel.missingGems(missingGems);
                    } else{
                        groupPanel.missingGems(missingGems);
                    }
                    if(characterPanel.missingEnchants()){
                        missingEnchants.add(name);
                        groupPanel.missingEnchants(missingEnchants);
                    }else{
                        groupPanel.missingEnchants(missingEnchants);
                    }
                    groupPanel.roleCount(tankCounter, healerCounter, dpsCounter);
                    groupWindow.add(groupPanel, BorderLayout.CENTER);

                    profileButton.add(nameL, BorderLayout.CENTER);
                    profileButton.setToolTipText("Click to open profile!");
                    profileButton.setBorder(new EmptyBorder(0, 10, 0, 0));
                    ImageIcon ic = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/cross.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH));
                    rButton.setIcon(ic);
                    rButton.setPreferredSize(new Dimension(16, 16));
                    rButton.setPreferredSize(new Dimension(16, 16));
                    rButton.setOpaque(false);
                    rButton.setContentAreaFilled(false);
                    rButton.setBorderPainted(false);
                    iconPanel.setOpaque(false);
                    buttonCross.add(rButton);
                    profileButton.add(buttonCross, BorderLayout.EAST);
                    buttonCross.setOpaque(false);

                    // Retrieve player and realm values from text file.
                    final String player = s[0];
                    final String realm = s[1];


                    profileButton.add(iconPanel, BorderLayout.WEST);
                    final Color defaultColor = profileButton.getBackground();
                    final Color highlightColor = profileButton.getBackground().darker();

                    // Set mouselisteners for the newly created button.
                    profileButton.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            profileButton.setBackground(highlightColor);
                        }

                        public void mouseExited(MouseEvent e) {
                            profileButton.setBackground(defaultColor);
                        }
                    });
                    rButton.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (role.equals("TANK")){
                                tankCounter --;
                            } else if (role.equals("HEALING")){
                                healerCounter --;
                            } else if (role.equals("DPS")){
                                dpsCounter --;
                            }else{
                                //do nothing
                            }
                            groupPanel.removeAll();
                            characters.remove(nameDummy); // Remove character from duplicate check
                            itemLevel.remove(nameDummy); // Remove character from average item level hashmap
                            missingEnchants.remove(capitalName);
                            missingGems.remove(capitalName);
                            groupPanel.avgItemLevelGroup(itemLevel);
                            groupPanel.tierUsers(characters);
                            groupPanel.missingGems(missingGems);
                            groupPanel.missingEnchants(missingEnchants);
                            groupPanel.roleCount(tankCounter, healerCounter, dpsCounter);
                            groupWindow.add(groupPanel, BorderLayout.CENTER);
                            // If charWindow is currently showing info about the
                            // character about to be deleted, clear the window
                            if (charWindow.getComponentCount() != 0) {
                                if (nameDummy.equals(charWindow.getComponent(0)
                                        .getName())) {
                                    charWindow.removeAll();
                                }
                            }
                            listPanel.remove(profileButton);
                            frame.validate();
                            frame.repaint();
                        }
                    });

                    listPanel.add(profileButton);
                    frame.validate();
                    frame.repaint();

                    profileButton.addMouseListener(new MouseInputAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            prog.setIndeterminate(true);
                            Executor executor = java.util.concurrent.Executors.newSingleThreadExecutor();
                            executor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    final CharacterPanel characterPanel = new CharacterPanel(nameDummy);

                                    // Read from wow api
                                    characterPanel.getURL(player, realm);
                                    characterPanel.getAuditAPI(player, realm);
                                    // Call methods to retrieve information from api and add the information as labels to charWindow
                                    characterPanel.getCharacterName();
                                    characterPanel.getLevel();
                                    characterPanel.getCharacterClass();
                                    characterPanel.getProfessions();
                                    characterPanel.getItemLevel();
                                    characterPanel.getAchievementPoints();
                                    characterPanel.enchanted();
                                    characterPanel.checkGems();
                                    try {
                                        characterPanel.getArmoryLink(player, realm);
                                    } catch (URISyntaxException e1) {
                                        e1.printStackTrace();
                                    }
                                    charWindow.removeAll();
                                    charWindow.add(characterPanel, BorderLayout.CENTER);
                                    frame.validate();
                                    frame.repaint();

                                    SwingUtilities.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            prog.setIndeterminate(false);
                                        }
                                    });
                                }

                            });
                        }
                    });
                } else {
                    JOptionPane.showMessageDialog(listPanel, "Please make sure that you have chosen the correct .txt file. Also check that the characters you wish to add exist and that that they are" +
                            " added in the format Character name-Server and that there are no duplicates" , "Character or realm not found", JOptionPane.ERROR_MESSAGE);
                    break;
                }
            }
        }
        file.close();
    }
}