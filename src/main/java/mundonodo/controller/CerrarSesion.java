package mundonodo.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet controlador encargado de finalizar de forma segura la sesión del usuario.
 * Realiza la invalidación de la sesión en el servidor, elimina los atributos 
 * almacenados y configura las cabeceras HTTP para prevenir que el contenido
 * sensible sea recuperado de la caché del navegador.
 * * @author Jose Antonio
 * @version 1.0
 */
@WebServlet(name = "CerrarSesion", urlPatterns = {"/CerrarSesion"})
public class CerrarSesion extends HttpServlet {

    /**
     * Procesa la petición GET para el cierre de sesión.
     * Invalida la sesión actual, limpia la caché del navegador mediante cabeceras HTTP
     * y redirige al usuario a la página de inicio.
     * * @param request  Objeto {@link HttpServletRequest}.
     * @param response Objeto {@link HttpServletResponse}.
     * @throws ServletException Si ocurre un error en el servidor.
     * @throws IOException      Si ocurre un error en la redirección.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Obtención de la sesión actual sin crear una nueva si no existe
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            session.removeAttribute("usuarioLogueado");
            session.removeAttribute("carrito");
            
            session.invalidate();
        }

        // 2. Configuración de cabeceras de caché 
        // Evita que el usuario pueda ver páginas protegidas al usar el botón "Atrás" del navegador
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0); 

        // 3. Redirección segura al inicio de la aplicación
        String urlRedireccion = request.getContextPath() + "/Inicio";
        response.sendRedirect(response.encodeRedirectURL(urlRedireccion));
    }

    /**
     * Procesa las peticiones POST delegando la lógica al método doGet.
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