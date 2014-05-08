package raid;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private String fileName;

    public TextReader() {
	Path currentRelativePath = Paths.get("");
	String s = currentRelativePath.toAbsolutePath().toString();
	System.out.println("Add .txt file here: " + s + "\\roster.txt");
	fileName = "roster.txt";
    }

    public void search(final HashMap<String, String> characters,
	    final GroupPanel groupPanel,
	    final HashMap<String, Integer> itemLevel, final JPanel groupWindow,
	    final JPanel listPanel, final JPanel charWindow, final JFrame frame,
	    final JProgressBar prog)
	    throws IOException {
	BufferedReader file = new BufferedReader(new InputStreamReader(
		new FileInputStream(fileName), "ISO-8859-1"));
	String line;
	while ((line = file.readLine()) != null) {
	    if (line.matches("\\D*\\w*-\\w*\\s\\w*")) {
		final String[] s = line.split("-");
		System.out.println("Adding " + s[0] + " - " + s[1]);
		CharacterPanel characterPanel = new CharacterPanel(
			s[0].toLowerCase());
		JSONObject obj = characterPanel.getURL(s[0], s[1]);
		if (!s[0].equals("") && obj != null
			&& !characters.containsKey((s[0].toLowerCase()))) {
		    // Create button with characters name and modify text-icon
		    // relation.

		    String name = s[0].toLowerCase();
		    final String nameDummy = name;
		    characters.put(nameDummy, characterPanel.wowClass());
		    name = Character.toUpperCase(name.charAt(0))
			    + name.substring(1);
		    final JPanel profileButton = new JPanel(new BorderLayout());
		    final JPanel buttonCross = new JPanel(new FlowLayout());
		    final JButton rButton = new JButton();
		    final JLabel nameL = new JLabel(name, JLabel.CENTER);

		    // Update group panel
		    groupPanel.removeAll();
		    itemLevel.put(nameDummy, characterPanel.itemLevelValue());
		    groupPanel.avgItemLevelGroup(itemLevel);
		    groupPanel.tierUsers(characters); // ...and the tierUsers
		    groupWindow.add(groupPanel, BorderLayout.CENTER);

		    profileButton.add(nameL, BorderLayout.CENTER);
		    profileButton.setToolTipText("Click to open profile!");
		    profileButton.setBorder(new EmptyBorder(0, 10, 0, 0));
		    rButton.setIcon(new ImageIcon("images/cross.png"));
		    rButton.setPreferredSize(new Dimension(16, 16));
		    rButton.setOpaque(false);
		    rButton.setContentAreaFilled(false);
		    rButton.setBorderPainted(false);
		    buttonCross.add(rButton);
		    profileButton.add(buttonCross, BorderLayout.EAST);
		    buttonCross.setOpaque(false);

		    // Retrieve player and realm values from text file.
		    final String player = s[0];
		    final String realm = s[1];

		    // Go through jsonobject to check which class the player is
		    // and choose icon and colour based on that.
		    // Using default class colours from:
		    // http://www.wowwiki.com/Class_colors
		    try {
			String playerClass = obj.get("class").toString();
			switch (Integer.parseInt(playerClass)) {
			case 1:
			    JLabel picLabelWarrior = new JLabel(new ImageIcon(
				    "images/warrior.png"));
			    profileButton.add(picLabelWarrior,
				    BorderLayout.WEST);
			    float[] colorWarrior = Color.RGBtoHSB(199, 156,
				    110, null);
			    profileButton.setBackground(Color.getHSBColor(
				    colorWarrior[0], colorWarrior[1],
				    colorWarrior[2]));
			    break;
			case 2:
			    JLabel picLabelPaladin = new JLabel(new ImageIcon(
				    "images/paladin.png"));
			    profileButton.add(picLabelPaladin,
				    BorderLayout.WEST);
			    float[] colorPaladin = Color.RGBtoHSB(245, 140,
				    186, null);
			    profileButton.setBackground(Color.getHSBColor(
				    colorPaladin[0], colorPaladin[1],
				    colorPaladin[2]));
			    ;
			    break;
			case 3:
			    JLabel picLabelHunter = new JLabel(new ImageIcon(
				    "images/hunter.png"));
			    profileButton
				    .add(picLabelHunter, BorderLayout.WEST);
			    float[] colorHunter = Color.RGBtoHSB(171, 212, 115,
				    null);
			    profileButton.setBackground(Color.getHSBColor(
				    colorHunter[0], colorHunter[1],
				    colorHunter[2]));
			    break;
			case 4:
			    JLabel picLabelRogue = new JLabel(new ImageIcon(
				    "images/rogue.png"));
			    profileButton.add(picLabelRogue, BorderLayout.WEST);
			    float[] colorRogue = Color.RGBtoHSB(255, 245, 105,
				    null);
			    profileButton.setBackground(Color
				    .getHSBColor(colorRogue[0], colorRogue[1],
					    colorRogue[2]));
			    break;
			case 5:
			    JLabel picLabelPriest = new JLabel(new ImageIcon(
				    "images/priest.png"));
			    profileButton
				    .add(picLabelPriest, BorderLayout.WEST);
			    float[] colorPriest = Color.RGBtoHSB(255, 255, 255,
				    null);
			    profileButton.setBackground(Color.getHSBColor(
				    colorPriest[0], colorPriest[1],
				    colorPriest[2]));
			    buttonCross.setBackground(Color.getHSBColor(
				    colorPriest[0], colorPriest[1],
				    colorPriest[2]));
			    break;
			case 6:
			    JLabel picLabelDeathknight = new JLabel(
				    new ImageIcon("images/deathknight.png"));
			    profileButton.add(picLabelDeathknight,
				    BorderLayout.WEST);
			    float[] colorDeathKnight = Color.RGBtoHSB(196, 30,
				    59, null);
			    profileButton.setBackground(Color.getHSBColor(
				    colorDeathKnight[0], colorDeathKnight[1],
				    colorDeathKnight[2]));
			    break;
			case 7:
			    JLabel picLabelShaman = new JLabel(new ImageIcon(
				    "images/shaman.png"));
			    profileButton
				    .add(picLabelShaman, BorderLayout.WEST);
			    float[] colorShaman = Color.RGBtoHSB(0, 112, 222,
				    null);
			    profileButton.setBackground(Color.getHSBColor(
				    colorShaman[0], colorShaman[1],
				    colorShaman[2]));
			    break;
			case 8:
			    JLabel picLabelMage = new JLabel(new ImageIcon(
				    "images/mage.png"));
			    profileButton.add(picLabelMage, BorderLayout.WEST);
			    float[] colorMage = Color.RGBtoHSB(105, 204, 240,
				    null);
			    profileButton.setBackground(Color.getHSBColor(
				    colorMage[0], colorMage[1], colorMage[2]));
			    break;
			case 9:
			    JLabel picLabelWarlock = new JLabel(new ImageIcon(
				    "images/warlock.png"));
			    profileButton.add(picLabelWarlock,
				    BorderLayout.WEST);
			    float[] colorWarlock = Color.RGBtoHSB(148, 130,
				    201, null);
			    profileButton.setBackground(Color.getHSBColor(
				    colorWarlock[0], colorWarlock[1],
				    colorWarlock[2]));
			    break;
			case 10:
			    JLabel picLabelMonk = new JLabel(new ImageIcon(
				    "images/monk.png"));
			    profileButton.add(picLabelMonk, BorderLayout.WEST);
			    float[] colorMonk = Color.RGBtoHSB(0, 255, 150,
				    null);
			    profileButton.setBackground(Color.getHSBColor(
				    colorMonk[0], colorMonk[1], colorMonk[2]));
			    break;
			case 11:
			    JLabel picLabelDruid = new JLabel(new ImageIcon(
				    "images/druid.png"));
			    profileButton.add(picLabelDruid, BorderLayout.WEST);
			    float[] colorDruid = Color.RGBtoHSB(255, 125, 10,
				    null);
			    profileButton.setBackground(Color
				    .getHSBColor(colorDruid[0], colorDruid[1],
					    colorDruid[2]));
			    break;
			}
		    } catch (Exception e1) {
			e1.printStackTrace();
		    }
		    final Color defaultColor = profileButton.getBackground();
		    final Color highlightColor = profileButton.getBackground()
			    .darker();

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
			    groupPanel.removeAll();
			    characters.remove(nameDummy); // Remove character
							  // from duplicate
							  // check
			    itemLevel.remove(nameDummy); // Remove character
							 // from average item
							 // level hashmap
			    groupPanel.avgItemLevelGroup(itemLevel);
			    groupPanel.tierUsers(characters);
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
			    Executor executor = java.util.concurrent.Executors
				    .newSingleThreadExecutor();
			    executor.execute(new Runnable() {
				@Override
				public void run() {
				    final CharacterPanel characterPanel = new CharacterPanel(
					    nameDummy);

				    // Read from wow api
				    characterPanel.getURL(player, realm);
				    // Call methods to retrieve
				    // information from api and
				    // add the information as
				    // labels to
				    // charWindow
				    characterPanel.getCharacterName();
				    characterPanel.getLevel();
				    characterPanel.getCharacterClass();
				    characterPanel.getProfessions();
				    characterPanel.getItemLevel();
				    characterPanel.getAchievementPoints();
				    characterPanel.checkGems();
				    try {
					characterPanel.getArmoryLink(player,
						realm);
				    } catch (URISyntaxException e1) {
					e1.printStackTrace();
				    }
				    charWindow.removeAll();
				    charWindow.add(characterPanel,
					    BorderLayout.CENTER);
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
		    JOptionPane
			    .showMessageDialog(
				    null,
				    "Please make sure that the name and realm are correct and that there are no duplicates",
				    "Character or realm not found",
				    JOptionPane.ERROR_MESSAGE);
		    break;
		}
	    }
	}
	file.close();
    }
}