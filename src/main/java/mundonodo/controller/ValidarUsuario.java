package mundonodo.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
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

@WebServlet(name = "ValidarUsuario", urlPatterns = {"/ValidarUsuario"})
public class ValidarUsuario extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirigimos al JSP usando la URL limpia controlada por el Servlet
        request.getRequestDispatcher("/JSP/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        // 1. Creamos un objeto Usuario vacío 
        Usuario loginData = new Usuario();
        try {

            BeanUtils.populate(loginData, request.getParameterMap());
        } catch (Exception e) {
            e.printStackTrace(); 
        }

        // 2. OBTENER EL POOL DEL CONTEXTO 
        DataSource ds = (DataSource) getServletContext().getAttribute("db_pool");

        // 3. USO DE FACTORÍA
        DAOFactory factoria = DAOFactory.getDAOFactory();
        UsuarioDao dao = factoria.getUsuarioDao();
        
        // 4. Login usando los datos extraídos del Bean poblado
        Usuario u = dao.login(ds, loginData.getCorreo(), loginData.getPassword());

        if (u != null) {
            HttpSession session = request.getSession();
            
            // Guardamos el objeto usuario completo en la sesión
            session.setAttribute("usuarioLogueado", u);
            
            // Guardamos la fecha del inicio de sesión actual
            session.setAttribute("fechaLogin", new Date());
            
            // 5. Actualizamos el último acceso
            dao.actualizarUltimoAcceso(ds, u.getIdusuario());
            
            // Verificamos redirecciones pendientes 
            String redir = (String) session.getAttribute("redireccionPostLogin");
            
            if (redir != null) {
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