package mundonodo.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import mundonodo.daofactory.DAOFactory;
import mundonodo.dao.ProductoDao;
import mundonodo.model.dto.Producto;

/**
 * Servlet controlador encargado de procesar el filtrado dinámico de productos.
 * Permite filtrar el catálogo por múltiples categorías y por un rango de precios.
 * Los resultados se sirven de forma parcial para ser integrados mediante AJAX.
 * * @author Jose Antonio
 * @version 1.0
 */
@WebServlet(name = "FiltrarProductos", urlPatterns = {"/FiltrarProductos"})
public class FiltrarProductos extends HttpServlet {

    /**
     * Procesa las peticiones GET para filtrar productos.
     * Recupera los parámetros de selección y precio, consulta al DAO y 
     * delega la visualización a un componente JSP parcial.
     * * @param request  Objeto {@link HttpServletRequest} que contiene los parámetros 'cat', 'min_precio' y 'max_precio'.
     * @param response Objeto {@link HttpServletResponse} para enviar la respuesta.
     * @throws ServletException Si ocurre un error interno en el procesamiento.
     * @throws IOException      Si ocurre un error de comunicación.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 1. OBTENER EL POOL DEL CONTEXTO (Inicializado por el Listener de la aplicación)
        DataSource ds = (DataSource) getServletContext().getAttribute("db_pool");

        // 2. USO DE FACTORY para obtener la instancia del DAO de productos
        DAOFactory factoria = DAOFactory.getDAOFactory();
        ProductoDao dao = factoria.getProductoDao();

        List<Producto> productosFiltrados;

        try {
            // 3. Obtener parámetros de la petición
            String[] categoriasSeleccionadas = request.getParameterValues("cat");
            String minPrecioStr = request.getParameter("min_precio");
            String maxPrecioStr = request.getParameter("max_precio");

            // Lógica de valores por defecto: si no vienen parámetros, se usan los valores 
            // mínimos y máximos globales almacenados en el contexto de la aplicación.
            double min = (minPrecioStr != null) ? Double.parseDouble(minPrecioStr) : (Double) getServletContext().getAttribute("precioMinDB");
            double max = (maxPrecioStr != null) ? Double.parseDouble(maxPrecioStr) : (Double) getServletContext().getAttribute("precioMaxDB");

            // 4. Ejecución del filtrado delegando en el DAO
            if (categoriasSeleccionadas != null && categoriasSeleccionadas.length > 0) {
                // Filtrado por categorías + rango de precios
                productosFiltrados = dao.filtrarAvanzado(ds, categoriasSeleccionadas, min, max);
            } else {
                // Si no hay categorías seleccionadas, se filtra únicamente por precio
                productosFiltrados = dao.filtrarSoloPrecio(ds, min, max);
            }

            // 5. Establecer la lista resultante como atributo para la vista parcial
            request.setAttribute("listaFiltrada", productosFiltrados);

            // 6. Redirección interna (Forward) al componente JSP que genera el HTML de los productos
            request.getRequestDispatcher("JSP/componentes/listaProductosAjax.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            // Manejo de error en caso de recibir valores de precio no numéricos
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error en el formato de los parámetros de precios");
        }
    }
}