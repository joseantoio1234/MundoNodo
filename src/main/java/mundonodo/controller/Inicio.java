package mundonodo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import mundonodo.daofactory.DAOFactory;
import mundonodo.dao.ProductoDao;
import mundonodo.model.dto.Producto;
import mundonodo.model.dto.Usuario;
import mundonodo.model.dto.ItemCarrito;
import mundonodo.util.Cookies; 

/**
 * Servlet controlador principal encargado de gestionar la página de inicio (Landing Page).
 * Orquestra la carga inicial del catálogo, la configuración de filtros dinámicos en el contexto,
 * la identificación del usuario y la recuperación de cestas de compra persistidas en cookies.
 * * @author Jose Antonio
 * @version 1.5
 */
@WebServlet(name = "Inicio", urlPatterns = {"/Inicio"})
public class Inicio extends HttpServlet {

    /**
     * Prepara y sirve la página de inicio de la tienda MundoNodo.
     * Implementa lógica de caché a nivel de aplicación para categorías y precios, 
     * y genera colecciones aleatorias de productos para las vitrinas de novedades y ventas.
     * * @param request  Objeto {@link HttpServletRequest}.
     * @param response Objeto {@link HttpServletResponse}.
     * @throws ServletException Si ocurre un error en el despacho de la vista.
     * @throws IOException      Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Gestión de la sesión del usuario
        HttpSession session = request.getSession();
        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        
        // 2. Obtención de infraestructura de datos
        DataSource ds = (DataSource) getServletContext().getAttribute("db_pool");
        DAOFactory factoria = DAOFactory.getDAOFactory();
        ProductoDao dao = factoria.getProductoDao();

        // --- 3. PERSISTENCIA DE CARRITO (COOKIES) ---
        // Si el usuario vuelve a la web y no tiene carrito activo en sesión,
        // intentamos restaurar los productos que dejó guardados en su navegador.
        if (session.getAttribute("carrito") == null) {
            String cestaCookieStr = Cookies.leerCookieCesta(request);
            
            if (cestaCookieStr != null && !cestaCookieStr.isEmpty()) {
      
                System.out.println(">>> COOKIE: Detectada cesta persistente para restaurar.");
            }
        }

        if (u != null) {
            request.setAttribute("nombreUsuario", u.getNombre());
        }

        // Cargamos categorías y rangos de precios solo si no están ya en memoria del servidor.
        ServletContext ctx = getServletContext();
        List<String> listaCategorias = (List<String>) ctx.getAttribute("listaCategorias");
        Double precioMinDB = (Double) ctx.getAttribute("precioMinDB");
        Double precioMaxDB = (Double) ctx.getAttribute("precioMaxDB");

        if (listaCategorias == null) {
            listaCategorias = dao.obtenerCategoriasUnicas(ds);
            ctx.setAttribute("listaCategorias", listaCategorias);
        }

        if (precioMinDB == null || precioMaxDB == null) {
            double[] rango = dao.obtenerRangoPrecios(ds);
            precioMinDB = rango[0];
            precioMaxDB = rango[1];
            ctx.setAttribute("precioMinDB", precioMinDB);
            ctx.setAttribute("precioMaxDB", precioMaxDB);
        }

        // Recuperamos el pool de productos y generamos dos listas dinámicas (Novedades y Más Vendidos)
        List<Producto> poolCompleto = dao.listarTodo(ds);
        List<Producto> listaNovedades = new ArrayList<>();
        List<Producto> listaMasVendidos = new ArrayList<>();
        
        if (poolCompleto != null && !poolCompleto.isEmpty()) {
            List<Producto> copiaPool = new ArrayList<>(poolCompleto);
            Collections.shuffle(copiaPool); // Aleatoriedad para la experiencia de usuario
            
            // Selección de los primeros 5 productos para la vitrina de Novedades
            int limiteNovedades = Math.min(5, copiaPool.size());
            for (int i = 0; i < limiteNovedades; i++) {
                listaNovedades.add(copiaPool.get(i));
            }
            
            // Eliminamos los ya seleccionados para no repetir en la vitrina de Más Vendidos
            copiaPool.removeAll(listaNovedades);
            
            int limiteVentas = Math.min(5, copiaPool.size());
            for (int i = 0; i < limiteVentas; i++) {
                listaMasVendidos.add(copiaPool.get(i));
            }
        }
        
        // 6. Envió de toda la carga de datos a la vista principal (index.jsp)
        request.setAttribute("listaCategorias", listaCategorias);
        request.setAttribute("precioMinDB", precioMinDB);
        request.setAttribute("precioMaxDB", precioMaxDB);
        request.setAttribute("listaNovedades", listaNovedades);
        request.setAttribute("listaMasVendidos", listaMasVendidos);
        
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}