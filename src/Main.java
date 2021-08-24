import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JButton;
import javax.swing.JLabel;
// import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;

// import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

public class Main implements ActionListener{

	public static boolean DEBUG = false;

	private static JFrame frame;
	private static JLayeredPane base;
	private static JButton startB;
	private static int selectedLevel = 1;
	private static JComboBox<String> lvlSelector ;

	private static Color cBase = 		new Color(0x0C1E42);

	public static String titleImagePath = "media/icons/title.png".replace("/",File.separator);

	public static void main(String args[]){
		System.out.println(titleImagePath);
		System.exit(1);
		if(args.length != 0) DEBUG = true;
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
		this.initLevel();
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
		// frame.setIconImage(new ImageIcon("archivo.png").getImage());
		frame.getContentPane().setBackground(cBase); //hacerle un color base
		// frame.setLayout(new BorderLayout(10,0));
		frame.setLayout(null);
		
	}

	private static void initBase(){
		// layered
		base = new JLayeredPane();
		base.setBackground(cBase);
		// base.setLayout(new BorderLayout(10,0));
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

	private void initLevel(){
		// Selector de dificultad
		String[] niveles = {"fácil", "Medio", "Dificl"};
	 	lvlSelector = new JComboBox<String>(niveles);
		lvlSelector.setSelectedItem(0);
		lvlSelector.setBounds(190, 350,120,20);
		frame.add(lvlSelector);
	}

	private void initSButton(){
		// Buton de inicion
		startB = new JButton("Inicio");
		startB.setBounds(200, 400, 100, 50);
		startB.addActionListener(this);
		startB.setActionCommand("Start");
		frame.add(startB);
	}

	private static void startMemorama(int lvl){
		// Inicia el Juego
		new Memorama(lvl).setVisible(true);
		frame.setVisible(false);
		frame.dispose();
	}

	public void actionPerformed(ActionEvent event){
		// If button selected
		if(event.getSource() == startB){
			selectedLevel = lvlSelector.getSelectedIndex() +1; // get lvl
			startMemorama(selectedLevel);// start game
		}
	}

}
