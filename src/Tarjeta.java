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
	public boolean isAnswer;
	public String bg;

	private BufferedImage master;

	public Tarjeta(int id, boolean ans, String bg){
		this.isAnswer = ans;
		this.id = id;

		// this.setSelectedIcon(getSizedIcon(bg));
		// this.setIcon(getSizedIcon(bg));
		try{
			master = ImageIO.read(new File(bg));
		}catch(IOException err){
			err.printStackTrace();
		}
		this.addComponentListener(new ComponentAdapter(){
			@Override
			public void componentResized(ComponentEvent event){
				Tarjeta tj = (Tarjeta) event.getComponent();
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
							java.awt.Image.SCALE_FAST);
				tj.setSelectedIcon(new ImageIcon(sizedImage));
				tj.setIcon(new ImageIcon(sizedImage));
			}

		});
	}

	// private ImageIcon getSizedIcon(String img){
	// 	ImageIcon sizedIcon = new ImageIcon(img);
	// 	Image bg = sizedIcon.getImage();


	// 	return sizedIcon;
	// }

}
