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

/**
 * Servlet controlador encargado de realizar validaciones asíncronas durante el registro.
 * Permite verificar en tiempo real si datos únicos (como el email o el NIF) ya existen
 * en la base de datos, respondiendo en formato JSON para ser procesado por JavaScript.
 * * @author Jose Antonio
 * @version 1.0
 */
@WebServlet(name = "ValidarRegistroAjax", urlPatterns = {"/ValidarRegistroAjax"})
public class ValidarRegistroAjax extends HttpServlet {

    /**
     * Procesa las peticiones GET para la validación de campos únicos.
     * Recibe el nombre del campo y el valor a buscar, consulta al DAO de usuarios
     * y retorna un objeto JSON indicando la disponibilidad del dato.
     * * @param request  Objeto {@link HttpServletRequest} que contiene 'campo' y 'valor'.
     * @param response Objeto {@link HttpServletResponse} que retorna el resultado en JSON.
     * @throws ServletException Si ocurre un error interno en el contenedor.
     * @throws IOException      Si ocurre un error en el flujo de salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Configuración de la respuesta formato JSON para intercambio de datos
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 2. Obtención de parámetros enviados por la petición AJAX
        String campo = request.getParameter("campo"); 
        String valor = request.getParameter("valor");

        // Logs de depuración en consola para monitorear las peticiones en tiempo real
        System.out.println("======= VALIDACIÓN AJAX =======");
        System.out.println("CAMPO RECIBIDO: " + (campo != null ? campo : "NULL"));
        System.out.println("VALOR RECIBIDO: " + (valor != null ? "[" + valor + "]" : "NULL"));

        boolean existe = false;

        try {
            // 3. Recuperación del Pool de conexiones desde el ServletContext
            DataSource ds = (DataSource) getServletContext().getAttribute("db_pool");

            // 4. Lógica de validación: Solo se consulta si los parámetros son válidos
            if (campo != null && valor != null && !valor.trim().isEmpty()) {
                
                // Implementación del patrón Factory para obtener el DAO de Usuario
                DAOFactory factoria = DAOFactory.getDAOFactory();
                UsuarioDao dao = factoria.getUsuarioDao();
                
                // Limpieza de espacios en blanco y consulta de persistencia
                String valorLimpio = valor.trim();
                existe = dao.verificarExistencia(ds, campo, valorLimpio);
                
                System.out.println("RESULTADO EN BD PARA " + valorLimpio + ": " + existe);
            }

            // 5. Respuesta al cliente: Envío del resultado estructurado en JSON
            try (PrintWriter out = response.getWriter()) {
                out.print("{\"existe\": " + existe + "}");
                out.flush();
            }
            
        } catch (Exception e) {
            // Gestión de errores críticos: Se registra la excepción y se envía estado 500
            System.err.println(">>> ERROR CRÍTICO en ValidarRegistroAjax: " + e.getMessage());
            e.printStackTrace();
            
            if (!response.isCommitted()) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } finally {
            System.out.println("===============================");
        }
    }
}