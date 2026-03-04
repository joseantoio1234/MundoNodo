package mundonodo.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CalcularLetraDNI")
public class CalcularLetraDNI extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String dniNumeros = request.getParameter("dni");
        
        if (dniNumeros != null && dniNumeros.length() == 8) {
            try {
                int numero = Integer.parseInt(dniNumeros);
                String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
                char letra = letras.charAt(numero % 23);
                
                response.setContentType("text/plain");
                response.getWriter().write(String.valueOf(letra));
            } catch (NumberFormatException e) {
                response.sendError(400, "Formato inválido");
            }
        }
    }
}