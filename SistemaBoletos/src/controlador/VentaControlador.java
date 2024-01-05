package controlador;

import controlador.TDA.lista.DynamicList;
import modelo.Venta;

/**
 *
 * @author romer
 */
public class VentaControlador {
    private Venta ventaBoleto;
    private DynamicList<Venta> ventas;

    public VentaControlador() {
        this.ventas = new DynamicList<>();
    }
    
    public boolean venderBoleto() {
        try {
            getVentaBoleto().setId(getVentas().getLenght());
            getVentas().add(getVentaBoleto());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public Venta getVentaBoleto() {
        if (ventaBoleto == null) {
            ventaBoleto = new Venta();
        }
        return ventaBoleto;
    }

    public void setVentaBoleto(Venta ventaBoleto) {
        this.ventaBoleto = ventaBoleto;
    }

    public DynamicList<Venta> getVentas() {
        return ventas;
    }

    public void setVentas(DynamicList<Venta> ventas) {
        this.ventas = ventas;
    }
}
