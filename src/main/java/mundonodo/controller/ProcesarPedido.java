package mundonodo.controller;

import java.io.IOException;
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

@WebServlet(name = "ProcesarPedido", urlPatterns = {"/ProcesarPedido"})
public class ProcesarPedido extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        Pedido nuevoPedido = null;

        // 1. Verificación de Seguridad
        if (usuario == null) {
            session.setAttribute("redireccionPostLogin", "Carrito"); // Redirigir al Servlet del carrito, no al JSP
            response.sendRedirect(request.getContextPath() + "/ValidarUsuario");
            return;
        }

        // 2. Verificación de Contenido
        if (carrito == null || carrito.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/Inicio");
            return;
        }

        try {
            // 3. OBTENER RECURSOS 
            DataSource ds = (DataSource) getServletContext().getAttribute("db_pool");
            DAOFactory factoria = DAOFactory.getDAOFactory();
            PedidoDAO pedidoDao = factoria.getPedidoDao();

            // 4. CREAR EL OBJETO PEDIDO
            nuevoPedido = new Pedido();
            nuevoPedido.setIdusuario(usuario.getIdusuario());
            nuevoPedido.setTotal(calcularTotal(carrito));

            // 5. GUARDAR EN BD 
            int idPedidoGenerado = pedidoDao.insertarPedidoCompleto(ds, nuevoPedido, carrito);

            if (idPedidoGenerado > 0) {
                // 6. Guardamos el ID en sesión para la factura y vaciamos carrito
                nuevoPedido.setIdpedido(idPedidoGenerado);
                
                // Redirigimos a la vista final (Factura)
                response.sendRedirect(request.getContextPath() + "/JSP/factura.jsp");
            } else {
                // Error en la inserción
                response.sendRedirect(request.getContextPath() + "/Carrito?error=db");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/Carrito?error=exception");
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