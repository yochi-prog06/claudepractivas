package dia1.model;


public class ActividadHibrida extends Actividad {
    private static final double RECARGO_MATERIAL = 750;

    public ActividadHibrida(String codigo, String nombre, double tarifaBase, int cupoTotal, int inscritos) {
        super(codigo, nombre, tarifaBase, cupoTotal, inscritos);
    }

    @Override
    public double calcularTarifaFinal() {
        return getTarifaBase() + RECARGO_MATERIAL;
    }

    @Override
    public TipoActividad getTipo() {
        return TipoActividad.Hibrida;
    }



}