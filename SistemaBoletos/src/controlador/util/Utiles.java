/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador.util;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.JTextField;
import modelo.Boleto;
import modelo.Pasajero;


public class Utiles {

    //Codigo para validar la cedula
    public static boolean validadorDeCedula(String cedula) {
        boolean cedulaCorrecta = false;

        try {

            if (cedula.length() == 10) // ConstantesApp.LongitudCedula
            {
                int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
                if (tercerDigito < 6) {
                    // Coeficientes de validación cédula
                    // El decimo digito se lo considera dígito verificador
                    int[] coefValCedula = {2, 1, 2, 1, 2, 1, 2, 1, 2};
                    int verificador = Integer.parseInt(cedula.substring(9, 10));
                    int suma = 0;
                    int digito = 0;
                    for (int i = 0; i < (cedula.length() - 1); i++) {
                        digito = Integer.parseInt(cedula.substring(i, i + 1)) * coefValCedula[i];
                        suma += ((digito % 10) + (digito / 10));
                    }

                    if ((suma % 10 == 0) && (suma % 10 == verificador)) {
                        cedulaCorrecta = true;
                    } else if ((10 - (suma % 10)) == verificador) {
                        cedulaCorrecta = true;
                    } else {
                        cedulaCorrecta = false;
                    }
                } else {
                    cedulaCorrecta = false;
                }
            } else {
                cedulaCorrecta = false;
            }
        } catch (NumberFormatException nfe) {
            cedulaCorrecta = false;
        } catch (Exception err) {
            System.out.println("Una excepcion ocurrio en el proceso de validadcion");
            cedulaCorrecta = false;
        }

        if (!cedulaCorrecta) {
            System.out.println("La Cédula ingresada es Incorrecta");
        }
        return cedulaCorrecta;
    }
    
    public static boolean validarFecha(String fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH);
        sdf.setLenient(false);

        try {
            sdf.parse(fecha);
            return true;
        } catch (ParseException e) {
            System.out.println("Error al parsear la fecha: " + e.getMessage());
            return false;
        }
    }
    
    public static Date setDateFormat(JTextField texto) throws ParseException{
        return new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).parse(texto.getText());
    }

    public static Date parseDate(String fecha) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).parse(fecha);
        } catch (ParseException e) {
            System.out.println("Error al parsear la fecha: " + e.getMessage());
            return null;
        }
    }
    
    public static Field getField(Class clazz, String attribute){
        Field field = null;
        //Field[] fields = clazz.getFields(); //ver los atributos declarados en la clase padre
        for (Field f: clazz.getSuperclass().getDeclaredFields()) {
            //System.out.println(f.getName() + " " + f.getType().getName());
            if (f.getName().equalsIgnoreCase(attribute)) {
                field = f;
                break;
            }
        }
        for (Field f: clazz.getDeclaredFields()) {
            //System.out.println(f.getName() + " " + f.getType().getName());
            if (f.getName().equalsIgnoreCase(attribute)) {
                field = f;
                break;
            }
        }
        
        if (field == null) {
            for (Field associatedField : clazz.getDeclaredFields()) {
                if (isAssociatedField(associatedField, attribute)) {
                    field = associatedField;
                    break;
                }
            }
        }
        return field;
    }
    
    private static boolean isAssociatedField(Field field, String attributeName) {
        if (field.getType().isAssignableFrom(Pasajero.class)) {
            return Utiles.getField(Pasajero.class, attributeName) != null || Utiles.getField(field.getType(), attributeName) != null;
        } else if (field.getType().isAssignableFrom(Boleto.class)) {
            return Utiles.getField(Boleto.class, attributeName) != null || Utiles.getField(field.getType(), attributeName) != null;
        }

        return false;
    }

    
}
