package labturnos;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private final TurnoManager manager = new TurnoManager(4);

    private final DefaultListModel<String> colaModel = new DefaultListModel<>();
    private final DefaultListModel<String> atendidosModel = new DefaultListModel<>();
    private final DefaultListModel<String> rankingModel = new DefaultListModel<>();

    private final JLabel[] cabinaLabels = new JLabel[4];
    private final JButton[] btnTerminarArr = new JButton[4];

    private final JButton btnNuevoAlta = new JButton("Nuevo ALTA");
    private final JButton btnNuevoMedia = new JButton("Nuevo MEDIA");
    private final JButton btnNuevoBaja = new JButton("Nuevo BAJA");
    private final JButton btnSiguiente = new JButton("Siguiente");

    public MainFrame() {
        super("Laboratorio - Turnos con Prioridad");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 650);
        setLocationRelativeTo(null);

        setContentPane(buildSingleScreen());
        refrescarVista();
    }

    private JPanel buildSingleScreen() {
        JPanel root = new JPanel(new BorderLayout(10,10));

        // ----- Control (superior) -----
        JPanel control = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        control.setBorder(BorderFactory.createTitledBorder("Control"));
        control.add(btnNuevoAlta);
        control.add(btnNuevoMedia);
        control.add(btnNuevoBaja);
        control.add(btnSiguiente);
        root.add(control, BorderLayout.NORTH);

        btnNuevoAlta.addActionListener(e -> { manager.tomarTurno(Turno.Prioridad.ALTA); refrescarVista(); });
        btnNuevoMedia.addActionListener(e -> { manager.tomarTurno(Turno.Prioridad.MEDIA); refrescarVista(); });
        btnNuevoBaja.addActionListener(e -> { manager.tomarTurno(Turno.Prioridad.BAJA); refrescarVista(); });
        // TODO(UI): aquí luego llamará al manager.siguiente() real cuando implementen la lógica
btnSiguiente.addActionListener(e -> {
            // En la base: no hay lógica; mostramos aviso amable.
            JOptionPane.showMessageDialog(this, "Funcionalidad por completar: asignación global 'Siguiente'.", "Demo", JOptionPane.INFORMATION_MESSAGE);
        });

        // ----- Centro: Cola y Ranking -----
        JPanel center = new JPanel(new GridLayout(1,2,10,10));
        JList<String> listCola = new JList<>(colaModel);
        center.add(wrap("Cola (mock: orden por seq)", new JScrollPane(listCola)));

        JList<String> listRanking = new JList<>(rankingModel);
        center.add(wrap("Cabinas libres (mock: por id)", new JScrollPane(listRanking)));
        root.add(center, BorderLayout.CENTER);

        // ----- Derecha: Cabinas -----
        JPanel right = new JPanel(new GridLayout(2,2,10,10));
        right.setBorder(BorderFactory.createTitledBorder("Cabinas"));
        for (int i=0; i<4; i++) {
            int idx = i;
            JPanel card = new JPanel(new BorderLayout(5,5));
            card.setBorder(BorderFactory.createTitledBorder("Cabina " + (i+1)));
            cabinaLabels[i] = new JLabel("Libre", SwingConstants.CENTER);
            cabinaLabels[i].setPreferredSize(new Dimension(200, 70));
            card.add(cabinaLabels[i], BorderLayout.CENTER);

            JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
            JButton btnTerminar = new JButton("Terminar");
            btnTerminarArr[i] = btnTerminar;
            btnTerminar.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Funcionalidad por completar: 'Terminar' en cabina " + (idx+1), "Demo", JOptionPane.INFORMATION_MESSAGE);
            });
            south.add(btnTerminar);
            card.add(south, BorderLayout.SOUTH);

            right.add(card);
        }
        root.add(right, BorderLayout.EAST);

        // ----- Sur: Atendidos -----
        JList<String> listAtendidos = new JList<>(atendidosModel);
        root.add(wrap("Atendidos (demo)", new JScrollPane(listAtendidos)), BorderLayout.SOUTH);

        return root;
    }

    private JPanel wrap(String title, JComponent c) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder(title));
        p.add(c, BorderLayout.CENTER);
        return p;
    }

    private void refrescarVista() {
        // Cola (demo)
        colaModel.clear();
        List<Turno> cola = manager.snapshotColaOrdenada();
        for (Turno t : cola) colaModel.addElement(t.getId());

        // Cabinas (todas libres en la demo) + botón terminar deshabilitado
        for (int i=0; i<cabinaLabels.length; i++) {
            cabinaLabels[i].setText("Libre");
            if (btnTerminarArr[i] != null) // TODO(UI): En versión final, habilitar Terminar solo cuando la cabina esté ocupada
btnTerminarArr[i].setEnabled(false); // en base: deshabilitado
        }

        // Ranking de cabinas libres (demo)
        rankingModel.clear();
        for (Cabina c : manager.snapshotCabinasLibresOrdenadas()) {
            rankingModel.addElement("Cabina " + c.getId() + " | atendidos=0");
        }

        // Atendidos (demo: vacío)
        atendidosModel.clear();

        // Botón Siguiente: solo se pinta verde/activo si hay turnos y cabina libre
        // TODO(UI): Siguiente solo habilitado si (hay turnos en cola) y (existe cabina libre)
boolean enableSiguiente = manager.hayTurnosEnCola() && manager.hayCabinaLibre();
        // TODO(UI): Colores: verde activo; gris apagado inactivo
btnSiguiente.setEnabled(enableSiguiente);
        btnSiguiente.setBackground(enableSiguiente ? new Color(0, 180, 0) : UIManager.getColor("Button.background"));
        btnSiguiente.setForeground(enableSiguiente ? Color.WHITE : UIManager.getColor("Button.foreground"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}