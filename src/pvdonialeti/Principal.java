package pvdonialeti;

import java.awt.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    public String[] list4 = new String[100];
    public double corteTotal = 0;
    
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
    
    public ArrayList<ClaseCorte> ListaCorte()
    {
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
    
    public ArrayList<ClaseRealizados> ListaRealizados()
    {
        ArrayList<ClaseRealizados> realizados = new ArrayList<>();
        
        Statement st;
        ResultSet rs;
         
        try{
            Connection con = hacerConexion();
            st = con.createStatement();
                              
            //BUSQUEDA DEFAULT (ALL)
            
            String searchQuery = "SELECT * FROM `ventas`";
            rs = st.executeQuery(searchQuery);

            ClaseRealizados claseRealizados;

            while(rs.next())
            {
                claseRealizados = new ClaseRealizados(
                                 rs.getInt("idVenta"),
                                 rs.getString("comidas"),
                                 rs.getDouble("total"),  
                                 rs.getString("fecha")
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
        DefaultTableModel model=(DefaultTableModel) tableComidas.getModel(); 
        //DefaultTableModel model = new DefaultTableModel();
        model.setNumRows(0);            //resetear la tabla
        //model.setColumnIdentifiers(new Object[]{"ID","Nombre","Costo"});
        
      
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
        int cantidad = 0;
        boolean esNumero = false;
        ArrayList<ClaseComida> comidas;
        comidas = ListaComidasSeleccionadas();
      
        DefaultTableModel model=(DefaultTableModel) tableCarrito.getModel(); 

        //model.setColumnIdentifiers(new Object[]{"ID","Nombre","Costo","Cantidad"});
        
        do{
            try{
                cantidad = Integer.valueOf(JOptionPane.showInputDialog(null, "Inserte cantidad:"));
                
                esNumero = true;
            }catch(NumberFormatException n){
                esNumero = false;
            }
        }while(esNumero == false);
       
        
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

        //model.setColumnIdentifiers(new Object[]{"ID","Fecha","Corte Realizado"});
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
        ArrayList<ClaseRealizados> realizados;
        realizados = ListaRealizados();
      
        DefaultTableModel model=(DefaultTableModel) tableRealizados4.getModel(); 

        //model.setColumnIdentifiers(new Object[]{"ID","Fecha","Corte Realizado"});
        model.setNumRows(0);            //resetear la tabla
        
       
        
        Object[] row = new Object[4];
        
        for(int i = 0; i < realizados.size(); i++)
        {
            row[0] = realizados.get(i).getId();
            row[1] = realizados.get(i).getComidas();
            row[2] = realizados.get(i).getTotal();
            row[3] = realizados.get(i).getFecha();

            
            
            model.addRow(row);
        }
        
       tableRealizados4.setModel(model);
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
    
    //para hacer la orde
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
            Statement statement = con.createStatement();
            statement.executeUpdate("INSERT INTO `comidas` (`id`, `nombre`, `costo`)"
                                    + "VALUES(NULL, '"+nombre2+"', '"+costo2+"')" );
            con.close();
            st.close();
            
            txtAgregarNombre2.setText("");
            txtAgregarCosto2.setText("");
            
            JOptionPane.showMessageDialog(null, "Comida "+nombre2+ " Creado con exito!");
        }catch(SQLException ex) {
             System.out.println(ex.getMessage());
            
        }
                
    }
    
    //para mostrar el ultimo ID en el apartado agregar
    public void mostrarId2(){
        try {
            Statement st;
            ResultSet rs = null;
            Connection con = hacerConexion();
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM `comidas` ORDER BY `id` DESC LIMIT 1");
     
            
            while(rs.next())
            {        
                
                txtAgregarID2.setText (rs.getInt(1)+1+"");
            }
            
            //txtIDCliente.setText(p.getllave()+""); 
            /*
             // or p.id=rs.getInt("userid"); by name of column
            ClaseClientes p = new ClaseClientes();
            txtIDCliente.setText(p.getllave()+""); 
            */
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

            
                String searchQuery = "SELECT * FROM `comidas` WHERE `id` ='"+txtModificarID2.getText()+"'";
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
            
            con.close();
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
       
        
        return comida;
    }
    
    //para mostrar la comida en los txt
    public void buscarComida2(){
        ArrayList<ClaseComida> comidas ;
        comidas = comidaBuscada2();
        //DefaultTableModel model = new DefaultTableModel();
       
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
        //HACER QUE MANDE NULL CUANDO ESTE EN BLACO, PROB CON IF
        try{
            Connection con = hacerConexion();
            Statement st = con.createStatement();
            Statement statement = con.createStatement();
            statement.executeUpdate("UPDATE `comidas` SET id = '"+id2+"', nombre = '"+nombre2+"', costo = '"+costo2+"'"
                                    +" WHERE `comidas`.`id` = "+id2+";" );
            con.close();
            st.close();
            
            txtModificarID2.setText("");
            txtModificarNombre2.setText("");
            txtModificarCosto2.setText("");
            JOptionPane.showMessageDialog(null, "Comida "+nombre2+" Modificado con Exito!");
        } catch (SQLException ex) {
             System.out.println(ex.getMessage());
        }
    }
    
    //para eliminar
    public void eliminar2(){
        int eliminar;
        eliminar = JOptionPane.showConfirmDialog(null, "¿Realmente desea ELIMINAR!!?", "ELIMINAR!!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(eliminar == JOptionPane.YES_OPTION){
            

            //HACER QUE MANDE NULL CUANDO ESTE EN BLACO, PROB CON IF
            try{
                
                
                Connection con = hacerConexion();
                Statement st = con.createStatement();
                Statement statement = con.createStatement();
                statement.executeUpdate("DELETE FROM `comidas` WHERE `comidas`.`id` = "+txtModificarID2.getText()+";" );
                con.close();
                st.close();
                
                JOptionPane.showMessageDialog(null, "Eliminado con Exito!");
            } catch (SQLException ex) {
                 System.out.println(ex.getMessage());
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
        jSeparator2 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtAgregarID2 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtAgregarNombre2 = new javax.swing.JTextField();
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
        tabCorte = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtVendido3 = new javax.swing.JTextField();
        btnCorte3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableCorte3 = new javax.swing.JTable();
        tabRealizadas = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tableRealizados4 = new javax.swing.JTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        lstComidas4 = new javax.swing.JList<>();

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
        setResizable(false);

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
                .addGroup(tabEncargosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtComida0, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
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
                .addContainerGap(84, Short.MAX_VALUE))
        );

        jTab.addTab("Encargos", tabEncargos);

        tabVentas.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                tabVentasComponentShown(evt);
            }
        });

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
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                        .addGap(26, 26, 26)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21))
                    .addGroup(tabVentasLayout.createSequentialGroup()
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

        jLabel3.setText("Agregar");

        jLabel4.setText("Eliminar/Modificar");

        jLabel5.setText("ID");

        txtAgregarID2.setEditable(false);
        txtAgregarID2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtAgregarID2.setEnabled(false);

        jLabel6.setText("Nombre");

        jLabel7.setText("Costo");

        jLabel8.setText("ID");

        jLabel9.setText("Nombre");

        jLabel10.setText("Costo");

        btnBuscar2.setText("Buscar");
        btnBuscar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscar2ActionPerformed(evt);
            }
        });

        btnModificar2.setText("Modificar");
        btnModificar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificar2ActionPerformed(evt);
            }
        });

        btnEliminar2.setText("ELIMINAR");
        btnEliminar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminar2ActionPerformed(evt);
            }
        });

        btnAgregar2.setText("Agregar");
        btnAgregar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregar2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tabActualizarLayout = new javax.swing.GroupLayout(tabActualizar);
        tabActualizar.setLayout(tabActualizarLayout);
        tabActualizarLayout.setHorizontalGroup(
            tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabActualizarLayout.createSequentialGroup()
                .addGap(125, 125, 125)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(139, 139, 139))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabActualizarLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(tabActualizarLayout.createSequentialGroup()
                            .addComponent(jLabel7)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtAgregarCosto2, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, tabActualizarLayout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtAgregarNombre2, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, tabActualizarLayout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addGap(102, 102, 102)
                            .addComponent(txtAgregarID2, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnAgregar2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
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
                .addGap(48, 48, 48))
            .addGroup(tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(tabActualizarLayout.createSequentialGroup()
                    .addGap(369, 369, 369)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(370, Short.MAX_VALUE)))
        );
        tabActualizarLayout.setVerticalGroup(
            tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabActualizarLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(tabActualizarLayout.createSequentialGroup()
                        .addGap(83, 83, 83)
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
                        .addGroup(tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGroup(tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tabActualizarLayout.createSequentialGroup()
                                .addGap(75, 75, 75)
                                .addComponent(jLabel5))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabActualizarLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                                    .addComponent(jLabel10))))))
                .addGap(48, 48, 48)
                .addGroup(tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnModificar2)
                    .addComponent(btnEliminar2)
                    .addComponent(btnAgregar2))
                .addContainerGap(75, Short.MAX_VALUE))
            .addGroup(tabActualizarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(tabActualizarLayout.createSequentialGroup()
                    .addGap(128, 128, 128)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(125, Short.MAX_VALUE)))
        );

        jTab.addTab("Actualizar", tabActualizar);

        tabCorte.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                tabCorteComponentShown(evt);
            }
        });

        jLabel11.setText("Total vendido desde el ultimo corte:");

        txtVendido3.setEditable(false);

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabCorteLayout.createSequentialGroup()
                .addGroup(tabCorteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(tabCorteLayout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtVendido3, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tabCorteLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCorte3)))
                .addGap(143, 143, 143))
            .addGroup(tabCorteLayout.createSequentialGroup()
                .addGap(134, 134, 134)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(155, Short.MAX_VALUE))
        );
        tabCorteLayout.setVerticalGroup(
            tabCorteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabCorteLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(tabCorteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtVendido3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                "ID Venta", "Comidas", "Total", "Fecha"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false
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

        jScrollPane7.setViewportView(lstComidas4);

        javax.swing.GroupLayout tabRealizadasLayout = new javax.swing.GroupLayout(tabRealizadas);
        tabRealizadas.setLayout(tabRealizadasLayout);
        tabRealizadasLayout.setHorizontalGroup(
            tabRealizadasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabRealizadasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        tabRealizadasLayout.setVerticalGroup(
            tabRealizadasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabRealizadasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabRealizadasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
                    .addComponent(jScrollPane7))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jTab.addTab("Ventas Realizadas", tabRealizadas);

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
        JOptionPane.showMessageDialog(null, "Seleccione comida!");
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

        sendshit1();
    }//GEN-LAST:event_btnSiguienteActionPerformed

    private void jTabFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTabFocusGained
        
    }//GEN-LAST:event_jTabFocusGained

    private void txtComida0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtComida0ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtComida0ActionPerformed

    private void btnEliminar0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminar0ActionPerformed
        // TODO add your handling code here:
        //lstComidas0.getSelectedIndex();
        int preguntar = JOptionPane.showConfirmDialog(null, "Desea eliminar?");
        if (preguntar == 0){
            list0 = quitarlista(list0, lstComidas0.getSelectedIndex());
            lstComidas0.setListData(list0);
        }
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

    private void tabActualizarComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_tabActualizarComponentShown
        // TODO add your handling code here
        mostrarId2();
    }//GEN-LAST:event_tabActualizarComponentShown

    private void btnAgregar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregar2ActionPerformed
        // TODO add your handling code here:
        agregarcomidas2();
        mostrarId2();
    }//GEN-LAST:event_btnAgregar2ActionPerformed

    private void btnModificar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificar2ActionPerformed
        // TODO add your handling code here:
        modificar2();
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
        RealizarCorte();
        traeraTabla3();
    }//GEN-LAST:event_btnCorte3ActionPerformed

    private void tabRealizadasComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_tabRealizadasComponentShown
        // TODO add your handling code here:
        traeraTabla4();
    }//GEN-LAST:event_tabRealizadasComponentShown

    private void tableRealizados4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableRealizados4MouseClicked
        // TODO add your handling code here:
        //tableRealiados4.getValueAt(1, tableRealizados4.getSelectedRow());
        String textoComidas;
        textoComidas = (String)tableRealizados4.getValueAt(tableRealizados4.getSelectedRow(), 1);
        
        list4 = textoComidas.split("\\-");
        lstComidas4.setListData(list4);
            
    }//GEN-LAST:event_tableRealizados4MouseClicked
    
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
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    public javax.swing.JTabbedPane jTab;
    private javax.swing.JTable jTable1;
    public javax.swing.JList<String> lstComidas0;
    private javax.swing.JList<String> lstComidas4;
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
