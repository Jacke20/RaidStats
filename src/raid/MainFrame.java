package raid;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.MouseInputAdapter;

import org.json.JSONObject;

/**
 * This class holds the main window and its components. Allows the user to enter a player name and realm
 * for the game World of Warcraft to retrieve and display some interesting facts about their character.
 * Created by Jacke on 2014-04-29.
 */
public class MainFrame{
    private static ArrayList<String> players = new ArrayList<String>();
    private static HashMap<String, Integer> itemLevel = new HashMap<String, Integer>();
    public MainFrame(){
    }
    // Use Nimbus and default UIManager
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
        final RealmList realmList = new RealmList();
        final GroupInfo groupInfo = new GroupInfo();
        final JFrame frame = new JFrame("RaidAssister");
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("wow.png")));
        final JPanel topPanel = new JPanel();
        final JPanel listPanel = new ListPanel();

        // Create components
        final JButton addPlayerButton = new JButton("Add character");
        final JTextField textFieldPlayer = new JTextField(10);
        final JLabel characterTag = new JLabel("Character:");
        final JLabel realmTag = new JLabel("Realm:");
        final JComboBox<String> realms = new JComboBox<String>(realmList.getRealm());
        final JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        final JPanel charPanel = new JPanel();
        charPanel.setLayout(new BorderLayout());
        charPanel.setBorder(BorderFactory.createTitledBorder(null, "Character Information", TitledBorder.TOP, TitledBorder.TOP, new Font("times new roman", Font.PLAIN, 12), Color.BLACK));
        charPanel.setPreferredSize(new Dimension(320, 200));
        final JPanel grpPanel = new JPanel();
        grpPanel.setLayout(new BorderLayout());
        grpPanel.setBorder(BorderFactory.createTitledBorder(null, "Group Information", TitledBorder.TOP, TitledBorder.TOP, new Font("times new roman", Font.PLAIN, 12), Color.BLACK));
        grpPanel.setPreferredSize(new Dimension(320, 200));

        // Add a scroll pane
        //final JScrollPane scrollPane = new JScrollPane(listPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //scrollPane.setBounds(20, 60, 0, 100);
        listPanel.setPreferredSize(new Dimension(500, 300));

        // Add components to panels
        topPanel.add(characterTag);
        topPanel.add(textFieldPlayer);
        topPanel.add(realmTag);
        topPanel.add(realms);
        topPanel.add(addPlayerButton);
        bottomPanel.add(charPanel);
        bottomPanel.add(grpPanel);

        // Add panels to content pane
        Container contentPane = frame.getContentPane();
        contentPane.add(topPanel, BorderLayout.NORTH);
        contentPane.add(listPanel, BorderLayout.CENTER);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        // Set addPlayerButton as the default button to listen to enter.
        frame.getRootPane().setDefaultButton(addPlayerButton);

        // Implement actions.
        addPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final PlayerProfile p = new PlayerProfile();
                JSONObject obj = p.getURL(textFieldPlayer.getText(), realms.getSelectedItem().toString());

                if (!textFieldPlayer.getText().equals("") && obj != null) { // && !players.contains(textFieldPlayer.getText().toLowerCase())
                    // Create button with players name and modify text-icon relation.

                    String name = textFieldPlayer.getText().toLowerCase();
                    final String nameDummy = name;
                    players.add(nameDummy);
                    name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                    final JPanel button = new JPanel(new BorderLayout());
                    final JPanel buttonCross = new JPanel(new FlowLayout());
                    final JButton rButton = new JButton();
                    final JLabel nameL = new JLabel(name, JLabel.CENTER);
                    
                 // Update group panel
                    groupInfo.removeAll();
                    itemLevel.put(nameDummy, p.itemLevelValue());
                    groupInfo.itemLevelGroup(itemLevel);
                    grpPanel.add(groupInfo, BorderLayout.CENTER);

                    button.add(nameL, BorderLayout.CENTER);
                    button.setToolTipText("Click to open profile!");
                    rButton.setIcon(new ImageIcon("images/cross.png"));
                    rButton.setPreferredSize(new Dimension(16, 16));
                    rButton.setOpaque(false);
                    rButton.setContentAreaFilled(false);
                    rButton.setBorderPainted(false);
                    buttonCross.add(rButton);
                    button.add(buttonCross, BorderLayout.EAST);
                    button.setBorder(new EmptyBorder(0, 10, 0, 0));
                    buttonCross.setOpaque(false);

                    // Retrieve player and realm values from text fields.
                    final String player = textFieldPlayer.getText();
                    final String realm = realms.getSelectedItem().toString();

                    // Go through jsonobject to check which class the player is and choose icon and colour based on that.
                    // Using default class colours from: http://www.wowwiki.com/Class_colors
                    try {
                        String playerClass = obj.get("class").toString();
                        switch (Integer.parseInt(playerClass)) {
                            case 1:
                                JLabel picLabelWarrior = new JLabel(new ImageIcon("images/warrior.png"));
                                button.add(picLabelWarrior, BorderLayout.WEST);
                                float[] colorWarrior = Color.RGBtoHSB(199, 156, 110, null);
                                button.setBackground(Color.getHSBColor(colorWarrior[0], colorWarrior[1], colorWarrior[2]));
                                //button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 500, 10, true));
                                break;
                            case 2:
                                JLabel picLabelPaladin = new JLabel(new ImageIcon("images/paladin.png"));
                                button.add(picLabelPaladin, BorderLayout.WEST);
                                float[] colorPaladin = Color.RGBtoHSB(245, 140, 186, null);
                                button.setBackground(Color.getHSBColor(colorPaladin[0], colorPaladin[1], colorPaladin[2]));
                                //button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 500, 10, true));
                                break;
                            case 3:
                                JLabel picLabelHunter = new JLabel(new ImageIcon("images/hunter.png"));
                                button.add(picLabelHunter, BorderLayout.WEST);
                                float[] colorHunter = Color.RGBtoHSB(171, 212, 115, null);
                                button.setBackground(Color.getHSBColor(colorHunter[0], colorHunter[1], colorHunter[2]));
                                //button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 500, 10, true));
                                break;
                            case 4:
                                JLabel picLabelRogue = new JLabel(new ImageIcon("images/rogue.png"));
                                button.add(picLabelRogue, BorderLayout.WEST);
                                float[] colorRogue = Color.RGBtoHSB(255, 245, 105, null);
                                button.setBackground(Color.getHSBColor(colorRogue[0], colorRogue[1], colorRogue[2]));
                                //button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 500, 10, true));
                                break;
                            case 5:
                                JLabel picLabelPriest = new JLabel(new ImageIcon("images/priest.png"));
                                button.add(picLabelPriest, BorderLayout.WEST);
                                float[] colorPriest = Color.RGBtoHSB(255, 255, 255, null);
                                button.setBackground(Color.getHSBColor(colorPriest[0], colorPriest[1], colorPriest[2]));
                                buttonCross.setBackground(Color.getHSBColor(colorPriest[0], colorPriest[1], colorPriest[2]));
                                //button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 500, 10, true));
                                break;
                            case 6:
                                JLabel picLabelDeathknight = new JLabel(new ImageIcon("images/deathknight.png"));
                                button.add(picLabelDeathknight, BorderLayout.WEST);
                                float[] colorDeathKnight = Color.RGBtoHSB(196, 30, 59, null);
                                button.setBackground(Color.getHSBColor(colorDeathKnight[0], colorDeathKnight[1], colorDeathKnight[2]));
                                //button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 500, 10, true));
                                break;
                            case 7:
                                JLabel picLabelShaman = new JLabel(new ImageIcon("images/shaman.png"));
                                button.add(picLabelShaman, BorderLayout.WEST);
                                float[] colorShaman = Color.RGBtoHSB(0, 112, 222, null);
                                button.setBackground(Color.getHSBColor(colorShaman[0], colorShaman[1], colorShaman[2]));
                                //button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 500, 10, true));
                                break;
                            case 8:
                                JLabel picLabelMage = new JLabel(new ImageIcon("images/mage.png"));
                                button.add(picLabelMage, BorderLayout.WEST);
                                float[] colorMage = Color.RGBtoHSB(105, 204, 240, null);
                                button.setBackground(Color.getHSBColor(colorMage[0], colorMage[1], colorMage[2]));
                                //button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 500, 10, true));
                                break;
                            case 9:
                                JLabel picLabelWarlock = new JLabel(new ImageIcon("images/warlock.png"));
                                button.add(picLabelWarlock, BorderLayout.WEST);
                                float[] colorWarlock = Color.RGBtoHSB(148, 130, 201, null);
                                button.setBackground(Color.getHSBColor(colorWarlock[0], colorWarlock[1], colorWarlock[2]));
                                //button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 500, 10, true));
                                break;
                            case 10:
                                JLabel picLabelMonk = new JLabel(new ImageIcon("images/monk.png"));
                                button.add(picLabelMonk, BorderLayout.WEST);
                                float[] colorMonk = Color.RGBtoHSB(0, 255, 150, null);
                                button.setBackground(Color.getHSBColor(colorMonk[0], colorMonk[1], colorMonk[2]));
                                //button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 500, 10, true));
                                break;
                            case 11:
                                JLabel picLabelDruid = new JLabel(new ImageIcon("images/druid.png"));
                                button.add(picLabelDruid, BorderLayout.WEST);
                                float[] colorDruid = Color.RGBtoHSB(255, 125, 10, null);
                                button.setBackground(Color.getHSBColor(colorDruid[0], colorDruid[1], colorDruid[2]));
                                //button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 500, 10, true));
                                break;
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    final Color defaultColor = button.getBackground();
                    final Color highlightColor = button.getBackground().darker();

                    // Set mouselisteners for the newly created button.
                    button.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            button.setBackground(highlightColor);
                        }

                        public void mouseExited(MouseEvent e) {
                            button.setBackground(defaultColor);
                        }
                    });
                    rButton.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            players.remove(nameDummy);
                            groupInfo.removeAll();
                            itemLevel.remove(nameDummy);
                            groupInfo.itemLevelGroup(itemLevel);
                            grpPanel.add(groupInfo, BorderLayout.CENTER);
                            listPanel.remove(button);
                            frame.validate();
                            frame.repaint();
                        }
                    });

                    listPanel.add(button);
                    frame.validate();
                    frame.repaint();

                    JOptionPane.showMessageDialog(null, "Player successfully added!", "Success!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/success.png"));

                    button.addMouseListener(new MouseInputAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            final PlayerProfile p = new PlayerProfile();

                            p.getURL(player, realm);
                            p.getPlayerName();
                            p.getLevel();
                            p.getCharacterClass();
                            p.getProfessions();
                            p.getItemLevel();
                            //frame.getContentPane().removeAll();
                            charPanel.removeAll();
                            charPanel.add(p, BorderLayout.CENTER);
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
        frame.setSize(572, 701);
        frame.setResizable(false);
        frame.setLocation(350, 50);
        frame.setVisible(true);
    }
}