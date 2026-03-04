package mundonodo.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource; 
import mundonodo.daofactory.DAOFactory; 
import mundonodo.dao.PedidoDAO;         
import mundonodo.model.dto.ItemCarrito;

@WebServlet(name = "DetallePedido", urlPatterns = {"/DetallePedido"})
public class DetallePedido extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        
        if (idParam != null) {
            try {
                int idPedido = Integer.parseInt(idParam);

                // 1. Obtener el DataSource desde el contexto 
                DataSource ds = (DataSource) getServletContext().getAttribute("db_pool");

                // 2. Obtener la factoría y el DAO
                DAOFactory factoria = DAOFactory.getDAOFactory();
               PedidoDAO pedidoDAO = factoria.getPedidoDao();

                // 3. Llamar al método pasando el DataSource  como primer parámetro
                List<ItemCarrito> detalles = pedidoDAO.listarDetallesPorPedido(ds, idPedido);
                
                request.setAttribute("detalles", detalles);
                request.setAttribute("idPedido", idPedido);
                request.getRequestDispatcher("/JSP/detallepedido.jsp").forward(request, response);
                
            } catch (NumberFormatException e) {
                System.err.println("ID de pedido no válido: " + idParam);
                response.sendRedirect("HistorialPedidos");
            }
        } else {
            response.sendRedirect("HistorialPedidos");
        }
    }
}