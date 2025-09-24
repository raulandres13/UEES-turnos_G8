package labturnos;

import javax.swing.*;
import java.awt.*;

public class Banner {

    public static void showBanner(Runnable onClose) {
        String banner =
                "  __  ______________\n" +
                        " / / / / __/ __/ __/\n" +
                        "/ /_/ / _// _/_\\ \\  \n" +
                        "\\____/___/___/___/\n";


        JWindow splash = new JWindow();
        splash.setSize(600, 450);
        splash.setLocationRelativeTo(null);

        // Panel principal con fondo
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.BLACK);

        // Texto del ASCII
        JTextArea asciiArea = new JTextArea(banner);
        asciiArea.setEditable(false);
        asciiArea.setFont(new Font("Monospaced", Font.BOLD, 20));
        asciiArea.setForeground(new Color(0, 200, 70)); // verde tipo consola
        asciiArea.setBackground(Color.BLACK);
        asciiArea.setMargin(new Insets(20, 20, 20, 20));

        // Texto informativo
        JLabel info = new JLabel("<html><center>"
                + "-------------------------------------------<br>"
                + "Universidad Espíritu Santo.<br><br>"
                + "Grupo 8.<br><br>"
                + "María Paz Gutiérrez.<br>"
                + "Raúl Izquierdo.<br><br>"
                + "Estructura de Datos.<br><br>"
                + "Sistema de Turnos Laboratorio Médico.<br>"
                + "-------------------------------------------"
                + "</center></html>", SwingConstants.CENTER);
        info.setForeground(Color.WHITE);
        info.setFont(new Font("SansSerif", Font.PLAIN, 16));

        // Agregar componentes
        panel.add(asciiArea, BorderLayout.NORTH);
        panel.add(info, BorderLayout.CENTER);

        splash.add(panel);
        splash.setVisible(true);

        // Se cierra sola a los 5 segundos
        Timer timer = new Timer(5000, e -> {
            splash.dispose();
            if (onClose != null) {
                onClose.run();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
}
