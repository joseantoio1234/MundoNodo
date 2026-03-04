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

@WebServlet(name = "FiltrarProductos", urlPatterns = {"/FiltrarProductos"})
public class FiltrarProductos extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 1. OBTENER EL POOL DEL CONTEXTO (El que puso el Listener)
        DataSource ds = (DataSource) getServletContext().getAttribute("db_pool");

        // 2. USO DE FACTORY para obtener el DAO
        DAOFactory factoria = DAOFactory.getDAOFactory();
        ProductoDao dao = factoria.getProductoDao();

        List<Producto> productosFiltrados;

        try {
            // 3. Obtener parámetros 
            String[] categoriasSeleccionadas = request.getParameterValues("cat");
            String minPrecioStr = request.getParameter("min_precio");
            String maxPrecioStr = request.getParameter("max_precio");

            // Dentro de FiltrarProductos.java
            double min = (minPrecioStr != null) ? Double.parseDouble(minPrecioStr) : (Double) getServletContext().getAttribute("precioMinDB");
            double max = (maxPrecioStr != null) ? Double.parseDouble(maxPrecioStr) : (Double) getServletContext().getAttribute("precioMaxDB");

            // 4. Ejecutar filtrado pasando el DataSource 'ds'
            if (categoriasSeleccionadas != null && categoriasSeleccionadas.length > 0) {
                productosFiltrados = dao.filtrarAvanzado(ds, categoriasSeleccionadas, min, max);
            } else {
                productosFiltrados = dao.filtrarSoloPrecio(ds, min, max);
            }

            // 5. Pasar la lista al fragmento AJAX
            request.setAttribute("listaFiltrada", productosFiltrados);

            // 6. Redirigir al JSP parcial
            request.getRequestDispatcher("JSP/componentes/listaProductosAjax.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error en formato de precios");
        }
    }
}
