package mundonodo.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
 * Servlet controlador encargado de la gestión del perfil de usuario.
 * Proporciona funcionalidades para la visualización del formulario de edición
 * y el procesamiento de cambios, incluyendo la actualización de contraseñas
 * y la subida de imágenes de avatar al servidor.
 * * @author Jose Antonio
 * @version 1.0
 */
@WebServlet(name = "ActualizarPerfil", urlPatterns = {"/ActualizarPerfil"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1,  
        maxFileSize = 1024 * 1024 * 2,       
        maxRequestSize = 1024 * 1024 * 10    
)
public class ActualizarPerfil extends HttpServlet {
    
    /**
     * Gestiona las peticiones GET para mostrar la vista del perfil.
     * * @param request  Objeto {@link HttpServletRequest}.
     * @param response Objeto {@link HttpServletResponse}.
     * @throws ServletException Si ocurre un error en el despacho.
     * @throws IOException Si ocurre un error.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/JSP/perfil.jsp").forward(request, response);
    }

    /**
     * Procesa las peticiones POST para actualizar los datos del usuario.
     * Implementa lógica de validación de contraseña, mapeo automático de campos mediante BeanUtils
     * y almacenamiento físico de archivos de imagen en el servidor.
     * * @param request  Objeto {@link HttpServletRequest} con los datos del formulario (Multipart).
     * @param response Objeto {@link HttpServletResponse} que devuelve códigos de estado en texto plano.
     * @throws ServletException Si ocurre un error específico del servlet.
     * @throws IOException Si ocurre un error de lectura/escritura.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Configuración para permitir caracteres especiales y respuesta AJAX
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuarioLogueado");

        // Control de acceso: Verificar si el usuario ha perdido la sesión
        if (usuarioSesion == null) {
            out.print("error_session");
            return;
        }

        try {
            DataSource ds = (DataSource) getServletContext().getAttribute("db_pool");
            
            // 1. Mapeo Automático: BeanUtils puebla el objeto Usuario con los nombres de los inputs del HTML
            Usuario uEditado = new Usuario();
            BeanUtils.populate(uEditado, request.getParameterMap());

            // 2. Normalización de campos: Asegurar que los datos fijos o con nombres distintos se mantengan
            uEditado.setIdusuario(usuarioSesion.getIdusuario());
            uEditado.setCorreo(usuarioSesion.getCorreo());
            uEditado.setDni(usuarioSesion.getDni());
            
            if(uEditado.getCp() == null) {
                uEditado.setCp(request.getParameter("codigo_postal"));
            }

            // 3. Lógica de Seguridad para Cambio de Contraseña
            String passActualInput = request.getParameter("passActual");
            String passNueva1 = request.getParameter("passNueva1");
            String passwordParaGuardar = usuarioSesion.getPassword();

            if (passNueva1 != null && !passNueva1.trim().isEmpty()) {
                // Verificar que la contraseña actual proporcionada coincida con la almacenada
                if (passwordParaGuardar != null && !passwordParaGuardar.equals(passActualInput)) {
                    out.print("pass_error");
                    return;
                }
                passwordParaGuardar = passNueva1;
            }
            uEditado.setPassword(passwordParaGuardar);

            // 4. Procesamiento de la imagen del Avatar
            String nombreImagen = usuarioSesion.getAvatar(); 
            try {
                Part filePart = request.getPart("fotoAvatar");
                if (filePart != null && filePart.getSize() > 0) {
                    // Crear un nombre único basado en timestamp para evitar conflictos.
                    nombreImagen = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
                    
                    // Definir ruta física de guardado en el servidor
                    String uploadPath = getServletContext().getRealPath("") + File.separator + "IMAGENES" + File.separator + "avatar";

                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) uploadDir.mkdirs();

                    filePart.write(uploadPath + File.separator + nombreImagen);
                }
            } catch (Exception e) {
                System.err.println("Aviso: No se procesó nueva imagen o el formato no es válido.");
            }
            uEditado.setAvatar(nombreImagen);

            // 5. Persistencia y Actualización de Sesión
            DAOFactory factoria = DAOFactory.getDAOFactory();
            UsuarioDao dao = factoria.getUsuarioDao();
            
            if (dao.actualizar(ds, uEditado)) {
                // Actualizamos el objeto en sesión para que los cambios se reflejen en el Header inmediatamente
                session.setAttribute("usuarioLogueado", uEditado);
                out.print("success");
            } else {
                out.print("error_db");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("exception");
        }
    }
}