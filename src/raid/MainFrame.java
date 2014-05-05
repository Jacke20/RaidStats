package raid;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.json.JSONObject;

/**
 * This class holds the main window and its components. Allows the user to enter a player name and realm
 * for the game World of Warcraft to retrieve and display some interesting facts about their character.
 * Created by Jacke on 2014-04-29.
 */
public class MainFrame{
	private static ArrayList<String> players = new ArrayList<String>();
	
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
        final JFrame frame = new JFrame("RaidAssister");
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("wow.png")));
        final JPanel topPanel = new JPanel();
        final JPanel listPanel = new ListPanel();

        // Create components
        final JButton addPlayerButton = new JButton("Add character");
        final JTextField textFieldPlayer = new JTextField(10);
        final JTextField textFieldRealm = new JTextField(10);
        final JLabel characterTag = new JLabel("Character:");
        final JLabel realmTag = new JLabel("Realm:");

        // Add a scroll pane
        final JScrollPane scrollPane = new JScrollPane(listPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(20, 60, 0, 100);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        // Add components to panels
        topPanel.add(characterTag);
        topPanel.add(textFieldPlayer);
        topPanel.add(realmTag);
        topPanel.add(textFieldRealm);
        topPanel.add(addPlayerButton);

        // Add panels to content pane
        Container contentPane = frame.getContentPane();
        contentPane.add(topPanel, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Set addPlayerButton as the default button to listen to enter.
        frame.getRootPane().setDefaultButton(addPlayerButton);
        // Implement actions.
        addPlayerButton.addActionListener(new ActionListener() {


			@Override
            public void actionPerformed(ActionEvent e) {
        final PlayerProfile p = new PlayerProfile(scrollPane, topPanel);
        JSONObject obj = p.getURL(textFieldPlayer.getText(), textFieldRealm.getText());


                if (!textFieldPlayer.getText().equals("") && obj != null && !players.contains(textFieldPlayer.getText().toLowerCase())) { //TODO: check for duplicates
                    // Create button with players name and modify text-icon relation.
                	String name = textFieldPlayer.getText().toLowerCase();
                	players.add(name);
                	name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                    final JButton button = new JButton(name);
                    button.setToolTipText("Click to open profile!");
                    button.setVerticalTextPosition(SwingConstants.TOP);
                    button.setHorizontalTextPosition(SwingConstants.CENTER);
                    button.setIconTextGap(0);

                    // Retrieve player and realm values from text fields.
                    final String player = textFieldPlayer.getText();
                    final String realm = textFieldRealm.getText();

                    // Go through jsonobject to check which class the player is and choose icon and colour based on that.
                    // Using default class colours from: http://www.wowwiki.com/Class_colors
                    try {
                        String playerClass = obj.get("class").toString();
                        switch (Integer.parseInt(playerClass)){
                            case 1: button.setIcon(new ImageIcon("images/warrior.png"));
                                float[] colorWarrior = Color.RGBtoHSB(199, 156, 110, null);
                                button.setBackground(Color.getHSBColor(colorWarrior[0], colorWarrior[1], colorWarrior[2]));
                                button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 400, 10, true));
                                break;
                            case 2: button.setIcon(new ImageIcon("images/paladin.png"));
                                float[] colorPaladin = Color.RGBtoHSB(245, 140, 186, null);
                                button.setBackground(Color.getHSBColor(colorPaladin[0], colorPaladin[1], colorPaladin[2]));
                                button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 400, 10, true));
                                break;
                            case 3: button.setIcon(new ImageIcon("images/hunter.png"));
                                float[] colorHunter = Color.RGBtoHSB(171, 212, 115, null);
                                button.setBackground(Color.getHSBColor(colorHunter[0], colorHunter[1], colorHunter[2]));
                                button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 400, 10, true));
                                break;
                            case 4: button.setIcon(new ImageIcon("images/rogue.png"));
                                float[] colorRogue = Color.RGBtoHSB(255, 145, 105, null);
                                button.setBackground(Color.getHSBColor(colorRogue[0], colorRogue[1], colorRogue[2]));
                                button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 400, 10, true));
                                break;
                            case 5: button.setIcon(new ImageIcon("images/priest.png"));
                                float[] colorPriest = Color.RGBtoHSB(255, 255, 255, null);
                                button.setBackground(Color.getHSBColor(colorPriest[0], colorPriest[1], colorPriest[2]));
                                button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 400, 10, true));
                                break;
                            case 6: button.setIcon(new ImageIcon("images/deathknight.png"));
                                float[] colorDeathKnight = Color.RGBtoHSB(196, 30, 59, null);
                                button.setBackground(Color.getHSBColor(colorDeathKnight[0], colorDeathKnight[1], colorDeathKnight[2]));
                                button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 400, 10, true));
                                break;
                            case 7: button.setIcon(new ImageIcon("images/shaman.png"));
                                float[] colorShaman = Color.RGBtoHSB(0, 112, 222, null);
                                button.setBackground(Color.getHSBColor(colorShaman[0], colorShaman[1], colorShaman[2]));
                                button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 400, 10, true));
                                break;
                            case 8: button.setIcon(new ImageIcon("images/mage.png"));
                                float[] colorMage = Color.RGBtoHSB(105, 204, 240, null);
                                button.setBackground(Color.getHSBColor(colorMage[0], colorMage[1], colorMage[2]));
                                button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 400, 10, true));
                                break;
                            case 9: button.setIcon(new ImageIcon("images/warlock.png"));
                                float[] colorWarlock = Color.RGBtoHSB(148, 130, 201, null);
                                button.setBackground(Color.getHSBColor(colorWarlock[0], colorWarlock[1], colorWarlock[2]));
                                button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 400, 10, true));
                                break;
                            case 10: button.setIcon(new ImageIcon("images/monk.png"));
                                float[] colorMonk = Color.RGBtoHSB(0, 255, 150, null);
                                button.setBackground(Color.getHSBColor(colorMonk[0], colorMonk[1], colorMonk[2]));
                                button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 400, 10, true));
                                break;
                            case 11: button.setIcon(new ImageIcon("images/druid.png"));
                                float[] colorDruid = Color.RGBtoHSB(255, 125, 10, null);
                                button.setBackground(Color.getHSBColor(colorDruid[0], colorDruid[1], colorDruid[2]));
                                button.setBorder(BorderFactory.createDashedBorder(Color.BLACK, 3, 400, 10, true));
                                break;
                        }
                    }catch (Exception e1){
                        e1.printStackTrace();
                    }
                    final Color defaultColor = button.getBackground();
                    final Color highlightColor = button.getBackground().darker();
                    button.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent e) {
                            button.setBackground(highlightColor);
                        }
                        public void mouseExited(MouseEvent e){
                            button.setBackground(defaultColor);
                        }
                    });
                    listPanel.add(button);
                    listPanel.validate();
                    listPanel.repaint();

                    JOptionPane.showMessageDialog(null, "Player successfully added!", "Success!", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/success.png"));
                    button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            final PlayerProfile p = new PlayerProfile(scrollPane, topPanel);

                            p.getPlayerName(player, realm);
                            p.getLevel(player, realm);
                            p.getClass(player, realm);
                            p.getProfessions(player, realm);
                            p.getItemLevel(player, realm);
                            frame.getContentPane().removeAll();
                            frame.getContentPane().add(p);
                            frame.validate();
                            frame.repaint();
                        }
                    });
                } else {
                    JOptionPane.showMessageDialog(null, "Please make sure that the name and realm are correct", "Character or realm not found", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        // Modify basic settings for the main frame.
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocation(450, 200);
        frame.setVisible(true);
            }
}
