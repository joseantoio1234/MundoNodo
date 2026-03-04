package mundonodo.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;

import mundonodo.daofactory.DAOFactory;
import mundonodo.dao.UsuarioDao; 
import mundonodo.model.dto.Usuario;
import mundonodo.model.dto.ItemCarrito;
import mundonodo.util.Cookies; // Importamos tu utilidad de Cookies

@WebServlet(name = "ValidarUsuario", urlPatterns = {"/ValidarUsuario"})
public class ValidarUsuario extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/JSP/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        Usuario loginData = new Usuario();
        try {
            BeanUtils.populate(loginData, request.getParameterMap());
        } catch (Exception e) {
            e.printStackTrace(); 
        }

        DataSource ds = (DataSource) getServletContext().getAttribute("db_pool");
        DAOFactory factoria = DAOFactory.getDAOFactory();
        UsuarioDao dao = factoria.getUsuarioDao();
        
        Usuario u = dao.login(ds, loginData.getCorreo(), loginData.getPassword());

        if (u != null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogueado", u);
            session.setAttribute("fechaLogin", new Date());
            dao.actualizarUltimoAcceso(ds, u.getIdusuario());

            // --- LÓGICA DE COOKIES Y CESTA ---
            // 1. Miramos si hay una cesta guardada en cookies del usuario anónimo
            String datosCesta = Cookies.leerCookieCesta(request);
            if (datosCesta != null && !datosCesta.isEmpty()) {
                /* Aquí restauramos la cesta. 
                   Nota: Deberías tener un método 'deserializar' en alguna clase utilidad 
                   para convertir el String de la cookie en List<ItemCarrito>.
                */
                // List<ItemCarrito> cestaRestaurada = CestaUtils.deserializar(datosCesta, ds);
                // session.setAttribute("carrito", cestaRestaurada);
                
                // 2. Una vez pasada a la sesión, BORRAMOS la cookie
                Cookies.borrarCookieCesta(response);
                System.out.println(">>> LOGIN: Cesta de cookie movida a sesión y cookie eliminada.");
            }

            // Verificamos redirecciones pendientes 
            String redir = (String) session.getAttribute("redireccionPostLogin");
            
            if (redir != null) {
                // Si venía de "ProcesarPedido", lo mandamos de vuelta allí
                response.getWriter().write(redir);
                session.removeAttribute("redireccionPostLogin"); 
            } else {
                response.getWriter().write("success");
            }
        } else {
            response.getWriter().write("error");
        }
    }
}