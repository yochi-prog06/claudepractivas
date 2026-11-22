package dia1;

import dia1.model.*;
import dia1.service.Gestor;
import dia1.ui.VentanaActividades;

import javax.swing.SwingUtilities;

public class MainGui {
    public static void main(String[] args) {
        Gestor<Actividad> gestor = new Gestor<>();
        gestor.agregar(new ActividadPresencial("A01", "Fotografia", 5000, 2, 0));
        gestor.agregar(new ActividadVirtual("A02", "Excel basico", 4000, 3, 0));
        gestor.agregar(new ActividadHibrida("A03", "SCRUM", 6000, 5, 2));
        gestor.agregar(new ActividadHibrida("A04", "Gestion empresarial", 3500, 8, 1));
        gestor.agregar(new ActividadPresencial("A05", "Teatro contemporaneo", 7000, 3, 0));
        gestor.agregar(new ActividadVirtual("A06", "IA", 8000, 10, 3));

        SwingUtilities.invokeLater(() -> new VentanaActividades(gestor).setVisible(true));
    }
}