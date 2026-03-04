package mundonodo.controller;

import java.io.IOException;
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
import mundonodo.dao.PedidoDAO;         
import mundonodo.model.dto.ItemCarrito;
import mundonodo.model.dto.Usuario;
import mundonodo.model.dto.Pedido;

@WebServlet(name = "ProcesarCompra", urlPatterns = {"/ProcesarCompra"})
public class ProcesarCompra extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        List<ItemCarrito> carritoSesion = (List<ItemCarrito>) session.getAttribute("carrito");

        // 1. Verificación de seguridad
        if (usuario != null && carritoSesion != null && !carritoSesion.isEmpty()) {
            
            DataSource ds = (DataSource) getServletContext().getAttribute("db_pool");
            DAOFactory factoria = DAOFactory.getDAOFactory();
            PedidoDAO pedidoDAO = factoria.getPedidoDao();

            // 2. Clonamos el carrito para que la JSP tenga datos aunque borremos la sesión
            List<ItemCarrito> copiaCarrito = new ArrayList<>(carritoSesion);

            // 3. Calculamos todos los importes necesarios para la factura
            double total = 0;
            for (ItemCarrito item : copiaCarrito) {
                total += item.getProducto().getPrecio() * item.getCantidad();
            }
            
            // Cálculos de impuestos (21% IVA)
            double baseImponible = total / 1.21;
            double ivaCalculado = total - baseImponible;

            // 4. Persistencia en Base de Datos (Transacción)
            Pedido p = new Pedido();
            p.setIdusuario(usuario.getIdusuario());
            p.setTotal(total);

            int idPedido = pedidoDAO.insertarPedidoCompleto(ds, p, copiaCarrito);

            if (idPedido != -1) {
                
                // Pasamos TODO al request. Estos atributos son los que leerá factura.jsp
                request.setAttribute("compraOk", true);
                request.setAttribute("numPedido", idPedido);
                request.setAttribute("totalFactura", total);
                request.setAttribute("baseImponible", baseImponible);
                request.setAttribute("ivaCalculado", ivaCalculado);
                request.setAttribute("carritoFinal", copiaCarrito); 

                // 5. Ahora sí, limpiamos el carrito de la sesión
                session.removeAttribute("carrito");
                
                // 6. Redirección interna (Forward) - Mantiene los atributos del request
                request.getRequestDispatcher("/JSP/factura.jsp").forward(request, response);
            } else {
                // Si falla la BD, volvemos al carrito con aviso de error
                response.sendRedirect(request.getContextPath() + "/JSP/carrito.jsp?error=db");
            }
        } else {
            // Si el usuario intenta procesar sin estar logueado o sin carrito
            response.sendRedirect(request.getContextPath() + "/Inicio");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Por seguridad, si intentan acceder por URL, mandamos al carrito
        response.sendRedirect(request.getContextPath() + "/JSP/carrito.jsp");
    }
}