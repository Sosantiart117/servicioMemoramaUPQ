import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.plaf.DimensionUIResource;
// import javax.swing.Action;
// import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.EventQueue;
import java.awt.Color;
import java.awt.BorderLayout;
// import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

public class Memorama extends JFrame implements ActionListener{

	private static Color cBase = 		new Color(0x0C1E42);
	private static Color cMain = 		new Color(0x0B378F);
	private static Color cOpt = 		new Color(0x401C15);
	private static Color cSec = 		new Color(0x2E4205);
	private static Color cContr = 	new Color(0x668F13);

	private static int winSize = 90; // para mantener un aspecto de 16:9 son 80px · 16:9
	private static JPanel juego;
	private static JPanel opciones;

	public static String imgDir = "../media/img/";
	private static File tarjetas = new File(imgDir);
		// Escoger tarjetas
	private static LinkedList<String> 
 		fTarjetas = new LinkedList<String>( 
				Arrays.asList( 
					tarjetas.list()));
	// global vars for comparation
	private static boolean pressed = false;
	private static Tarjeta selected,preSelected;

	// global for win state / game state
	public static boolean running = false;
	public static int score=0;
	private static int nPairs;
	private static int cuenta;
	private static int level;

	public static void main(String[] args){
		new Memorama().setVisible(true);;
	}

	public Memorama(){
		this(1);
	}

	public Memorama(int lvl){
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
					ex.printStackTrace();
				}
				// Init COmponenets
				initComponents(lvl);
			}
		});
	}

	private void initComponents(int lvl){
		// Pantalla principal
		initFrame();
		// Inicia Panel secundario
		initOptions();
		// Inicia Juego
		level=lvl;
		initGame(lvl);
		// toRun
		running=true;
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

	private void initOptions(){
		// Panel al lado
		opciones = new JPanel();
		opciones.setPreferredSize(new DimensionUIResource(winSize*5,100));
		opciones.setBackground(cMain);
		this.add(opciones, BorderLayout.EAST);
	}

	private void initGame(int lvl){
		int fLvl = lvl+1;
		nPairs = (int)Math.pow(2,fLvl);
		cuenta = 0;
		// Panel de las tarejatas
		juego = new JPanel();
		juego.setBackground(cBase);
	 	juego.setLayout(new GridLayout(fLvl==5?4:fLvl,fLvl,10,10));
	 	// juego.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));

		// Obtiene una lista aleatoria de imagenes del conjutno de imagenes
		String img;	
		LinkedList<Tarjeta> Seleccion = new LinkedList<Tarjeta>();
		// Agrgar Tarjetas
		for(int i=0; i<nPairs;i++){
			// Agrega el numero de cartas segun la dificultad
			img = getTarjetaImg(); 
			// res
			Seleccion.add(makeTarjeta(i,false,img));
			// answer
			Seleccion.add(makeTarjeta(i,true,img));
		}

		// Randomiza el orden de las tarjetas
		int randSelection;
		for(int i=0; i<nPairs*2; i++){
			randSelection = (int)(Math.random() * (double) Seleccion.size());
			juego.add(Seleccion.get(randSelection));
			Seleccion.remove(randSelection);
		}

		// Agregar Panel de jeugo a la ventana
		this.add(juego, BorderLayout.CENTER); 
	}

	private static String getTarjetaImg(){
		String tarjeta=imgDir;
		int randSelection = (int)(Math.random() * (double)fTarjetas.size());
		tarjeta += fTarjetas.get(randSelection);
		fTarjetas.remove(randSelection);
		return tarjeta;
	}

	private Tarjeta makeTarjeta(int id, boolean isAns, String img ){
		Tarjeta tj = new Tarjeta(id,isAns,img);
		tj.addActionListener(this);
		// tj.setActionCommand("Memoria");
		return tj;

	}

	public void restart(){
		System.out.println("Restart Game");
		new Memorama(level).setVisible(true);;
		this.dispose();
	}

	public void actionPerformed(ActionEvent event){
		int waitTime = 300;
		// checar pares
		if(pressed){
			//invertimos el estado presionado
			pressed = false;
			selected =  (Tarjeta)event.getSource();
			if(!selected.equals(preSelected) && selected.id == preSelected.id){
				wait(waitTime);
				preSelected.setEnabled(false);
				selected.setEnabled(false);
				cuenta++;
			}else if(!selected.equals(preSelected)){
				wait(waitTime*2);
				selected.setSelected(false);
				preSelected.setSelected(false);
			}
		}else{
			pressed = true;
			preSelected = (Tarjeta)event.getSource();
		}
		// gano ? 
		if(cuenta == nPairs){
			restart();
		}
		
	}

	public static void wait(int ms) {
			try {
					Thread.sleep(ms);
			} catch(InterruptedException ex) {
					Thread.currentThread().interrupt();
			}
	}
		
}
