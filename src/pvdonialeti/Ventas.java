package pvdonialeti;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Erik Ortiz Q.
 */
public class Ventas extends javax.swing.JFrame {
    double total = 0;
    String[] list0 = new String[100];
    int[] comida = new int[100];
    int[] cantidad = new int[100];
    double[] costo = new double[100];
    String listaComidas = "";
    public int maxDato = 0;
    public double corteTotal = 0;
    int ultimoID = 0;

    public void calcularTotal(){       
        total = 0;
        
        try{
            for (int i = 0; i < tableVentas.getRowCount(); i++){
                total = total + ((double)tableVentas.getValueAt(i, 2)*(int)tableVentas.getValueAt(i, 3));
            }
            txtTotal.setText(""+total);
        }catch(NullPointerException ex) {
            System.out.println(ex.getMessage());  
            txtTotal.setText("Verifique Cantidad!");
        }   
    }
    
    public Connection hacerConexion(){
            Connection con = null;

            try{
                con = DriverManager.getConnection("jdbc:mysql://localhost/pvdonialeti2","root","");
            }catch(SQLException ex){
                System.out.println(ex.getMessage());
            }

            return con;
    }
    
    public void returnshit (boolean ventaHecha){
        if (ventaHecha == false){
            Principal framePrincipal = new Principal();
            framePrincipal.setVisible(true);
            //regresar el carrito
            framePrincipal.tableCarrito.setModel(tableVentas.getModel());
            //regresar los encargos
            framePrincipal.lstComidas0.setListData(list0);
            framePrincipal.list0 = this.list0;
            framePrincipal.maxDato = this.maxDato;
            framePrincipal.corteTotal = this.corteTotal;
            framePrincipal.jTab.setSelectedIndex(1);
            this.dispose();
        }else{
            Principal framePrincipal = new Principal();
            framePrincipal.setVisible(true);

            //regresar los encargos
            framePrincipal.lstComidas0.setListData(list0);
            framePrincipal.list0 = this.list0;
            framePrincipal.maxDato = this.maxDato;
            framePrincipal.corteTotal = this.corteTotal;
            framePrincipal.jTab.setSelectedIndex(1);
            this.dispose();
        }
        
    }
    
    public void obtenerComidas(){   
        
        tableVentas.selectAll();
        for(int i=0; i<tableVentas.getRowCount();i++){
           
            try{
                comida[i] = (int) tableVentas.getValueAt(i, 0);
                costo[i] = (double) tableVentas.getValueAt(i, 2);
                cantidad[i] = (int) tableVentas.getValueAt(i, 3);
            }catch(NullPointerException ex){      
                tableVentas.clearSelection();
                JOptionPane.showConfirmDialog(null, ex);
            }
        }
    }
    
    public void stringidComidas(){
        listaComidas = "";
        for (int i=0; i<tableVentas.getRowCount(); i++){
            //CORREGIR
            listaComidas = "["+comida[i] + ", Cantidad: " + cantidad[i] + "]-" + listaComidas ;
        }
        
    }
    
    public void registrarVentaTotal(){
        Date fecha = new Date();
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, d MMM yyyy");
        String fechaString = DATE_FORMAT.format(fecha);
        
        try{
            Connection con = hacerConexion();
            Statement st = con.createStatement();
            Statement statement = con.createStatement();
            statement.executeUpdate("INSERT INTO `ventas_totales` (`id_venta`, `fecha_venta`, `precio`)"
                                    + "VALUES(NULL, '"+fechaString+"', '"+total+"')" );
           
            con.close();
            st.close();
            corteTotal += total;
            //JOptionPane.showMessageDialog(null, "Venta registrada con Exito!");
            //returnshit(true);
        }catch(SQLException ex) {
             System.out.println(ex.getMessage());
            
        }
    }
    
    public void obtenerUltimoID(){
        try {
            Statement st;
            ResultSet rs = null;
            Connection con = hacerConexion();
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM `ventas_totales` ORDER BY `id_venta` DESC LIMIT 1");          
            while(rs.next())
            {         
                ultimoID = rs.getInt(1);
            }
            con.close();
            rs.close();
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
    
    public void registrarVentaUnitaria(){
        double precioTot = 0;
        
        for (int i=0; i<comida.length; i++){
            try{
                precioTot = costo[i]*cantidad[i];
                Connection con = hacerConexion();
                Statement st = con.createStatement();
                st.executeUpdate("INSERT INTO `ventas_unitarias` (`id_producto`, `id_venta_tot`, `cantidad`, `precio_total`)"
                                        + "VALUES('"+comida[i]+"','"+ultimoID+"','"+cantidad[i]+"','"+precioTot+"')" );
                con.close();
                st.close();
                
                
            }catch(SQLException ex) {
                System.out.println(ex.getMessage());
            
            }
            
        }
        JOptionPane.showMessageDialog(null, "Venta registrada con Exito!");
        returnshit(true);
        
    }
    
    /**
     * Creates new form Ventas
     */
    public Ventas() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tableVentas = new javax.swing.JTable();
        btnRegresar = new javax.swing.JButton();
        txtTotal = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btnRealizar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Realizar Venta");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        tableVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableVentas);

        btnRegresar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnRegresar.setText("Regresar");
        btnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarActionPerformed(evt);
            }
        });

        txtTotal.setEditable(false);
        txtTotal.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        txtTotal.setForeground(new java.awt.Color(0, 0, 204));
        txtTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("TOTAL:");

        btnRealizar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnRealizar.setText("Realizar Venta");
        btnRealizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRealizarActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Resumen de la Venta");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/document-preview.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 103, Short.MAX_VALUE)
                                .addComponent(btnRealizar))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(txtTotal))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel1))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(37, 37, 37)
                                        .addComponent(jLabel2))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(74, 74, 74)
                                        .addComponent(jLabel3)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnRegresar)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(32, 32, 32)
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(82, 82, 82)
                        .addComponent(btnRealizar)))
                .addGap(18, 18, 18)
                .addComponent(btnRegresar)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarActionPerformed
        if (tableVentas.getCellEditor() != null) {
            tableVentas.getCellEditor().stopCellEditing();
        }
        
        returnshit(false);;
        /*
        Principal framePrincipal = new Principal();
        framePrincipal.setVisible(true);
        //regresar el carrito
        framePrincipal.tableCarrito.setModel(jTable1.getModel());
        //regresar los encargos
        framePrincipal.lstComidas0.setListData(list0);
        framePrincipal.list0 = this.list0;
        framePrincipal.maxDato = this.maxDato;
        
        this.dispose();
        */
    }//GEN-LAST:event_btnRegresarActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        calcularTotal();
    }//GEN-LAST:event_formWindowOpened

    private void btnRealizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizarActionPerformed
        // TODO add your handling code here:
        //obtenerComidas();
        //stringidComidas();
        registrarVentaTotal();
        obtenerUltimoID();
        obtenerComidas();
        registrarVentaUnitaria();
    }//GEN-LAST:event_btnRealizarActionPerformed

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
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ventas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ventas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRealizar;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTable tableVentas;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
