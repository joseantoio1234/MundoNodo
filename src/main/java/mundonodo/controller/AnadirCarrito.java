package mundonodo.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import mundonodo.daofactory.DAOFactory;
import mundonodo.dao.ProductoDao;
import mundonodo.model.dto.ItemCarrito;
import mundonodo.model.dto.Producto;
import mundonodo.util.Cookies;
import mundonodo.util.CestaUtils;

/**
 * Servlet controlador encargado de gestionar la adición de productos al carrito.
 * Implementa una lógica dual que soporta peticiones estándar y peticiones asíncronas (AJAX),
 * además de gestionar la persistencia del carrito mediante el uso de sesiones y cookies.
 * * @author Jose Antonio
 * @version 1.2
 */
@WebServlet(name = "AnadirCarrito", urlPatterns = {"/AnadirCarrito"})
public class AnadirCarrito extends HttpServlet {

    /**
     * Procesa la petición GET para añadir un producto al carrito.
     * Recupera el producto por ID, actualiza la lista en sesión y sincroniza
     * la información con una cookie persistente.
     * * @param request  Objeto {@link HttpServletRequest} con el parámetro 'id'.
     * @param response Objeto {@link HttpServletResponse} para la respuesta o redirección.
     * @throws ServletException Si ocurre un error interno en el servlet.
     * @throws IOException      Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        
        if (idStr == null || idStr.isEmpty()) {
            enviarRespuestaError(request, response, "ID no válido");
            return; 
        }

        try {
            int idProducto = Integer.parseInt(idStr);
            DataSource ds = (DataSource) getServletContext().getAttribute("db_pool");
            DAOFactory factoria = DAOFactory.getDAOFactory();
            ProductoDao dao = factoria.getProductoDao();

            // 1. Obtención del producto desde la base de datos
            Producto p = dao.obtenerPorId(ds, idProducto);

            if (p != null) {
                HttpSession session = request.getSession();
                List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");

                if (carrito == null) {
                    carrito = new ArrayList<>();
                }

                // 2. Lógica para incrementar cantidad si el producto ya existe en el carrito
                boolean existe = false;
                for (ItemCarrito item : carrito) {
                    if (item.getProducto().getIdproducto() == idProducto) {
                        item.setCantidad(item.getCantidad() + 1);
                        existe = true;
                        break;
                    }
                }

                if (!existe) {
                    carrito.add(new ItemCarrito(p, 1));
                }

                // 3. Persistencia en Sesión (Memoria volátil del servidor)
                session.setAttribute("carrito", carrito);
                
                // Serializamos la cesta a formato String para guardarla en el navegador por 2 días
                String datosCesta = CestaUtils.serializarCesta(carrito);
                Cookies.crearCookieCesta(response, datosCesta);
                
                String requestedWith = request.getHeader("X-Requested-With");
                
                if ("XMLHttpRequest".equals(requestedWith)) {
                    // Respuesta para peticiones AJAX (SweetAlert2 o actualizaciones de Header)
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter out = response.getWriter();
                    
                    int totalUnidades = calcularTotalItems(carrito);
                    // Devolvemos formato simple 'status|total' para fácil parsing en JS
                    out.print("success|" + totalUnidades);
                    out.flush();
                    out.close(); 
                } else {
                    // Respuesta para peticiones estándar: Volver a la página anterior
                    String referer = request.getHeader("Referer");
                    response.sendRedirect((referer != null) ? referer : request.getContextPath() + "/Inicio");
                }
            }

        } catch (NumberFormatException e) {
            enviarRespuestaError(request, response, "Error de formato de ID");
        }
    }
    
    /**
     * Calcula la suma total de unidades de todos los productos en el carrito.
     * * @param carrito La lista de {@link ItemCarrito} actual.
     * @return El número total de productos.
     */
    private int calcularTotalItems(List<ItemCarrito> carrito) {
        int total = 0;
        if (carrito != null) {
            for (ItemCarrito item : carrito) {
                total += item.getCantidad();
            }
        }
        return total;
    }

    /**
     * Centraliza la respuesta en caso de error, adaptándose al origen de la petición.
     * * @param request  La petición original.
     * @param response La respuesta del servidor.
     * @param msg      Mensaje descriptivo del error.
     * @throws IOException Si falla el flujo de salida.
     */
    private void enviarRespuestaError(HttpServletRequest request, HttpServletResponse response, String msg) throws IOException {
        String requestedWith = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(requestedWith)) {
            response.setStatus(400);
            response.getWriter().print("error");
        } else {
            response.sendRedirect(request.getContextPath() + "/Inicio");
        }
    }
}