package com.memorama;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ImgSaver {

	public File archivo;

	public ImgSaver(JFrame parent) throws IOException{
		JFileChooser fc = new JFileChooser();
		if(fc.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION){
			archivo = fc.getSelectedFile();
			if(!archivo.toString().endsWith(".jpg"))
				archivo = new File(archivo.getAbsoluteFile() + ".jpg");
		}
		
		BufferedImage buffer = new BufferedImage(
				parent.getWidth(),
				parent.getHeight(),
				BufferedImage.TYPE_INT_RGB
				);

		Graphics2D g2d = buffer.createGraphics();
		parent.print(g2d);
		g2d.dispose();
		ImageIO.write(buffer, "jpg", archivo);
	}

}

