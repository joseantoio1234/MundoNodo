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

/**
 * Servlet controlador encargado de preparar la vista de la factura.
 * Realiza las comprobaciones finales de seguridad y contenido (usuario y carrito)
 * y calcula los importes impositivos antes de delegar la presentación a la vista JSP.
 * * @author Jose Antonio
 * @version 1.0
 */
@WebServlet(name = "IrAFactura", urlPatterns = {"/IrAFactura"})
public class IrAFactura extends HttpServlet {

    /**
     * Procesa la petición GET para acceder a la factura.
     * Valida la existencia de una sesión activa y productos en la cesta,
     * desglosa el IVA del total acumulado y realiza un forward a la vista de factura.
     * * @param request  Objeto {@link HttpServletRequest} que contiene la sesión y el carrito.
     * @param response Objeto {@link HttpServletResponse} para redirecciones de seguridad.
     * @throws ServletException Si ocurre un error en el despacho del recurso.
     * @throws IOException      Si ocurre un error en la comunicación.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Obtención de datos de sesión
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");

        // 2. Control de Seguridad y Flujo: 
        // Si no hay usuario autenticado o el carrito ha expirado/está vacío, se aborta el proceso.
        if (usuario == null || carrito == null || carrito.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/JSP/carrito.jsp");
            return;
        }

        // --- 3. LÓGICA DE CÁLCULO FINANCIERO ---
        // Se calcula el importe bruto acumulado de todos los items en la cesta
        double total = 0;
        for (ItemCarrito item : carrito) {
            total += item.getProducto().getPrecio() * item.getCantidad();
        }

        // Aplicación del algoritmo de desglose de IVA (21%) para la representación legal de la factura
        double baseImponible = total / 1.21;
        double ivaCalculado = total - baseImponible;

        // 4. Inyección de atributos en el Request
        // Estos datos son efímeros y solo sirven para alimentar la página factura.jsp
        request.setAttribute("totalFactura", total);
        request.setAttribute("baseImponible", baseImponible);
        request.setAttribute("ivaCalculado", ivaCalculado);

        request.getRequestDispatcher("/JSP/factura.jsp").forward(request, response);
    }
}