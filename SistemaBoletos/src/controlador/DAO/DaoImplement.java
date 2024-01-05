package controlador.DAO;

import com.thoughtworks.xstream.XStream;
import controlador.TDA.lista.DynamicList;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoImplement<T> implements DaoInterface<T>{
    private Class<T> clazz;
    private XStream conection;
    private String URL;

    public DaoImplement(Class<T> clazz) {
        this.clazz = clazz;
        conection = Bridge.getConection();
        URL = Bridge.URL + clazz.getSimpleName() + ".json";
    }

    @Override
    public Boolean persist(T data) {
        DynamicList<T> ld = all();
        ld.add(data);
        try {
            this.conection.toXML(ld, new FileWriter(URL));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean merge(T data, Integer index) {
        try {
            DynamicList<T> list = all();
            list.merge(data, index);
            conection.toXML(list, new FileWriter(URL));
            return true;
        } catch (Exception e) {
            Logger.getLogger(DaoImplement.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }
    @Override
    public DynamicList<T> all() {
        DynamicList<T> dl = new DynamicList<>();
        try {
            dl = (DynamicList<T>)conection.fromXML(new FileReader(URL));
        } catch (Exception e) {
        }
        return dl;
    }

    @Override
    public T get(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
