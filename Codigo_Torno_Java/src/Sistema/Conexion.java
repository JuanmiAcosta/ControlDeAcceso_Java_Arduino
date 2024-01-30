package Sistema;

import java.sql.*;
import javax.swing.JOptionPane;

public class Conexion {
    
    //Constructor por defecto
    private Conexion(){}
    
    //Creamos variable para guardar el estado de la conexion con BD
    private static Connection conexion;
    
    //Una variable para crear una sola instancia (Singleotn)
    private static Conexion instancia;
    
    //Variables para conectarnos a la BD
    private static final String URL="jdbc:mysql://localhost/prueba_torno"; 
    private static final String USERNAME="root"; 
    private static final String PASSWORD=""; 
    
    public Connection probar_conectar(){ // Método de conexión a la BD
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL,USERNAME,PASSWORD    );
             JOptionPane.showMessageDialog(null, "Conexion exitosa :)");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error en la conexión a la BD : " + e);
        }
        return conexion;
    }
    
    public Connection conectar(){ // Método de conexión a la BD
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL,USERNAME,PASSWORD    );
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error al conectar con la BD  : " + e);
        }
        return conexion;
    }
    
    public void cierreConexion() throws SQLException{ // Método para cerrar la conexión
        
        try{
            conexion.close();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error al cerrar la conexión con la BD  : " + e);
            conexion.close();
        }finally{
            conexion.close(); // En caso de fallo catastrófico
        }
        
    }
    
    //Patron Singleton
    public static Conexion getInstance(){
        if (instancia == null){
            instancia = new Conexion();
        }
        return instancia;
    }
}
