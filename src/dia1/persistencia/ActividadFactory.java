package dia1.persistencia;

import dia1.model.*;

/** Patrón Factory: decide qué subclase crear según el tipo. */
public final class ActividadFactory {
    private ActividadFactory() {}

    public static Actividad crear(String tipo, String codigo, String nombre,
                                  double tarifa, int cupo, int inscritos) {
        return switch (tipo.trim().toUpperCase()) {
            case "PRESENCIAL" -> new ActividadPresencial(codigo, nombre, tarifa, cupo, inscritos);
            case "VIRTUAL"    -> new ActividadVirtual(codigo, nombre, tarifa, cupo, inscritos);
            case "HIBRIDA"    -> new ActividadHibrida(codigo, nombre, tarifa, cupo, inscritos);
            default -> throw new IllegalArgumentException("Tipo desconocido: " + tipo);
        };
    }
}
