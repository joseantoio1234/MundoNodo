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

@WebServlet(name = "HistorialPedidos", urlPatterns = {"/HistorialPedidos"})
public class HistorialPedidos extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        
        // 1. Verificamos si el usuario está identificado
        if (u != null) {
            try {
                // 2. OBTENER EL POOL DEL CONTEXTO 
                DataSource ds = (DataSource) getServletContext().getAttribute("db_pool");

                // 3. OBTENER EL DAO A TRAVÉS DE LA FACTORÍA
                DAOFactory factoria = DAOFactory.getDAOFactory();
                PedidoDAO pedidoDAO = factoria.getPedidoDao();
                
                // 4. Llamamos al método pasando el DataSource como primer parámetro
                List<Pedido> lista = pedidoDAO.listarPedidosPorUsuario(ds, u.getIdusuario());
                
                // 5. Pasamos la lista al JSP mediante un atributo de request
                request.setAttribute("listaPedidos", lista);
                
                // 6. Redirigimos a la vista del historial
                request.getRequestDispatcher("/JSP/historial.jsp").forward(request, response);
                
            } catch (Exception e) {
                System.err.println("Error al cargar historial: " + e.getMessage());
                response.sendRedirect(request.getContextPath() + "/Inicio");
            }
        } else {
            // Si no está logueado, lo mandamos al login
            response.sendRedirect(request.getContextPath() + "/JSP/login.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}