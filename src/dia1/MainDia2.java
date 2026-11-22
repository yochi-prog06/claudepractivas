package dia1;


import dia1.controller.ActividadController;
import dia1.persistencia.*;
import dia1.service.ActividadService;
import dia1.ui.VentanaActividades;

import javax.swing.*;

public class MainDia2 {
    public static void main(String[] args) {
        try {
            // Aquí (y SOLO aquí) se elige TXT. Mañana se cambia por MySQL en esta línea.
            RepositorioActividad repo = new RepositorioActividadTxt("datos/actividades.txt");
            ActividadService service = new ActividadService(repo);
            ActividadController controller = new ActividadController(service);
            SwingUtilities.invokeLater(() -> new VentanaActividades(controller).setVisible(true));
        } catch (PersistenciaException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "No se pudo iniciar", JOptionPane.ERROR_MESSAGE);
        }
    }
}
