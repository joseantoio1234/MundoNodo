package mundonodo.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
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
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        Usuario u = new Usuario();
        
        try {
            // 1. OBTENER EL POOL DEL CONTEXTO
            DataSource ds = (DataSource) getServletContext().getAttribute("db_pool");

            BeanUtils.populate(u, request.getParameterMap());
            
            if(u.getCp() == null) {
                u.setCp(request.getParameter("codigo_postal"));
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

            // 3. USO DE FACTORY para obtener el DAO
            DAOFactory factoria = DAOFactory.getDAOFactory();
            UsuarioDao dao = factoria.getUsuarioDao();

            // 4. Registrar y Login automático
            if (dao.registrar(ds, u)) {
                Usuario usuarioCompleto = dao.login(ds, u.getCorreo(), u.getPassword());
                HttpSession session = request.getSession();
                session.setAttribute("usuarioLogueado", usuarioCompleto);
                
                String redir = (String) session.getAttribute("redireccionPostLogin");
                if (redir != null) {
                    out.print(redir);
                    session.removeAttribute("redireccionPostLogin");
                } else {
                    out.print("success");
                }
            } else {
                out.print("error_db");
            }

        } catch (Exception e) {
            System.err.println("EXCEPCIÓN EN REGISTRO: " + e.getMessage());
            out.print("exception");
        } finally {
            out.flush();
            out.close();
        }
    }
}