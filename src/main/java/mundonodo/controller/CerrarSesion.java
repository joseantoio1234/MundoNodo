package mundonodo.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "CerrarSesion", urlPatterns = {"/CerrarSesion"})
public class CerrarSesion extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            session.removeAttribute("usuarioLogueado");
            session.removeAttribute("carrito");
            
            session.invalidate();
        }

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0); 

        String urlRedireccion = request.getContextPath() + "/Inicio";
        response.sendRedirect(response.encodeRedirectURL(urlRedireccion));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}