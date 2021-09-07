import javax.swing.JToggleButton;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.io.File;
import java.io.IOException;

public class Tarjeta extends JToggleButton{

	public int id;
	public String bg;
	
	private static String cubierta = 
		Main.PATH+"media/icons/cubierta.png"
		.replace("/",File.separator);
	private BufferedImage master;
	private BufferedImage card;

	// public static boolean enable=false;

	// public void allEnable(){
	// 	this.setEnabled(enable);
	// }

	public Tarjeta(int id, String bg){
		// This if flow grid 
		// int size=25;
		// this.setPreferredSize(new Dimension(size*9,size*16));
		this.setBorder(null);
		this.setBackground(null);
		this.setFocusable(false);
		// this.setEnabled(enable);

		// custom properties
		this.id = id;

		// this.setSelectedIcon(getSizedIcon(bg));
		// this.setIcon(getSizedIcon(bg));
		try{
			master = ImageIO.read(new File(bg));
			card = ImageIO.read(new File(cubierta));
		}catch(IOException err){
			err.printStackTrace();
			System.err.println("Foto no cargo");
		}
		this.addComponentListener(new ComponentAdapter(){
			@Override
			public void componentResized(ComponentEvent event){
				Tarjeta tj = (Tarjeta) event.getComponent();
				// Agregar a la lista de rendereo
				Thread tjRen = new Thread(
						new Runnable(){
							@Override
							public void run(){
								Dimension size = tj.getSize();
								Insets insets = tj.getInsets();
								size.width -= insets.left + insets.right;
								size.height -= insets.top + insets.bottom;
								if (size.width > size.height) {
									size.width = -1;
								} else {
									size.height = -1;
								}
								Image sizedImage = 
									master.getScaledInstance(
											size.width,
											size.height,
											java.awt.Image.SCALE_DEFAULT);
								tj.setSelectedIcon(
										new ImageIcon(sizedImage));
								// Back Image
								Image cover = 
									card.getScaledInstance(
											size.width,
											size.height,
											java.awt.Image.SCALE_REPLICATE);
								tj.setIcon(new ImageIcon(cover));
							};
						}
						);
				tjRen.setName("Tarjeta-"+tj.id);
				tjRen.start();
			}
		});
	}

}
