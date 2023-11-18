package com.memorama;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;

import java.nio.file.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;

public class Main implements ActionListener {

    public static ClassLoader classLoader = Main.class.getClassLoader();

    public static String PATH = "./";
    public static String FILESDIR = "media/img/";
    public static String SELECTEDDIR;
    public static JFrame frame;

    private static JLayeredPane base;
    private static JButton startB;
    private static LevelSelector lvlSelector;
    private static JComboBox<String> dirSelector;
    private static Color cBase = new Color(0x0C1E42);
    private boolean isDirSelected = false;

    // public static String titleImagePath;
    public static String titleImagePath = "media/icons/title.png";
    private static String HOMEPATH;

    public static void main(String args[]) {
        new Main();
        frame.setVisible(true);
    }

    // Pantalla principal
    public Main() {
        // Inicia la ventana
        setFrame();
        // Inicia el layout para la ventana
        initBase();
        // Add title
        initTitle();
        // Dificultad
        initLevel();
        // Distintis temas
        this.initDirSelector();
        // Botton de inicio
        this.initSButton();

    }

    private static void setFrame() {
        frame = new JFrame();
        // técnico
        frame.setTitle("Memorama"); // Nombre de la ventana
        frame.setResizable(false); // tamaño fijo
        frame.setSize(500, 600); // x y
        frame.setLocationRelativeTo(null); // aparece en el centro del moitor
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Termina cuando cierre

        // Apraciencia
        // El image hay que importarlo y tener un archivo
        frame.getContentPane().setBackground(cBase); // hacerle un color base
        frame.setLayout(null);

    }

    private static void initBase() {
        // layered
        base = new JLayeredPane();
        base.setBackground(cBase);
        frame.add(base);
    }

    private static void initTitle() {
        // Subtitle
        JLabel AL = new JLabel("Algebra Lineal");
        AL.setFont(new Font("Arial", Font.PLAIN, 20));
        AL.setForeground(Color.WHITE);
        AL.setHorizontalAlignment(JLabel.CENTER);
        AL.setBounds(100, 30, 300, 20);
        frame.add(AL);
        // Inicia el titulo principal
        ImageIcon tIcon = new ImageIcon(classLoader.getResource(titleImagePath));
        // JLabel title = new JLabel("Algebra", JLabel.CENTER);
        JLabel title = new JLabel();
        title.setText("Memorama");
        title.setHorizontalTextPosition(JLabel.CENTER);
        title.setVerticalTextPosition(JLabel.TOP);
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        title.setIconTextGap(50);
        title.setIcon(tIcon);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setBounds(50, 50, 400, 300);
        frame.add(title);
    }

    private void initDirSelector() {
        // Selector de Directorios
        System.out.println(FILESDIR);
        String[] dirs = getDirs(FILESDIR);
        dirSelector = new JComboBox<String>(dirs);
        dirSelector.setSelectedItem(null);
        dirSelector.setBounds(190, 350, 120, 20);
        dirSelector.addActionListener(this);
        frame.add(dirSelector);
    }

    private static void initLevel() {
        // Selector de dificultad
        lvlSelector = new LevelSelector();
        lvlSelector.setBounds(190, 380, 120, 20);
        frame.add(lvlSelector);
    }

    // public static String[] getDirs(String dirName) {
    // try {
    // URI uri = classLoader.getResource(dirName).toURI();
    // String[] dirs = new File(uri).list();
    // return dirs;
    // } catch (URISyntaxException e) {
    // e.printStackTrace();
    // return new String[0];
    // }
    // }

    private void initSButton() {
        // Buton de inicion
        startB = new JButton("Inicio");
        startB.setBounds(200, 420, 100, 50);
        startB.addActionListener(this);
        startB.setActionCommand("Start");
        frame.add(startB);
    }

    private static void startMemorama(int lvl, String lvlDir) {
        // Inicia el Juego
        new Memorama(lvl, lvlDir).setVisible(true);
        frame.setVisible(false);
        frame.dispose();
    }

    private static boolean askName() {
        // POP up para pedir el nombre
        Memorama.userName = JOptionPane.showInputDialog("¿Como te llamas?");
        if (Memorama.userName == null)
            return false;
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == dirSelector) {
            isDirSelected = true;
            Memorama.subject = dirSelector.getSelectedItem().toString();
            SELECTEDDIR = FILESDIR + Memorama.subject;
            lvlSelector.updateSelection(SELECTEDDIR);
        }

        // If button selected
        if (e.getSource() == startB) {
            if (!isDirSelected)
                return;
            if (!askName())
                return;
            int selectedLevel = lvlSelector.getSelectedLevel() + 1;
            startMemorama(selectedLevel, SELECTEDDIR);// start game
            frame.dispose();
        }
    }

    // ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    // FIx JAR
    public static String[] getDirs(String dirName) {
        return getDirs(dirName, 1);
    }

    public static String[] getDirs(String dirName, int depth) {
        try {
            URI uri = classLoader.getResource(dirName).toURI();

            String[] dirs;
            if ("jar".equals(uri.getScheme())) {
                dirs = safeWalkJar(dirName, uri, depth);
            } else {
                dirs = Files.walk(Paths.get(uri), depth)
                        .map(pathInDirectory -> pathInDirectory.getFileName().toString())
                        .toArray(String[]::new);
            }
            return Arrays.copyOfRange(dirs, 1, dirs.length);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return new String[0];
    }

    private static ConcurrentMap<String, Object> locks = new ConcurrentHashMap<>();

    private static String parseFileName(URI uri) {
        String schemeSpecificPart = uri.getSchemeSpecificPart();
        return schemeSpecificPart.substring(0, schemeSpecificPart.indexOf("!"));
    }

    private static String[] safeWalkJar(String path, URI uri, int depth) {
        try {
            synchronized (getLock(uri)) {
                try (FileSystem fs = getFileSystem(uri)) {
                    return Files.walk(fs.getPath(path), depth)
                            .map(pathInJar -> pathInJar.getFileName().toString())
                            .toArray(String[]::new);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[0];
    }

    private static Object getLock(URI uri) {
        String fileName = parseFileName(uri);
        locks.computeIfAbsent(fileName, s -> new Object());
        return locks.get(fileName);
    }

    private static FileSystem getFileSystem(URI uri) throws IOException {
        try {
            return FileSystems.getFileSystem(uri);
        } catch (FileSystemNotFoundException e) {
            return FileSystems.newFileSystem(uri, Collections.emptyMap());
        }
    }
}
