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
// Importamos tus nuevas utilidades para la persistencia de la cesta
import mundonodo.util.Cookies;
import mundonodo.util.CestaUtils;

@WebServlet(name = "ActualizarCantidad", urlPatterns = {"/ActualizarCantidad"})
public class ActualizarCantidad extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
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
                    Iterator<ItemCarrito> it = carrito.iterator();
                    while (it.hasNext()) {
                        ItemCarrito item = it.next();
                        
                        if (item.getProducto().getIdproducto() == idProducto) {
                            encontrado = true;
                            
                            if ("sumar".equals(accion)) {
                                item.setCantidad(item.getCantidad() + 1);
                            } else if ("restar".equals(accion)) {
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
                    
                    // Recalcular el total general tras la modificación
                    int totalItemsHeader = 0;
                    for (ItemCarrito item : carrito) {
                        nuevoTotal += item.getProducto().getPrecio() * item.getCantidad();
                        totalItemsHeader += item.getCantidad();
                    }
                    
                    // 1. Actualizar la Sesión (Memoria del servidor)
                    session.setAttribute("carrito", carrito);

                    // --- 2. LÓGICA DE PERSISTENCIA (COOKIES) ---
                    if (carrito.isEmpty()) {
                        // Si el carrito se quedó vacío tras eliminar, borramos la cookie
                        Cookies.borrarCookieCesta(response);
                    } else {
                        // Si tiene productos, actualizamos la cookie con el nuevo estado (2 días)
                        String datosCesta = CestaUtils.serializarCesta(carrito);
                        Cookies.crearCookieCesta(response, datosCesta);
                    }
                }
            }

            // Devolver respuesta JSON al cliente AJAX
            out.print("{");
            out.print("\"status\":\"success\",");
            out.print("\"nuevaQty\":" + nuevaCantidad + ",");
            out.print("\"nuevoSubtotal\":" + nuevoSubtotalItem + ",");
            out.print("\"nuevoTotal\":" + nuevoTotal);
            out.print("}");

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"status\":\"error\", \"message\":\"" + e.getMessage() + "\"}");
        }
    }
}