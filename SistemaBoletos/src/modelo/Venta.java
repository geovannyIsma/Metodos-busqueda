/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author romer
 */
public class Venta {
    private Integer id;
    private Pasajero pasajero;
    private Boleto boleto;

    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pasajero getPasajero() {
        if (pasajero == null) {
            pasajero = new Pasajero();
        }
        return pasajero;
    }

    public void setPasajero(Pasajero pasajero) {
        this.pasajero = pasajero;
    }

    public Boleto getBoleto() {
        if (boleto == null) {
            boleto = new Boleto();
        }
        return boleto;
    }

    public void setBoleto(Boleto boleto) {
        this.boleto = boleto;
    }
    

    public Boolean compare(Venta v, String field, Integer type) {
        switch (type) {
            case 0:
                if (field.equalsIgnoreCase("nombre")) {
                    return pasajero.getNombre().compareTo(v.getPasajero().getNombre()) < 0;
                } else if (field.equalsIgnoreCase("apellido")) {
                    return pasajero.getApellido().compareTo(v.getPasajero().getApellido()) < 0;
                } else if (field.equalsIgnoreCase("dni")) {
                    return pasajero.getDNI().compareTo(v.getPasajero().getDNI()) < 0;
                } else if (field.equalsIgnoreCase("fechaSalida")) {
                    return boleto.getFechaSalida().compareTo(v.getBoleto().getFechaSalida()) < 0;
                } else if (field.equalsIgnoreCase("fechaCompra")) {
                    return boleto.getFechaCompra().compareTo(v.getBoleto().getFechaCompra()) < 0;
                } else if (field.equalsIgnoreCase("destino")) {
                    return boleto.getDestino().compareTo(v.getBoleto().getDestino()) < 0;
                } else if (field.equalsIgnoreCase("origen")) {
                    return boleto.getOrigen().compareTo(v.getBoleto().getOrigen()) < 0;
                } else if (field.equalsIgnoreCase("valor")) {
                    return boleto.getValor().compareTo(v.getBoleto().getValor()) < 0;
                }
            case 1:
                if (field.equalsIgnoreCase("nombre")) {
                    return pasajero.getNombre().compareTo(v.getPasajero().getNombre()) > 0;
                } else if (field.equalsIgnoreCase("apellido")) {
                    return pasajero.getApellido().compareTo(v.getPasajero().getApellido()) > 0;
                } else if (field.equalsIgnoreCase("dni")) {
                    return pasajero.getDNI().compareTo(v.getPasajero().getDNI()) > 0;
                } else if (field.equalsIgnoreCase("fechaSalida")) {
                    return boleto.getFechaSalida().compareTo(v.getBoleto().getFechaSalida()) > 0;
                } else if (field.equalsIgnoreCase("fechaCompra")) {
                    return boleto.getFechaCompra().compareTo(v.getBoleto().getFechaCompra()) > 0;
                } else if (field.equalsIgnoreCase("destino")) {
                    return boleto.getDestino().compareTo(v.getBoleto().getDestino()) > 0;
                } else if (field.equalsIgnoreCase("origen")) {
                    return boleto.getOrigen().compareTo(v.getBoleto().getOrigen()) > 0;
                } else if (field.equalsIgnoreCase("valor")) {
                    return boleto.getValor().compareTo(v.getBoleto().getValor()) > 0;
                }
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return pasajero + " " + boleto;
    }
}
