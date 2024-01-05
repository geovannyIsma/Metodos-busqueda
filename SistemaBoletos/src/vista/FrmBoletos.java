/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import controlador.DAO.pasajero.VentaControlDao;
import controlador.TDA.lista.Exceptions.EmptyException;
import controlador.util.Utiles;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import vista.tablas.ModeloTablaVenta;

/**
 *
 * @author romer
 */
public class FrmBoletos extends javax.swing.JFrame {
    private VentaControlDao ventaControlador = new VentaControlDao();
    private ModeloTablaVenta mtv = new ModeloTablaVenta();
    
    
    public Boolean verificar() {
        return (!txtNombres.getText().trim().isEmpty()
                && !txtApellidos.getText().trim().isEmpty()
                && !txtApellidos.getText().trim().isEmpty()
                && !txtDNI.getText().trim().isEmpty()
                && !txtTelefono.getText().trim().isEmpty());
    }
    
    private void cargarTabla() throws EmptyException{
        mtv.setVentas(ventaControlador.all());
        tbVenta.setModel(mtv);
        tbVenta.updateUI();
        
        double montoTotal = ventaControlador.calcularMontoTotal();
        lblMontoTotal.setText("Monto Total de Boletos Vendidos: " + montoTotal);
    }
    
    private void cargarVista(){
        int fila = tbVenta.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(null, "Escoja un registro de la tabla", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                ventaControlador.setVenta(mtv.getVentas().getInfo(fila));
                txtNombres.setText(ventaControlador.getVenta().getPasajero().getNombre());
                txtApellidos.setText(ventaControlador.getVenta().getPasajero().getApellido());
                txtDNI.setText(ventaControlador.getVenta().getPasajero().getDNI());
                txtDNI.setEnabled(false);
                txtTelefono.setText(ventaControlador.getVenta().getPasajero().getTelefono());
                txtOrigen.setText(ventaControlador.getVenta().getBoleto().getOrigen());
                txtDestino.setText(ventaControlador.getVenta().getBoleto().getDestino());
                txtFechaCompra.setText(ventaControlador.getVenta().getBoleto().getFormattedFechaCompra());
                txtFechaSalida.setText(ventaControlador.getVenta().getBoleto().getFormattedFechaSalida());
                
                double valor = ventaControlador.getVenta().getBoleto().getValor();
                txtValor.setText(String.valueOf(valor));
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    } 
    
    public void guardar() throws EmptyException, ParseException{
        if (verificar()) {
            if (Utiles.validadorDeCedula(txtDNI.getText())) {
                ventaControlador.getVenta().getPasajero().setNombre(txtNombres.getText());
                ventaControlador.getVenta().getPasajero().setApellido(txtApellidos.getText());
                ventaControlador.getVenta().getPasajero().setDNI(txtDNI.getText());
                ventaControlador.getVenta().getPasajero().setTelefono(txtTelefono.getText());
                ventaControlador.getVenta().getBoleto().setOrigen(txtOrigen.getText());
                ventaControlador.getVenta().getBoleto().setDestino(txtDestino.getText());
                
                if (Utiles.validarFecha(txtFechaSalida.getText()) && Utiles.validarFecha(txtFechaCompra.getText())) {
                    ventaControlador.getVenta().getBoleto().setFechaSalida(Utiles.setDateFormat(txtFechaSalida));
                    ventaControlador.getVenta().getBoleto().setFechaCompra(Utiles.setDateFormat(txtFechaCompra));
                    try {
                        double valor = Double.parseDouble(txtValor.getText());
                        ventaControlador.getVenta().getBoleto().setValor(valor);
                        if (ventaControlador.persist()) {
                            JOptionPane.showMessageDialog(null, "Datos guardados");
                            cargarTabla();
                            limpiar();
                            ventaControlador.setVenta(null);
                        } else {
                            JOptionPane.showMessageDialog(null, "No se pudo guardar, hubo un error");
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Valor inválido", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Fechas invalidas");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Cedula no valida");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Falta llenar campos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void modificar(){
        int fila = tbVenta.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(null, "Escoja un registro de la tabla", "Error", JOptionPane.ERROR_MESSAGE);
        } else{
            try {
                ventaControlador.getVenta().getPasajero().setNombre(txtNombres.getText());
                ventaControlador.getVenta().getPasajero().setApellido(txtApellidos.getText());
                ventaControlador.getVenta().getPasajero().setDNI(txtDNI.getText());
                ventaControlador.getVenta().getPasajero().setTelefono(txtTelefono.getText());
                ventaControlador.getVenta().getBoleto().setOrigen(txtOrigen.getText());
                ventaControlador.getVenta().getBoleto().setDestino(txtDestino.getText());
                ventaControlador.getVenta().getBoleto().setFechaSalida(Utiles.setDateFormat(txtFechaSalida));
                ventaControlador.getVenta().getBoleto().setFechaCompra(Utiles.setDateFormat(txtFechaCompra));
                
                double valor = Double.parseDouble(txtValor.getText());
                ventaControlador.getVenta().getBoleto().setValor(valor);
                
                if (ventaControlador.merge(ventaControlador.getVenta(), fila)) {
                    JOptionPane.showMessageDialog(null, "Datos modificados");
                    cargarTabla();
                    limpiar();
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo modificar, hubo un error");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void buscar(){
        String texto  = txtBusqueda.getText();
        String criterio = cbxCriterio1.getSelectedItem().toString();
        try {
            if (cbxMetodoBusqueda.getSelectedIndex() == 0) {
                mtv.setVentas(ventaControlador.busquedaBinaria(texto, ventaControlador.getVentas(), criterio));
            } else {
                mtv.setVentas(ventaControlador.busquedaLinealBinaria(texto, ventaControlador.getVentas(), criterio));
            }
            tbVenta.setModel(mtv);
            tbVenta.updateUI();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /*private void ordenar(){
        String criterio = cbxCriterio.getSelectedItem().toString();
        Integer tipo = 0;
        if (btnTipo.isSelected()) {
            tipo = 1;
        }
        try {
            if (cbxMetodo.getSelectedIndex() == 0) {
                mtv.setVentas(ventaControlador.quickSort(ventaControlador.getVentas(),
                        0, ventaControlador.getVentas().getLenght()-1, criterio, tipo));
            } else {
                mtv.setVentas(ventaControlador.shellSort(ventaControlador.getVentas(), criterio, tipo));
            }
            
            tbVenta.setModel(mtv);
            tbVenta.updateUI();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        } 
        
    }*/
    
    private void limpiar() throws EmptyException{
        txtNombres.setText("");
        txtApellidos.setText("");
        txtDNI.setEnabled(true);
        txtDNI.setText("");
        txtTelefono.setText("");
        txtDestino.setText("");
        txtOrigen.setText("");
        txtDestino.setText("");
        txtFechaCompra.setText("");
        txtFechaSalida.setText("");
        txtValor.setText("");
        cargarTabla();
    }
    
    /**
     * Creates new form RegistrarPasajero
     */
    public FrmBoletos() throws EmptyException {
        initComponents();
        limpiar();
        setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNombres = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtApellidos = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtDNI = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtOrigen = new javax.swing.JTextField();
        txtDestino = new javax.swing.JTextField();
        txtValor = new javax.swing.JTextField();
        txtFechaCompra = new javax.swing.JTextField();
        txtFechaSalida = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbVenta = new javax.swing.JTable();
        btnGuardar = new javax.swing.JButton();
        btnSeleccionar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        lblMontoTotal = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        cbxCriterio1 = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        txtBusqueda = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        cbxMetodoBusqueda = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Hack Nerd Font Mono", 0, 14)); // NOI18N
        jLabel1.setText("Registro Ventas");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Datos del pasajero");

        txtNombres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombresActionPerformed(evt);
            }
        });

        jLabel3.setText("Apellidos:");

        jLabel4.setText("Nombres:");

        jLabel5.setText("DNI:");

        jLabel6.setText("Telefono:");

        txtTelefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Datos del boleto:");

        jLabel8.setText("Origen:");

        jLabel9.setText("Destino:");

        jLabel10.setText("Fecha de salida: ");

        jLabel11.setText("Fecha de compra:");

        jLabel12.setText("Valor:");

        txtFechaCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFechaCompraActionPerformed(evt);
            }
        });

        tbVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6"
            }
        ));
        jScrollPane1.setViewportView(tbVenta);

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnSeleccionar.setText("Seleccionar");
        btnSeleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeleccionarActionPerformed(evt);
            }
        });

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        lblMontoTotal.setText("Monto");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(153, 153, 153));
        jLabel13.setText("dd/MM/yyyy hh:mm a");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(153, 153, 153));
        jLabel14.setText("dd/MM/yyyy hh:mm a");

        jLabel17.setText("Buscar por:");

        cbxCriterio1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "nombre", "apellido", "destino", "origen" }));

        jLabel18.setText("Busqueda");

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jLabel15.setText("Metodo:");

        cbxMetodoBusqueda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Busqueda binaria", "Busqueda lineal binaria" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(489, 489, 489)
                        .addComponent(jLabel1)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(txtNombres)
                                                .addComponent(txtApellidos)
                                                .addComponent(txtDNI)
                                                .addComponent(txtTelefono)))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addComponent(jLabel10))
                                            .addGap(28, 28, 28)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(txtDestino)
                                                .addComponent(txtFechaSalida)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(txtOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(0, 0, Short.MAX_VALUE))))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel11)
                                            .addGap(18, 18, 18)
                                            .addComponent(txtFechaCompra))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel7))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING))
                                        .addGap(64, 64, 64)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1015, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel15)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cbxMetodoBusqueda, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel17)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cbxCriterio1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel18)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnBuscar))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnGuardar)
                                .addGap(18, 18, 18)
                                .addComponent(btnModificar)
                                .addGap(13, 13, 13)
                                .addComponent(btnSeleccionar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnCancelar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnSalir))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblMontoTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel17)
                    .addComponent(cbxCriterio1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(txtBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar)
                    .addComponent(jLabel15)
                    .addComponent(cbxMetodoBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtDNI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(48, 48, 48)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(txtFechaSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(txtFechaCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblMontoTotal)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnSeleccionar)
                    .addComponent(btnModificar)
                    .addComponent(btnSalir)
                    .addComponent(btnCancelar))
                .addGap(28, 28, 28))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNombresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombresActionPerformed

    private void txtTelefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoActionPerformed

    private void txtFechaCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFechaCompraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFechaCompraActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        try {
            limpiar();
        } catch (EmptyException ex) {
            Logger.getLogger(FrmBoletos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeleccionarActionPerformed
        cargarVista();
    }//GEN-LAST:event_btnSeleccionarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        try {
            guardar();
        } catch (EmptyException ex) {
            Logger.getLogger(FrmBoletos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(FrmBoletos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        modificar();
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        buscar();
    }//GEN-LAST:event_btnBuscarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmBoletos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmBoletos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmBoletos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmBoletos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new FrmBoletos().setVisible(true);
                } catch (EmptyException ex) {
                    Logger.getLogger(FrmBoletos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnSeleccionar;
    private javax.swing.JComboBox<String> cbxCriterio1;
    private javax.swing.JComboBox<String> cbxMetodoBusqueda;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblMontoTotal;
    private javax.swing.JTable tbVenta;
    private javax.swing.JTextField txtApellidos;
    private javax.swing.JTextField txtBusqueda;
    private javax.swing.JTextField txtDNI;
    private javax.swing.JTextField txtDestino;
    private javax.swing.JTextField txtFechaCompra;
    private javax.swing.JTextField txtFechaSalida;
    private javax.swing.JTextField txtNombres;
    private javax.swing.JTextField txtOrigen;
    private javax.swing.JTextField txtTelefono;
    private javax.swing.JTextField txtValor;
    // End of variables declaration//GEN-END:variables
}
