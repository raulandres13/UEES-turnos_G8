package labturnos;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {

    private final TurnoManager manager = new TurnoManager();
    // UI – arriba (control)
    private final JButton btnNuevoAlta = new JButton("Nuevo ALTA");
    private final JButton btnNuevoMedia = new JButton("Nuevo MEDIA");
    private final JButton btnNuevoBaja = new JButton("Nuevo BAJA");
    private final JButton btnSiguiente = new JButton("Siguiente (global)");
    // UI – izquierda (cola)
    private final DefaultListModel<String> colaModel = new DefaultListModel<>();
    private final JList<String> colaList = new JList<>(colaModel);

    // UI – centro derecha (ranking cabinas libres)
    private final DefaultListModel<String> rankingModel = new DefaultListModel<>();
    private final JList<String> rankingList = new JList<>(rankingModel);

    // UI – derecha (cabinas 2x2)
    private final JButton[] btnTerminar = new JButton[4];
    private final JLabel[] lblCabina = new JLabel[4];

    // UI – abajo (bucket atendidos)
    private final DefaultListModel<String> atendidosModel = new DefaultListModel<>();
    private final JList<String> atendidosList = new JList<>(atendidosModel);

    public MainFrame() {
        ((JComponent) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel superior (control)
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        top.add(btnNuevoAlta);
        top.add(btnNuevoMedia);
        top.add(btnNuevoBaja);
        top.add(Box.createHorizontalStrut(20));
        top.add(btnSiguiente);
        add(top, BorderLayout.NORTH);

        // Panel izquierda (cola)
        JPanel left = new JPanel(new BorderLayout());
        left.add(new JLabel("Cola de turnos (ALTA > MEDIA > BAJA, FIFO):"), BorderLayout.NORTH);
        left.add(new JScrollPane(colaList), BorderLayout.CENTER);
        add(left, BorderLayout.WEST);
        left.setPreferredSize(new Dimension(280, 400));

        // Panel centro-derecha (ranking)
        JPanel centerRight = new JPanel(new BorderLayout(10, 10));
        JPanel rankingPane = new JPanel(new BorderLayout());
        rankingPane.add(new JLabel("Ranking cabinas libres:"), BorderLayout.NORTH);
        rankingPane.add(new JScrollPane(rankingList), BorderLayout.CENTER);
        centerRight.add(rankingPane, BorderLayout.WEST);

        // Panel derecha (cabinas 2x2)
        JPanel cabinasGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        for (int i = 0; i < 4; i++) {
            int idx = i;
            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createTitledBorder("Cabina " + (i + 1)));
            lblCabina[i] = new JLabel("Libre");
            lblCabina[i].setBorder(new EmptyBorder(6, 6, 6, 6));
            btnTerminar[i] = new JButton("Terminar");
            btnTerminar[i].addActionListener(e -> onTerminar(idx + 1));
            card.add(lblCabina[i], BorderLayout.CENTER);
            card.add(btnTerminar[i], BorderLayout.SOUTH);
            cabinasGrid.add(card);
        }

        centerRight.add(cabinasGrid, BorderLayout.CENTER);
        add(centerRight, BorderLayout.CENTER);

        // Panel inferior (bucket)
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(new JLabel("Atendidos:"), BorderLayout.NORTH);
        bottom.add(new JScrollPane(atendidosList), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
        bottom.setPreferredSize(new Dimension(1000, 180));

        // Acciones
        btnNuevoAlta.addActionListener(e -> { manager.nuevoTurno(Turno.Prioridad.ALTA); refresh(); });
        btnNuevoMedia.addActionListener(e -> { manager.nuevoTurno(Turno.Prioridad.MEDIA); refresh(); });
        btnNuevoBaja.addActionListener(e -> { manager.nuevoTurno(Turno.Prioridad.BAJA); refresh(); });
        btnSiguiente.addActionListener(e -> { manager.siguienteGlobal(); refresh(); });

        // Se ajusta tamaño a los preferredSize ya definidos
        pack();

        // Primera pintura
        refresh();
        setLocationRelativeTo(null);

        // Nombre de ventana y close
        setTitle("Laboratorio - Sistema de turnos con prioridad");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void onTerminar(int cabinaId) {
        try {
            Turno t = manager.terminarCabina(cabinaId);
            atendidosModel.addElement(t.getId());
        } catch (Exception ex) {
            // No emergentes obligatorios (según consigna); si lo deseas, comenta la siguiente línea
            // JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        }
        refresh();
    }


    private void refresh() {
        // Cola
        colaModel.clear();
        for (Turno t : manager.getColaSnapshot()) {
            colaModel.addElement(t.toString());
        }


        // Ranking libres
        rankingModel.clear();
        List<Cabina> libres = manager.getRankingLibresSnapshot();
        for (Cabina c : libres) {
            String last = (c.getLastFinishedAt() == null) ? "—" : c.getLastFinishedAt().toString();
            rankingModel.addElement("Cabina " + c.getId() + " | atendidos=" + c.getAtendidos() + " | last=" + last);
        }


        // Cabinas
        for (Cabina c : manager.getCabinas()) {
            int i = c.getId() - 1;
            if (c.isLibre()) {
                lblCabina[i].setText("Libre");
                btnTerminar[i].setEnabled(false);
            } else {
                lblCabina[i].setText("Atendiendo: " + c.getActual().getId());
                btnTerminar[i].setEnabled(true);
            }
        }


        // Siguiente habilitado solo si hay turnos y hay alguna cabina libre
        boolean habilitado = manager.hayTurnosEnCola() && manager.hayCabinaLibre();
        btnSiguiente.setEnabled(habilitado);
        estilizarSiguiente(habilitado);
    }


    // TODO(UI): colores y estado del botón Siguiente (verde cuando habilitado; gris apagado cuando no)
    private void estilizarSiguiente(boolean habilitado) {
        if (habilitado) {
            btnSiguiente.setBackground(new Color(0x4CAF50)); // verde
            btnSiguiente.setForeground(Color.WHITE);
        } else {
            btnSiguiente.setBackground(new Color(0xEEEEEE)); // gris claro
            btnSiguiente.setForeground(new Color(0x555555)); // gris oscuro
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }

}
