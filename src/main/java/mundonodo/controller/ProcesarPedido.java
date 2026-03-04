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

@WebServlet(name = "ProcesarPedido", urlPatterns = {"/ProcesarPedido"})
public class ProcesarPedido extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");

        // 1. Verificación de Seguridad: Si no está logueado, mandamos a login
        if (usuario == null) {
            session.setAttribute("redireccionPostLogin", "Carrito"); 
            response.sendRedirect(request.getContextPath() + "/ValidarUsuario");
            return;
        }

        // 2. Verificación de Contenido: Si el carrito está vacío, al inicio
        if (carrito == null || carrito.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/Inicio");
            return;
        }

        try {
            // 3. CÁLCULOS PARA LA VISTA PREVIA (Sin tocar la Base de Datos)
            double total = calcularTotal(carrito);
            double baseImponible = total / 1.21;
            double ivaCalculado = total - baseImponible;

            // 4. PASAR DATOS AL REQUEST
            // Estos atributos los usará factura.jsp para mostrar el resumen antes de confirmar
            request.setAttribute("totalFactura", total);
            request.setAttribute("baseImponible", baseImponible);
            request.setAttribute("ivaCalculado", ivaCalculado);
            request.setAttribute("modoPreview", true); // Para saber que aún no se ha confirmado

            // 5. REDIRIGIR A LA FACTURA (Usamos Forward para que los atributos lleguen)
            request.getRequestDispatcher("/JSP/factura.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/JSP/carrito.jsp?error=exception");
        }
    }

    private double calcularTotal(List<ItemCarrito> carrito) {
        double total = 0;
        for (ItemCarrito item : carrito) {
            total += item.getProducto().getPrecio() * item.getCantidad();
        }
        return total;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}