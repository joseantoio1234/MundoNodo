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
// Importamos tus nuevas utilidades
import mundonodo.util.Cookies;
import mundonodo.util.CestaUtils;

@WebServlet(name = "AnadirCarrito", urlPatterns = {"/AnadirCarrito"})
public class AnadirCarrito extends HttpServlet {

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

            Producto p = dao.obtenerPorId(ds, idProducto);

            if (p != null) {
                HttpSession session = request.getSession();
                List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");

                if (carrito == null) {
                    carrito = new ArrayList<>();
                }

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

                // 1. Guardar en Sesión (Memoria activa)
                session.setAttribute("carrito", carrito);
                
                // --- 2. LÓGICA DE PERSISTENCIA (COOKIES) ---
                // Convertimos el carrito a texto y lo guardamos por 2 días
                String datosCesta = CestaUtils.serializarCesta(carrito);
                Cookies.crearCookieCesta(response, datosCesta);
                
                // --- GESTIÓN DE RESPUESTA PARA AJAX ---
                String requestedWith = request.getHeader("X-Requested-With");
                
                if ("XMLHttpRequest".equals(requestedWith)) {
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter out = response.getWriter();
                    
                    int totalUnidades = calcularTotalItems(carrito);
                    out.print("success|" + totalUnidades);
                    out.flush();
                    out.close(); 
                } else {
                    String referer = request.getHeader("Referer");
                    response.sendRedirect((referer != null) ? referer : request.getContextPath() + "/Inicio");
                }
            }

        } catch (NumberFormatException e) {
            enviarRespuestaError(request, response, "Error de formato");
        }
    }
    
    private int calcularTotalItems(List<ItemCarrito> carrito) {
        int total = 0;
        if (carrito != null) {
            for (ItemCarrito item : carrito) {
                total += item.getCantidad();
            }
        }
        return total;
    }

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