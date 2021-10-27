import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.plaf.DimensionUIResource;

import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Font;


public class OptionsPanel extends JPanel{

	private static Color cBase = 		new Color(0x0C1E42);
	private static Color cMain = 		new Color(0x0B378F);
	// private static Color cOpt = 		new Color(0x401C15);
	// private static Color cSec = 		new Color(0x2E4205);
	// private static Color cContr = 	new Color(0x668F13);

	public JLabel timeLabel;
	public JLabel nameLabel;
	public JLabel subjectLabel;
	public LevelSelector levelSelector;
	public JButton sButton;
	public JButton resButton;
	public JToggleButton pButton;
	
	public static Font optFont;

	OptionsPanel(int winSize,int level,String timeText,ActionListener listener){
		int pannelWidth = winSize *5;
		optFont = new Font("Hack",Font.BOLD,26);

		// Panel al lado
		this.setPreferredSize(
				new DimensionUIResource(pannelWidth,100));
		this.setLayout(null);
		this.setBackground(cMain);

		// Name Label
		if(Memorama.userName == null || Memorama.userName.isBlank())
			Memorama.userName = "Nombre";
		this.nameLabel = new JLabel(Memorama.userName);
		this.nameLabel.setFocusable(false);
		this.nameLabel.setHorizontalAlignment(JLabel.CENTER);
		this.nameLabel.setVerticalAlignment(JLabel.CENTER);
		this.nameLabel.setForeground(cMain);
		this.nameLabel.setOpaque(true);
		this.nameLabel.setBackground(cBase);
		this.nameLabel.setFont(optFont);
		this.nameLabel.setBounds((pannelWidth-200)/2,50,200,80);
		this.nameLabel.setBorder(
			BorderFactory.createTitledBorder(
						BorderFactory.createLineBorder(cMain),
						"Nombre:",
						TitledBorder.LEFT,
						TitledBorder.BELOW_TOP,
						new Font("Hack",Font.BOLD,16),
						cMain
					));
		this.add(nameLabel);

		// TimeLabel
		this.timeLabel = new JLabel(timeText);
		this.timeLabel.setFocusable(false);
		this.timeLabel.setHorizontalAlignment(JLabel.CENTER);
		this.timeLabel.setVerticalAlignment(JLabel.CENTER);
		this.timeLabel.setBackground(cBase);
		this.timeLabel.setOpaque(true);
		this.timeLabel.setForeground(cMain);
		this.timeLabel.setFont(optFont);
		this.timeLabel.setBounds((pannelWidth-200)/2,150,200,200);
		this.add(timeLabel);
		
		// Subject Label
		this.subjectLabel = new JLabel(Memorama.subject);
		this.subjectLabel.setFocusable(false);
		this.subjectLabel.setHorizontalAlignment(JLabel.CENTER);
		this.subjectLabel.setVerticalAlignment(JLabel.CENTER);
		this.subjectLabel.setForeground(cBase);
		this.subjectLabel.setFont(optFont);
		this.subjectLabel.setBounds((pannelWidth-250)/2, 365, 250, 80);
		this.subjectLabel.setBorder(
			BorderFactory.createTitledBorder(
						BorderFactory.createLineBorder(cBase),
						"Tema:",
						TitledBorder.LEFT,
						TitledBorder.BELOW_TOP,
						new Font("Hack",Font.BOLD,16),
						cBase
					));
		this.add(subjectLabel);

		// Level Selector
		this.levelSelector = new LevelSelector(Main.SELECTEDDIR);
		this.levelSelector.setSelectedIndex(level-1);
		this.levelSelector.setFocusable(false);
		this.levelSelector.setBounds((pannelWidth-120)/2, 460, 120, 20);
		this.levelSelector.setEnabled(false);
		this.add(levelSelector);

		// Start Button
		this.sButton = new JButton("Inicio");
		this.sButton.setActionCommand("start");
		this.sButton.setFocusable(false);
		this.sButton.setBounds((pannelWidth-200)/2, 580, 200, 50);
		this.sButton.setFont(optFont);
		this.sButton.addActionListener(listener);
		this.add(sButton);

		// Reset Button
		this.resButton = new JButton("Reinicio");
		this.resButton.setActionCommand("restart");
		this.resButton.setFocusable(false);
		this.resButton.setBounds((pannelWidth-250)/2, 500, 250, 50);
		this.resButton.setFont(optFont);
		this.resButton.setEnabled(false);
		this.resButton.addActionListener(listener);
		this.add(resButton);

		// Pause Button
		this.pButton = new JToggleButton("Pausa");
		this.pButton.setActionCommand("stop");
		this.pButton.setFocusable(false);
		this.pButton.setBounds((pannelWidth-200)/2, 580, 200, 50);
		this.pButton.setFont(optFont);
		this.pButton.setVisible(false);
		this.pButton.addActionListener(listener);
		this.add(pButton);
	}
	
}
