package raid;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executor;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.MouseInputAdapter;

import com.sun.javafx.fxml.builder.JavaFXImageBuilder;
import org.json.JSONObject;

//TODO: Implement option to clear entire list.
//TODO: Allow for characters with same name from different servers to be added.
//TODO: Add more information about the whole group

/**
 * This class holds the main window and its components. Allows the user to enter
 * a player name and realm for the game World of Warcraft to retrieve and
 * display some interesting facts about their character as well as some
 * information about the profiles in relation to eachother. Created by Jacke on
 * 2014-04-29.
 */
public class MainFrame {
    private static HashMap<String, String> characters = new HashMap<String, String>();
    private static HashMap<String, Integer> itemLevel = new HashMap<String, Integer>();
    private static ArrayList<String> missingGems = new ArrayList<String>();
    private static ArrayList<String> missingEnchants = new ArrayList<String>();
    static File file;

    static int tankCount;
    static int healerCount;
    static int dpsCount;

    public MainFrame() {
    }

    // Use Nimbus and default UI
    public static void main(String[] args) {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
        }
        // Initialize panels
        final RealmList realmList = new RealmList();
        final GroupPanel groupPanel = new GroupPanel();
        final JFrame frame = new JFrame("Raid Assister");
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
        final JPanel progPanel = new JPanel(new FlowLayout());
        final JProgressBar prog = new JProgressBar();
        final JLabel namePL = new JLabel();
        rFileButton.setToolTipText("This option allows you to read from a .txt file if you wish to add a whole raidgroup at once." +
                " The format for adding players in this .txt file is Character name-Server.");
        progPanel.setPreferredSize(new Dimension(150, 40));
        namePL.setOpaque(true);
        namePL.setHorizontalAlignment(SwingConstants.CENTER);
        namePL.setPreferredSize(new Dimension(130, 13));
        progPanel.add(namePL, BorderLayout.NORTH);
        namePL.setPreferredSize(new Dimension(130, 13));
        progPanel.add(prog, BorderLayout.SOUTH);
        final JLabel characterTag = new JLabel("Character:");
        final JLabel realmTag = new JLabel("Realm:");
        final JComboBox<String> realms = new JComboBox<String>(realmList.getRealm());
        final JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        final JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("C:\\Users"));

        // Modify frame settings
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("images/wow.png").getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH));
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
        topPanel.add(progPanel);
        bottomPanel.add(charWindow);
        bottomPanel.add(groupWindow);
        menuItem.setToolTipText("Clear the panel of all profiles");
        menu.add(menuItem);

        // Add panels to content pane and set the program to use the menu
        // created
        Container contentPane = frame.getContentPane();
        contentPane.add(topPanel, BorderLayout.NORTH);
        contentPane.add(listPanel, BorderLayout.CENTER);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
        menuItem.setToolTipText("Clear the panel of all profiles");

        // Add clear function to menu.
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear windows and lists
                characters.clear();
                itemLevel.clear();
                missingEnchants.clear();
                missingGems.clear();
                tankCount = 0;
                healerCount = 0;
                dpsCount = 0;
                groupWindow.removeAll();
                charWindow.removeAll();
                listPanel.removeAll();
                frame.validate();
                frame.repaint();
            }
        });

        // Implement actions for read from file button
        rFileButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                Executor executor = java.util.concurrent.Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (e.getSource() == rFileButton) {
                            int returnVal = fc.showOpenDialog(frame);

                            if (returnVal == JFileChooser.APPROVE_OPTION) {
                                file = fc.getSelectedFile();
                                //This is where a real application would open the file.
                            } else {
                                // do nothing
                            }
                            prog.setIndeterminate(true);
                        }
                        TextReader tr = new TextReader();
                        try {
                            tr.search(characters, groupPanel, itemLevel, groupWindow, listPanel, charWindow, frame, missingGems, missingEnchants, prog, namePL, tankCount, healerCount, dpsCount, file);
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                namePL.setText("");
                                prog.setIndeterminate(false);
                            }
                        });
                    }
                });
            }
        });

        // Implement actions.
        addCharacterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!textFieldCharacter.getText().equals("")) {
                    prog.setIndeterminate(true);
                    namePL.setText("<HTML><FONT size =\"2\">Adding " + Character.toUpperCase(textFieldCharacter.getText().charAt(0))
                            + textFieldCharacter.getText().substring(1) + "</FONT></HTML>");
                }
                Executor executor = java.util.concurrent.Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        final CharacterPanel characterPanel = new CharacterPanel(textFieldCharacter.getText().toLowerCase());
                        JSONObject obj = characterPanel.getURL(textFieldCharacter.getText(), realms.getSelectedItem().toString());
                        characterPanel.getAuditAPI(textFieldCharacter.getText(), realms.getSelectedItem().toString());
                        characterPanel.getTalentsAPI(textFieldCharacter.getText(), realms.getSelectedItem().toString());

                        // If there are no duplicates and both character name and realm are valid, proceed to button creation
                        if (!textFieldCharacter.getText().equals("") && obj != null && !characters.containsKey(textFieldCharacter.getText().toLowerCase())
                                && obj.length() != 0) {

                            // Add the character name to an arraylist to keep track of duplicates
                            String name = textFieldCharacter.getText().toLowerCase();
                            final String nameDummy = name;
                            characters.put(nameDummy, characterPanel.wowClass());
                            name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                            final String capitalName = name;

                            // Create profile button
                            final JPanel profileButton = new JPanel(new BorderLayout());

                            // Add a remove button inside a panel
                            final JPanel buttonCross = new JPanel(new FlowLayout());
                            final JPanel iconPanel = new JPanel(new FlowLayout());
                            final JButton rButton = new JButton();
                            // Create label for the profile button
                            final JLabel nameL = new JLabel(name, JLabel.CENTER);

                            // Add role icon to iconPanel
                           final String role = characterPanel.getRole();
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
                                tankCount ++;
                                ImageIcon img = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/tank.png").getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
                                JLabel roleLabel = new JLabel(img);
                                iconPanel.add(roleLabel);
                            } else if (role.equals("HEALING")){
                                healerCount ++;
                                ImageIcon img = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/healer.png").getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
                                JLabel roleLabel = new JLabel(img);
                                iconPanel.add(roleLabel);
                            } else if (role.equals("DPS")){
                                dpsCount ++;
                                ImageIcon img = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/dps.png").getScaledInstance(20,20, java.awt.Image.SCALE_SMOOTH));
                                JLabel roleLabel = new JLabel(img);
                                iconPanel.add(roleLabel);
                            }else{
                                //do nothing
                            }

                            // Update groupWindow
                            groupPanel.removeAll();
                            itemLevel.put(nameDummy, characterPanel.itemLevelValue()); // Add player and the corresponding ilvl to a hashmap
                            groupPanel.avgItemLevelGroup(itemLevel); // Call the avgItem
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
                            } else{
                                groupPanel.missingEnchants(missingEnchants);
                            }
                            groupPanel.roleCount(tankCount, healerCount, dpsCount);
                            groupWindow.add(groupPanel, BorderLayout.CENTER);

                            // Modify settings for profile button and add the remove button to it
                            profileButton.add(nameL, BorderLayout.CENTER);
                            profileButton.setToolTipText("Click to open profile!");
                            ImageIcon ic = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/cross.png").getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH));
                            rButton.setIcon(ic);
                            rButton.setPreferredSize(new Dimension(16, 16));
                            rButton.setOpaque(false);
                            rButton.setContentAreaFilled(false);
                            rButton.setBorderPainted(false);
                            iconPanel.setOpaque(false);
                            buttonCross.add(rButton);
                            profileButton.add(buttonCross, BorderLayout.EAST);
                            profileButton.setBorder(new EmptyBorder(0, 10, 0, 0));
                            buttonCross.setOpaque(false);

                            // Retrieve player and realm values from text fields.
                            final String player = textFieldCharacter.getText();
                            final String realm = realms.getSelectedItem().toString();



                            profileButton.add(iconPanel, BorderLayout.WEST);
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
                                    if (role.equals("TANK")){
                                        tankCount --;
                                    } else if (role.equals("HEALING")){
                                        healerCount --;
                                    } else if (role.equals("DPS")){
                                        dpsCount --;
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
                                    groupPanel.roleCount(tankCount, healerCount, dpsCount);
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

                            // Add profileButton to listPanel and repaint
                            listPanel.add(profileButton);
                            frame.validate();
                            frame.repaint();

                            // Implement action for profileButton
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
                            JOptionPane.showMessageDialog(listPanel, "Please make sure that the name and realm are correct and that there are no duplicates", "Character or realm not found", JOptionPane.ERROR_MESSAGE);
                        }
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                namePL.setText("");
                                prog.setIndeterminate(false);
                            }
                        });
                    }
                });
            }
        });
        // Modify basic settings for the main frame.
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 630);
        frame.setResizable(false);
        frame.setLocation(350, 50);
        frame.setVisible(true);
    }
}