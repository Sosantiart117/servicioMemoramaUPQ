package com.memorama;

import javax.swing.JComboBox;

import java.util.Arrays;
import java.util.ArrayList;

public class LevelSelector extends JComboBox<String> {

    public static String[] LEVELS = { "Facil", "Medio", "Dificil" };
    public String selectedDir;

    LevelSelector() {
        super();
        this.addItems(LEVELS);
    }

    LevelSelector(String dir) {
        this();
        this.selectedDir = dir;
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
    public void updateSelection(String dir) {
        this.selectedDir = dir;
    }
    public int getSelectedLevel() {
        return Arrays.asList(LEVELS).indexOf(
                this.getSelectedItem().toString());
    }

    // THis is dunb, we just have 3 levels...
    //
    // public void updateSelection() {
    //     this.removeAllItems();
    //     this.addItems(getLevels(this.selectedDir));
    // }
    //

}
