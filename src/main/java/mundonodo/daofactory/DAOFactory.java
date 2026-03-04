package mundonodo.daofactory;

import mundonodo.dao.PedidoDAO;
import mundonodo.dao.ProductoDao;
import mundonodo.dao.UsuarioDao;

/**
 * Clase abstracta que implementa el patrón de diseño Abstract Factory para el acceso a datos.
 * Define la interfaz para crear objetos DAO (Data Access Object) y proporciona un método
 * estático para obtener la implementación específica para el motor de base de datos utilizado.
 * * @author Jose Antonio
 * @version 1.0
 */
public abstract class DAOFactory {

    /**
     * Define el contrato para obtener el DAO encargado de la gestión de usuarios.
     * @return Una instancia de {@link UsuarioDao}.
     */
    public abstract UsuarioDao getUsuarioDao();

    /**
     * Define el contrato para obtener el DAO encargado de la gestión del catálogo de productos.
     * @return Una instancia de {@link ProductoDao}.
     */
    public abstract ProductoDao getProductoDao();

    /**
     * Define el contrato para obtener el DAO encargado de la gestión de pedidos y ventas.
     * @return Una instancia de {@link PedidoDAO}.
     */
    public abstract PedidoDAO getPedidoDao();

    /**
     * Método de factoría estático que devuelve la implementación concreta de la DAOFactory.
     * Actualmente, el sistema está configurado para retornar {@link MySQLDAOFactory}.
     * * @return Una instancia de la factoría específica para MySQL.
     */
    public static DAOFactory getDAOFactory() {
        return new MySQLDAOFactory();
    }
}