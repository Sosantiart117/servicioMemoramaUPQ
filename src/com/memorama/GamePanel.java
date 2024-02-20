package com.memorama;

import javax.swing.JPanel;

import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.GridLayout;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

public class GamePanel extends JPanel {

    public int cuenta;
    public int penalizacion;
    public int bonificacion;
    public int tiempo;
    public int nPairs;
    public LinkedList<String> fTarjetas;

    private static int seg = Memorama.SEG;
    private ActionListener listener;

    private static Color cBase = new Color(0x0C1E42);
    // private static Color cMain = new Color(0x0B378F);
    // private static Color cOpt = new Color(0x401C15);
    // private static Color cSec = new Color(0x2E4205);
    // private static Color cContr = new Color(0x668F13);

    GamePanel(int winSize, int level, String imgDir, ActionListener listener) {
        this.setBackground(cBase);

        this.listener = listener;
        this.penalizacion = 2 * seg * level;
        this.bonificacion = 5 * seg * level;
        this.cuenta = 0;
        this.tiempo = 30 * seg * level * 2;

        // Size and level manage
        this.nPairs = (2 * level) + 2;
        int[] gridSize = new int[2];
        gridSize[0] = level + 1;
        gridSize[1] = 4;
        this.setLayout(
                new GridLayout(
                        gridSize[0],
                        gridSize[1],
                        level == 2 ? winSize : 10,
                        10));

        // Get a random list form the "Questions" Repo
        // It is expected that answers have the same name as Questions
        fTarjetas = new LinkedList<String>(Arrays.asList(Main.getDirs(imgDir + "/preg")));
        String img;
        LinkedList<Tarjeta> Seleccion = new LinkedList<Tarjeta>();
        // Add cards
        for (int i = 0; i < nPairs; i++) {
            img = getTarjetaImg();
            // Question
            Seleccion.add(makeTarjeta(i, imgDir + "/preg/" + img));
            // Answer
            Seleccion.add(makeTarjeta(i, imgDir + "/res/" + img));
        }

        // Randomiza el orden de las tarjetas
        int randSelection;
        for (int i = 0; i < nPairs * 2; i++) {
            randSelection = (int) (Math.random() * (double) Seleccion.size());
            this.add(Seleccion.get(randSelection));
            Seleccion.remove(randSelection);
        }

        this.setVisible(false);
    }

    private String getTarjetaImg() {
        int randSelection = (int) (Math.random() * (double) fTarjetas.size());
        String tarjeta = this.fTarjetas.get(randSelection);
        this.fTarjetas.remove(randSelection);
        return tarjeta;
    }

    private Tarjeta makeTarjeta(int id, String img) {
        Tarjeta tj = new Tarjeta(id, img);
        tj.addActionListener(this.listener);
        tj.setActionCommand("tarjeta");
        return tj;
    }

}
