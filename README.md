# Métodos de Busqueda

Geovanny Romero
Computacion 3ro "A"

## Busqueda binaria

```java
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
```

## Busqueda lineal binaria

```java
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
```
