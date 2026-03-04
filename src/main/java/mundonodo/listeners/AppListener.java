package mundonodo.listeners;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

/**
 * Listener del ciclo de vida de la aplicación MundoNodo.
 * Se ejecuta automáticamente al iniciar y detener el servidor web.
 * Su responsabilidad principal es realizar el Lookup JNDI para inicializar
 * el Pool de conexiones (DataSource) y dejarlo disponible en el ámbito global 
 * de la aplicación (ServletContext).
 * * @author Jose Antonio
 * @version 1.0
 */
@WebListener
public class AppListener implements ServletContextListener {

    /**
     * Se ejecuta cuando la aplicación web se está iniciando.
     * Establece la comunicación con el servicio de nombres del servidor (JNDI),
     * recupera el recurso de base de datos definido en el context.xml y lo guarda
     * como un atributo de contexto bajo el nombre "db_pool".
     * * @param sce Evento que contiene el contexto de la aplicación (ServletContext).
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            // 1. Obtenemos el contexto inicial de nombres del servidor
            Context initContext = new InitialContext();
            
            // 2. Buscamos el subcontexto de entorno estándar de Java EE (java:/comp/env)
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            
            // 3. Recuperamos el DataSource configurado mediante el nombre JNDI 'jdbc/mundo_nodo'
            DataSource ds = (DataSource) envContext.lookup("jdbc/mundo_nodo");

            if (ds != null) {
                // Guardamos el pool en el contexto de la aplicación para que sea accesible
                // desde cualquier Servlet mediante getServletContext().getAttribute("db_pool")
                sce.getServletContext().setAttribute("db_pool", ds);
                System.out.println(">>> [SUCCESS] Pool JNDI 'jdbc/mundo_nodo' encontrado y guardado en Context.");
            } else {
                System.err.println(">>> [ERROR] El DataSource recuperado es NULO. Revisa la configuración de Tomcat.");
            }

        } catch (NamingException e) {
            System.err.println(">>> [CRITICAL] Error JNDI en AppListener: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Se ejecuta cuando la aplicación web se detiene o se reinicia.
     * Limpia los recursos del contexto para asegurar un apagado limpio de la aplicación.
     * * @param sce Evento que contiene el contexto de la aplicación.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Eliminamos la referencia al pool para evitar fugas de memoria (Memory Leaks)
        sce.getServletContext().removeAttribute("db_pool");
        System.out.println(">>> [INFO] Contexto destruido y referencia al pool liberada.");
    }
}