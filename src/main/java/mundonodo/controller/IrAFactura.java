package mundonodo.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mundonodo.model.dto.ItemCarrito;
import mundonodo.model.dto.Usuario;

@WebServlet(name = "IrAFactura", urlPatterns = {"/IrAFactura"})
public class IrAFactura extends HttpServlet {

@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    
    HttpSession session = request.getSession();
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
    List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");

    if (usuario == null || carrito == null || carrito.isEmpty()) {
        response.sendRedirect(request.getContextPath() + "/JSP/carrito.jsp");
        return;
    }

    // --- LÓGICA DE COMPRA ---
    double total = 0;
    for (ItemCarrito item : carrito) {
        total += item.getProducto().getPrecio() * item.getCantidad();
    }

    // --- CÁLCULOS PARA LA VISTA ---
    double baseImponible = total / 1.21;
    double ivaCalculado = total - baseImponible;

    request.setAttribute("totalFactura", total);
    request.setAttribute("baseImponible", baseImponible);
    request.setAttribute("ivaCalculado", ivaCalculado);
 

    request.getRequestDispatcher("/JSP/factura.jsp").forward(request, response);
}
}