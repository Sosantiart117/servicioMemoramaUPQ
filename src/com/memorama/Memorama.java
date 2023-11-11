package com.memorama;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.EventQueue;
import java.awt.Color;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Memorama extends JFrame implements ActionListener {

    private static Color cBase = new Color(0x0C1E42);
    private static Color cMain = new Color(0x0B378F);
    // private static Color cOpt = new Color(0x401C15);
    // private static Color cSec = new Color(0x2E4205);
    // private static Color cContr = new Color(0x668F13);

    private static int WINSIZE = 80; // Cambio a aspecto apple 16:10 *88 = 1280 * 800
    private static JLabel gameStateText;

    private static OptionsPanel opciones;
    private static GamePanel juego;

    // Escoger tarjetas
    // dir preg sirve como master para sacar imagenes
    public static String LEVELDIR = Main.PATH + "media/img/Matrices/"
            .replace("/", File.separator);

    // global vars for comparation
    private static boolean pressed = false;
    private static Tarjeta selected, preSelected;

    // global for win state / game state
    public static int TIME = 0;
    private static int PAIRS;
    private static int COUNT;
    private static int LEVEL;
    private static int PENAL;
    private static int BONUS;

    // For time
    public static int SEG = 1000;
    private static int DELAY = 250;
    private static int MINS = 0, SEGS = 0;
    private static String TIMETEXT = "--:--";
    private static Timer gameTimer = new Timer(true);
    private static Timer clock = new Timer(false);
    private static TimerTask gameClock;
    private static TimerTask gameTime;
    public static boolean running = false;

    // Save Img
    public static String userName;
    public static String subject;

    public static void main(String[] args) {
        new Memorama().setVisible(true);
        ;
    }

    public Memorama() {
        this(1, LEVELDIR);
    }

    public Memorama(String levelDir) {
        this(1, levelDir);
    }

    public Memorama(int lvl) {
        this(lvl, LEVELDIR);
    }

    public Memorama(int lvl, String levelDir) {
        LEVEL = lvl;
        LEVELDIR = levelDir;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (
                        ClassNotFoundException | InstantiationException | IllegalAccessException
                        | UnsupportedLookAndFeelException ex) {
                    System.err.println("Error al cargar");
                    // ex.printStackTrace();
                }
                // Init COmponenets
                initComponents();
            }
        });
    }

    private void initComponents() {
        // Pantalla principal
        initFrame();
        // Inicia Panel secundario
        initOptions();
        // Inicia Juego
        initGame();
        // toRun
        initTimer();
    }

    private void initFrame() {
        // técnico
        this.setTitle("Memorama"); // Nombre de la ventana
        // this.setResizable(true); // tamaño fijo
        this.setSize(WINSIZE * 16, WINSIZE * 10); // x y
        this.setLocationRelativeTo(null); // aparece en el centro del moitor
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Termina cuando cierre

        // Apraciencia
        // El image hay que importarlo y tener un archivo
        // this.setIconImage(new ImageIcon("archivo.png").getImage());
        this.getContentPane().setBackground(cBase); // hacerle un color base
        this.setLayout(new BorderLayout(10, 0));

        // Game State
        gameStateText = new JLabel("Memorama");
        gameStateText.setFont(new Font("Arial", Font.BOLD, 65));
        gameStateText.setFocusable(false);
        gameStateText.setVerticalAlignment(JLabel.CENTER);
        gameStateText.setHorizontalAlignment(JLabel.CENTER);
        gameStateText.setForeground(cMain);
        gameStateText.setSize(600, 100);
        this.add(gameStateText);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                JFrame frame = (JFrame) e.getComponent();
                gameStateText.setLocation((frame.getWidth() / 2) - WINSIZE * 5 - 75, (frame.getHeight() / 2) - 50);
                gameStateText.setSize(600, 100);
            }

        });

    }

    private void initOptions() {
        opciones = new OptionsPanel(WINSIZE, LEVEL, TIMETEXT, this);
        this.add(opciones, BorderLayout.EAST);
    }

    private void initGame() {
        juego = new GamePanel(WINSIZE, LEVEL, LEVELDIR, this);
        TIME = juego.tiempo;
        PAIRS = juego.nPairs;
        COUNT = juego.cuenta;
        PENAL = juego.penalizacion;
        BONUS = juego.penalizacion;
        this.add(juego, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        switch (cmd) {
            case "tarjeta":
                checkPairs((Tarjeta) e.getSource());
                break;
            case "restart":
                this.restart();
                break;
            case "stop":
                stop();
                break;
            case "start":
                start();
                break;
            case "back":
                fullStop();
                this.dispose();
                new Main().frame.setVisible(true);
                break;
        }
    }

    private void fullStop() {
        gameStateText.setVisible(true);
        running = false;
        juego.setVisible(false);
        gameClock.cancel();
        gameTime.cancel();
    }

    private void restart() {
        gameStateText.setText("Reiniciando...");
        gameStateText.setVisible(true);
        gameClock.cancel();
        LEVEL = opciones.levelSelector.getSelectedLevel() + 1;
        this.remove(juego);
        initGame();
        initTimer();
        this.invalidate();
        this.validate();
        this.repaint();
        running = false;
        opciones.sButton.setVisible(true);
        if (opciones.pButton.isSelected())
            opciones.pButton.setSelected(false);
        if (!opciones.pButton.isEnabled())
            opciones.pButton.setEnabled(true);
        opciones.levelSelector.setEnabled(false);
        wait(3 * SEG);
        start();
    }

    private static void stop() {
        if (opciones.pButton.isSelected()) {
            gameStateText.setVisible(true);
            running = false;
            juego.setVisible(false);
        } else {
            gameStateText.setVisible(false);
            running = true;
            juego.setVisible(true);
        }
    }

    private static void start() {
        running = true;
        gameStateText.setVisible(false);
        gameStateText.setText("Juego Pausado");
        juego.setVisible(true);
        opciones.sButton.setVisible(false);
        opciones.pButton.setVisible(true);
        opciones.resButton.setEnabled(true);
    }

    private void won() {
        int res = JOptionPane.showConfirmDialog(this, "Ganaste!, Deseas tomar captura?");
        if (res == 0)
            try {

                new ImgSaver(this);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Ocurrio un Error al salvar");
            }
        running = false;
        opciones.levelSelector.setEnabled(true);
        opciones.pButton.setVisible(false);
        gameStateText.setText("Ganaste!");
    }

    private static void lost() {
        TIME = 0;
        running = false;
        opciones.pButton.setEnabled(false);
        gameStateText.setText("Vuelve a Intentar!");
        gameStateText.setVisible(true);
        juego.setVisible(false);
    }

    private void checkPairs(Tarjeta tj) {
        // checar pares
        if (pressed) {
            pressed = false;
            selected = tj;
            if (!selected.equals(preSelected)
                    && selected.id == preSelected.id) {
                TIME += BONUS;
                wait(DELAY);
                preSelected.setEnabled(false);
                selected.setEnabled(false);
                COUNT++;
            } else if (!selected.equals(preSelected)) {
                TIME -= PENAL;
                wait(DELAY * 2);
                selected.setSelected(false);
                preSelected.setSelected(false);
            }
        } else {
            pressed = true;
            preSelected = tj;
        }
        // gano ?
        if (COUNT == PAIRS) {
            won();
        }
    }

    private static void initTimer() {
        // set up timer
        gameClock = new TimerTask() {
            public void run() {
                if (!running)
                    return;
                TIME -= SEG;
            }
        };
        clock.scheduleAtFixedRate(gameClock, 2 * SEG, SEG);
        gameTime = new TimerTask() {
            public void run() {
                // Show time
                SEGS = (TIME / SEG) % 60;
                MINS = (TIME / (SEG * 60)) % 60;
                TIMETEXT = String.format("%02d:%02d", MINS, SEGS);
                opciones.timeLabel.setText(TIMETEXT);
                if (TIME < SEG)
                    lost();
            }
        };
        gameTimer.scheduleAtFixedRate(gameTime, 0, 100);
    }

    public static void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

}
