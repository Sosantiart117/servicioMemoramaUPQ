import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

public class Main{

	private static JFrame frame;
	private static JLayeredPane base;
	private static JButton startB;

	private static Color cBase = 		new Color(0x0C1E42);
	// Pantalla principal
	Main (){

		//----------------------------------------------Frame
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
		

		//-------------------------------------------Panel general
		// layered
		base = new JLayeredPane();
		base.setBackground(cBase);
		// base.setLayout(new BorderLayout(10,0));

		frame.add(base);
		// --------------------------------
		// Name
		ImageIcon tIcon = new ImageIcon("../media/icons/title.png");
		// JLabel title = new JLabel("Algebra", JLabel.CENTER);
		JLabel title = new JLabel();
		title.setText("Algerba Lineal\n Memorama:");
		title.setHorizontalTextPosition(JLabel.CENTER);
		title.setVerticalTextPosition(JLabel.TOP);
		title.setFont(new Font("Arial",Font.BOLD,26));
		title.setForeground(Color.BLACK);
		title.setIconTextGap(50);
		title.setIcon(tIcon);
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setBounds(50, 50, 400, 300);
		frame.add(title);
		
		// Buton de inicion
		startB = new JButton("Inicio");
		startB.setBounds(200, 400, 100, 50);
	
		frame.add(startB);

		// Falta agregar el selector de dificultad

	}

	public static void main(String Args[]){
		new Main();
		frame.setVisible(true);
		new Memorama(1).setVisible(true);
		new Memorama(2).setVisible(true);
		new Memorama(3).setVisible(true);

	}

}
