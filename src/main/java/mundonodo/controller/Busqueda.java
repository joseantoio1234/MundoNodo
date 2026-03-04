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

@WebServlet(name = "Busqueda", urlPatterns = {"/Busqueda"})
public class Busqueda extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 1. Obtener el parámetro 'query' de la barra de búsqueda
        String query = request.getParameter("query");

        // 2. OBTENER EL POOL DEL CONTEXTO 
        DataSource ds = (DataSource) getServletContext().getAttribute("db_pool");

        // 3. USO DE FACTORY para obtener el DAO
        DAOFactory factoria = DAOFactory.getDAOFactory();
        ProductoDao dao = factoria.getProductoDao();

        List<Producto> resultados;

        // 4. Lógica de búsqueda pasando el DataSource 'ds'
        if (query != null && !query.trim().isEmpty()) {
            // Buscamos productos que coincidan con el nombre
            resultados = dao.buscarPorNombre(ds, query);
        } else {
            // Si la búsqueda está vacía, listamos todo
            resultados = dao.listarTodo(ds);
        }

        // 5. Pasamos los resultados al fragmento AJAX
        request.setAttribute("listaFiltrada", resultados);

        // 6. Redirigimos al componente parcial
        request.getRequestDispatcher("JSP/componentes/listaProductosAjax.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
