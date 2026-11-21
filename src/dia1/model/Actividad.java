package dia1.model;

import java.util.Collections;
import java.util.Objects;
import java.util.List;

public abstract class Actividad implements Cobrable, Comparable<Actividad> {
    private final String codigo;
    private final String nombre;
    private final double tarifaBase;
    private final int cupoTotal;
    private int inscritos;

    public Actividad(String codigo, String nombre, double tarifaBase, int cupoTotal, int inscritos) {
        if (codigo == null || codigo.isBlank())  //si no hay nada o esta en blanco
            throw new IllegalArgumentException("El código es obligatorio.");
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio.");
        if (!(tarifaBase > 0) || Double.isInfinite(tarifaBase))
            throw new IllegalArgumentException("La tarifa debe ser mayor que cero.");
        if (cupoTotal <= 0)
            throw new IllegalArgumentException("El cupo debe ser mayor que cero.");
        if (inscritos < 0 || inscritos > cupoTotal)
            throw new IllegalArgumentException("Inscritos fuera de rango.");
        this.codigo = codigo.trim();
        this.nombre = nombre.trim();
        this.tarifaBase = tarifaBase;
        this.cupoTotal = cupoTotal;
        this.inscritos = inscritos;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public double getTarifaBase() { return tarifaBase; }
    public int getCupoTotal() { return cupoTotal; }
    public int getInscritos() { return inscritos; }
    public int getCuposDisponibles() { return cupoTotal - inscritos; }

    public void inscribir() {
        if (inscritos >= cupoTotal)
            throw new IllegalStateException("No hay cupos en " + codigo);
        inscritos++;
    }

    public abstract TipoActividad getTipo();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return codigo.equalsIgnoreCase(((Actividad) o).codigo);
    }

    @Override
    public int hashCode() { return Objects.hash(codigo.toLowerCase()); }

    @Override
    public String toString() {
        return String.format("%s | %s | %s | tarifa %.2f | %d/%d",
                codigo, nombre, getTipo(), calcularTarifaFinal(), inscritos, cupoTotal);
    }


}
