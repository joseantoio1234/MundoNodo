package mundonodo.model.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

public class Conexion {
    
    public static Connection getConexion(DataSource ds) throws SQLException {
        if (ds == null) {
            // Este error saldrá si el Listener no encontró el recurso jdbc/mundo_nodo
            throw new SQLException("Error: El DataSource (Pool) es nulo. Revisa el context.xml");
        }
        
        // El DataSource nos da una conexión del "estanque" (Pool)
        Connection cn = ds.getConnection();
        
        if (cn != null) {
            // Log para confirmar en la consola del instituto que el pool funciona
            System.out.println(">>> Conexión obtenida correctamente del POOL JNDI");
        }
        
        return cn;
    }
}