package dia1.persistencia;

import dia1.model.Actividad;
import java.util.List;

/** Contrato: el Service solo conoce esta interfaz, nunca TXT ni MySQL. */
public interface RepositorioActividad {
    List<Actividad> cargarTodos() throws PersistenciaException;
    void insertar(Actividad actividad) throws PersistenciaException;
    void actualizar(Actividad actividad) throws PersistenciaException;
    void eliminar(String codigo) throws PersistenciaException;
}
