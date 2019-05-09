package pvdonialeti;

/**
 *
 * @author Erik Ortiz Q.
 */
public class ClaseRealizados {
    int id;
    String comidas;
    double total;
    String fecha;

    public ClaseRealizados(int id, String comidas, double total, String fecha) {
        this.id = id;
        this.comidas = comidas;
        this.total = total;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComidas() {
        return comidas;
    }

    public void setComidas(String comidas) {
        this.comidas = comidas;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    
}
