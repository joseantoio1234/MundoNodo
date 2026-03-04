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

@WebServlet(name = "Carrito", urlPatterns = {"/Carrito"})
public class Carrito extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Obtener la sesión y el carrito
        HttpSession session = request.getSession();
        List<ItemCarrito> listaCarrito = (List<ItemCarrito>) session.getAttribute("carrito");
        
        double total = 0;
        
        // 2. Calcular el total (Solo si el carrito existe y no está vacío)
        if (listaCarrito != null && !listaCarrito.isEmpty()) {
            for (ItemCarrito item : listaCarrito) {
                total += item.getProducto().getPrecio() * item.getCantidad();
            }
        }
        
        // 3. Cálculos de Impuestos 
        double baseImponible = total / 1.21;
        double iva = total - baseImponible;
        
        // 4. Pasar los datos calculados al JSP
        request.setAttribute("totalCarrito", total);
        request.setAttribute("baseImponible", baseImponible);
        request.setAttribute("ivaCalculado", iva);
        
        request.getRequestDispatcher("/JSP/carrito.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}