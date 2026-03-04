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
import mundonodo.util.Cookies; 

/**
 * Servlet controlador encargado de la autenticación de usuarios.
 * Gestiona el inicio de sesión, la persistencia de la sesión del usuario, 
 * la actualización de metadatos de acceso y la recuperación de carritos de compra
 * almacenados previamente en cookies.
 * * @author Jose Antonio
 * @version 1.0
 */
@WebServlet(name = "ValidarUsuario", urlPatterns = {"/ValidarUsuario"})
public class ValidarUsuario extends HttpServlet {

    /**
     * Gestiona las peticiones GET para mostrar la página de inicio de sesión.
     * * @param request  Objeto {@link HttpServletRequest}.
     * @param response Objeto {@link HttpServletResponse}.
     * @throws ServletException Si ocurre un error en el despacho a la vista.
     * @throws IOException      Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/JSP/login.jsp").forward(request, response);
    }

    /**
     * Procesa las peticiones POST de autenticación.
     * Valida las credenciales contra la base de datos, registra el acceso,
     * sincroniza carritos anónimos desde cookies y gestiona redirecciones pendientes
     * para mejorar el flujo de compra (UX).
     * * @param request  Objeto {@link HttpServletRequest} con los parámetros de login.
     * @param response Objeto {@link HttpServletResponse} que retorna el estado del proceso.
     * @throws ServletException Si ocurre un error en el procesamiento.
     * @throws IOException      Si ocurre un error en la comunicación.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Configuración de respuesta para comunicación asíncrona (AJAX)
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        Usuario loginData = new Usuario();
        try {
            // Mapeo automático de los parámetros del formulario al DTO temporal
            BeanUtils.populate(loginData, request.getParameterMap());
        } catch (Exception e) {
            System.err.println(">>> Error mapeando datos de login: " + e.getMessage());
        }

        DataSource ds = (DataSource) getServletContext().getAttribute("db_pool");
        DAOFactory factoria = DAOFactory.getDAOFactory();
        UsuarioDao dao = factoria.getUsuarioDao();
        
        // Verificación de credenciales en la base de datos
        Usuario u = dao.login(ds, loginData.getCorreo(), loginData.getPassword());

        if (u != null) {
            // LOGIN EXITOSO
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogueado", u);
            session.setAttribute("fechaLogin", new Date());
            
            // Registro de auditoría: Actualizar la fecha del último acceso en la DB
            dao.actualizarUltimoAcceso(ds, u.getIdusuario());

            // --- LÓGICA DE PERSISTENCIA DE CARRITO (Cookies) ---
            // Se comprueba si el usuario tenía productos en la cesta antes de identificarse
            String datosCesta = Cookies.leerCookieCesta(request);
            if (datosCesta != null && !datosCesta.isEmpty()) {
                
                // Una vez recuperada la información, se limpia la cookie del navegador
                Cookies.borrarCookieCesta(response);
                System.out.println(">>> LOGIN: Cesta recuperada de cookie y sincronizada con la sesión.");
            }

            // Verifica si el usuario fue enviado aquí desde un paso interrumpido (ej: ProcesarPedido)
            String redir = (String) session.getAttribute("redireccionPostLogin");
            
            if (redir != null) {
                // Respondemos con la URL de redirección para que el script de JS redirija al usuario
                response.getWriter().write(redir);
                session.removeAttribute("redireccionPostLogin"); 
            } else {
                response.getWriter().write("success");
            }
        } else {
            // LOGIN FALLIDO: Credenciales incorrectas
            response.getWriter().write("error");
        }
    }
}