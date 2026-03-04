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

/**
 * Servlet controlador encargado de finalizar la transacción de compra.
 * Coordina la persistencia del pedido en la base de datos, gestiona la limpieza 
 * del carrito de sesión y prepara la confirmación final de la factura para el cliente.
 * * @author Jose Antonio
 * @version 1.0
 */
@WebServlet(name = "ProcesarCompra", urlPatterns = {"/ProcesarCompra"})
public class ProcesarCompra extends HttpServlet {

    /**
     * Procesa la petición POST para registrar el pedido de forma definitiva.
     * Realiza validaciones de sesión, clona los datos del carrito para su visualización final,
     * calcula importes e invoca la lógica de persistencia transaccional.
     * * @param request  Objeto {@link HttpServletRequest} con la sesión y datos de compra.
     * @param response Objeto {@link HttpServletResponse} para la navegación post-compra.
     * @throws ServletException Si ocurre un error interno en el servidor.
     * @throws IOException      Si ocurre un error en el flujo de entrada/salida.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        List<ItemCarrito> carritoSesion = (List<ItemCarrito>) session.getAttribute("carrito");

        // 1. Verificación de seguridad y estado: Solo procesa si hay usuario y productos.
        if (usuario != null && carritoSesion != null && !carritoSesion.isEmpty()) {
            
            DataSource ds = (DataSource) getServletContext().getAttribute("db_pool");
            DAOFactory factoria = DAOFactory.getDAOFactory();
            PedidoDAO pedidoDAO = factoria.getPedidoDao();

            // 2. Clonación de datos: Creamos una copia del carrito. 
            List<ItemCarrito> copiaCarrito = new ArrayList<>(carritoSesion);

            // 3.Recalcular importes finales para asegurar consistencia.
            double total = 0;
            for (ItemCarrito item : copiaCarrito) {
                total += item.getProducto().getPrecio() * item.getCantidad();
            }
            
            // Cálculos de impuestos (IVA 21%)
            double baseImponible = total / 1.21;
            double ivaCalculado = total - baseImponible;

            // 4. Creación del objeto Pedido y llamada al DAO transaccional.
            Pedido p = new Pedido();
            p.setIdusuario(usuario.getIdusuario());
            p.setTotal(total);

            // Se inserta cabecera y detalle en una sola transacción SQL.
            int idPedido = pedidoDAO.insertarPedidoCompleto(ds, p, copiaCarrito);

            if (idPedido != -1) {
                
                // 5. Preparación de la vista de éxito: 
                // Pasamos los datos al request para la renderización de la factura.
                request.setAttribute("compraOk", true);
                request.setAttribute("numPedido", idPedido);
                request.setAttribute("totalFactura", total);
                request.setAttribute("baseImponible", baseImponible);
                request.setAttribute("ivaCalculado", ivaCalculado);
                request.setAttribute("carritoFinal", copiaCarrito); 

                // 6. Gestión de Sesión: Limpieza del carrito tras la compra exitosa.
                session.removeAttribute("carrito");
                
                // 7. Navegación: Forward a factura.jsp para mostrar el ticket final.
                request.getRequestDispatcher("/JSP/factura.jsp").forward(request, response);
            } else {
                // Gestión de fallos: Si la base de datos rechaza la transacción.
                response.sendRedirect(request.getContextPath() + "/JSP/carrito.jsp?error=db");
            }
        } else {
            // Acceso denegado o sesión expirada.
            response.sendRedirect(request.getContextPath() + "/Inicio");
        }
    }

    /**
     * Gestiona las peticiones GET redirigiéndolas al carrito.
     * Por seguridad, el proceso de compra no puede iniciarse mediante una URL directa (GET).
     * * @param request  Objeto {@link HttpServletRequest}.
     * @param response Objeto {@link HttpServletResponse}.
     * @throws ServletException Si ocurre un error.
     * @throws IOException      Si ocurre un error.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/JSP/carrito.jsp");
    }
}