package mundonodo.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "VaciarCarrito", urlPatterns = {"/VaciarCarrito"})
public class VaciarCarrito extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        

        HttpSession session = request.getSession();
        

        session.removeAttribute("carrito");
        

        response.sendRedirect(request.getContextPath() + "/JSP/carrito.jsp");
    }
}