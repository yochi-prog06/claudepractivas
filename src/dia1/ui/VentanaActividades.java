package dia1.ui;

import dia1.model.Actividad;
import dia1.service.Gestor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;

public class VentanaActividades extends JFrame {
    private final Gestor<Actividad> gestor;
    private final DefaultTableModel modelo =
            new DefaultTableModel(new String[]{"Código", "Nombre", "Tipo", "Tarifa", "Cupos libres"}, 0) {

    @Override
    public boolean isCellEditable(int fila, int col) { return false; }
    };

    private final JTable tabla = new JTable(modelo);

    public VentanaActividades(Gestor<Actividad> gestor) {
        this.gestor = gestor;
        setTitle("Actividades");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 350);
        setLocationRelativeTo(null);

        JButton btnInscribir = new JButton("Inscribir en la seleccionada");
        btnInscribir.addActionListener(e -> inscribirSeleccionada());   // EVENTO

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(btnInscribir, BorderLayout.SOUTH);
        refrescarTabla();
    }

    private void inscribirSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una actividad.");
            return;
        }
        String codigo = (String) modelo.getValueAt(fila, 0);
        try {
            Actividad a = gestor.buscarPrimero(x -> x.getCodigo().equals(codigo)).orElseThrow();
            a.inscribir();
            refrescarTabla();
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void refrescarTabla() {
        modelo.setRowCount(0);
        for (Actividad a : gestor.listar()) {
            modelo.addRow(new Object[]{a.getCodigo(), a.getNombre(), a.getTipo(),
                    a.calcularTarifaFinal(), a.getCuposDisponibles()});
        }
    }
}