package dia1.persistencia;

/** Error al leer/escribir datos. Esconde si abajo hay TXT o MySQL. */
public class PersistenciaException extends Exception {
    public PersistenciaException(String mensaje, Throwable causa) { super(mensaje, causa); }
    public PersistenciaException(String mensaje) { super(mensaje); }
}
