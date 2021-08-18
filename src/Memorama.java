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

	private static int winSize = 90; // para mantener un aspecto de 16:9 son 80px · 16:9
	private static JPanel juego;
	private static JPanel opciones;
	private static JLabel timeLabel;
	private static JLabel gameStateText;
	private static JButton sButton;
	private static JButton resButton;
	private static JToggleButton pButton;
	private static JComboBox<String> levelSelector;

	// Escoger tarjetas
	// dir preg sirve como master para sacar imagenes
	public static String imgDir = "../media/img/";
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

	// pal tiempo
	private static int actionDelay =250;
	private static int seg =1000;
	private static int mins=0, segs=0;
	private static String timeText = "--:--";
	private static Timer gameTimer = new Timer(false);
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
				} catch (	ClassNotFoundException |
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
		this.setSize(winSize*16,winSize*9); // x y 
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
		gameStateText.setBounds(200, 350, 600, 100);
		this.add(gameStateText);

	}

	private void initOptions(){
		Font optFont = new Font("Hack",Font.BOLD,26);

		// Panel al lado
		opciones = new JPanel();
		opciones.setPreferredSize(new DimensionUIResource(winSize*5,100));
		opciones.setLayout(null);
		opciones.setBackground(cMain);

		// TImeLabel
		timeLabel = new JLabel(timeText);
		timeLabel.setFocusable(false);
		timeLabel.setHorizontalAlignment(JLabel.CENTER);
		timeLabel.setVerticalAlignment(JLabel.CENTER);
		timeLabel.setBackground(cBase);
		timeLabel.setOpaque(true);
		timeLabel.setForeground(cMain);
		timeLabel.setFont(optFont);
		timeLabel.setBounds(125,100,200,200);
		opciones.add(timeLabel);

		String[] lvls = {"Fácil","Medio","Difícil"};
		levelSelector = new JComboBox<String>(lvls);
		levelSelector.setFocusable(false);
		levelSelector.setBounds(165, 400, 120, 20);
		levelSelector.setSelectedIndex(level-1);
		levelSelector.setEnabled(false);
		opciones.add(levelSelector);

		sButton = new JButton("Inicio");
		sButton.setActionCommand("start");
		sButton.setFocusable(false);
		sButton.setBounds(125, 600, 200, 50);
		sButton.setFont(optFont);
		sButton.addActionListener(this);
		opciones.add(sButton);

		resButton = new JButton("Reinicio");
		resButton.setActionCommand("restart");
		resButton.setFocusable(false);
		resButton.setBounds(100, 500, 250, 50);
		resButton.setFont(optFont);
		resButton.setEnabled(false);
		resButton.addActionListener(this);
		opciones.add(resButton);

		pButton = new JToggleButton("Pausa");
		pButton.setActionCommand("stop");
		pButton.setFocusable(false);
		pButton.setBounds(125, 600, 200, 50);
		pButton.setFont(optFont);
		pButton.setVisible(false);
		pButton.addActionListener(this);
		opciones.add(pButton);

		// acabar agregadno esto al memorama
		this.add(opciones, BorderLayout.EAST);
	}

	private void initGame(){
		int fLvl = level+1;
		nPairs = (int)Math.pow(2,fLvl);
		cuenta = 0;
		tiempo = 30*seg*level;
		// Panel de las tarejatas
		juego = new JPanel();
		juego.setBackground(cBase);
	 	juego.setLayout(new GridLayout(fLvl==5?4:fLvl,fLvl,10,10));
	 	// juego.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));

		// Obtiene una lista aleatoria de imagenes del conjutno de imagenes
		// obten la lista de "caratulas"a usar por juego
 		fTarjetas = new LinkedList<String>( 
				Arrays.asList( 
					new File(imgDir+"preg/").list()));
		String img;	
		LinkedList<Tarjeta> Seleccion = new LinkedList<Tarjeta>();
		// Agrgar Tarjetas
		for(int i=0; i<nPairs;i++){
			// Agrega el numero de cartas segun la dificultad
			img = getTarjetaImg(); 
			// res
			Seleccion.add(makeTarjeta(i,false,imgDir+"preg/"+img));
			// answer
			Seleccion.add(makeTarjeta(i,true,imgDir+"res/"+img));
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
		// System.out.println("Restart Game");
		gameStateText.setText("Reiniciando...");
		gameStateText.setVisible(true);
		gameTime.cancel();
		level = levelSelector.getSelectedIndex()+1;
		this.remove(juego);
		initGame();
		initTimer();
		this.invalidate();
		this.validate();
		this.repaint();
		running=false;
		sButton.setVisible(true);
		if(pButton.isSelected()) pButton.setSelected(false);
		if(!pButton.isEnabled()) pButton.setEnabled(true);
		levelSelector.setEnabled(false);
		wait(3*seg);
		start();
	}

	private static void stop(){
		if(pButton.isSelected()){
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
		sButton.setVisible(false);
		pButton.setVisible(true);
		resButton.setEnabled(true);
	}

	private static void won(){
		running=false;
		levelSelector.setEnabled(true);
		pButton.setVisible(false);
		gameStateText.setText("Ganaste!");
	}

	private static void lost(){
		running=false;
		pButton.setEnabled(false);
		gameStateText.setText("Vuelve a Intentar!");
		gameStateText.setVisible(true);
		juego.setVisible(false);
	}

	private void checkPairs(Tarjeta tj){
		// checar pares
		if(pressed){
			pressed = false;
			selected = tj;
			if(!selected.equals(preSelected) && selected.id == preSelected.id){
				tiempo+=level*10*seg;
				wait(actionDelay);
				preSelected.setEnabled(false);
				selected.setEnabled(false);
				cuenta++;
			}else if(!selected.equals(preSelected)){
				tiempo-=5*seg;
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
		gameTime  = new TimerTask() {
			public void run() {
				if(!running) return;
				tiempo-=1000;
				segs=(tiempo/seg)%60;
				mins=(tiempo/(seg*60))%60;
				timeText = String.format("%02d:%02d",mins,segs);
				timeLabel.setText(timeText);
				if(tiempo<seg) lost();
			}
		};
		gameTimer.scheduleAtFixedRate(gameTime,actionDelay,seg);
	}


	public static void wait(int ms) {
			try {
					Thread.sleep(ms);
			} catch(InterruptedException ex) {
					Thread.currentThread().interrupt();
			}
	}
		
}
