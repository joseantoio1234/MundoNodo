package mundonodo.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Cookies {
    // 2 días en segundos: 2 * 24 * 60 * 60
    public static final int DURACION_CESTA = 172800;
    public static final String NOMBRE_COOKIE = "cestaCookie";

    /**
     * Crea o actualiza la cookie de la cesta
     */
    public static void crearCookieCesta(HttpServletResponse response, String valor) {
        Cookie c = new Cookie(NOMBRE_COOKIE, valor);
        c.setMaxAge(DURACION_CESTA);
        c.setPath("/"); // Disponible en toda la aplicación
        response.addCookie(c);
    }

    /**
     * Recupera el valor de la cookie si existe
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
     * Borra la cookie (se usa al loguearse)
     */
    public static void borrarCookieCesta(HttpServletResponse response) {
        Cookie c = new Cookie(NOMBRE_COOKIE, "");
        c.setMaxAge(0); // Elimina la cookie inmediatamente
        c.setPath("/");
        response.addCookie(c);
    }
}
