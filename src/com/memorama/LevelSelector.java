package com.memorama;

import javax.swing.JComboBox;

import java.util.Arrays;
import java.util.ArrayList;

public class LevelSelector extends JComboBox<String> {

    public static String[] LEVELS = { "Facil", "Medio", "Dificil" };
    public String selectedDir;

    LevelSelector() {
        super();
    }

    LevelSelector(String dir) {
        this();
        this.selectedDir = dir;
        this.addItems(getLevels(this.selectedDir));
    }

    LevelSelector(String[] levels) {
        this();
        this.addItems(levels);
    }

    public void addItems(String[] items) {
        for (String item : items) {
            this.addItem(item);
        }
    }

    public int getSelectedLevel() {
        return Arrays.asList(LEVELS).indexOf(
                this.getSelectedItem().toString());
    }

    public void updateSelection() {
        this.removeAllItems();
        this.addItems(getLevels(this.selectedDir));
    }

    public void updateSelection(String dir) {
        this.selectedDir = dir;
        this.updateSelection();
    }

    public static String[] getLevels(String levelsDir) {
        // fix, expected 0..2 for levels
        ArrayList<String> lvls = new ArrayList<String>();
        for (String x : Main.getDirs(levelsDir))
            lvls.add(LEVELS[Integer.parseInt(x)]);
        String[] res = new String[lvls.size()];
        for (int i = 0; i < lvls.size(); i++)
            res[i] = (String) lvls.toArray()[lvls.size() - 1 - i];
        return res;
    }

}
