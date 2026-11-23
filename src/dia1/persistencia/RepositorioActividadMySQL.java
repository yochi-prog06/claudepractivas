package dia1.persistencia;

import dia1.model.Actividad;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Misma interfaz que el TXT, pero guarda en MySQL.
 * Solo llama procedimientos almacenados con CallableStatement.
 * Credenciales por variables de entorno (NUNCA escritas en el código):
 *   ACT_DB_URL (opcional), ACT_DB_USER (opcional), ACT_DB_PASSWORD (obligatoria)
 */
public class RepositorioActividadMySQL implements RepositorioActividad {
    private final String url;
    private final String usuario;
    private final String clave;

    public RepositorioActividadMySQL() {
        this.url = env("ACT_DB_URL", "jdbc:mysql://localhost:3306/actividades_db");
        this.usuario = env("ACT_DB_USER", "act_app");
        this.clave = System.getenv("ACT_DB_PASSWORD");
    }

    private static String env(String nombre, String porDefecto) {
        String v = System.getenv(nombre);
        return (v == null || v.isBlank()) ? porDefecto : v;
    }

    private Connection conectar() throws PersistenciaException {
        if (clave == null)
            throw new PersistenciaException("Falta la variable de entorno ACT_DB_PASSWORD");
        try {
            return DriverManager.getConnection(url, usuario, clave);
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo conectar a la base de datos", e);
        }
    }

    @Override
    public List<Actividad> cargarTodos() throws PersistenciaException {
        List<Actividad> resultado = new ArrayList<>();
        try (Connection cn = conectar();
             CallableStatement cs = cn.prepareCall("{CALL sp_listar_actividades()}");
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                resultado.add(ActividadFactory.crear(
                        rs.getString("tipo"), rs.getString("codigo"), rs.getString("nombre"),
                        rs.getDouble("tarifa_base"), rs.getInt("cupo_total"), rs.getInt("inscritos")));
            }
        } catch (SQLException e) {
            throw traducir("No se pudo listar las actividades", e);
        }
        return resultado;
    }

    @Override
    public void insertar(Actividad a) throws PersistenciaException {
        try (Connection cn = conectar();
             CallableStatement cs = cn.prepareCall("{CALL sp_insertar_actividad(?,?,?,?,?)}")) {
            cs.setString(1, a.getCodigo());
            cs.setString(2, a.getNombre());
            cs.setString(3, a.getTipo().name());
            cs.setDouble(4, a.getTarifaBase());
            cs.setInt(5, a.getCupoTotal());
            cs.execute();
        } catch (SQLException e) {
            throw traducir("No se pudo insertar la actividad", e);
        }
    }

    @Override
    public void actualizar(Actividad a) throws PersistenciaException {
        try (Connection cn = conectar();
             CallableStatement cs = cn.prepareCall("{CALL sp_actualizar_inscritos(?,?)}")) {
            cs.setString(1, a.getCodigo());
            cs.setInt(2, a.getInscritos());
            cs.execute();
        } catch (SQLException e) {
            throw traducir("No se pudo actualizar la actividad", e);
        }
    }

    @Override
    public void eliminar(String codigo) throws PersistenciaException {
        try (Connection cn = conectar();
             CallableStatement cs = cn.prepareCall("{CALL sp_eliminar_actividad(?)}")) {
            cs.setString(1, codigo);
            cs.execute();
        } catch (SQLException e) {
            throw traducir("No se pudo eliminar la actividad", e);
        }
    }

    /** Si el error vino de un SIGNAL (SQLSTATE 45000), el mensaje ya es de negocio: se muestra tal cual. */
    private PersistenciaException traducir(String mensaje, SQLException e) {
        if ("45000".equals(e.getSQLState())) return new PersistenciaException(e.getMessage(), e);
        return new PersistenciaException(mensaje, e);
    }
}
