package mundonodo.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Clase de utilidad para la gestión de cookies de la aplicación MundoNodo.
 * Centraliza la lógica de creación, lectura y eliminación de cookies, 
 * enfocándose principalmente en la persistencia del carrito de la compra 
 * para usuarios anónimos o sesiones que se restauran.
 * * @author Jose Antonio
 * @version 1.0
 */
public class Cookies {
    
    /** * Duración de la cookie de la cesta: 2 días expresados en segundos.
     */
    public static final int DURACION_CESTA = 172800;
    
    /** * Nombre identificador de la cookie en el navegador del cliente. 
     */
    public static final String NOMBRE_COOKIE = "cestaCookie";

    /**
     * Genera o sobrescribe la cookie de la cesta en el navegador del usuario.
     * Configura la persistencia para que la cookie esté disponible en todo el 
     * dominio de la aplicación y expire tras el tiempo definido en {@link #DURACION_CESTA}.
     * * @param response Objeto {@link HttpServletResponse} donde se adjuntará la cookie.
     * @param valor    Cadena de texto serializada que representa el contenido del carrito.
     */
    public static void crearCookieCesta(HttpServletResponse response, String valor) {
        Cookie c = new Cookie(NOMBRE_COOKIE, valor);
        c.setMaxAge(DURACION_CESTA);
        c.setPath("/"); // Define el alcance global dentro del contexto de la app
        response.addCookie(c);
    }

    /**
     * Localiza y recupera el contenido de la cookie de la cesta desde la petición del cliente.
     * * @param request Objeto {@link HttpServletRequest} que contiene las cookies del navegador.
     * @return {@link String} con el valor de la cookie si existe; {@code null} en caso contrario.
     */
    public static String leerCookieCesta(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals(NOMBRE_COOKIE)) {
                    return c.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Elimina la cookie de la cesta del navegador del cliente.
     * Este proceso se realiza estableciendo el tiempo de vida de la cookie (Max-Age) a 0.
     * Se recomienda invocar este método tras procesar un pedido o al realizar un login exitoso.
     * * @param response Objeto {@link HttpServletResponse} para enviar la instrucción de borrado.
     */
    public static void borrarCookieCesta(HttpServletResponse response) {
        Cookie c = new Cookie(NOMBRE_COOKIE, "");
        c.setMaxAge(0); // Instrucción de eliminación inmediata para el navegador
        c.setPath("/");
        response.addCookie(c);
    }
}