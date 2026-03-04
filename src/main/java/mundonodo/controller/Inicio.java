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

@WebServlet(name = "Inicio", urlPatterns = {"/Inicio"})
public class Inicio extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Gestionar sesión de usuario
        HttpSession session = request.getSession();
        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        if (u != null) {
            request.setAttribute("nombreUsuario", u.getNombre());
        }

        // 2. Obtener Pool y DAO
        DataSource ds = (DataSource) getServletContext().getAttribute("db_pool");
        DAOFactory factoria = DAOFactory.getDAOFactory();
        ProductoDao dao = factoria.getProductoDao();

        // --- 3. GESTIÓN DEL CONTEXTO  ---
        ServletContext ctx = getServletContext();

        // Intentamos recuperar los datos del contexto
        List<String> listaCategorias = (List<String>) ctx.getAttribute("listaCategorias");
        Double precioMinDB = (Double) ctx.getAttribute("precioMinDB");
        Double precioMaxDB = (Double) ctx.getAttribute("precioMaxDB");

        // Si no existen en el contexto, los traemos de la BD y los guardamos para siempre
        if (listaCategorias == null) {
            listaCategorias = dao.obtenerCategoriasUnicas(ds);
            ctx.setAttribute("listaCategorias", listaCategorias);
            System.out.println(">>> CONTEXTO: Categorías guardadas en el ServletContext.");
        }

        if (precioMinDB == null || precioMaxDB == null) {
            double[] rango = dao.obtenerRangoPrecios(ds);
            precioMinDB = rango[0];
            precioMaxDB = rango[1];
            ctx.setAttribute("precioMinDB", precioMinDB);
            ctx.setAttribute("precioMaxDB", precioMaxDB);
            System.out.println(">>> CONTEXTO: Rango de precios guardado en el ServletContext.");
        }

        // --- 4. GESTIÓN DE PRODUCTOS (LANDING PAGE) ---
        List<Producto> poolCompleto = dao.listarTodo(ds);
        List<Producto> listaNovedades = new ArrayList<>();
        List<Producto> listaMasVendidos = new ArrayList<>();
        
        if (poolCompleto != null && !poolCompleto.isEmpty()) {
            List<Producto> copiaPool = new ArrayList<>(poolCompleto);
            Collections.shuffle(copiaPool);
            
            int limiteNovedades = Math.min(5, copiaPool.size());
            for (int i = 0; i < limiteNovedades; i++) {
                listaNovedades.add(copiaPool.get(i));
            }
            
            copiaPool.removeAll(listaNovedades);
            
            int limiteVentas = Math.min(5, copiaPool.size());
            for (int i = 0; i < limiteVentas; i++) {
                listaMasVendidos.add(copiaPool.get(i));
            }
        }
        
        // 5. Enviar datos necesarios para la vista (algunos vienen del contexto ahora)
        request.setAttribute("listaCategorias", listaCategorias);
        request.setAttribute("precioMinDB", precioMinDB);
        request.setAttribute("precioMaxDB", precioMaxDB);
        request.setAttribute("listaNovedades", listaNovedades);
        request.setAttribute("listaMasVendidos", listaMasVendidos);
        
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}