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
 * Servlet controlador encargado de la preparación y validación del pedido.
 * Actúa como intermediario entre el carrito de compras y la confirmación final.
 * Realiza cálculos fiscales (IVA, Base Imponible) y verifica el estado de la sesión
 * antes de permitir el acceso a la vista previa de la factura.
 * * @author Jose Antonio
 * @version 1.0
 */
@WebServlet(name = "ProcesarPedido", urlPatterns = {"/ProcesarPedido"})
public class ProcesarPedido extends HttpServlet {

    /**
     * Procesa la petición GET para generar el resumen del pedido.
     * Realiza validaciones de seguridad (usuario logueado) y de contenido (carrito no vacío).
     * * @param request  Objeto {@link HttpServletRequest} que contiene la sesión del usuario.
     * @param response Objeto {@link HttpServletResponse} para redirecciones o envíos de vista.
     * @throws ServletException Si ocurre un error en el despacho de la petición.
     * @throws IOException      Si ocurre un error en la red o flujo de salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");

        // 1. Verificación de Seguridad: Si el usuario no está identificado, se redirige al login.
        // Se guarda la intención de volver al "Carrito" tras el inicio de sesión exitoso.
        if (usuario == null) {
            session.setAttribute("redireccionPostLogin", "Carrito"); 
            response.sendRedirect(request.getContextPath() + "/ValidarUsuario");
            return;
        }

        // 2. Verificación de Contenido: Evita procesar pedidos sin productos seleccionados.
        if (carrito == null || carrito.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/Inicio");
            return;
        }

        try {
            // 3. Lógica de negocio: Cálculos financieros para la factura detallada.
            // Se asume un IVA del 21% para el mercado español.
            double total = calcularTotal(carrito);
            double baseImponible = total / 1.21;
            double ivaCalculado = total - baseImponible;

            // 4. Preparación de datos para la capa de presentación.
            // Se utilizan atributos de request para que los datos no persistan tras la visualización.
            request.setAttribute("totalFactura", total);
            request.setAttribute("baseImponible", baseImponible);
            request.setAttribute("ivaCalculado", ivaCalculado);
            request.setAttribute("modoPreview", true); 

            // 5. Delegación a la vista: Se usa Forward para mantener los atributos del request.
            request.getRequestDispatcher("/JSP/factura.jsp").forward(request, response);

        } catch (Exception e) {
            // Gestión de errores inesperados durante el cálculo.
            System.err.println(">>> Error en ProcesarPedido: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/JSP/carrito.jsp?error=exception");
        }
    }

    /**
     * Calcula el importe total sumando los subtotales de todos los items del carrito.
     * * @param carrito La lista de {@link ItemCarrito} a procesar.
     * @return El importe total bruto acumulado.
     */
    private double calcularTotal(List<ItemCarrito> carrito) {
        double total = 0;
        for (ItemCarrito item : carrito) {
            total += item.getProducto().getPrecio() * item.getCantidad();
        }
        return total;
    }

    /**
     * Redirige las peticiones POST al método GET para centralizar la lógica.
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