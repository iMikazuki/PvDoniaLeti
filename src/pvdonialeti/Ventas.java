package pvdonialeti;

import java.sql.Connection;
import java.sql.DriverManager;
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
    String[] comida = new String[100];
    int[] cantidad = new int[100];
    String listaComidas = "";
    public int maxDato = 0;
    public double corteTotal = 0;
      //VER PORQUE NO PUEDO  SACAR EL TOTAL, ME MANDA UNA EXCEPCION AL CALCULARLO
    public void calcularTotal(){
        //ESTOY PENSANDO EN HACER ALGO PARA QUE ME MUESTRE EL TOTAL EN EL TXT
        //HAY UN PROBLEMA Y ES QUE TENGO QUE HACER QUE NO MANDE NULL POINTER
        //DESDE BUSCAR STOCK OOOOOOOOOOOOOOOOOOOOOO HAGO UN TRY CATCH
        //HACIENDO UN NUEVO METODO SOLO PARA ESO!!!
    
        
        total = 0;
        
        try{
            //jTable1.selectAll();
            for (int i = 0; i < tableVentas.getRowCount(); i++){
                //cantidad[i] = (int) jTable1.getValueAt(i, 5);
                //total = total + (double) jTable1.getValueAt(i, 3);
                total = total + ((double)tableVentas.getValueAt(i, 2)*(int)tableVentas.getValueAt(i, 3));
                //JOptionPane.showMessageDialog(jTable1.getValueAt(i, 2)+"");
            }
            txtTotal.setText(""+total);
            //jTable1.clearSelection();
        }catch(NullPointerException ex) {
            System.out.println(ex.getMessage());  
            //JOptionPane.showMessageDialog(null, "Ponga Cantidad!");
            txtTotal.setText("Verifique Cantidad!");
        }   
        
    }
    
    public Connection hacerConexion(){
            Connection con = null;

            try{
                con = DriverManager.getConnection("jdbc:mysql://localhost/pvdonialeti","root","");
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
        //HACER UN TRY `PARA NULLPOINTER PARA VERIFICAR QUE NO META NULL EN ARREGLO INT
       
        tableVentas.selectAll();
        for(int i=0; i<tableVentas.getRowCount();i++){
           
            try{
                comida[i] = (String) tableVentas.getValueAt(i, 1);
                cantidad[i] = (int) tableVentas.getValueAt(i, 3);
            }catch(NullPointerException ex){
                
                tableVentas.clearSelection();
                //jTable1.changeSelection(i, 5, true, true);
            }
            
            //JOptionPane.showMessageDialog(null, cantidad[i]);
        }
        
      
    }
    
    public void stringidComidas(){
        listaComidas = "";
        for (int i=0; i<tableVentas.getRowCount(); i++){
            //CORREGIR
            listaComidas = "["+comida[i] + ", Cantidad: " + cantidad[i] + "]-" + listaComidas ;
        }
        
    }
    
    public void registrarVenta(){
        Date fecha = new Date();
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, d MMM yyyy");
        String fechaString = DATE_FORMAT.format(fecha);
        
        try{
            Connection con = hacerConexion();
            Statement st = con.createStatement();
            Statement statement = con.createStatement();
            statement.executeUpdate("INSERT INTO `ventas` (`idVenta`, `comidas`, `total`,`fecha`)"
                                    + "VALUES(NULL, '"+listaComidas+"', '"+total+"', '"+fechaString+"')" );
            con.close();
            st.close();
            corteTotal += total;
            JOptionPane.showMessageDialog(null, "Venta registrada con Exito!");
            returnshit(true);
        }catch(SQLException ex) {
             System.out.println(ex.getMessage());
            
        }
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

        btnRegresar.setText("Regresar");
        btnRegresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarActionPerformed(evt);
            }
        });

        txtTotal.setEditable(false);
        txtTotal.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        txtTotal.setForeground(new java.awt.Color(0, 0, 204));
        txtTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("TOTAL:");

        btnRealizar.setText("Realizar Venta");
        btnRealizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRealizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTotal)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 135, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnRegresar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRealizar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRegresar)
                    .addComponent(btnRealizar))
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
        obtenerComidas();
        stringidComidas();
        registrarVenta();
        
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
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTable tableVentas;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
