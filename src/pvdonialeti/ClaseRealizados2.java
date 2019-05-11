package pvdonialeti;

/**
 *
 * @author Erik Ortiz Q.
 */
public class ClaseRealizados2 {
    String nombre;
    int codVenta;
    String fecha;
    int cantidad;
    double precioTotal;

    public ClaseRealizados2(String nombre, int codVenta, String fecha, int cantidad, double precioTotal) {
        this.nombre = nombre;
        this.codVenta = codVenta;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.precioTotal = precioTotal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCodVenta() {
        return codVenta;
    }

    public void setCodVenta(int codVenta) {
        this.codVenta = codVenta;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }
    
          
}
