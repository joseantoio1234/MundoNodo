package mundonodo.daofactory;

import mundonodo.dao.PedidoDAO; 
import mundonodo.dao.ProductoDao;
import mundonodo.dao.UsuarioDao;

/**
 * Implementación concreta de la factoría de DAOs para el motor de base de datos MySQL.
 * Esta clase extiende {@link DAOFactory} y se encarga de instanciar las versiones
 * específicas de los objetos de acceso a datos que utilizan JDBC y SQL.
 * * @author Jose Antonio
 * @version 1.0
 */
public class MySQLDAOFactory extends DAOFactory {

    /**
     * Proporciona una instancia de UsuarioDao para operaciones con la tabla de usuarios.
     * @return Una nueva instancia de {@link UsuarioDao}.
     */
    @Override
    public UsuarioDao getUsuarioDao() {
        return new UsuarioDao();
    }

    /**
     * Proporciona una instancia de ProductoDao para operaciones con el catálogo de productos.
     * @return Una nueva instancia de {@link ProductoDao}.
     */
    @Override
    public ProductoDao getProductoDao() {
        return new ProductoDao();
    }

    /**
     * Proporciona una instancia de PedidoDAO para gestionar compras y líneas de pedido.
     * @return Una nueva instancia de {@link PedidoDAO}.
     */
    @Override
    public PedidoDAO getPedidoDao() { 
        return new PedidoDAO();
    }
}