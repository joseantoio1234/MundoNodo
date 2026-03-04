package mundonodo.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet de utilidad encargado de calcular la letra correspondiente a un número de DNI.
 * Implementa el algoritmo oficial de verificación del Ministerio del Interior para 
 * proporcionar validación en tiempo real mediante peticiones asíncronas.
 * * @author Jose Antonio
 * @version 1.0
 */
@WebServlet(name = "CalcularLetraDNI", urlPatterns = {"/CalcularLetraDNI"})
public class CalcularLetraDNI extends HttpServlet {

    /**
     * Procesa la petición GET para calcular la letra del DNI.
     * Recibe una cadena de 8 dígitos, aplica el algoritmo de módulo 23 y devuelve
     * el carácter resultante en texto plano.
     * * @param request  Objeto {@link HttpServletRequest} que debe contener el parámetro 'dni'.
     * @param response Objeto {@link HttpServletResponse} que retorna la letra calculada.
     * @throws ServletException Si ocurre un error interno en el servlet.
     * @throws IOException      Si ocurre un error en el flujo de salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Obtención del parámetro que contiene únicamente los números del DNI
        String dniNumeros = request.getParameter("dni");
        
        // Validación básica: El DNI debe tener exactamente 8 caracteres numéricos
        if (dniNumeros != null && dniNumeros.length() == 8) {
            try {
                // Conversión a entero para realizar la operación aritmética
                int numero = Integer.parseInt(dniNumeros);
                
                // Cadena de asignación oficial (Algoritmo Módulo 23)
                String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
                
                // El resto de la división por 23 determina la posición de la letra
                char letra = letras.charAt(numero % 23);
                
                // Configuración de la respuesta como texto plano para consumo desde JS (AJAX)
                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(String.valueOf(letra));
                
            } catch (NumberFormatException e) {
                // Respuesta de error en caso de que el parámetro no sea un número válido
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Formato de DNI inválido");
            }
        } else {
            // Si el parámetro no cumple la longitud, no se realiza ninguna acción
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }
}