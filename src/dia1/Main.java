package dia1;

import dia1.model.*;
import dia1.service.Gestor;
import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        Gestor<Actividad> gestor = new Gestor<>();
        gestor.agregar(new ActividadPresencial("A01", "Fotografia", 5000, 2, 0));
        gestor.agregar(new ActividadVirtual("A02", "Excel basico", 4000, 3, 0));

        // 1. Polimorfismo: referencia Actividad, metodo de la subclase real
        for (Actividad a : gestor.listar()) System.out.println(a);

        // 2. Excepciones de negocio
        Actividad a01 = gestor.buscarPrimero(a -> a.getCodigo().equalsIgnoreCase(" a01 ".trim())).orElseThrow();
        a01.inscribir();
        a01.inscribir();
        try { a01.inscribir(); }
        catch (IllegalStateException e) { System.out.println("Rechazado: " + e.getMessage()); }

        // 3. Optional: ausencia sin null
        System.out.println(gestor.buscarPrimero(a -> a.getCodigo().equals("Z99"))
                .map(Actividad::toString).orElse("Actividad no encontrada"));

        // 4. Streams y lambdas
        System.out.println("Virtuales: " + gestor.filtrar(a -> a.getTipo() == TipoActividad.VIRTUAL).size());
        gestor.listar().stream()
                .sorted(Comparator.comparingDouble(Actividad::calcularTarifaFinal).reversed())
                .forEach(a -> System.out.println("  por tarifa: " + a.getCodigo()));

        // 5. Aliasing y copia defensiva
        Actividad alias = a01;                      // mismo objeto
        System.out.println(alias == a01);           // true
        gestor.listar().clear();                    // limpia la COPIA
        System.out.println(gestor.listar().size()); // 2: la lista interna sigue intacta

        // 6. equals sobrescrito: mismo código = misma actividad
        System.out.println(new ActividadVirtual("a01", "otra", 1, 1, 0).equals(a01)); // false: distinta clase
        System.out.println(new ActividadPresencial("a01", "otra", 1, 1, 0).equals(a01)); // true

        // 7. Validación en el constructor
        try { new ActividadVirtual("X", "Y", -5, 1, 0); }
        catch (IllegalArgumentException e) { System.out.println("Inválido: " + e.getMessage()); }

        // 8. Contar
        System.out.println("Numero: " + gestor.contar(a -> a.getTipo() == TipoActividad.VIRTUAL));
    }

}
