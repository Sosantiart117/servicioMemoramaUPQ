import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.plaf.DimensionUIResource;
// import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public class Memorama extends JFrame{

	private static Color cBase = 		new Color(0x0C1E42);
	private static Color cMain = 		new Color(0x0B378F);
	private static Color cOpt = 		new Color(0x401C15);
	private static Color cSec = 		new Color(0x2E4205);
	private static Color cContr = 	new Color(0x668F13);

	private static int winSize = 90; // para mantener un aspecto de 16:9 son 80px · 16:9
	private static JPanel juego;
	private static JPanel opciones;

	public Memorama(){
		initComponents(3);
	}

	public Memorama(int lvl){
		// Init COmponenets
		initComponents(lvl);
	}

	private void initComponents(int lvl){
		// Pantalla principal
		initFrame();
		// init tarjetas
		initPanels(lvl);

	}

	private void initFrame(){
		// técnico 
		this.setTitle("Memorama"); //Nombre de la ventana
		// this.setResizable(true); // tamaño fijo
		this.setSize(winSize*16,winSize*9); // x y 
		this.setLocationRelativeTo(null); //aparece en el centro del moitor
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Termina cuando cierre
		
		// Apraciencia
		// El image hay que importarlo y tener un archivo
		// this.setIconImage(new ImageIcon("archivo.png").getImage());
		this.getContentPane().setBackground(cBase); //hacerle un color base
		this.setLayout(new BorderLayout(10,0));
	}

	private void initPanels(int lvl){
		
		int fLvl = lvl+1;

		// Panel de las tarejatas
		juego = new JPanel();
		juego.setBackground(cBase);
	 	juego.setLayout(new GridLayout(fLvl==5?4:fLvl,fLvl,10,10));
	 	// juego.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));

		// Agrgar Tarjetas
		 for(int i=0; i<(Math.pow(2,fLvl));i++){
			// Agrega el numero de cartas segun la dificultad
			juego.add(new Tarjeta("Pregunta"));
			juego.add(new Tarjeta("Respuesta"));
		 }

		this.add(juego, BorderLayout.CENTER); 
		
		// Panel al lado
		opciones = new JPanel();
		opciones.setPreferredSize(new DimensionUIResource(winSize*5,100));
		opciones.setBackground(cMain);
		this.add(opciones, BorderLayout.EAST);
	}

}
