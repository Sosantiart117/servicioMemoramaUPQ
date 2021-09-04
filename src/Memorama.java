import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JLabel;
import javax.swing.plaf.DimensionUIResource;
// import javax.swing.Action;
// import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.EventQueue;
import java.awt.Color;
import java.awt.Font;
import java.awt.BorderLayout;
// import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class Memorama extends JFrame implements ActionListener{

	private static Color cBase = 		new Color(0x0C1E42);
	private static Color cMain = 		new Color(0x0B378F);
	// private static Color cOpt = 		new Color(0x401C15);
	// private static Color cSec = 		new Color(0x2E4205);
	// private static Color cContr = 	new Color(0x668F13);

	private static int winSize = 80; // Cambio a aspecto apple 16:10 *88 = 1280 * 800
	private static JLabel gameStateText;

	private static OptionsPanel opciones;

	private static JPanel juego;
	// Escoger tarjetas
	// dir preg sirve como master para sacar imagenes
	public static String imgDir = 
		Main.PATH+"media/img/"
		.replace("/",File.separator);
	// y se espera encontrar lo mismo en res...
	private static LinkedList<String> fTarjetas;
	
	// global vars for comparation
	private static boolean pressed = false;
	private static Tarjeta selected,preSelected;

	// global for win state / game state
	public static int tiempo=0;
	private static int nPairs;
	private static int cuenta;
	private static int level;
	private static int penalizacion;
	private static int bonificacion;

	// pal tiempo
	private static int actionDelay =250;
	private static int seg =1000;
	private static int mins=0, segs=0;
	private static String timeText = "--:--";
	private static Timer gameTimer = new Timer(true);
	private static Timer clock = new Timer(false);
	private static TimerTask gameClock;
	private static TimerTask gameTime;
	public static boolean running = false;

	public static void main(String[] args){
		new Memorama().setVisible(true);;
	}

	public Memorama(){
		this(1);
	}

	public Memorama(int lvl){
		level=lvl;
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (	
						ClassNotFoundException |
						InstantiationException |
						IllegalAccessException |
						UnsupportedLookAndFeelException ex) {
					System.err.println("Error al cargar");
					// ex.printStackTrace();
				}
				// Init COmponenets
				initComponents();
			}
		});
	}

	private void initComponents(){
		// Pantalla principal
		initFrame();
		// Inicia Panel secundario
		initOptions();
		// Inicia Juego
		initGame();
		// toRun
		initTimer();
	}

	private void initFrame(){
		// técnico 
		this.setTitle("Memorama"); //Nombre de la ventana
		// this.setResizable(true); // tamaño fijo
		this.setSize(winSize*16,winSize*10); // x y 
		this.setLocationRelativeTo(null); //aparece en el centro del moitor
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Termina cuando cierre
		
		// Apraciencia
		// El image hay que importarlo y tener un archivo
		// this.setIconImage(new ImageIcon("archivo.png").getImage());
		this.getContentPane().setBackground(cBase); //hacerle un color base
		this.setLayout(new BorderLayout(10,0));

		// Game Pause
		gameStateText = new JLabel("Memorama");
		gameStateText.setFont(new Font("Arial",Font.BOLD,65));
		gameStateText.setFocusable(false);
		gameStateText.setVerticalAlignment(JLabel.CENTER);
		gameStateText.setHorizontalAlignment(JLabel.CENTER);
		gameStateText.setForeground(cMain);
		gameStateText.setSize(600, 100);
		this.add(gameStateText);

		this.addComponentListener(new ComponentAdapter(){
			@Override
			public void componentResized(ComponentEvent e){
				JFrame frame = (JFrame) e.getComponent();
				gameStateText.setLocation((frame.getWidth()/2)-winSize*5-75, (frame.getHeight()/2)-50);
				gameStateText.setSize(600, 100);
			}

		});

	}

	private void initOptions(){
		opciones = new OptionsPanel(winSize, level, timeText, this);
		this.add(opciones, BorderLayout.EAST);
	}

	private void initGame(){
		penalizacion = 2*seg*level;
		bonificacion = 5*seg*level;
		cuenta = 0;
		tiempo = 30*seg*level*2;
		// Panel de las tarejatas
		juego = new JPanel();
		juego.setBackground(cBase);

		// Size and level manage
		nPairs = (2*level)+2;
		int[] gridSize = new int[2];
		gridSize[0] = level+1;
		gridSize[1] = 4;
	 	juego.setLayout(
				new GridLayout(
					gridSize[0],
					gridSize[1],
					level==2?winSize:10,
					10
					));
	 	// juego.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));

		// Obtiene una lista aleatoria de imagenes del conjutno de imagenes
		// obten la lista de "caratulas"a usar por juego
 		fTarjetas = new LinkedList<String>( 
				Arrays.asList( 
					new File(imgDir+"preg"+File.separator).list()));
		String img;	
		LinkedList<Tarjeta> Seleccion = new LinkedList<Tarjeta>();
		// Agrgar Tarjetas
		for(int i=0; i<nPairs;i++){
			// Agrega el numero de cartas segun la dificultad
			img = getTarjetaImg(); 
			// res
			Seleccion.add(
					makeTarjeta(
						i,
						false,
						imgDir+"preg"+File.separator+img)
					);
			// answer
			Seleccion.add(
					makeTarjeta(
						i,
						true,
						imgDir+"res"+File.separator+img)
					);
		}

		// Randomiza el orden de las tarjetas
		int randSelection;
		for(int i=0; i<nPairs*2; i++){
			randSelection = (int)(Math.random() * (double) Seleccion.size());
			juego.add(Seleccion.get(randSelection));
			Seleccion.remove(randSelection);
		}

		juego.setVisible(false);
		// Agregar Panel de jeugo a la ventana
		this.add(juego, BorderLayout.CENTER);
	}

	private static String getTarjetaImg(){
		int randSelection = (int)(Math.random() * (double)fTarjetas.size());
		String tarjeta = fTarjetas.get(randSelection);
		fTarjetas.remove(randSelection);
		return tarjeta;
	}

	private Tarjeta makeTarjeta(int id, boolean isAns, String img ){
		Tarjeta tj = new Tarjeta(id,isAns,img);
		tj.addActionListener(this);
		tj.setActionCommand("tarjeta");
		return tj;
	}

	public void actionPerformed(ActionEvent e){
		String cmd = e.getActionCommand();
		switch(cmd){
			case "tarjeta":
				checkPairs((Tarjeta)e.getSource());
				break;
			case "restart":
				this.restart();
				break;
			case "stop":
				stop();
				break;
			case "start":
				start();
				break;
		}
	}

	public void restart(){
		gameStateText.setText("Reiniciando...");
		gameStateText.setVisible(true);
		gameClock.cancel();
		level = opciones.levelSelector.getSelectedIndex()+1;
		this.remove(juego);
		initGame();
		initTimer();
		this.invalidate();
		this.validate();
		this.repaint();
		running=false;
		opciones.sButton.setVisible(true);
		if(opciones.pButton.isSelected()) opciones.pButton.setSelected(false);
		if(!opciones.pButton.isEnabled()) opciones.pButton.setEnabled(true);
		opciones.levelSelector.setEnabled(false);
		wait(3*seg);
		start();
	}

	private static void stop(){
		if(opciones.pButton.isSelected()){
			gameStateText.setVisible(true);
			running = false;
			juego.setVisible(false);
		}else{
			gameStateText.setVisible(false);
			running = true;
			juego.setVisible(true);
		}
	}

	private static void start(){
		running=true;
		gameStateText.setVisible(false);
		gameStateText.setText("Juego Pausado");
		juego.setVisible(true);
		opciones.sButton.setVisible(false);
		opciones.pButton.setVisible(true);
		opciones.resButton.setEnabled(true);
	}

	private static void won(){
		running=false;
		opciones.levelSelector.setEnabled(true);
		opciones.pButton.setVisible(false);
		gameStateText.setText("Ganaste!");
	}

	private static void lost(){
		tiempo=0;
		running=false;
		opciones.pButton.setEnabled(false);
		gameStateText.setText("Vuelve a Intentar!");
		gameStateText.setVisible(true);
		juego.setVisible(false);
	}

	private void checkPairs(Tarjeta tj){
		// checar pares
		if(pressed){
			pressed = false;
			selected = tj;
			if(!selected.equals(preSelected) 
					&& selected.id == preSelected.id){
				tiempo+=bonificacion;
				wait(actionDelay);
				preSelected.setEnabled(false);
				selected.setEnabled(false);
				cuenta++;
			}else if(!selected.equals(preSelected)){
				tiempo-=penalizacion;
				wait(actionDelay*2);
				selected.setSelected(false);
				preSelected.setSelected(false);
			}
		}else{
			pressed = true;
			preSelected = tj;
		}
		// gano ? 
		if(cuenta == nPairs){
			won();
		}
	}

	private static void initTimer(){
		// set up timer
		gameClock  = new TimerTask() {
			public void run() {
				if(!running) return;
				tiempo-=seg;
			}
		};
		clock.scheduleAtFixedRate(gameClock,2*seg,seg);
		gameTime  = new TimerTask() {
			public void run() {
				// Show time
				segs=(tiempo/seg)%60;
				mins=(tiempo/(seg*60))%60;
				timeText = String.format("%02d:%02d",mins,segs);
				opciones.timeLabel.setText(timeText);
				if(tiempo<seg) lost();
			}
		};
		gameTimer.scheduleAtFixedRate(gameTime, 0,100);
	}


	public static void wait(int ms) {
		try {
				Thread.sleep(ms);
		} catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
		}
	}
		
}
