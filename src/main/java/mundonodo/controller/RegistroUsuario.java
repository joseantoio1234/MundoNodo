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

/**
 * Servlet controlador encargado del registro de nuevos usuarios en la plataforma.
 * Gestiona la recepción de datos personales, la subida opcional de un avatar 
 * y realiza un inicio de sesión automático tras la creación exitosa de la cuenta.
 * * @author Jose Antonio
 * @version 1.0
 */
@WebServlet(name = "RegistroUsuario", urlPatterns = {"/RegistroUsuario"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1,  // 1MB
    maxFileSize = 1024 * 1024 * 2,       // 2MB
    maxRequestSize = 1024 * 1024 * 10    // 10MB
)
public class RegistroUsuario extends HttpServlet {
    
    /**
     * Muestra el formulario de registro de usuario.
     * * @param request  Objeto {@link HttpServletRequest}.
     * @param response Objeto {@link HttpServletResponse}.
     * @throws ServletException Si ocurre un error en el despacho a la vista.
     * @throws IOException Si ocurre un error.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/JSP/registro.jsp").forward(request, response);
    }
    
    /**
     * Procesa los datos del formulario de registro.
     * Utiliza BeanUtils para el mapeo de campos, procesa la subida de imágenes
     * y gestiona la sesión del usuario recién creado para mejorar la experiencia de navegación.
     * * @param request  Objeto {@link HttpServletRequest} con los datos del nuevo usuario.
     * @param response Objeto {@link HttpServletResponse} para la redirección final.
     * @throws ServletException Si ocurre un error en el procesamiento.
     * @throws IOException      Si ocurre un error de comunicación.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Aseguramos la codificación para soportar caracteres internacionales
        request.setCharacterEncoding("UTF-8");

        Usuario u = new Usuario();
        
        try {
            DataSource ds = (DataSource) getServletContext().getAttribute("db_pool");

            // 1. Mapeo automático de parámetros del formulario al objeto DTO
            BeanUtils.populate(u, request.getParameterMap());
            
            // Ajuste manual para el código postal en caso de discrepancia de nombres en el JSP
            if(u.getCp() == null || u.getCp().isEmpty()) {
                u.setCp(request.getParameter("cp"));
            }

            // 2. Gestión del Avatar: Si el usuario no sube una foto, se asigna 'pinguavatar.png' por defecto
            Part filePart = request.getPart("fotoAvatar");
            String nombreImagen = "pinguavatar.png"; 

            if (filePart != null && filePart.getSize() > 0) {
                String fileNameOriginal = filePart.getSubmittedFileName();
                // Generamos un nombre único para evitar sobreescritura de archivos
                nombreImagen = System.currentTimeMillis() + "_" + fileNameOriginal;

                // Definición de la ruta física de almacenamiento
                String uploadPath = getServletContext().getRealPath("") + File.separator + "IMAGENES" + File.separator + "avatars";
                
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdirs();

                filePart.write(uploadPath + File.separator + nombreImagen);
            }
            u.setAvatar(nombreImagen); 

            // 3. Persistencia mediante el patrón DAO
            DAOFactory factoria = DAOFactory.getDAOFactory();
            UsuarioDao dao = factoria.getUsuarioDao();

            if (dao.registrar(ds, u)) {
                // Registro exitoso: Procedemos con el Login Automático
                // Recuperamos el usuario de la DB para obtener el ID autogenerado y el Timestamp de acceso
                Usuario usuarioCompleto = dao.login(ds, u.getCorreo(), u.getPassword());
                HttpSession session = request.getSession();
                session.setAttribute("usuarioLogueado", usuarioCompleto);
                
                // 4. Redirección Inteligente:
                // Si el usuario venía de intentar comprar sin estar logueado, lo devolvemos al carrito.
                String redir = (String) session.getAttribute("redireccionPostLogin");
                if (redir != null) {
                    session.removeAttribute("redireccionPostLogin");
                    response.sendRedirect(redir);
                } else {
                    response.sendRedirect(request.getContextPath() + "/Inicio");
                }
            } else {
                // Fallo en la inserción (ej: correo duplicado no validado previamente)
                response.sendRedirect(request.getContextPath() + "/RegistroUsuario?error=db");
            }

        } catch (Exception e) {
            System.err.println(">>> EXCEPCIÓN EN REGISTRO_USUARIO: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/RegistroUsuario?error=exception");
        }
    }
}