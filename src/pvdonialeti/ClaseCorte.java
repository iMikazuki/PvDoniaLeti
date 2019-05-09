package pvdonialeti;

/**
 *
 * @author Erik Ortiz Q.
 */
public class ClaseCorte {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getCorte() {
        return corte;
    }

    public void setCorte(double corte) {
        this.corte = corte;
    }

    public ClaseCorte(int id, String fecha, double corte) {
        this.id = id;
        this.fecha = fecha;
        this.corte = corte;
    }
    public int id;
    public String fecha;
    public double corte;
        
}
