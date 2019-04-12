package pvdonialeti;

import java.awt.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Erik Ortiz Q.
 */
public class Principal extends javax.swing.JFrame {
    
    private int[] comidaSeleccionada = new int[100];
    public int[] comidaParaVenta = new int[100];
    public String[] list0 = new String[100];
    //public List<String> list0 = new ArrayList<String>();
    int maxDato = 0;
     // CONECTAR A  LA BD
    public Connection hacerConexion(){
            Connection con = null;

            try{
                con = DriverManager.getConnection("jdbc:mysql://localhost/pvdonialeti","root","");
            }catch(SQLException ex){
                System.out.println(ex.getMessage());
            }

            return con;
    }
    
   
    public ArrayList<ClaseComida> ListaComidas()
    {
        ArrayList<ClaseComida> comida = new ArrayList<>();
        
        Statement st;
        ResultSet rs;
         
        try{
            Connection con = hacerConexion();
            st = con.createStatement();
                              
            //BUSQUEDA DEFAULT (ALL)
            
            String searchQuery = "SELECT * FROM `comidas`";
            rs = st.executeQuery(searchQuery);

            ClaseComida claseComida;

            while(rs.next())
            {
                claseComida = new ClaseComida(
                                 rs.getInt("id"),
                                 rs.getString("nombre"),
                                 rs.getDouble("costo")                                     
                                );
                comida.add(claseComida);
            }
            
            con.close();
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        
        return comida;
    }
    
    
    //para regresar con las comidas en el carrito
    public ArrayList<ClaseComida> ListaComidasSeleccionadas()
    {
        ArrayList<ClaseComida> comida = new ArrayList<>();
        
        Statement st;
        ResultSet rs;
         
        try{
            Connection con = hacerConexion();
            st = con.createStatement();
                              
            ClaseComida claseComida;

           for (int i=0; i< comidaSeleccionada.length; i++){
                String searchQuery = "SELECT * FROM `comidas` WHERE `id` ='"+comidaSeleccionada[i]+"'";
                rs = st.executeQuery(searchQuery);
                while(rs.next())
                {
                    claseComida = new ClaseComida(
                                     rs.getInt("id"),
                                 rs.getString("nombre"),
                                 rs.getDouble("costo")                                     
                                );
                    comida.add(claseComida);
                }
            }
            
            con.close();
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        comidaSeleccionada = new int[comidaSeleccionada.length];
        return comida;
    }
    
    public void traeraTabla()
    {
        ArrayList<ClaseComida> comidas;
        comidas = ListaComidas();
      
        //DefaultTableModel model=(DefaultTableModel) tableComidas.getModel(); 
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new Object[]{"ID","Nombre","Costo"});
   
        
        Object[] row = new Object[3];
        
        for(int i = 0; i < comidas.size(); i++)
        {
            row[0] = comidas.get(i).getId();
            row[1] = comidas.get(i).getNombre();
            row[2] = comidas.get(i).getCosto();
            
            model.addRow(row);
        }
       // tableComidas.setAutoResizeMode(tableComidas.AUTO_RESIZE_ALL_COLUMNS);
       
       tableComidas.setModel(model);
       
      
    }
    
    //para traer a la tabla del carrito
    public void traeraTabla2()
    {
        ArrayList<ClaseComida> comidas;
        comidas = ListaComidasSeleccionadas();
      
        DefaultTableModel model=(DefaultTableModel) tableCarrito.getModel(); 

        model.setColumnIdentifiers(new Object[]{"ID","Nombre","Costo","Cantidad"});
   
        
        Object[] row = new Object[3];
        
        for(int i = 0; i < comidas.size(); i++)
        {
            row[0] = comidas.get(i).getId();
            row[1] = comidas.get(i).getNombre();
            row[2] = comidas.get(i).getCosto();
            
            model.addRow(row);
        }
       tableCarrito.setModel(model);
       
    }
    
    public void eliminarComida(){
        DefaultTableModel model=(DefaultTableModel) tableCarrito.getModel();
        if (!tableCarrito.isRowSelected(tableCarrito.getSelectedRow()))
           JOptionPane.showMessageDialog(null, "Seleccione comida a ELIMINAR!");
        int[] row = new int[100];
        row = tableCarrito.getSelectedRows();
        for (int i=0; i<row.length; i++){
            try{
                model.removeRow(row[i]);
        

            }catch(ArrayIndexOutOfBoundsException ex){
                JOptionPane.showMessageDialog(null,"Porfavor, seleccione una comida");
            }
        }
        tableComidas.clearSelection();
    }
    public static String[] quitarlista(String[] list0, int index) 
    { 
  
        // If the array is empty 
        // or the index is not in array range 
        // return the original array 
        if (list0 == null|| index < 0|| index >= list0.length) { 
            JOptionPane.showMessageDialog(null,  "Seleccione Elemento");
            return list0; 
        } 
  
        // Create another array of size one less 
        String[] anotherArray = new String[list0.length - 1]; 
  
        // Copy the elements except the index 
        // from original array to the other array 
        for (int i = 0, k = 0; i < list0.length; i++) { 
  
            // if the index is 
            // the removal element index 
            if (i == index) { 
                continue; 
            } 
  
            // if the index is not 
            // the removal element index 
            anotherArray[k++] = list0[i]; 
        } 
  
        // return the resultant array 
        return anotherArray; 
    } 
    
    public void sendshit(){
        Ventas frameVentas = new Ventas();
        frameVentas.setVisible(true);
        //send tabla del carro
        frameVentas.jTable1.setModel(tableCarrito.getModel());
        //send encargos
        frameVentas.list0 = this.list0;
        frameVentas.maxDato = this.maxDato;
        
        this.setVisible(false);
        
    }
    
    /**
     * Creates new form Principal
     */
    public Principal() {
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

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTab = new javax.swing.JTabbedPane();
        tabEncargos = new javax.swing.JPanel();
        txtComida0 = new javax.swing.JTextField();
        btnAgregar0 = new javax.swing.JButton();
        btnEliminar0 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        lstComidas0 = new javax.swing.JList<>();
        tabVentas = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnAgregar1 = new javax.swing.JButton();
        btnSiguiente = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableComidas = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableCarrito = new javax.swing.JTable();
        btnEliminar = new javax.swing.JButton();
        tabActualizar = new javax.swing.JPanel();
        tabCorte = new javax.swing.JPanel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Punto de Venta - Doña Leti");

        jTab.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTabFocusGained(evt);
            }
        });

        tabEncargos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tabEncargosFocusGained(evt);
            }
        });

        txtComida0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtComida0ActionPerformed(evt);
            }
        });

        btnAgregar0.setText("Agregar");
        btnAgregar0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregar0ActionPerformed(evt);
            }
        });

        btnEliminar0.setText("Eliminar de la Lista");
        btnEliminar0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminar0ActionPerformed(evt);
            }
        });

        jScrollPane5.setViewportView(lstComidas0);

        javax.swing.GroupLayout tabEncargosLayout = new javax.swing.GroupLayout(tabEncargos);
        tabEncargos.setLayout(tabEncargosLayout);
        tabEncargosLayout.setHorizontalGroup(
            tabEncargosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabEncargosLayout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addGroup(tabEncargosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtComida0)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(tabEncargosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAgregar0, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnEliminar0, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(134, 134, 134))
        );
        tabEncargosLayout.setVerticalGroup(
            tabEncargosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabEncargosLayout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(tabEncargosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtComida0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregar0))
                .addGap(75, 75, 75)
                .addGroup(tabEncargosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabEncargosLayout.createSequentialGroup()
                        .addComponent(btnEliminar0)
                        .addGap(11, 11, 11)))
                .addContainerGap(87, Short.MAX_VALUE))
        );

        jTab.addTab("Encargos", tabEncargos);

        jLabel1.setText("Seleccione un Item");

        jLabel2.setText("Carrito");

        btnAgregar1.setText("Agregar al Carrito");
        btnAgregar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregar1ActionPerformed(evt);
            }
        });

        btnSiguiente.setText("Siguiente");
        btnSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSiguienteActionPerformed(evt);
            }
        });

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        tableComidas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "Costo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tableComidas);
        if (tableComidas.getColumnModel().getColumnCount() > 0) {
            tableComidas.getColumnModel().getColumn(0).setPreferredWidth(15);
            tableComidas.getColumnModel().getColumn(2).setPreferredWidth(20);
        }

        tableCarrito.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "Costo", "Cantidad"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Integer.class
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
        jScrollPane4.setViewportView(tableCarrito);
        if (tableCarrito.getColumnModel().getColumnCount() > 0) {
            tableCarrito.getColumnModel().getColumn(0).setPreferredWidth(15);
            tableCarrito.getColumnModel().getColumn(2).setPreferredWidth(20);
        }

        btnEliminar.setText("Eliminar del carrito");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tabVentasLayout = new javax.swing.GroupLayout(tabVentas);
        tabVentas.setLayout(tabVentasLayout);
        tabVentasLayout.setHorizontalGroup(
            tabVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabVentasLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(tabVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabVentasLayout.createSequentialGroup()
                        .addGroup(tabVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(tabVentasLayout.createSequentialGroup()
                                .addComponent(btnAgregar1)
                                .addGap(256, 256, 256)
                                .addComponent(btnEliminar)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(tabVentasLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(25, 25, 25))
                    .addGroup(tabVentasLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(172, 172, 172))
                    .addGroup(tabVentasLayout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                        .addGap(26, 26, 26)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21))))
        );
        tabVentasLayout.setVerticalGroup(
            tabVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabVentasLayout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addGroup(tabVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(40, 40, 40)
                .addGroup(tabVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabVentasLayout.createSequentialGroup()
                        .addGroup(tabVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(tabVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAgregar1)
                            .addComponent(btnEliminar))
                        .addGap(18, 18, 18)
                        .addComponent(btnSiguiente)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabVentasLayout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(120, 120, 120))))
        );

        jTab.addTab("Ventas", tabVentas);

        javax.swing.GroupLayout tabActualizarLayout = new javax.swing.GroupLayout(tabActualizar);
        tabActualizar.setLayout(tabActualizarLayout);
        tabActualizarLayout.setHorizontalGroup(
            tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 741, Short.MAX_VALUE)
        );
        tabActualizarLayout.setVerticalGroup(
            tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 374, Short.MAX_VALUE)
        );

        jTab.addTab("Actualizar", tabActualizar);

        javax.swing.GroupLayout tabCorteLayout = new javax.swing.GroupLayout(tabCorte);
        tabCorte.setLayout(tabCorteLayout);
        tabCorteLayout.setHorizontalGroup(
            tabCorteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 741, Short.MAX_VALUE)
        );
        tabCorteLayout.setVerticalGroup(
            tabCorteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 374, Short.MAX_VALUE)
        );

        jTab.addTab("Corte", tabCorte);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTab)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTab)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregar1ActionPerformed
        // TODO add your handling code here:
        // get nomre para buscar en bd
        //QUEDO PERFECTO, SOLO TENGO QUE HACER QUE VERIFIQUE QUE NO AGREGUE LA MISMA COMIDA
        int columna = 0;
        int[] row = new int[100];
        if (tableCarrito.getCellEditor() != null) {
            tableCarrito.getCellEditor().stopCellEditing();
        }

        row = tableComidas.getSelectedRows();
        
        //pruebas para idiotas
        if (!tableComidas.isRowSelected(tableComidas.getSelectedRow()))
        JOptionPane.showMessageDialog(null, "Seleccione reloj!");
        for (int i=0; i<row.length; i++){
            comidaSeleccionada[i] =  (int) tableComidas.getValueAt(row[i], columna);
            //JOptionPane.showMessageDialog(null, comidaSeleccionada[i] );
        }
        if (comidaSeleccionada != null )
        traeraTabla2();

        tableComidas.clearSelection();
        
    
    }//GEN-LAST:event_btnAgregar1ActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        eliminarComida();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiguienteActionPerformed
        // TODO add your handling code here:
        

        int columna = 0;
        int[] row = new int[100];
        //para hacer que guarde el ultimo cambio en el editor
        if (tableCarrito.getCellEditor() != null) {
            tableCarrito.getCellEditor().stopCellEditing();
        }
        //jTable1.clearSelection();
        tableCarrito.selectAll();
        row = tableCarrito.getSelectedRows();
        for (int i=0; i<row.length; i++){
            try{
                comidaParaVenta[i] =  (int) tableCarrito.getValueAt(row[i], columna);
            }catch(ArrayIndexOutOfBoundsException ex){
                JOptionPane.showMessageDialog(null,"Porfavor, seleccione una comida");
            }
        }

        sendshit();
    }//GEN-LAST:event_btnSiguienteActionPerformed

    private void jTabFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTabFocusGained
        traeraTabla();
    }//GEN-LAST:event_jTabFocusGained

    private void txtComida0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtComida0ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtComida0ActionPerformed

    private void btnEliminar0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminar0ActionPerformed
        // TODO add your handling code here:
        //lstComidas0.getSelectedIndex();
        list0 = quitarlista(list0, lstComidas0.getSelectedIndex());
        lstComidas0.setListData(list0);
    }//GEN-LAST:event_btnEliminar0ActionPerformed

    private void btnAgregar0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregar0ActionPerformed
        // TODO add your handling code here:
        if(txtComida0.equals(null)){
            JOptionPane.showMessageDialog(null, "Espacio en blanco!");
            
        }else{
            
            list0 [maxDato] = txtComida0.getText();
            maxDato++;
            lstComidas0.setListData(list0);
            txtComida0.setText("");
        }
        //lstComidas0.set
    }//GEN-LAST:event_btnAgregar0ActionPerformed

    private void tabEncargosFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tabEncargosFocusGained
        // TODO add your handling code here:
        if (list0 != null)
            lstComidas0.setListData(list0);
    }//GEN-LAST:event_tabEncargosFocusGained
    
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
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar0;
    private javax.swing.JButton btnAgregar1;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnEliminar0;
    private javax.swing.JButton btnSiguiente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTab;
    private javax.swing.JTable jTable1;
    public javax.swing.JList<String> lstComidas0;
    private javax.swing.JPanel tabActualizar;
    private javax.swing.JPanel tabCorte;
    private javax.swing.JPanel tabEncargos;
    private javax.swing.JPanel tabVentas;
    public javax.swing.JTable tableCarrito;
    private javax.swing.JTable tableComidas;
    private javax.swing.JTextField txtComida0;
    // End of variables declaration//GEN-END:variables
}
