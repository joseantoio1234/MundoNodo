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

/**
 * Servlet controlador encargado de la gestión y visualización del carrito de compras.
 * Prepara los datos económicos (base imponible, IVA y total) a partir de los items 
 * almacenados en la sesión del usuario para su correcta presentación en la vista.
 * * @author Jose Antonio
 * @version 1.0
 */
@WebServlet(name = "Carrito", urlPatterns = {"/Carrito"})
public class Carrito extends HttpServlet {

    /**
     * Procesa la petición GET para visualizar el contenido del carrito.
     * Recupera la lista de productos de la sesión, realiza el cálculo de impuestos
     * (IVA 21%) y despacha la petición hacia el JSP correspondiente.
     * * @param request  Objeto {@link HttpServletRequest} que contiene la sesión y atributos.
     * @param response Objeto {@link HttpServletResponse} para el manejo de la respuesta.
     * @throws ServletException Si ocurre un error en el despacho de la vista.
     * @throws IOException      Si ocurre un error en el flujo de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Obtener la sesión y recuperar la lista de items del carrito
        HttpSession session = request.getSession();
        List<ItemCarrito> listaCarrito = (List<ItemCarrito>) session.getAttribute("carrito");
        
        double total = 0;
        
        // 2. Cálculo del importe total bruto
        // Solo se itera si el carrito existe en sesión y contiene elementos
        if (listaCarrito != null && !listaCarrito.isEmpty()) {
            for (ItemCarrito item : listaCarrito) {
                total += item.getProducto().getPrecio() * item.getCantidad();
            }
        }
        
        // 3. Cálculos de Impuestos 21%
        // Desglosamos el total para obtener la base imponible y la cuota de IVA
        double baseImponible = total / 1.21;
        double iva = total - baseImponible;
        
        // 4. Transferencia de datos a la capa de vista
        // Se utilizan atributos de request para que los cálculos se regeneren en cada acceso
        request.setAttribute("totalCarrito", total);
        request.setAttribute("baseImponible", baseImponible);
        request.setAttribute("ivaCalculado", iva);
        
        // 5. Redirección interna hacia el JSP de la interfaz de usuario
        request.getRequestDispatcher("/JSP/carrito.jsp").forward(request, response);
    }

    /**
     * Redirige las peticiones POST al método GET.
     * Asegura que el acceso al carrito sea consistente independientemente del método HTTP.
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