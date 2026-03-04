package mundonodo.model.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * Clase de utilidad para la gestión de conexiones a la base de datos MySQL.
 * Proporciona un mecanismo centralizado para obtener conexiones activas
 * desde el Pool de conexiones (DataSource) configurado en el servidor mediante JNDI.
 * * @author Jose Antonio
 * @version 1.0
 */
public class Conexion {
    
    /**
     * Obtiene una conexión activa del DataSource.
     * El DataSource debe ser suministrado por el contexto de la aplicación, 
     * generalmente inicializado en un Listener o Servlet.
     * * @param ds El objeto {@link DataSource} configurado en el archivo context.xml del servidor.
     * @return {@link Connection} Una conexión abierta y lista para ejecutar sentencias SQL.
     * @throws SQLException Si el DataSource es nulo o si ocurre un error al intentar 
     * establecer la comunicación con la base de datos.
     */
    public static Connection getConexion(DataSource ds) throws SQLException {
        if (ds == null) {
            // Este error suele indicar que el recurso JNDI 'jdbc/mundo_nodo' no fue encontrado
            throw new SQLException("Error: El DataSource (Pool) es nulo. Revisa el context.xml");
        }
        
        // El DataSource nos entrega una conexión gestionada desde el estanque (Pool)
        Connection cn = ds.getConnection();
        
        if (cn != null) {
            System.out.println(">>> Conexión obtenida correctamente del POOL JNDI");
        }
        
        return cn;
    }
}