package mundonodo.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource; 
import mundonodo.daofactory.DAOFactory; 
import mundonodo.dao.UsuarioDao;

@WebServlet(name = "ValidarRegistroAjax", urlPatterns = {"/ValidarRegistroAjax"})
public class ValidarRegistroAjax extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Forzar el tipo de respuesta JSON inmediatamente
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 2. Obtener parámetros de la URL
        String campo = request.getParameter("campo"); 
        String valor = request.getParameter("valor");

        System.out.println("======= VALIDACIÓN AJAX =======");
        System.out.println("CAMPO RECIBIDO: " + (campo != null ? campo : "NULL"));
        System.out.println("VALOR RECIBIDO: " + (valor != null ? "[" + valor + "]" : "NULL"));

        boolean existe = false;

        try {
            // 3. RECUPERAR EL POOL DEL CONTEXTO 
            DataSource ds = (DataSource) getServletContext().getAttribute("db_pool");

            // 4. Validar seguridad
            if (campo != null && valor != null && !valor.trim().isEmpty()) {
                
                // USO DE FACTORY
                DAOFactory factoria = DAOFactory.getDAOFactory();
                UsuarioDao dao = factoria.getUsuarioDao();
                
                String valorLimpio = valor.trim();
                
                // Llamada al DAO pasando el DataSource 
                existe = dao.verificarExistencia(ds, campo, valorLimpio);
                
                System.out.println("RESULTADO EN BD PARA " + valorLimpio + ": " + existe);
            }

            // 5. Enviar la respuesta JSON al navegador
            try (PrintWriter out = response.getWriter()) {
                out.print("{\"existe\": " + existe + "}");
                out.flush();
            }
            
        } catch (Exception e) {
            System.err.println("ERROR CRÍTICO en ValidarRegistroAjax: " + e.getMessage());
            e.printStackTrace();
            
            if (!response.isCommitted()) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } finally {
            System.out.println("===============================");
        }
    }
}