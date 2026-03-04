package mundonodo.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Importamos la utilidad de Cookies para la gestión de persistencia
import mundonodo.util.Cookies;

/**
 * Servlet controlador encargado de realizar el vaciado completo del carrito de compras.
 * Implementa una limpieza integral eliminando la lista de productos de la sesión 
 * del servidor y destruyendo la cookie de persistencia en el navegador del cliente.
 * * @author Jose Antonio
 * @version 1.0
 */
@WebServlet(name = "VaciarCarrito", urlPatterns = {"/VaciarCarrito"})
public class VaciarCarrito extends HttpServlet {

    /**
     * Procesa la petición GET para resetear el estado del carrito.
     * Invalida los datos de la cesta en los dos niveles de almacenamiento (Server-side y Client-side)
     * y redirige al usuario a la vista actualizada del carrito.
     * * @param request  Objeto {@link HttpServletRequest} que contiene la sesión actual.
     * @param response Objeto {@link HttpServletResponse} utilizado para manipular las cookies y redirigir.
     * @throws ServletException Si ocurre un error interno en el contenedor.
     * @throws IOException      Si ocurre un error en la redirección de salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Gestión de Memoria en Servidor 
        // Obtenemos la sesión actual para eliminar el atributo que contiene la lista de ítems.
        HttpSession session = request.getSession();
        session.removeAttribute("carrito");
        
        Cookies.borrarCookieCesta(response);
        
        // Log de trazabilidad en consola para depuración durante el desarrollo
        System.out.println(">>> CARRITO: Cesta vaciada en sesión y cookie de persistencia eliminada.");

        // 3. Redirección de flujo
        // Enviamos al usuario de vuelta a la página del carrito, la cual se renderizará 
        // mostrando el mensaje de "Carrito vacío" al no encontrar el atributo en sesión.
        response.sendRedirect(request.getContextPath() + "/JSP/carrito.jsp");
    }
}