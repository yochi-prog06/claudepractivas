package dia1.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/** Clase genérica: sirve para Actividad, Estudiante, Mascota... cualquier T. */
public class Gestor<T> {
    private final List<T> elementos = new ArrayList<>();

    public void agregar(T elemento) { elementos.add(elemento); }

    /** Devuelve una COPIA: quien llama no puede alterar la lista interna. */
    public List<T> listar() { return new ArrayList<>(elementos); }

    public List<T> filtrar(Predicate<T> condicion) {
        return elementos.stream().filter(condicion).toList();
    }

    public Optional<T> buscarPrimero(Predicate<T> condicion) {
        return elementos.stream().filter(condicion).findFirst();
    }

    public long contar(Predicate<T> condicion) {
        return elementos.stream().filter(condicion).count();
    }
}
