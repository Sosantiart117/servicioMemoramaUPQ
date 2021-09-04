import javax.swing.JToggleButton;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
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
	public JComboBox<String> levelSelector;
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

		// TimeLabel
		this.timeLabel = new JLabel(timeText);
		this.timeLabel.setFocusable(false);
		this.timeLabel.setHorizontalAlignment(JLabel.CENTER);
		this.timeLabel.setVerticalAlignment(JLabel.CENTER);
		this.timeLabel.setBackground(cBase);
		this.timeLabel.setOpaque(true);
		this.timeLabel.setForeground(cMain);
		this.timeLabel.setFont(optFont);
		this.timeLabel.setBounds((pannelWidth-200)/2,100,200,200);
		this.add(timeLabel);

		// Level Selector
		String[] lvls = {"Facil","Medio","Dificil"};
		this.levelSelector = new JComboBox<String>(lvls);
		this.levelSelector.setFocusable(false);
		this.levelSelector.setBounds((pannelWidth-120)/2, 400, 120, 20);
		this.levelSelector.setSelectedIndex(level-1);
		this.levelSelector.setEnabled(false);
		this.add(levelSelector);

		// Start Button
		this.sButton = new JButton("Inicio");
		this.sButton.setActionCommand("start");
		this.sButton.setFocusable(false);
		this.sButton.setBounds((pannelWidth-200)/2, 600, 200, 50);
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
		this.pButton.setBounds((pannelWidth-200)/2, 600, 200, 50);
		this.pButton.setFont(optFont);
		this.pButton.setVisible(false);
		this.pButton.addActionListener(listener);
		this.add(pButton);
	}

	
}
