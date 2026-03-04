package mundonodo.daofactory;

import mundonodo.dao.PedidoDAO;
import mundonodo.dao.ProductoDao;
import mundonodo.dao.UsuarioDao;

public abstract class DAOFactory {

    public abstract UsuarioDao getUsuarioDao();
    public abstract ProductoDao getProductoDao();
    public abstract PedidoDAO getPedidoDao();

    public static DAOFactory getDAOFactory() {
        return new MySQLDAOFactory();
    }
}