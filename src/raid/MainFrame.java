package raid;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.MouseInputAdapter;

import org.json.JSONObject;

//TODO: Implement option to clear entire list.
//TODO: Allow for characters with same name from different servers to be added.
//TODO: Add more information about the whole group

/**
 * This class holds the main window and its components. Allows the user to enter a player name and realm
 * for the game World of Warcraft to retrieve and display some interesting facts about their character as well as
 * some information about the profiles in relation to eachother.
 * Created by Jacke on 2014-04-29.
 */
public class MainFrame{
    private static ArrayList<String> characters = new ArrayList<String>();
    private static HashMap<String, Integer> itemLevel = new HashMap<String, Integer>();
    public MainFrame(){
    }
    // Use Nimbus and default UI
    public static void main(String[] args){
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
        // Initialize panels
        final RealmList realmList = new RealmList();
        final GroupPanel groupPanel = new GroupPanel();
        final JFrame frame = new JFrame("RaidAssister");
        final JPanel topPanel = new JPanel();
        final JPanel listPanel = new JPanel();
        final JPanel charWindow = new JPanel();
        final JPanel groupWindow = new JPanel();

        // Add a menu bar
        final JMenuBar menu = new JMenuBar();
        final JMenuItem menuItem = new JMenuItem("Clear");

        // Modify panel settings
        charWindow.setLayout(new BorderLayout());
        charWindow.setBorder(BorderFactory.createTitledBorder(null, "Character Information", TitledBorder.TOP, TitledBorder.TOP, new Font("times new roman", Font.PLAIN, 12), Color.BLACK));
        charWindow.setPreferredSize(new Dimension(320, 200));
        groupWindow.setLayout(new BorderLayout());
        groupWindow.setBorder(BorderFactory.createTitledBorder(null, "Group Information", TitledBorder.TOP, TitledBorder.TOP, new Font("times new roman", Font.PLAIN, 12), Color.BLACK));
        groupWindow.setPreferredSize(new Dimension(320, 200));
        // Set layout and panel settings for listPanel
        GridLayout g = new GridLayout(5, 5);
        g.setHgap(5);
        g.setVgap(5);
        listPanel.setLayout(g);
        listPanel.setBorder(BorderFactory.createTitledBorder(null, "Added characters", TitledBorder.TOP, TitledBorder.TOP, new Font("times new roman", Font.PLAIN, 12), Color.BLACK));
        listPanel.setForeground(Color.WHITE);
        listPanel.setPreferredSize(new Dimension(400, 300));

        // Create components
        final JButton addCharacterButton = new JButton("Add character");
        final JButton rFileButton = new JButton("Read from file...");
        final JTextField textFieldCharacter = new JTextField(10);
        final JLabel characterTag = new JLabel("Character:");
        final JLabel realmTag = new JLabel("Realm:");
        final JComboBox<String> realms = new JComboBox<String>(realmList.getRealm());
        final JPanel bottomPanel = new JPanel(new GridLayout(1, 2));

        // Modify frame settings
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("wow.png")));
        frame.setJMenuBar(menu);
        // Set addPlayerButton as the default button to listen to enter.
        frame.getRootPane().setDefaultButton(addCharacterButton);

        // Add components to panels and menu
        topPanel.add(characterTag);
        topPanel.add(textFieldCharacter);
        topPanel.add(realmTag);
        topPanel.add(realms);
        topPanel.add(addCharacterButton);
        topPanel.add(rFileButton);
        bottomPanel.add(charWindow);
        bottomPanel.add(groupWindow);
        menu.add(menuItem);

        // Add panels to content pane and set the program to use the menu created
        Container contentPane = frame.getContentPane();
        contentPane.add(topPanel, BorderLayout.NORTH);
        contentPane.add(listPanel, BorderLayout.CENTER);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
        menuItem.setToolTipText("Clear the panel of all profiles");

        // Add clear function to menu.
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear panels and lists
                characters.clear();
                groupPanel.removeAll();
                itemLevel.clear();
                // Call avgItemLevelGroup with empty hashmap
                groupPanel.avgItemLevelGroup(itemLevel);
                // Add the empty hashmap to groupWindow
                groupWindow.add(groupPanel, BorderLayout.CENTER);
                // Clear listpanel and repaint
                listPanel.removeAll();
                frame.validate();
                frame.repaint();
            }
        });

        //Implement actions for read from file button
        rFileButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TextReader tr = new TextReader();
				
				try {
					tr.search(characters, groupPanel, itemLevel, groupWindow, listPanel, charWindow, frame);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
        
        // Implement actions.
        addCharacterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final CharacterPanel characterPanel = new CharacterPanel();
                JSONObject obj = characterPanel.getURL(textFieldCharacter.getText(), realms.getSelectedItem().toString());

                // If there are no duplicates and both character name and realm are valid, proceed to button creation
                if (!textFieldCharacter.getText().equals("") && obj != null && !characters.contains(textFieldCharacter.getText().toLowerCase())){

                    // Add the character name to an arraylist to keep track of duplicates
                    String name = textFieldCharacter.getText().toLowerCase();
                    final String nameDummy = name;
                    characters.add(nameDummy);
                    name = Character.toUpperCase(name.charAt(0)) + name.substring(1);

                    // Create profile button
                    final JPanel profileButton = new JPanel(new BorderLayout());

                    // Add a remove button inside a panel
                    final JPanel buttonCross = new JPanel(new FlowLayout());
                    final JButton rButton = new JButton();
                    // Create label for the profile button
                    final JLabel nameL = new JLabel(name, JLabel.CENTER);

                    // Update groupWindow
                    groupPanel.removeAll();
                    itemLevel.put(nameDummy, characterPanel.itemLevelValue()); // Add player and the corresponding ilvl to a hashmap
                    groupPanel.avgItemLevelGroup(itemLevel); // Call the avgItem
                    groupWindow.add(groupPanel, BorderLayout.CENTER);

                    // Modify settings for profile button and add the remove button to it
                    profileButton.add(nameL, BorderLayout.CENTER);
                    profileButton.setToolTipText("Click to open profile!");
                    rButton.setIcon(new ImageIcon("images/cross.png"));
                    rButton.setPreferredSize(new Dimension(16, 16));
                    rButton.setOpaque(false);
                    rButton.setContentAreaFilled(false);
                    rButton.setBorderPainted(false);
                    buttonCross.add(rButton);
                    profileButton.add(buttonCross, BorderLayout.EAST);
                    profileButton.setBorder(new EmptyBorder(0, 10, 0, 0));
                    buttonCross.setOpaque(false);

                    // Retrieve player and realm values from text fields.
                    final String player = textFieldCharacter.getText();
                    final String realm = realms.getSelectedItem().toString();

                    // Go through jsonobject to check which class the player is and choose icon and colour based on that.
                    // Using default class colours from: http://www.wowwiki.com/Class_colors
                    try {
                        String playerClass = obj.get("class").toString();
                        switch (Integer.parseInt(playerClass)) {
                            case 1:
                                JLabel picLabelWarrior = new JLabel(new ImageIcon("images/warrior.png"));
                                profileButton.add(picLabelWarrior, BorderLayout.WEST);
                                float[] colorWarrior = Color.RGBtoHSB(199, 156, 110, null);
                                profileButton.setBackground(Color.getHSBColor(colorWarrior[0], colorWarrior[1], colorWarrior[2]));
                                //button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 500, 10, true));
                                break;
                            case 2:
                                JLabel picLabelPaladin = new JLabel(new ImageIcon("images/paladin.png"));
                                profileButton.add(picLabelPaladin, BorderLayout.WEST);
                                float[] colorPaladin = Color.RGBtoHSB(245, 140, 186, null);
                                profileButton.setBackground(Color.getHSBColor(colorPaladin[0], colorPaladin[1], colorPaladin[2]));
                                //button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 500, 10, true));
                                break;
                            case 3:
                                JLabel picLabelHunter = new JLabel(new ImageIcon("images/hunter.png"));
                                profileButton.add(picLabelHunter, BorderLayout.WEST);
                                float[] colorHunter = Color.RGBtoHSB(171, 212, 115, null);
                                profileButton.setBackground(Color.getHSBColor(colorHunter[0], colorHunter[1], colorHunter[2]));
                                //button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 500, 10, true));
                                break;
                            case 4:
                                JLabel picLabelRogue = new JLabel(new ImageIcon("images/rogue.png"));
                                profileButton.add(picLabelRogue, BorderLayout.WEST);
                                float[] colorRogue = Color.RGBtoHSB(255, 245, 105, null);
                                profileButton.setBackground(Color.getHSBColor(colorRogue[0], colorRogue[1], colorRogue[2]));
                                //button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 500, 10, true));
                                break;
                            case 5:
                                JLabel picLabelPriest = new JLabel(new ImageIcon("images/priest.png"));
                                profileButton.add(picLabelPriest, BorderLayout.WEST);
                                float[] colorPriest = Color.RGBtoHSB(255, 255, 255, null);
                                profileButton.setBackground(Color.getHSBColor(colorPriest[0], colorPriest[1], colorPriest[2]));
                                buttonCross.setBackground(Color.getHSBColor(colorPriest[0], colorPriest[1], colorPriest[2]));
                                //button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 500, 10, true));
                                break;
                            case 6:
                                JLabel picLabelDeathknight = new JLabel(new ImageIcon("images/deathknight.png"));
                                profileButton.add(picLabelDeathknight, BorderLayout.WEST);
                                float[] colorDeathKnight = Color.RGBtoHSB(196, 30, 59, null);
                                profileButton.setBackground(Color.getHSBColor(colorDeathKnight[0], colorDeathKnight[1], colorDeathKnight[2]));
                                //button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 500, 10, true));
                                break;
                            case 7:
                                JLabel picLabelShaman = new JLabel(new ImageIcon("images/shaman.png"));
                                profileButton.add(picLabelShaman, BorderLayout.WEST);
                                float[] colorShaman = Color.RGBtoHSB(0, 112, 222, null);
                                profileButton.setBackground(Color.getHSBColor(colorShaman[0], colorShaman[1], colorShaman[2]));
                                //button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 500, 10, true));
                                break;
                            case 8:
                                JLabel picLabelMage = new JLabel(new ImageIcon("images/mage.png"));
                                profileButton.add(picLabelMage, BorderLayout.WEST);
                                float[] colorMage = Color.RGBtoHSB(105, 204, 240, null);
                                profileButton.setBackground(Color.getHSBColor(colorMage[0], colorMage[1], colorMage[2]));
                                //button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 500, 10, true));
                                break;
                            case 9:
                                JLabel picLabelWarlock = new JLabel(new ImageIcon("images/warlock.png"));
                                profileButton.add(picLabelWarlock, BorderLayout.WEST);
                                float[] colorWarlock = Color.RGBtoHSB(148, 130, 201, null);
                                profileButton.setBackground(Color.getHSBColor(colorWarlock[0], colorWarlock[1], colorWarlock[2]));
                                //button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 500, 10, true));
                                break;
                            case 10:
                                JLabel picLabelMonk = new JLabel(new ImageIcon("images/monk.png"));
                                profileButton.add(picLabelMonk, BorderLayout.WEST);
                                float[] colorMonk = Color.RGBtoHSB(0, 255, 150, null);
                                profileButton.setBackground(Color.getHSBColor(colorMonk[0], colorMonk[1], colorMonk[2]));
                                //button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 500, 10, true));
                                break;
                            case 11:
                                JLabel picLabelDruid = new JLabel(new ImageIcon("images/druid.png"));
                                profileButton.add(picLabelDruid, BorderLayout.WEST);
                                float[] colorDruid = Color.RGBtoHSB(255, 125, 10, null);
                                profileButton.setBackground(Color.getHSBColor(colorDruid[0], colorDruid[1], colorDruid[2]));
                                //button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 500, 10, true));
                                break;
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    // Create own highlighting
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

                    // Implement actions for remove button
                    rButton.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            groupPanel.removeAll();
                            characters.remove(nameDummy); // Remove character from duplicate check
                            itemLevel.remove(nameDummy); // Remove character from average item level hashmap
                            groupPanel.avgItemLevelGroup(itemLevel);
                            groupWindow.add(groupPanel, BorderLayout.CENTER);
                            listPanel.remove(profileButton);
                            frame.validate();
                            frame.repaint();
                        }
                    });

                    // Add profileButton to listPanel and repaint
                    listPanel.add(profileButton);
                    frame.validate();
                    frame.repaint();

                    // Success popup
                    JOptionPane.showMessageDialog(null, "Player successfully added!", "Success!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/success.png"));

                    // Implement action for profileButton
                    profileButton.addMouseListener(new MouseInputAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            final CharacterPanel characterPanel = new CharacterPanel();

                            // Read from wow api
                            characterPanel.getURL(player, realm);
                            // Call methods to retrieve information from api and add the information as labels to charWindow
                            characterPanel.getCharacterName();
                            characterPanel.getLevel();
                            characterPanel.getCharacterClass();
                            characterPanel.getProfessions();
                            characterPanel.getItemLevel();
                            charWindow.removeAll();
                            charWindow.add(characterPanel, BorderLayout.CENTER);
                            frame.validate();
                            frame.repaint();
                        }
                    });
                } else {
                    JOptionPane.showMessageDialog(null, "Please make sure that the name and realm are correct and that there are no duplicates", "Character or realm not found", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        // Modify basic settings for the main frame.
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(660, 630);
        frame.setResizable(true);
        frame.setLocation(350, 50);
        frame.setVisible(true);
    }
}