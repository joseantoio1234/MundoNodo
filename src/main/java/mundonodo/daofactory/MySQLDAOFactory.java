package mundonodo.daofactory;

import mundonodo.dao.PedidoDAO; 
import mundonodo.dao.ProductoDao;
import mundonodo.dao.UsuarioDao;

public class MySQLDAOFactory extends DAOFactory {

    @Override
    public UsuarioDao getUsuarioDao() {
        return new UsuarioDao();
    }

    @Override
    public ProductoDao getProductoDao() {
        return new ProductoDao();
    }

    @Override
    public PedidoDAO getPedidoDao() { 
        return new PedidoDAO();
    }
}