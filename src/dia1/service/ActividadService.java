package dia1.service;

import dia1.model.Actividad;
import dia1.persistencia.*;

import java.util.ArrayList;
import java.util.List;

/** Reglas de negocio. No sabe nada de ventanas ni de archivos. */
public class ActividadService {
    private final RepositorioActividad repo;
    private final List<Actividad> actividades;                    // copia en memoria

    public ActividadService(RepositorioActividad repo) throws PersistenciaException {
        this.repo = repo;
        this.actividades = new ArrayList<>(repo.cargarTodos());
    }

    public List<Actividad> listar() { return new ArrayList<>(actividades); }

    public void registrar(String tipo, String codigo, String nombre,
                          double tarifa, int cupo) throws PersistenciaException {

        // validacion en service. para claude: ¿donde va esto, aqui o en el constructor Actividad?
        // o ¿cualquiera de los dos?
        /*
        if(tarifa > 100000) {
            throw new IllegalArgumentException("Tarifa debe ser menor a 100000");
        }*/
        // el constructor valida (lanza IllegalArgumentException)
        Actividad nueva = ActividadFactory.crear(tipo, codigo, nombre, tarifa, cupo, 0);

        if (buscar(nueva.getCodigo()) != null)
            throw new IllegalArgumentException("Ya existe la actividad " + nueva.getCodigo());
        repo.insertar(nueva);                                     // primero guarda...
        actividades.add(nueva);                                   // ...y luego actualiza memoria
    }

    public void inscribir(String codigo) throws PersistenciaException {
        Actividad a = buscar(codigo);
        if (a == null) throw new IllegalArgumentException("No existe la actividad " + codigo);
        a.inscribir();                                            // puede lanzar IllegalStateException
        repo.actualizar(a);
    }

    public void eliminar(String codigo) throws PersistenciaException {
        Actividad a = buscar(codigo);
        if (a == null) throw new IllegalArgumentException("No existe la actividad " + codigo);
        repo.eliminar(codigo);
        actividades.remove(a);
    }

    private Actividad buscar(String codigo) {
        return actividades.stream()
                .filter(a -> a.getCodigo().equalsIgnoreCase(codigo.trim()))
                .findFirst().orElse(null);
    }
}
