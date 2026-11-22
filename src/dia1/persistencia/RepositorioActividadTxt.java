package dia1.persistencia;

import dia1.model.Actividad;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/** Formato de cada línea:  codigo;nombre;TIPO;tarifaBase;cupoTotal;inscritos */
public class RepositorioActividadTxt implements RepositorioActividad {
    private final Path archivo;               //nio file

    public RepositorioActividadTxt(String ruta) {
        this.archivo = Path.of(ruta);
    }

    @Override
    public List<Actividad> cargarTodos() throws PersistenciaException {
        List<Actividad> resultado = new ArrayList<>();
        if (Files.notExists(archivo)) return resultado;           // primera vez: vacío
        try {
            int numLinea = 0;
            for (String linea : Files.readAllLines(archivo)) {
                numLinea++;
                if (linea.isBlank()) continue;
                String[] p = linea.split(";", -1);                // -1 conserva campos vacíos
                if (p.length != 6)
                    throw new PersistenciaException("Línea " + numLinea + " mal formada: " + linea);
                try {
                    resultado.add(ActividadFactory.crear(p[2], p[0], p[1],
                            Double.parseDouble(p[3]), Integer.parseInt(p[4]), Integer.parseInt(p[5])));
                } catch (IllegalArgumentException e) {            // incluye NumberFormatException
                    throw new PersistenciaException("Línea " + numLinea + " inválida: " + e.getMessage(), e);
                }
            }
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo leer " + archivo, e);
        }
        return resultado;
    }

    @Override
    public void insertar(Actividad a) throws PersistenciaException {
        try {
            if (archivo.getParent() != null) Files.createDirectories(archivo.getParent());
            Files.writeString(archivo, aLinea(a) + System.lineSeparator(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo escribir en " + archivo, e);
        }
    }

    @Override
    public void actualizar(Actividad modificada) throws PersistenciaException {
        List<Actividad> todas = cargarTodos();
        List<String> lineas = new ArrayList<>();
        boolean encontrada = false;
        for (Actividad a : todas) {
            if (a.getCodigo().equalsIgnoreCase(modificada.getCodigo())) {
                lineas.add(aLinea(modificada));
                encontrada = true;
            } else {
                lineas.add(aLinea(a));
            }
        }
        if (!encontrada) throw new PersistenciaException("No existe la actividad " + modificada.getCodigo());
        try {
            Files.write(archivo, lineas);                          // reescribe todo el archivo
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo reescribir " + archivo, e);
        }
    }

    public void eliminar(String codigo) throws PersistenciaException{
        List<Actividad> todas = cargarTodos();
        List<String> lineas = new ArrayList<>();
        for (Actividad a : todas) {
            if (a.getCodigo().equalsIgnoreCase(codigo)) {
                continue;
            }
            lineas.add(aLinea(a));
        }
        try {
            Files.write(archivo, lineas);
        }catch (IOException e) {
            throw new PersistenciaException("No se pudo escribir " + archivo, e);
        }
    }


    private String aLinea(Actividad a) {
        return String.join(";", a.getCodigo(), a.getNombre(), a.getTipo().name(),
                String.valueOf(a.getTarifaBase()), String.valueOf(a.getCupoTotal()),
                String.valueOf(a.getInscritos()));
    }
}
