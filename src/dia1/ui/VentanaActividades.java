package dia1.ui;

import dia1.model.Actividad;
import dia1.controller.ActividadController;
import dia1.persistencia.PersistenciaException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/** La vista SOLO muestra y recoge datos. Toda decisión la toma el Controller. */
public class VentanaActividades extends JFrame {
    private final ActividadController controller;

    private final JTextField txtCodigo = new JTextField(6);
    private final JTextField txtNombre = new JTextField(12);
    private final JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"PRESENCIAL", "VIRTUAL", "HIBRIDA"});
    private final JTextField txtTarifa = new JTextField(6);
    private final JTextField txtCupo = new JTextField(4);
        private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"Código", "Nombre", "Tipo", "Tarifa final", "Inscritos", "Cupo"}, 0) {
        @Override public boolean isCellEditable(int f, int c) { return false; }
    };
    private final JTable tabla = new JTable(modelo);

    public VentanaActividades(ActividadController controller) {
        this.controller = controller;
        setTitle("Actividades");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(860, 420);
        setLocationRelativeTo(null);

        JPanel formulario = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formulario.add(new JLabel("Código")); formulario.add(txtCodigo);
        formulario.add(new JLabel("Nombre")); formulario.add(txtNombre);
        formulario.add(new JLabel("Tipo"));   formulario.add(cmbTipo);
        formulario.add(new JLabel("Tarifa")); formulario.add(txtTarifa);
        formulario.add(new JLabel("Cupo"));   formulario.add(txtCupo);
        JButton btnRegistrar = new JButton("Registrar");
        formulario.add(btnRegistrar);

        JButton btnEliminar = new JButton("Eliminar");
        formulario.add(btnEliminar);

        JButton btnInscribir = new JButton("Inscribir en la seleccionada");

        add(formulario, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(btnInscribir, BorderLayout.SOUTH);

        btnRegistrar.addActionListener(e -> registrar());
        btnInscribir.addActionListener(e -> inscribir());
        btnEliminar.addActionListener(e -> eliminar());
        refrescar();
    }

    private void registrar() {
        ejecutar(() -> {
            controller.registrar((String) cmbTipo.getSelectedItem(), txtCodigo.getText(),
                    txtNombre.getText(), txtTarifa.getText(), txtCupo.getText());
            txtCodigo.setText(""); txtNombre.setText(""); txtTarifa.setText(""); txtCupo.setText("");
        });
    }

    private void inscribir() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione una actividad."); return; }
        String codigo = (String) modelo.getValueAt(fila, 0);
        ejecutar(() -> controller.inscribir(codigo));
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione una actividad."); return; }
        String codigo = (String) modelo.getValueAt(fila, 0);
        ejecutar(() -> controller.eliminar(codigo));
    }

    /** Un solo lugar donde se traducen las excepciones a mensajes. */
    private void ejecutar(Accion accion) {
        try {
            accion.run();
            refrescar();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Dato inválido", JOptionPane.WARNING_MESSAGE);
        } catch (PersistenciaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refrescar() {
        modelo.setRowCount(0);
        for (Actividad a : controller.listar()) {
            modelo.addRow(new Object[]{a.getCodigo(), a.getNombre(), a.getTipo(),
                    a.calcularTarifaFinal(), a.getInscritos(), a.getCupoTotal()});
        }
    }

    @FunctionalInterface
    private interface Accion { void run() throws PersistenciaException; }
}
