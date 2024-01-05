/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador.DAO.pasajero;

import controlador.DAO.DaoImplement;
import controlador.TDA.lista.DynamicList;
import controlador.TDA.lista.Exceptions.EmptyException;
import controlador.util.Utiles;
import java.lang.reflect.Field;
import modelo.Boleto;
import modelo.Pasajero;
import modelo.Venta;

/**
 *
 * @author romer
 */
public class VentaControlDao extends DaoImplement<Venta>{
    private DynamicList<Venta> ventas;
    private Venta venta;
    long tiempoInicio;
    long tiempoFinal;

    public VentaControlDao() {
        super(Venta.class);
    }

    public DynamicList<Venta> getVentas() {
        ventas = all();
        return ventas;
    }

    public void setVentas(DynamicList<Venta> ventas) {
        this.ventas = ventas;
    }

    public Venta getVenta() {
        if (venta == null) {
            venta = new Venta();
        }
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Boolean persist(){
        venta.setId(all().getLenght()+1);
        return persist(venta);
    }

    public Boolean merge(Integer index){
        return merge(venta, index);
    }

    public double calcularMontoTotal() throws EmptyException {
        double montoTotal = 0.0;
        for (int i = 0; i < all().getLenght(); i++) {
            Venta venta = all().getInfo(i);
            montoTotal += venta.getBoleto().getValor();
        }
        return montoTotal;
    }

    //metodo quicksort
    public DynamicList<Venta> quickSort(DynamicList<Venta> lista, int inicio, int fin, String field, Integer tipo) throws Exception {
        tiempoInicio = 0;
        tiempoFinal = 0;
        Field attribute = Utiles.getField(Venta.class, field);
        Integer n = lista.getLenght() - 1;
        Venta[] ventasArr = lista.toArray();
        tiempoInicio = System.nanoTime();
        if (attribute != null) {
            quickSortRecursivo(ventasArr, 0, n, field, tipo);
        } else {
            throw new Exception("no existe el crieterio de busqueda");
        }
        tiempoFinal = System.nanoTime();
        System.out.println("Tiempo quick sort: " + (tiempoFinal-tiempoInicio) + " nanosegundos");
        return lista.toList(ventasArr);
    }

   private void quickSortRecursivo(Venta[] arr, int inicio, int fin, String field, Integer tipo){
        if (inicio < fin) {
            // Particionar el arreglo, arr[p] estará ahora en el lugar correcto
            int p = particion(arr, inicio, fin, field, tipo);

            // Ordenar recursivamente los elementos antes y después de la partición
            quickSortRecursivo(arr, inicio, p - 1, field, tipo);
            quickSortRecursivo(arr, p + 1, fin, field, tipo);
        }
    }

   private int particion(Venta[] arr, int inicio, int fin, String field, Integer tipo) {
        Venta pivote = arr[fin];
        int i = inicio - 1;

        for (int j = inicio; j < fin; j++) {
            if (arr[j].compare(pivote, field, tipo)) {
                i++;
                Venta temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        Venta temp = arr[i + 1];
        arr[i + 1] = arr[fin];
        arr[fin] = temp;

        return i + 1;
    }

    //metodo shellsort
    public DynamicList<Venta> shellSort(DynamicList<Venta> lista, String field, Integer type) throws EmptyException{
        tiempoInicio = 0;
        tiempoFinal = 0;
        Field attribute = Utiles.getField(Venta.class, field);
        int n = lista.getLenght();
        Venta[] personas = lista.toArray();
        tiempoInicio = System.nanoTime();
        if (attribute != null) {
            for (int gap = n / 2; gap > 0; gap /= 2) {
                for (int i = gap; i < n; i++) {
                    Venta aux = personas[i];
                    int j = i;
                    while (j >= gap && !personas[j - gap].compare(aux, field, type)) {
                        personas[j] = personas[j - gap];
                        j -= gap;
                    }
                    personas[j] = aux;
                }
            }
        } else {
            System.out.println("no existe el crieterio de busqueda");
        }
        tiempoFinal = System.nanoTime();
        System.out.println("Tiempo shell sort: " + (tiempoFinal-tiempoInicio) + " nanosegundos");
        return lista.toList(personas);
    }

    //busqueda binaria
    public DynamicList<Venta> busquedaBinaria(String texto, DynamicList<Venta> ventaList, String field) {
        DynamicList<Venta> resultados = new DynamicList<>();
        try {
            DynamicList<Venta> listaOrdenada = shellSort(ventaList, field, 0);
            Venta[] aux = listaOrdenada.toArray();

            int inicio = 0;
            int fin = aux.length - 1;

            while (inicio <= fin) {
                int medio = inicio + (fin - inicio) / 2;
                Venta venta = aux[medio];

                Field attribute = Utiles.getField(Venta.class, field);
                if (attribute != null) {
                    attribute.setAccessible(true);
                    Object fieldValue = attribute.get(venta);

                    if (fieldValue != null && getFieldValue(fieldValue, field).toLowerCase().contains(texto.toLowerCase())) {
                        // Se encontró el elemento
                        resultados.add(venta);
                        return resultados;
                    }
                    // Ajustar los índices según la comparación
                    if (fieldValue != null && getFieldValue(fieldValue, field).toLowerCase().compareTo(texto.toLowerCase()) < 0) {
                        inicio = medio + 1;
                    } else {
                        fin = medio - 1;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error en buscarBinario " + e.getMessage());
        }
        return resultados;
    }
    
    //busqueda Lineal binaria
    public DynamicList<Venta> busquedaLinealBinaria(String texto, DynamicList<Venta> ventaList, String field) {
        DynamicList<Venta> resultados = new DynamicList<>();
        try {
            DynamicList<Venta> listaOrdenada = shellSort(ventaList, field, 0);
            Venta[] aux = listaOrdenada.toArray();

            int inicio = 0;
            int fin = aux.length - 1;

            while (inicio <= fin) {
                int medio = inicio + (fin - inicio) / 2;
                Venta venta = aux[medio];

                Field attribute = Utiles.getField(Venta.class, field);
                if (attribute != null) {
                    attribute.setAccessible(true);
                    Object fieldValue = attribute.get(venta);

                    if (fieldValue != null && getFieldValue(fieldValue, field).toLowerCase().contains(texto.toLowerCase())) {
                        // Se encontró el elemento
                        resultados.add(venta);
                        // buscar hacia atrás para incluir todos los resultados
                        for (int i = medio - 1; i >= 0; i--) {
                            Venta ventaAnterior = aux[i];
                            if (getFieldValue(ventaAnterior, field).toLowerCase().contains(texto.toLowerCase())) {
                                resultados.add(ventaAnterior);
                            }
                        }
                        //buscar hacia adelante para incluir todos los resultados
                        for (int i = medio + 1; i < aux.length; i++) {
                            Venta ventaSiguiente = aux[i];
                            if (getFieldValue(ventaSiguiente, field).toLowerCase().contains(texto.toLowerCase())) {
                                resultados.add(ventaSiguiente);
                            }
                        }
                        return resultados;
                    }
                    // Ajustar los índices según la comparación
                    if (fieldValue != null && getFieldValue(fieldValue, field).toLowerCase().compareTo(texto.toLowerCase()) < 0) {
                        inicio = medio + 1;
                    } else {
                        fin = medio - 1;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error en buscarBinario " + e.getMessage());
        }
        return resultados;
    }

    public static String getFieldValue(Object object, String fieldName) {
        try {
            Field field = Utiles.getField(object.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                Object value = field.get(object);

                // Modificar para obtener el valor específico del campo
                if (value != null) {
                    if (value instanceof Boleto) {
                        Boleto boleto = (Boleto) value;
                        if ("destino".equals(fieldName)) {
                            return boleto.getDestino();
                        } else if ("origen".equals(fieldName)) {
                            return boleto.getOrigen();
                        }
                    } else if (value instanceof Pasajero) {
                        Pasajero pasajero = (Pasajero) value;
                        if ("nombre".equals(fieldName)) {
                            return pasajero.getNombre();
                        } else if ("apellido".equals(fieldName)) {
                            return pasajero.getApellido();
                        }
                    }
                }

                return (value != null) ? value.toString() : "";
            }
        } catch (IllegalAccessException e) {
            System.out.println("Error al acceder al campo: " + e.getMessage());
        }
        return "";
    }
}
