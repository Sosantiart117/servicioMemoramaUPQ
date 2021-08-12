import javax.swing.JToggleButton;
import java.awt.Dimension;

public class Tarjeta extends JToggleButton{

	public int id;
	public String type;
	public boolean isAnswer;

	Tarjeta(String name){
		this.setText(name);
		// this.preferredSize(new Dimension(50,50);
	}
}
