package mundonodo.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource; 

import mundonodo.model.dto.Pedido;
import mundonodo.model.dto.Usuario;
import mundonodo.daofactory.DAOFactory; 
import mundonodo.dao.PedidoDAO;         

/**
 * Servlet controlador encargado de gestionar la visualización del historial de compras.
 * Recupera todos los pedidos realizados por el usuario actualmente autenticado, 
 * permitiendo una navegación hacia los detalles específicos de cada transacción.
 * * @author Jose Antonio
 * @version 1.0
 */
@WebServlet(name = "HistorialPedidos", urlPatterns = {"/HistorialPedidos"})
public class HistorialPedidos extends HttpServlet {

    /**
     * Procesa la petición GET para recuperar el historial de pedidos.
     * Realiza una validación de sesión para asegurar que solo usuarios autenticados
     * accedan a sus datos, consulta el DAO de pedidos y despacha el resultado a la vista.
     * * @param request  Objeto {@link HttpServletRequest} que contiene la sesión del usuario.
     * @param response Objeto {@link HttpServletResponse} para gestionar redirecciones de seguridad.
     * @throws ServletException Si ocurre un error en el contenedor de servlets.
     * @throws IOException      Si ocurre un error en la comunicación de red.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Recuperación de la sesión para identificar al usuario logueado
        HttpSession session = request.getSession();
        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        
        // 2. Control de Acceso: Verificamos si el usuario está identificado
        if (u != null) {
            try {
                // 3. Obtención del Pool de conexiones  del contexto de la aplicación
                DataSource ds = (DataSource) getServletContext().getAttribute("db_pool");

                // 4. Implementación del patrón Factory para obtener el DAO de Pedidos
                DAOFactory factoria = DAOFactory.getDAOFactory();
                PedidoDAO pedidoDAO = factoria.getPedidoDao();
                
                // 5. Consulta de persistencia: Recuperar la lista de pedidos del usuario específico
                List<Pedido> lista = pedidoDAO.listarPedidosPorUsuario(ds, u.getIdusuario());
                
                // 6. Preparación de datos para la capa de presentación (JSP)
                request.setAttribute("listaPedidos", lista);
                
                // 7. Redirección interna hacia la vista del historial
                request.getRequestDispatcher("/JSP/historial.jsp").forward(request, response);
                
            } catch (Exception e) {
                // Gestión de excepciones: Log de error y redirección defensiva al inicio
                System.err.println(">>> Error al cargar historial: " + e.getMessage());
                response.sendRedirect(request.getContextPath() + "/Inicio");
            }
        } else {
            // Seguridad: Si no hay sesión activa, redirigir al formulario de autenticación
            response.sendRedirect(request.getContextPath() + "/JSP/login.jsp");
        }
    }

    /**
     * Redirige las peticiones POST al método doGet para mantener una lógica de consulta única.
     * * @param request  Objeto {@link HttpServletRequest}.
     * @param response Objeto {@link HttpServletResponse}.
     * @throws ServletException Si ocurre un error.
     * @throws IOException      Si ocurre un error.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}