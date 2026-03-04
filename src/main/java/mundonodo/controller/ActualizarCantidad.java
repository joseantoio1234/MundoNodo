package mundonodo.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mundonodo.model.dto.ItemCarrito;
import mundonodo.util.Cookies;
import mundonodo.util.CestaUtils;

/**
 * Servlet controlador encargado de gestionar las cantidades del carrito mediante AJAX.
 * Procesa operaciones de incremento, decremento y eliminación de productos,
 * recalculando importes en tiempo real y sincronizando los cambios tanto en la
 * sesión del servidor como en las cookies del cliente para persistencia anónima.
 * * @author Jose Antonio
 * @version 1.1
 */
@WebServlet(name = "ActualizarCantidad", urlPatterns = {"/ActualizarCantidad"})
public class ActualizarCantidad extends HttpServlet {

    /**
     * Procesa las peticiones GET enviadas de forma asíncrona.
     * Actualiza la lógica del carrito en memoria y devuelve un objeto JSON con los 
     * nuevos cálculos de subtotales y totales generales.
     * * @param request  Objeto {@link HttpServletRequest} que contiene 'id' y 'accion'.
     * @param response Objeto {@link HttpServletResponse} que retorna un JSON.
     * @throws ServletException Si ocurre un fallo en la lógica del servlet.
     * @throws IOException      Si ocurre un error en el flujo de salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Establecemos el tipo de respuesta a JSON para que el script del cliente lo procese
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        double nuevoTotal = 0;
        double nuevoSubtotalItem = 0;
        int nuevaCantidad = 0;
        boolean encontrado = false;

        try {
            String idParam = request.getParameter("id");
            String accion = request.getParameter("accion");

            if (idParam != null && accion != null) {
                int idProducto = Integer.parseInt(idParam);
                HttpSession session = request.getSession();
                List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");

                if (carrito != null) {
                    // Usamos Iterator para poder eliminar elementos de forma segura mientras recorremos
                    Iterator<ItemCarrito> it = carrito.iterator();
                    while (it.hasNext()) {
                        ItemCarrito item = it.next();
                        
                        if (item.getProducto().getIdproducto() == idProducto) {
                            encontrado = true;
                            
                            // Lógica de modificación de cantidades
                            if ("sumar".equals(accion)) {
                                item.setCantidad(item.getCantidad() + 1);
                            } else if ("restar".equals(accion)) {
                                // Evitamos que la cantidad baje de 1
                                if (item.getCantidad() > 1) {
                                    item.setCantidad(item.getCantidad() - 1);
                                }
                            } else if ("eliminar".equals(accion)) {
                                it.remove();
                                encontrado = false; 
                            }
                            
                            if (encontrado) {
                                nuevaCantidad = item.getCantidad();
                                nuevoSubtotalItem = item.getProducto().getPrecio() * item.getCantidad();
                            }
                        }
                    }
                    
                    // Recalcular el total general del carrito tras la modificación
                    for (ItemCarrito item : carrito) {
                        nuevoTotal += item.getProducto().getPrecio() * item.getCantidad();
                    }
                    
                    // 1. Sincronización de Sesión (Memoria volátil del servidor)
                    session.setAttribute("carrito", carrito);

                    // 2. Persistencia en Cookies: Permite recuperar la cesta si el usuario cierra el navegador
                    if (carrito.isEmpty()) {
                        // Limpieza de cookie si el carrito se vacía
                        Cookies.borrarCookieCesta(response);
                    } else {
                        // Actualización de cookie (serialización de la lista de productos)
                        String datosCesta = CestaUtils.serializarCesta(carrito);
                        Cookies.crearCookieCesta(response, datosCesta);
                    }
                }
            }

            // Construcción manual de la respuesta JSON (Estructura de intercambio)
            out.print("{");
            out.print("\"status\":\"success\",");
            out.print("\"nuevaQty\":" + nuevaCantidad + ",");
            out.print("\"nuevoSubtotal\":" + nuevoSubtotalItem + ",");
            out.print("\"nuevoTotal\":" + nuevoTotal);
            out.print("}");

        } catch (Exception e) {
            // Manejo de errores devolviendo un código de estado 500 y el mensaje de error en JSON
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"status\":\"error\", \"message\":\"" + e.getMessage() + "\"}");
        }
    }
}