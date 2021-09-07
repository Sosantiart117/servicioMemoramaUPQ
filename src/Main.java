import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

public class Main implements ActionListener{

	public static String PATH = "./";
	public static String FILESDIR = PATH+"media/img/".replace("/", File.separator);
	public static String SELECTEDDIR;

	private static JFrame frame;
	private static JLayeredPane base;
	private static JButton startB;
	private static LevelSelector lvlSelector ;
	private static JComboBox<String> dirSelector ;
	private static Color cBase = 		new Color(0x0C1E42);

	public static String titleImagePath;

	public static void main(String args[]){
		PATH = args.length!=0?args[0]:"./";
		titleImagePath = PATH+"media/icons/title.png";
		titleImagePath.replace("/",File.separator);
		new Main();
		frame.setVisible(true);
	}


	// Pantalla principal
	public Main (){
		// Inicia la ventana
		setFrame();
		// Inicia el layout para la ventana
		initBase();
		// Add title
		initTitle();
		// Dificultad
		initLevel();
		// Distintis temas
		this.initDirSelector();
		// Botton de inicio
		this.initSButton();

	}

	private static void setFrame(){
		frame = new JFrame();
		// técnico 
		frame.setTitle("Memorama"); //Nombre de la ventana
		frame.setResizable(false); // tamaño fijo
		frame.setSize(500,500); // x y 
		frame.setLocationRelativeTo(null); //aparece en el centro del moitor
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Termina cuando cierre
		
		// Apraciencia
		// El image hay que importarlo y tener un archivo
		frame.getContentPane().setBackground(cBase); //hacerle un color base
		frame.setLayout(null);
		
	}

	private static void initBase(){
		// layered
		base = new JLayeredPane();
		base.setBackground(cBase);
		frame.add(base);
	}

	private static void initTitle(){
		// Subtitle 
		JLabel AL = new JLabel("Algebra Lineal");
		AL.setFont(new Font("Arial", Font.PLAIN,20));
		AL.setForeground(Color.WHITE);
		AL.setHorizontalAlignment(JLabel.CENTER);
		AL.setBounds(100, 30, 300, 20);
		frame.add(AL);
		// Inicia el titulo principal
		ImageIcon tIcon = new ImageIcon(titleImagePath);
		// JLabel title = new JLabel("Algebra", JLabel.CENTER);
		JLabel title = new JLabel();
		title.setText("Memorama");
		title.setHorizontalTextPosition(JLabel.CENTER);
		title.setVerticalTextPosition(JLabel.TOP);
		title.setFont(new Font("Arial",Font.BOLD,40));
		title.setForeground(Color.WHITE);
		title.setIconTextGap(50);
		title.setIcon(tIcon);
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setBounds(50, 50, 400, 300);
		frame.add(title);
	}

	private void initDirSelector(){
		// Selector de Directorios 
		String[] dirs = getDirs(FILESDIR);
	 	dirSelector = new JComboBox<String>(dirs);
		dirSelector.setSelectedItem(null);
		dirSelector.setBounds(190, 350,120,20);
		dirSelector.addActionListener(this);
		frame.add(dirSelector);
	}

	private static void initLevel(){
		// Selector de dificultad
	 	lvlSelector = new LevelSelector();
		lvlSelector.setBounds(190, 380,120,20);
		frame.add(lvlSelector);
	}


	public static String[] getDirs(String dirName){
		String[] dirs = new File(dirName).list(); 
		return dirs;
	}

	private void initSButton(){
		// Buton de inicion
		startB = new JButton("Inicio");
		startB.setBounds(200, 420, 100, 50);
		startB.addActionListener(this);
		startB.setActionCommand("Start");
		frame.add(startB);
	}

	private static void startMemorama(int lvl,String lvlDir){
		// Inicia el Juego
		new Memorama(lvl, lvlDir).setVisible(true);
		frame.setVisible(false);
		frame.dispose();
	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == dirSelector){
			SELECTEDDIR = FILESDIR + dirSelector.getSelectedItem().toString();
			lvlSelector.updateSelection(SELECTEDDIR);
		}

		// If button selected
		if(e.getSource() == startB){
			int selectedLevel = lvlSelector.getSelectedLevel();

			FILESDIR +=
				dirSelector.getSelectedItem().toString() +
				File.separator + 
				lvlSelector.getSelectedItem().toString() +
				File.separator;

			startMemorama(selectedLevel,FILESDIR);// start game
		}
	}

}
