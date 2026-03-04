package mundonodo.listeners;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

@WebListener
public class AppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            // 1. Obtenemos el contexto inicial de nombres
            Context initContext = new InitialContext();
            
            // 2. Buscamos el subcontexto de entorno 
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            
            DataSource ds = (DataSource) envContext.lookup("jdbc/mundo_nodo");

            if (ds != null) {
                // Guardamos el pool en el contexto de la aplicación
                sce.getServletContext().setAttribute("db_pool", ds);
                System.out.println(">>> [SUCCESS] Pool JNDI 'jdbc/mundo_nodo' encontrado y guardado.");
            } else {
                System.err.println(">>> [ERROR] El DataSource recuperado es NULO.");
            }

        } catch (NamingException e) {
            System.err.println(">>> [CRITICAL] Error JNDI en AppListener: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().removeAttribute("db_pool");
        System.out.println(">>> [INFO] Contexto destruido y pool liberado.");
    }
}