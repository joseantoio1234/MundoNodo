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

@WebServlet(name = "ActualizarPerfil", urlPatterns = {"/ActualizarPerfil"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, 
        maxFileSize = 1024 * 1024 * 2, 
        maxRequestSize = 1024 * 1024 * 10 
)
public class ActualizarPerfil extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/JSP/perfil.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuarioSesion == null) {
            out.print("error_session");
            return;
        }

        try {
            DataSource ds = (DataSource) getServletContext().getAttribute("db_pool");
            
            // 1. Creamos el objeto donde pondremos los datos del formulario
            Usuario uEditado = new Usuario();
            BeanUtils.populate(uEditado, request.getParameterMap());

            // 2. RELLENAR CAMPOS QUE NO VIENEN EN EL FORMULARIO O SON FIJOS
            uEditado.setIdusuario(usuarioSesion.getIdusuario());
            uEditado.setCorreo(usuarioSesion.getCorreo());
            uEditado.setDni(usuarioSesion.getDni());
            
            // Manejo manual de campos con nombres distintos 
            if(uEditado.getCp() == null) {
                uEditado.setCp(request.getParameter("codigo_postal"));
            }

            // 3. GESTIÓN DE CONTRASEÑA (Lógica de validación)
            String passActualInput = request.getParameter("passActual");
            String passNueva1 = request.getParameter("passNueva1");
            String passwordParaGuardar = usuarioSesion.getPassword();

            if (passNueva1 != null && !passNueva1.trim().isEmpty()) {
                if (passwordParaGuardar != null && !passwordParaGuardar.equals(passActualInput)) {
                    out.print("pass_error");
                    return;
                }
                passwordParaGuardar = passNueva1;
            }
            uEditado.setPassword(passwordParaGuardar);

            // 4. GESTIÓN DE AVATAR
            String nombreImagen = usuarioSesion.getAvatar(); 
            try {
                Part filePart = request.getPart("fotoAvatar");
                if (filePart != null && filePart.getSize() > 0) {
                    nombreImagen = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
                    String uploadPath = getServletContext().getRealPath("") + File.separator + "IMAGENES" + File.separator + "avatars";

                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) uploadDir.mkdirs();

                    filePart.write(uploadPath + File.separator + nombreImagen);
                }
            } catch (Exception e) {
                System.err.println("Aviso: No se procesó nueva imagen.");
            }
            uEditado.setAvatar(nombreImagen);

            // 5. LLAMADA AL DAO
            DAOFactory factoria = DAOFactory.getDAOFactory();
            UsuarioDao dao = factoria.getUsuarioDao();
            
            if (dao.actualizar(ds, uEditado)) {
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