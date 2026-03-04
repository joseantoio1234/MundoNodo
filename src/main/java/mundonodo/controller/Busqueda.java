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
 * Servlet controlador encargado de gestionar las búsquedas de productos.
 * Recibe los términos de búsqueda del usuario y devuelve una lista filtrada
 * de productos procesada a través de un componente parcial (AJAX).
 * * @author Jose Antonio
 * @version 1.0
 */
@WebServlet(name = "Busqueda", urlPatterns = {"/Busqueda"})
public class Busqueda extends HttpServlet {

    /**
     * Procesa las peticiones GET para la búsqueda de productos.
     * Utiliza el patrón DAO a través de una Factoría para recuperar los datos
     * y delega la visualización a un fragmento JSP.
     * * @param request  Objeto {@link HttpServletRequest} que contiene el parámetro 'query'.
     * @param response Objeto {@link HttpServletResponse} para enviar la respuesta al cliente.
     * @throws ServletException Si ocurre un error específico del servlet.
     * @throws IOException      Si ocurre un error de entrada/salida en la comunicación.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 1. Obtener el parámetro 'query' de la barra de búsqueda
        String query = request.getParameter("query");

        // 2. Obtener el Pool de conexiones (DataSource) del contexto del servidor
        DataSource ds = (DataSource) getServletContext().getAttribute("db_pool");

        // 3. Uso del patrón Factory para obtener la implementación del DAO de Productos
        DAOFactory factoria = DAOFactory.getDAOFactory();
        ProductoDao dao = factoria.getProductoDao();

        List<Producto> resultados;

        // 4. Filtrado de resultados según el término de búsqueda
        if (query != null && !query.trim().isEmpty()) {
            // Buscamos productos que coincidan con el nombre proporcionado
            resultados = dao.buscarPorNombre(ds, query);
        } else {
            // Si la búsqueda está vacía o es nula, recuperamos el catálogo completo
            resultados = dao.listarTodo(ds);
        }

        // 5. Establecer los resultados como atributo del request para la vista
        request.setAttribute("listaFiltrada", resultados);

        // 6. Redirección interna (Forward) al fragmento JSP encargado de renderizar la lista
        // Este fragmento suele ser cargado mediante una petición asíncrona (AJAX)
        request.getRequestDispatcher("JSP/componentes/listaProductosAjax.jsp").forward(request, response);
    }

    /**
     * Procesa las peticiones POST redirigiéndolas al método doGet.
     * Permite que la funcionalidad de búsqueda sea accesible por ambos métodos HTTP.
     * * @param request  Objeto {@link HttpServletRequest}.
     * @param response Objeto {@link HttpServletResponse}.
     * @throws ServletException Si ocurre un error.
     * @throws IOException      Si ocurre un error.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}