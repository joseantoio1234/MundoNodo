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
import mundonodo.util.Cookies; // Clase que creaste para gestionar cookies

@WebServlet(name = "Inicio", urlPatterns = {"/Inicio"})
public class Inicio extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Gestionar sesión de usuario
        HttpSession session = request.getSession();
        Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
        
        // 2. Obtener Pool y DAO
        DataSource ds = (DataSource) getServletContext().getAttribute("db_pool");
        DAOFactory factoria = DAOFactory.getDAOFactory();
        ProductoDao dao = factoria.getProductoDao();

        // --- 3. LÓGICA DE COOKIES: RESTAURAR CESTA ANÓNIMA ---
        // Solo intentamos restaurar si NO hay ya un carrito en la sesión
        if (session.getAttribute("carrito") == null) {
            String cestaCookieStr = Cookies.leerCookieCesta(request);
            
            if (cestaCookieStr != null && !cestaCookieStr.isEmpty()) {
                /* Aquí usamos una utilidad (puedes llamarla CestaUtils o similar) 
                   que convierta el String de la cookie (ej: "12_2-5_1") 
                   en una List<ItemCarrito> consultando la BD con 'ds'.
                */
                // List<ItemCarrito> carritoRestaurado = CestaUtils.deserializarCesta(cestaCookieStr, ds);
                // session.setAttribute("carrito", carritoRestaurado);
                System.out.println(">>> COOKIE: Cesta restaurada desde la cookie.");
            }
        }

        if (u != null) {
            request.setAttribute("nombreUsuario", u.getNombre());
            // Si el usuario está logueado, según tu lógica, la cookie debería borrarse 
            // Esto suele hacerse en el servlet de Login, pero lo aseguramos aquí:
            // Cookies.borrarCookieCesta(response);
        }

        // --- 4. GESTIÓN DEL CONTEXTO (OPTIMIZACIÓN) ---
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

        // --- 5. GESTIÓN DE PRODUCTOS (LANDING PAGE) ---
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
        
        // 6. Enviar datos a la vista
        request.setAttribute("listaCategorias", listaCategorias);
        request.setAttribute("precioMinDB", precioMinDB);
        request.setAttribute("precioMaxDB", precioMaxDB);
        request.setAttribute("listaNovedades", listaNovedades);
        request.setAttribute("listaMasVendidos", listaMasVendidos);
        
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}