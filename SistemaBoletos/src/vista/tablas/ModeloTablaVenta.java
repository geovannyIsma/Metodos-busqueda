/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista.tablas;

import controlador.TDA.lista.DynamicList;
import javax.swing.table.AbstractTableModel;
import modelo.Venta;

/**
 *
 * @author romer
 */
public class ModeloTablaVenta extends AbstractTableModel {
    private DynamicList<Venta> ventas;

    @Override
    public int getRowCount() {
        return ventas.getLenght();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            Venta venta = ventas.getInfo(rowIndex);
            
            switch (columnIndex) {
                case 0:
                    return  (venta != null)? venta.getPasajero().getDNI() : "";
                case 1:
                    return (venta != null)? venta.getPasajero().getNombre() + " " + venta.getPasajero().getApellido() : " ";
                case 2:
                    return (venta != null)? venta.getBoleto().getOrigen() : "";
                case 3:
                    return (venta != null)? venta.getBoleto().getDestino(): "";
                case 4:
                    return (venta != null)? venta.getBoleto().getFormattedFechaSalida(): "";
                case 5:
                    return (venta != null)? venta.getBoleto().getFormattedFechaCompra(): "";
                case 6:
                    return (venta != null)? venta.getBoleto().getValor() : "";
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "DNI";
            case 1:
                return "PASAJERO";
            case 2:
                return "ORIGEN";
            case 3:
                return "DESTINO";
            case 4:
                return "FECHA SALIDA";
            case 5:
                return "FECHA COMPRA";
            case 6:
                return "VALOR";
            default:
                return null;
        }
    }

    public DynamicList<Venta> getVentas() {
        return ventas;
    }

    public void setVentas(DynamicList<Venta> ventas) {
        this.ventas = ventas;
    }
}
