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

/**
 * Servlet controlador encargado de mostrar el desglose de un pedido específico.
 * Recupera la información detallada de los productos asociados a una transacción
 * histórica para que el usuario pueda consultar su ticket o factura de compra.
 * * @author Jose Antonio
 * @version 1.0
 */
@WebServlet(name = "DetallePedido", urlPatterns = {"/DetallePedido"})
public class DetallePedido extends HttpServlet {

    /**
     * Procesa la petición GET para consultar los detalles de un pedido.
     * Valida el identificador del pedido, consulta al DAO para obtener la lista
     * de productos asociados y despacha la información a la vista detallada.
     * * @param request  Objeto {@link HttpServletRequest} que contiene el parámetro 'id' del pedido.
     * @param response Objeto {@link HttpServletResponse} para redirecciones en caso de error.
     * @throws ServletException Si ocurre un error interno en el contenedor.
     * @throws IOException      Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Obtención del identificador del pedido desde los parámetros de la URL
        String idParam = request.getParameter("id");
        
        if (idParam != null) {
            try {
                int idPedido = Integer.parseInt(idParam);

                // 1. Recuperación del Pool de conexiones desde el contexto global de la aplicación
                DataSource ds = (DataSource) getServletContext().getAttribute("db_pool");

                // 2. Obtención de la factoría y el DAO de pedidos siguiendo el patrón Abstract Factory
                DAOFactory factoria = DAOFactory.getDAOFactory();
                PedidoDAO pedidoDAO = factoria.getPedidoDao();

                // 3. Consulta de los items asociados al pedido. 
                // Se reutiliza la clase ItemCarrito para representar cada línea del detalle.
                List<ItemCarrito> detalles = pedidoDAO.listarDetallesPorPedido(ds, idPedido);
                
                // 4. Preparación de los datos para la capa de presentación (JSP)
                request.setAttribute("detalles", detalles);
                request.setAttribute("idPedido", idPedido);
                
                // 5. Redirección interna hacia la página de detalle
                request.getRequestDispatcher("/JSP/detallepedido.jsp").forward(request, response);
                
            } catch (NumberFormatException e) {
                // Gestión de error: Si el ID no es un número, se registra y se devuelve al historial
                System.err.println(">>> Error: ID de pedido no válido: " + idParam);
                response.sendRedirect("HistorialPedidos");
            }
        } else {
            // Si se intenta acceder sin parámetro ID, se redirige por seguridad
            response.sendRedirect("HistorialPedidos");
        }
    }
}