package pvdonialeti;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Erik Ortiz Q.
 */
public class Principal extends javax.swing.JFrame {
    
    private int[] comidaSeleccionada = new int[100];    //array para transportar ID al carrito
    public int[] comidaParaVenta = new int[100];        //array para transportar ID al carrito
    public String[] list0 = new String[100];            //array para pestaña de Encargos
    public double corteTotal = 0;                       //corte general
    int maxDato = 0;                                    //me sirve para saber el dato del array list0
    
     // CONECTAR A  LA BD
    public Connection hacerConexion(){
            Connection con = null;

            try{
                con = DriverManager.getConnection("jdbc:mysql://localhost/pvdonialeti2","root","");
            }catch(SQLException ex){
                System.out.println(ex.getMessage());
            }

            return con;
    }
    
    //////////////////////////////////      ARRAYLIST     ///////////////////////////////////////////////// 
    public ArrayList<ClaseComida> ListaComidas(){
        ArrayList<ClaseComida> comida = new ArrayList<>();
        Statement st;
        ResultSet rs;
         
        try{
            Connection con = hacerConexion();
            st = con.createStatement();
                              
            //BUSQUEDA DEFAULT (ALL)
            
            String searchQuery = "SELECT * FROM `productos`";
            rs = st.executeQuery(searchQuery);

            ClaseComida claseComida;

            while(rs.next())
            {
                claseComida = new ClaseComida(
                                 rs.getInt("id_prod"),
                                 rs.getString("nombre_prod"),
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
    
    public ArrayList<ClaseCorte> ListaCorte(){
        ArrayList<ClaseCorte> corte = new ArrayList<>();
        
        Statement st;
        ResultSet rs;
         
        try{
            Connection con = hacerConexion();
            st = con.createStatement();
                              
            //BUSQUEDA DEFAULT (ALL)
            
            String searchQuery = "SELECT * FROM `cortes`";
            rs = st.executeQuery(searchQuery);

            ClaseCorte claseCorte;

            while(rs.next())
            {
                claseCorte = new ClaseCorte(
                                 rs.getInt("idCorte"),
                                 rs.getString("fecha"),
                                 rs.getDouble("corte")                                     
                                );
                corte.add(claseCorte);
            }
            
            con.close();
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        
        return corte;
    }
    
    public ArrayList<ClaseRealizados2> ListaRealizados(){
        ArrayList<ClaseRealizados2> realizados = new ArrayList<>();
        
        Statement st;
        ResultSet rs;
         
        try{
            Connection con = hacerConexion();
            st = con.createStatement();
                              
            //BUSQUEDA DEFAULT (ALL)
            
            String searchQuery = "SELECT nombre_prod nombre,ventas_totales.id_venta as'codigo de venta',"
                    + "ventas_totales.fecha_venta as'fecha de venta', cantidad, ventas_unitarias.precio_total precio_total FROM productos,"
                    + "ventas_unitarias,ventas_totales WHERE productos.id_prod=ventas_unitarias.id_producto AND "
                    + "ventas_totales.id_venta=ventas_unitarias.id_venta_tot ORDER BY `id_venta_tot`";
            
            
            rs = st.executeQuery(searchQuery);
           
            
            ClaseRealizados2 claseRealizados;

            while(rs.next())
            {
                claseRealizados = new ClaseRealizados2(
                        rs.getString("nombre"),
                        rs.getInt("codigo de venta"),
                        rs.getString("fecha de venta"),
                        rs.getDouble("cantidad"),
                        rs.getDouble("precio_total")
                );
                realizados.add(claseRealizados);
            }
            con.close();
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        
        return realizados;
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
                String searchQuery = "SELECT * FROM `productos` WHERE `id_prod` ='"+comidaSeleccionada[i]+"'";
                rs = st.executeQuery(searchQuery);
                while(rs.next())
                {
                    claseComida = new ClaseComida(
                                     rs.getInt("id_prod"),
                                 rs.getString("nombre_prod"),
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
    
        ///////////////////////////////////////     FUNCIONES   /////////////////////////////////////////////
    
    //Trae la tabla al tab1 productos
    public void traeraTabla()
    {
        ArrayList<ClaseComida> comidas;
        comidas = ListaComidas();
        DefaultTableModel model=(DefaultTableModel) tableComidas.getModel(); 
        
        model.setNumRows(0);            //resetear la tabla
        Object[] row = new Object[3];
        
        for(int i = 0; i < comidas.size(); i++)
        {
            row[0] = comidas.get(i).getId();
            row[1] = comidas.get(i).getNombre();
            row[2] = comidas.get(i).getCosto();
            
            model.addRow(row);
        }
       
       tableComidas.setModel(model);
       
    }
    
    //para traer a la tabla del carrito
    public void traeraTabla2(double cantidad)
    {
        ArrayList<ClaseComida> comidas;
        
        comidas = ListaComidasSeleccionadas();
        DefaultTableModel model=(DefaultTableModel) tableCarrito.getModel(); 
        
        
            
            
        Object[] row = new Object[4];

        for(int i = 0; i < comidas.size(); i++)
        {
            row[0] = comidas.get(i).getId();
            row[1] = comidas.get(i).getNombre();
            row[2] = comidas.get(i).getCosto();
            row[3] = cantidad;

            model.addRow(row);
        }

       tableCarrito.setModel(model);
       
       
    }
    
    public void traeraTabla3()
    {
        txtVendido3.setText(corteTotal+"");
        ArrayList<ClaseCorte> corte;
        corte = ListaCorte();
        DefaultTableModel model=(DefaultTableModel) tableCorte3.getModel(); 

        model.setNumRows(0);            //resetear la tabla
        
        Object[] row = new Object[3];
        
        for(int i = 0; i < corte.size(); i++)
        {
            row[0] = corte.get(i).getId();
            row[1] = corte.get(i).getFecha();
            row[2] = corte.get(i).getCorte();
               
            model.addRow(row);
        }
        
       tableCorte3.setModel(model);
       
    }
    
    public void traeraTabla4(){
        ArrayList<ClaseRealizados2> realizados;
        realizados = ListaRealizados();  
        DefaultTableModel model=(DefaultTableModel) tableRealizados4.getModel(); 

        model.setNumRows(0);            //resetear la tabla
        
        Object[] row = new Object[5];
        
        for(int i = 0; i < realizados.size(); i++)
        {
            row[0] = realizados.get(i).getNombre();
            row[1] = realizados.get(i).getCodVenta();
            row[2] = realizados.get(i).getFecha();
            row[3] = realizados.get(i).getCantidad();
            row[4] = realizados.get(i).getPrecioTotal();
   
            model.addRow(row);
        }
        
       tableRealizados4.setModel(model);
    }
    
    public void eliminarComida(){
        int[] row = new int[100];
        DefaultTableModel model=(DefaultTableModel) tableCarrito.getModel();
        
        if (!tableCarrito.isRowSelected(tableCarrito.getSelectedRow()))
           JOptionPane.showMessageDialog(null, "Seleccione comida a ELIMINAR!");
        
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
        //para el array vacio
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
    
    //para hacer la orden 
    public void sendshit1(){
        Ventas frameVentas = new Ventas();
        frameVentas.setVisible(true);
        //send tabla del carro
        frameVentas.tableVentas.setModel(tableCarrito.getModel());
        //send encargos
        frameVentas.list0 = this.list0;
        frameVentas.maxDato = this.maxDato;
        frameVentas.corteTotal = this.corteTotal;
        this.setVisible(false);
        
    }
    
    //para el btn agregar
    public void agregarcomidas2(){
        String nombre2, costo2;
        nombre2 = txtAgregarNombre2.getText();
        costo2 = txtAgregarCosto2.getText();
       
        try{
            
                
            Connection con = hacerConexion();
            Statement st = con.createStatement();
            st.executeUpdate("INSERT INTO `productos` (`id_prod`, `nombre_prod`, `costo`)"
                                    + "VALUES(NULL, '"+nombre2+"', '"+costo2+"')" );
            con.close();
            st.close();
            
            txtAgregarNombre2.setText("");
            txtAgregarCosto2.setText("");
            
            JOptionPane.showMessageDialog(null, "Comida "+nombre2+ " Creado con exito!");
        }catch(SQLException ex) {
             System.out.println(ex.getMessage());
             JOptionPane.showMessageDialog(null, "Verifique que los campos sean correctos");
            
        }         
    }
    
    //para mostrar el ultimo ID en el apartado agregar
    public void mostrarId2(){
        try {
            Statement st;
            ResultSet rs = null;
            Connection con = hacerConexion();
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM `productos` ORDER BY `id_prod` DESC LIMIT 1");
     
            while(rs.next()){        
                txtAgregarID2.setText (rs.getInt(1)+1+"");
            }
            
            con.close();
            rs.close();
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
    
    //para hacer que me mande el objeto en a modificar
    public ArrayList<ClaseComida> comidaBuscada2(){
        ArrayList<ClaseComida> comida = new ArrayList<>();
        Statement st;
        ResultSet rs;
         
        try{
            Connection con = hacerConexion();
            st = con.createStatement();                            
            ClaseComida claseComida;

            String searchQuery = "SELECT * FROM `productos` WHERE `id_prod` ='"+txtModificarID2.getText()+"'";
            rs = st.executeQuery(searchQuery);
            while(rs.next())
            {
                claseComida = new ClaseComida(
                             rs.getInt("id_prod"),
                             rs.getString("nombre_prod"),
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
    
    //para mostrar la comida en los txt
    public void buscarComida2(){
        txtModificarNombre2.setText("");
        txtModificarCosto2.setText("");
        ArrayList<ClaseComida> comidas ;
        comidas = comidaBuscada2();
       
        for(int i = 0; i < comidas.size(); i++)
        {
            txtModificarID2.setText(comidas.get(i).getId()+"");
            txtModificarNombre2.setText(comidas.get(i).getNombre()+"");
            txtModificarCosto2.setText(comidas.get(i).getCosto()+"");
        }
    }
    
    //para modificar
    public void modificar2(){
        String id2,nombre2, costo2;
        id2 = txtModificarID2.getText();
        nombre2 = txtModificarNombre2.getText();
        costo2 = txtModificarCosto2.getText();
        
        try{
            Connection con = hacerConexion();
            Statement st = con.createStatement();
            Statement statement = con.createStatement();
            statement.executeUpdate("UPDATE `productos` SET id_prod = '"+id2+"', nombre_prod = '"+nombre2+"', costo = '"+costo2+"'"
                                    +" WHERE `productos`.`id_prod` = "+id2+";" );
            con.close();
            st.close();
            
            txtModificarID2.setText("");
            txtModificarNombre2.setText("");
            txtModificarCosto2.setText("");
            JOptionPane.showMessageDialog(null, "Comida "+nombre2+" Modificado con Exito!");
        } catch (SQLException ex) {
             System.out.println(ex.getMessage());
             JOptionPane.showMessageDialog(null, "Ingrese una busqueda correcta!");
        }
    }
    
    //para eliminar
    public void eliminar2(){
        int eliminar;
        eliminar = JOptionPane.showConfirmDialog(null, "¿Realmente desea ELIMINAR!!?", "ELIMINAR!!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(eliminar == JOptionPane.YES_OPTION){
            try{
                Connection con = hacerConexion();
                Statement st = con.createStatement();
                Statement statement = con.createStatement();
                
                statement.executeUpdate("DELETE FROM `productos` WHERE `productos`.`id_prod` = "+txtModificarID2.getText()+";" );
                con.close();
                st.close();
                
                JOptionPane.showMessageDialog(null, "Eliminado con Exito!");
            } catch (SQLException ex) {
                 System.out.println(ex.getMessage());
                 JOptionPane.showMessageDialog(null, "Ingrese una busqueda correcta!");
            }
        }
        
    }
    
    //para realizar corte
    public void RealizarCorte(){
        Date fecha = new Date();
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        String fechaString = DATE_FORMAT.format(fecha);
        
        try{
            Connection con = hacerConexion();
            Statement st = con.createStatement();
            Statement statement = con.createStatement();
            statement.executeUpdate("INSERT INTO `cortes` (`idCorte`, `fecha`, `corte`)"
                                    + "VALUES(NULL, '"+fechaString+"', '"+corteTotal+"')" );
            con.close();
            st.close();
            
            corteTotal = 0;
            txtVendido3.setText(corteTotal+"");
            
            JOptionPane.showMessageDialog(null, "Corte registrado con Exito!");
        }catch(SQLException ex) {
             System.out.println(ex.getMessage());
            
        }
    }
    
    /*public boolean PruebaParaIdiotas(){
        boolean esEstupido = false;

        Pattern patNombre = Pattern.compile("(\\w|\\s){0,29}");
        Matcher matNombre = patNombre.matcher(txtAgregarNombre2.getText());
        if (!matNombre.matches()){
            JOptionPane.showMessageDialog(null,"Revise Nombre, maximo 30 caracteres alfanumericos");
            txtAgregarNombre2.setText("");
            esEstupido = true;
        }
        
        Pattern patCost = Pattern.compile("(\\d){0,5}|(\\d{0,8}.\\d\\d?)");
        Matcher matCost = patCost.matcher(txtAgregarCosto2.getText());
        if(!matCost.matches()){
            JOptionPane.showMessageDialog(null,"Revise Costo, formato (8,2)");
            txtAgregarCosto2.setText("");
            esEstupido = true;
        }
        
        
        
        return esEstupido;
    }*/
    
    
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
        btnAgregar0 = new javax.swing.JButton();
        btnEliminar0 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        lstComidas0 = new javax.swing.JList<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtComida0 = new javax.swing.JTextField();
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
        jSeparator2 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtAgregarID2 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtAgregarCosto2 = new javax.swing.JTextField();
        txtModificarCosto2 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtModificarID2 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtModificarNombre2 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        btnBuscar2 = new javax.swing.JButton();
        btnModificar2 = new javax.swing.JButton();
        btnEliminar2 = new javax.swing.JButton();
        btnAgregar2 = new javax.swing.JButton();
        txtAgregarNombre2 = new javax.swing.JTextField();
        tabCorte = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtVendido3 = new javax.swing.JTextField();
        btnCorte3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableCorte3 = new javax.swing.JTable();
        tabRealizadas = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tableRealizados4 = new javax.swing.JTable();

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
        setTitle("Cocina Economica - Doña Leti");
        setResizable(false);

        jTab.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        tabEncargos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tabEncargosFocusGained(evt);
            }
        });

        btnAgregar0.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAgregar0.setText("Agregar");
        btnAgregar0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregar0ActionPerformed(evt);
            }
        });

        btnEliminar0.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnEliminar0.setText("Eliminar de la Lista");
        btnEliminar0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminar0ActionPerformed(evt);
            }
        });

        jScrollPane5.setViewportView(lstComidas0);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel12.setText("Agregar un encargo");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel13.setText("Encargos Disponibles:");

        txtComida0.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtComida0KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout tabEncargosLayout = new javax.swing.GroupLayout(tabEncargos);
        tabEncargos.setLayout(tabEncargosLayout);
        tabEncargosLayout.setHorizontalGroup(
            tabEncargosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabEncargosLayout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addGroup(tabEncargosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabEncargosLayout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(tabEncargosLayout.createSequentialGroup()
                        .addGroup(tabEncargosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
                            .addComponent(txtComida0))
                        .addGap(18, 18, 18)
                        .addGroup(tabEncargosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEliminar0, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnAgregar0, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(93, 93, 93))
                    .addGroup(tabEncargosLayout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        tabEncargosLayout.setVerticalGroup(
            tabEncargosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabEncargosLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel12)
                .addGap(18, 18, 18)
                .addGroup(tabEncargosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregar0)
                    .addComponent(txtComida0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(tabEncargosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabEncargosLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE))
                    .addGroup(tabEncargosLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEliminar0)))
                .addGap(28, 28, 28))
        );

        jTab.addTab("Encargos", tabEncargos);

        tabVentas.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                tabVentasComponentShown(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Seleccione un Item");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Carrito");

        btnAgregar1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAgregar1.setText("Agregar al Carrito");
        btnAgregar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregar1ActionPerformed(evt);
            }
        });

        btnSiguiente.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
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
                java.lang.String.class, java.lang.String.class, java.lang.String.class
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
            tableComidas.getColumnModel().getColumn(0).setPreferredWidth(1);
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
                false, false, false, false
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
            tableCarrito.getColumnModel().getColumn(3).setPreferredWidth(1);
        }

        btnEliminar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
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
                                .addGap(242, 242, 242)
                                .addComponent(btnEliminar)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(tabVentasLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(25, 25, 25))
                    .addGroup(tabVentasLayout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                        .addGap(26, 26, 26)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21))
                    .addGroup(tabVentasLayout.createSequentialGroup()
                        .addGap(97, 97, 97)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(170, 170, 170))))
        );
        tabVentasLayout.setVerticalGroup(
            tabVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabVentasLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(tabVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(tabVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(tabVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregar1)
                    .addComponent(btnEliminar))
                .addGap(18, 18, 18)
                .addComponent(btnSiguiente)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabVentasLayout.createSequentialGroup()
                .addContainerGap(67, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(120, 120, 120))
        );

        jTab.addTab("Hacer una venta", tabVentas);

        tabActualizar.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                tabActualizarComponentShown(evt);
            }
        });

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/svn-commit.png"))); // NOI18N
        jLabel3.setText("Agregar");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pics/configure.png"))); // NOI18N
        jLabel4.setText("Eliminar/Modificar");

        jLabel5.setText("ID");

        txtAgregarID2.setEditable(false);
        txtAgregarID2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtAgregarID2.setEnabled(false);

        jLabel6.setText("Nombre");

        jLabel7.setText("Costo");

        txtAgregarCosto2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAgregarCosto2KeyTyped(evt);
            }
        });

        txtModificarCosto2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtModificarCosto2KeyTyped(evt);
            }
        });

        jLabel8.setText("ID");

        txtModificarID2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtModificarID2KeyTyped(evt);
            }
        });

        jLabel9.setText("Nombre");

        txtModificarNombre2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtModificarNombre2KeyTyped(evt);
            }
        });

        jLabel10.setText("Costo");

        btnBuscar2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnBuscar2.setText("Buscar");
        btnBuscar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscar2ActionPerformed(evt);
            }
        });

        btnModificar2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnModificar2.setText("Modificar");
        btnModificar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificar2ActionPerformed(evt);
            }
        });

        btnEliminar2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnEliminar2.setText("ELIMINAR");
        btnEliminar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminar2ActionPerformed(evt);
            }
        });

        btnAgregar2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAgregar2.setText("Agregar");
        btnAgregar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregar2ActionPerformed(evt);
            }
        });

        txtAgregarNombre2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAgregarNombre2KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout tabActualizarLayout = new javax.swing.GroupLayout(tabActualizar);
        tabActualizar.setLayout(tabActualizarLayout);
        tabActualizarLayout.setHorizontalGroup(
            tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabActualizarLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, tabActualizarLayout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addGap(102, 102, 102)
                            .addComponent(txtAgregarID2, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(tabActualizarLayout.createSequentialGroup()
                            .addGroup(tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel7)
                                .addComponent(jLabel6))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtAgregarCosto2, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                                .addComponent(txtAgregarNombre2))))
                    .addComponent(btnAgregar2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                .addGroup(tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(tabActualizarLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(102, 102, 102)
                        .addComponent(txtModificarID2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnBuscar2, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabActualizarLayout.createSequentialGroup()
                        .addComponent(btnModificar2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEliminar2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabActualizarLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtModificarCosto2, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tabActualizarLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtModificarNombre2, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(32, 32, 32))
            .addGroup(tabActualizarLayout.createSequentialGroup()
                .addGap(129, 129, 129)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(92, 92, 92))
            .addGroup(tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(tabActualizarLayout.createSequentialGroup()
                    .addGap(369, 369, 369)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(370, Short.MAX_VALUE)))
        );
        tabActualizarLayout.setVerticalGroup(
            tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabActualizarLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                .addGroup(tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabActualizarLayout.createSequentialGroup()
                        .addGroup(tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(tabActualizarLayout.createSequentialGroup()
                                .addComponent(txtAgregarID2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(21, 21, 21)
                                .addGroup(tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(txtAgregarNombre2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtAgregarCosto2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7)))
                            .addGroup(tabActualizarLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel5)))
                        .addGap(48, 48, 48)
                        .addComponent(btnAgregar2))
                    .addGroup(tabActualizarLayout.createSequentialGroup()
                        .addGroup(tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtModificarID2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnBuscar2))
                            .addComponent(jLabel8))
                        .addGap(19, 19, 19)
                        .addGroup(tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtModificarNombre2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtModificarCosto2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGap(48, 48, 48)
                        .addGroup(tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnModificar2)
                            .addComponent(btnEliminar2))))
                .addGap(40, 40, 40))
            .addGroup(tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(tabActualizarLayout.createSequentialGroup()
                    .addGap(45, 45, 45)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(37, Short.MAX_VALUE)))
        );

        jTab.addTab("Actualizar", tabActualizar);

        tabCorte.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                tabCorteComponentShown(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("Total vendido desde el ultimo corte:");

        txtVendido3.setEditable(false);
        txtVendido3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        btnCorte3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnCorte3.setText("Realizar Corte");
        btnCorte3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCorte3ActionPerformed(evt);
            }
        });

        tableCorte3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Fecha", "Corte Realizado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableCorte3);
        if (tableCorte3.getColumnModel().getColumnCount() > 0) {
            tableCorte3.getColumnModel().getColumn(0).setPreferredWidth(2);
        }

        javax.swing.GroupLayout tabCorteLayout = new javax.swing.GroupLayout(tabCorte);
        tabCorte.setLayout(tabCorteLayout);
        tabCorteLayout.setHorizontalGroup(
            tabCorteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabCorteLayout.createSequentialGroup()
                .addGap(198, 198, 198)
                .addComponent(jLabel11)
                .addGap(28, 28, 28)
                .addComponent(txtVendido3, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(179, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabCorteLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(tabCorteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnCorte3)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(121, 121, 121))
        );
        tabCorteLayout.setVerticalGroup(
            tabCorteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabCorteLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(tabCorteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtVendido3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addComponent(btnCorte3)
                .addGap(34, 34, 34))
        );

        jTab.addTab("Corte", tabCorte);

        tabRealizadas.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                tabRealizadasComponentShown(evt);
            }
        });

        tableRealizados4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Codigo Venta", "Fecha Venta", "Cantidad", "Precio Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableRealizados4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableRealizados4MouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tableRealizados4);
        if (tableRealizados4.getColumnModel().getColumnCount() > 0) {
            tableRealizados4.getColumnModel().getColumn(0).setPreferredWidth(1);
            tableRealizados4.getColumnModel().getColumn(2).setPreferredWidth(1);
            tableRealizados4.getColumnModel().getColumn(3).setPreferredWidth(1);
        }

        javax.swing.GroupLayout tabRealizadasLayout = new javax.swing.GroupLayout(tabRealizadas);
        tabRealizadas.setLayout(tabRealizadasLayout);
        tabRealizadasLayout.setHorizontalGroup(
            tabRealizadasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabRealizadasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 721, Short.MAX_VALUE)
                .addContainerGap())
        );
        tabRealizadasLayout.setVerticalGroup(
            tabRealizadasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabRealizadasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jTab.addTab("Ventas Realizadas", tabRealizadas);

        jTab.setSelectedIndex(1);

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
                .addComponent(jTab, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregar1ActionPerformed
        int columna = 0;
        int[] row = new int[100];

        row = tableComidas.getSelectedRows();
        
        //pruebas para idiotas
        if (!tableComidas.isRowSelected(tableComidas.getSelectedRow()))
             JOptionPane.showMessageDialog(null, "Seleccione comida!");
        else{
            for (int i=0; i<row.length; i++){
                comidaSeleccionada[i] =  (int) tableComidas.getValueAt(row[i], columna);
            }
            if (comidaSeleccionada != null ){
                double cantidad = 0;
                String input = JOptionPane.showInputDialog("Inserte cantidad:");
                
                try{
                    
                    if (input.matches("\\d.5|\\d+|\\d+.5||.5")){
                        cantidad = Double.parseDouble(input);
                        traeraTabla2(cantidad);
                    }
                    
                }catch(NumberFormatException n ){
                    
                    JOptionPane.showMessageDialog(null, "Porfavor instroduzca un digito valido");
                }catch(PatternSyntaxException n){
                   
                    JOptionPane.showMessageDialog(null, "Porfavor instroduzca un digito valido");
                }catch(NullPointerException n ){
                    if (cantidad != 0)
                    JOptionPane.showMessageDialog(null, "Porfavor instroduzca un digito valido");
                }
                
            }
            tableComidas.clearSelection();
        }
    
    }//GEN-LAST:event_btnAgregar1ActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        eliminarComida();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiguienteActionPerformed
        
        int columna = 0;
        int[] row = new int[100];
        tableCarrito.selectAll();
        row = tableCarrito.getSelectedRows();
        if (row.length != 0){        
            //for (int i=0; i<row.length; i++){
                    //comidaParaVenta[i] =  (int) tableCarrito.getValueAt(row[i], columna);
             sendshit1();
            //}
        }else{
            JOptionPane.showMessageDialog(null,"Carrito vacio!");  
        }
        
    }//GEN-LAST:event_btnSiguienteActionPerformed

    private void btnEliminar0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminar0ActionPerformed
        int preguntar = JOptionPane.showConfirmDialog(null, "Desea eliminar?");
        
        if (preguntar == 0){
            list0 = quitarlista(list0, lstComidas0.getSelectedIndex());
            lstComidas0.setListData(list0);
        }
    }//GEN-LAST:event_btnEliminar0ActionPerformed

    private void btnAgregar0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregar0ActionPerformed
        if(txtComida0.equals(null)){
            JOptionPane.showMessageDialog(null, "Espacio en blanco!");
        }else{
            list0 [maxDato] = txtComida0.getText();
            maxDato++;
            lstComidas0.setListData(list0);
            txtComida0.setText("");
        }

    }//GEN-LAST:event_btnAgregar0ActionPerformed

    private void tabEncargosFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tabEncargosFocusGained
        if (list0 != null)
            lstComidas0.setListData(list0);
    }//GEN-LAST:event_tabEncargosFocusGained

    private void tabActualizarComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_tabActualizarComponentShown
        // TODO add your handling code here
        mostrarId2();
    }//GEN-LAST:event_tabActualizarComponentShown

    private void btnAgregar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregar2ActionPerformed
        
        if (txtAgregarCosto2.getText().isEmpty() == false && txtAgregarNombre2.getText().isEmpty() == false &&
                txtAgregarNombre2.getText().matches("\\s+") == false){
            agregarcomidas2();
            mostrarId2();
        }else{
            JOptionPane.showMessageDialog(null, "Verifique campos vacios");
                    
        }
            
        
        
        
    }//GEN-LAST:event_btnAgregar2ActionPerformed

    private void btnModificar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificar2ActionPerformed
        if (txtModificarCosto2.getText().isEmpty() == false && txtModificarNombre2.getText().isEmpty() == false &&
                txtModificarNombre2.getText().matches("\\s+") == false){
            modificar2();
        }else{
            JOptionPane.showMessageDialog(null, "Verifique campos vacios");
                    
        }
        
    }//GEN-LAST:event_btnModificar2ActionPerformed

    private void btnBuscar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscar2ActionPerformed
        // TODO add your handling code here:
        buscarComida2();
    }//GEN-LAST:event_btnBuscar2ActionPerformed

    private void btnEliminar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminar2ActionPerformed
        // TODO add your handling code here:
        eliminar2();
    }//GEN-LAST:event_btnEliminar2ActionPerformed

    private void tabCorteComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_tabCorteComponentShown
        // TODO add your handling code here:
        traeraTabla3();
    }//GEN-LAST:event_tabCorteComponentShown

    private void tabVentasComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_tabVentasComponentShown
        // TODO add your handling code here:
         traeraTabla();
    }//GEN-LAST:event_tabVentasComponentShown

    private void btnCorte3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCorte3ActionPerformed
        // TODO add your handling code here:
        if (corteTotal == 0)
            JOptionPane.showMessageDialog(null, "No hay nada vendido aun!");
        else{
            RealizarCorte();
            traeraTabla3();
        }
        
    }//GEN-LAST:event_btnCorte3ActionPerformed

    private void tabRealizadasComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_tabRealizadasComponentShown
        // TODO add your handling code here:
        traeraTabla4();
    }//GEN-LAST:event_tabRealizadasComponentShown

    private void tableRealizados4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableRealizados4MouseClicked

    }//GEN-LAST:event_tableRealizados4MouseClicked

    private void txtAgregarCosto2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAgregarCosto2KeyTyped
        char test = evt.getKeyChar();
        
        if (!Character.isDigit(test)&&!(test =='.'))
            evt.consume();
        if (txtAgregarCosto2.getText().length()>7)
            evt.consume();
    }//GEN-LAST:event_txtAgregarCosto2KeyTyped

    private void txtAgregarNombre2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAgregarNombre2KeyTyped
        char test = evt.getKeyChar();
        
        if (txtAgregarNombre2.getText().length()>29)
            evt.consume();
    }//GEN-LAST:event_txtAgregarNombre2KeyTyped

    private void txtModificarNombre2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtModificarNombre2KeyTyped
        char test = evt.getKeyChar();
        
        if (txtAgregarNombre2.getText().length()>29)
            evt.consume();
    }//GEN-LAST:event_txtModificarNombre2KeyTyped

    private void txtModificarCosto2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtModificarCosto2KeyTyped
        char test = evt.getKeyChar();
        
        if (!Character.isDigit(test)&&!(test =='.'))
            evt.consume();
        if (txtAgregarCosto2.getText().length()>7)
            evt.consume();
    }//GEN-LAST:event_txtModificarCosto2KeyTyped

    private void txtModificarID2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtModificarID2KeyTyped
        char test = evt.getKeyChar();
        
        if (!Character.isDigit(test))
            evt.consume();
        if (txtAgregarCosto2.getText().length()>9)
            evt.consume();
    }//GEN-LAST:event_txtModificarID2KeyTyped

    private void txtComida0KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtComida0KeyTyped
        char test = evt.getKeyChar();
        
        if (txtComida0.getText().length()>49)
            evt.consume();
    }//GEN-LAST:event_txtComida0KeyTyped
    
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
    private javax.swing.JButton btnAgregar2;
    private javax.swing.JButton btnBuscar2;
    private javax.swing.JButton btnCorte3;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnEliminar0;
    private javax.swing.JButton btnEliminar2;
    private javax.swing.JButton btnModificar2;
    private javax.swing.JButton btnSiguiente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    public javax.swing.JTabbedPane jTab;
    private javax.swing.JTable jTable1;
    public javax.swing.JList<String> lstComidas0;
    private javax.swing.JPanel tabActualizar;
    private javax.swing.JPanel tabCorte;
    private javax.swing.JPanel tabEncargos;
    private javax.swing.JPanel tabRealizadas;
    public javax.swing.JPanel tabVentas;
    public javax.swing.JTable tableCarrito;
    private javax.swing.JTable tableComidas;
    private javax.swing.JTable tableCorte3;
    private javax.swing.JTable tableRealizados4;
    private javax.swing.JTextField txtAgregarCosto2;
    private javax.swing.JTextField txtAgregarID2;
    private javax.swing.JTextField txtAgregarNombre2;
    private javax.swing.JTextField txtComida0;
    private javax.swing.JTextField txtModificarCosto2;
    private javax.swing.JTextField txtModificarID2;
    private javax.swing.JTextField txtModificarNombre2;
    private javax.swing.JTextField txtVendido3;
    // End of variables declaration//GEN-END:variables
}
