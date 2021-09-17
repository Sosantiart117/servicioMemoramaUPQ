import javax.swing.JComboBox;

import java.util.Arrays;

public class LevelSelector extends JComboBox<String>{


	public static String[] LEVELS = {"Facil","Medio","Dificil"};
	public String selectedDir;

	LevelSelector(){
		super();
	}
	LevelSelector(String dir){
		this();
		this.selectedDir = dir;
		this.addItems(getLevels(this.selectedDir));
	}
	LevelSelector(String[] levels){
		this();
		this.addItems(levels);
	}

	public void addItems(String[] items){
		for(String item : items){
			this.addItem(item);
		}
	}

	public int getSelectedLevel(){
		return Arrays.asList(LEVELS).indexOf(
						this.getSelectedItem().toString()) +1;
	}

	public void updateSelection(){
		this.removeAllItems();
		this.addItems(getLevels(this.selectedDir));
	}

	public void updateSelection(String dir){
		this.selectedDir = dir;
		this.updateSelection();
	}
	
	public static String[] getLevels(String levelsDir){
		return Main.getDirs(levelsDir);
	}

}
