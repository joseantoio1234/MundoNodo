package mundonodo.controller;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;

import mundonodo.daofactory.DAOFactory;
import mundonodo.dao.UsuarioDao;
import mundonodo.model.dto.Usuario;

@WebServlet(name = "RegistroUsuario", urlPatterns = {"/RegistroUsuario"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1,  
    maxFileSize = 1024 * 1024 * 2,       
    maxRequestSize = 1024 * 1024 * 10    
)
public class RegistroUsuario extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/JSP/registro.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        // Eliminamos el ContentType "text/plain" porque vamos a redirigir, no a escribir texto

        Usuario u = new Usuario();
        
        try {
            DataSource ds = (DataSource) getServletContext().getAttribute("db_pool");

            // Poblamos el objeto Usuario con los campos del formulario
            BeanUtils.populate(u, request.getParameterMap());
            
            // Corrección para el código postal si el nombre del input en el JSP es "cp"
            if(u.getCp() == null || u.getCp().isEmpty()) {
                u.setCp(request.getParameter("cp"));
            }

            // 2. Gestión de Avatar 
            Part filePart = request.getPart("fotoAvatar");
            String nombreImagen = "pinguavatar.png"; 

            if (filePart != null && filePart.getSize() > 0) {
                String fileNameOriginal = filePart.getSubmittedFileName();
                nombreImagen = System.currentTimeMillis() + "_" + fileNameOriginal;

                String uploadPath = getServletContext().getRealPath("") + File.separator + "IMAGENES" + File.separator + "avatars";
                
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdirs();

                filePart.write(uploadPath + File.separator + nombreImagen);
            }
            u.setAvatar(nombreImagen); 

            DAOFactory factoria = DAOFactory.getDAOFactory();
            UsuarioDao dao = factoria.getUsuarioDao();

            // 3. Registrar y Login automático
            if (dao.registrar(ds, u)) {
                // Obtenemos el usuario completo (con su ID generado) para la sesión
                Usuario usuarioCompleto = dao.login(ds, u.getCorreo(), u.getPassword());
                HttpSession session = request.getSession();
                session.setAttribute("usuarioLogueado", usuarioCompleto);
                
                // 4. REDIRECCIÓN EN LUGAR DE PRINT
                String redir = (String) session.getAttribute("redireccionPostLogin");
                if (redir != null) {
                    session.removeAttribute("redireccionPostLogin");
                    response.sendRedirect(redir);
                } else {
                    // Si no hay redirección previa, vamos al Inicio
                    response.sendRedirect(request.getContextPath() + "/Inicio");
                }
            } else {
                // Si falla el registro, volvemos al formulario con un error
                response.sendRedirect(request.getContextPath() + "/RegistroUsuario?error=db");
            }

        } catch (Exception e) {
            System.err.println("EXCEPCIÓN EN REGISTRO: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/RegistroUsuario?error=exception");
        }
    }
}