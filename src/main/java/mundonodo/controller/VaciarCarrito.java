package mundonodo.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Importamos tu utilidad de Cookies
import mundonodo.util.Cookies;

@WebServlet(name = "VaciarCarrito", urlPatterns = {"/VaciarCarrito"})
public class VaciarCarrito extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Obtener la sesión actual
        HttpSession session = request.getSession();
        
        // 2. Eliminar el carrito de la sesión (Memoria del servidor)
        session.removeAttribute("carrito");
        
        // --- 3. LÓGICA DE PERSISTENCIA (COOKIES) ---
        // Borramos la cookie física del navegador estableciendo su tiempo de vida a 0
        Cookies.borrarCookieCesta(response);
        
        System.out.println(">>> CARRITO: Cesta vaciada en sesión y cookie eliminada.");

        // 4. Redirigir de vuelta a la vista del carrito (ahora aparecerá vacía)
        response.sendRedirect(request.getContextPath() + "/JSP/carrito.jsp");
    }
}