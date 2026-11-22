package dia1.controller;

import dia1.model.Actividad;
import dia1.persistencia.PersistenciaException;
import dia1.service.ActividadService;

import java.util.List;

/** Traduce lo que escribe el usuario (Strings) a datos y llama al Service. */
public class ActividadController {
    private final ActividadService service;

    public ActividadController(ActividadService service) { this.service = service; }

    public List<Actividad> listar() { return service.listar(); }

    public void registrar(String tipo, String codigo, String nombre,
                          String tarifaTxt, String cupoTxt) throws PersistenciaException {
        double tarifa;
        int cupo;
        try {
            tarifa = Double.parseDouble(tarifaTxt.trim());
            cupo = Integer.parseInt(cupoTxt.trim());

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Tarifa y cupo deben ser números.");
        }
        service.registrar(tipo, codigo, nombre, tarifa, cupo);
    }

    public void inscribir(String codigo) throws PersistenciaException {
        service.inscribir(codigo);
    }

    public void eliminar(String codigo) throws PersistenciaException {
        service.eliminar(codigo);
    }
}
