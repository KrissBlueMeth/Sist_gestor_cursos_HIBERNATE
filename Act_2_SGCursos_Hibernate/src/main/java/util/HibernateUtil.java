package util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

    // La factoría de sesiones se inicializa una sola vez.
    private static final SessionFactory sessionFactory = buildSessionFactory();

    //Metodo para construir la SessionFactory
    private static SessionFactory buildSessionFactory() {
        try {
            // Carga la configuración desde hibernate.cfg.xml
            Configuration configuration = new Configuration().configure();

            // Construye el registro de servicios necesario para la SessionFactory
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();

            // Construye la SessionFactory
            return configuration.buildSessionFactory(serviceRegistry);

        } catch (Throwable ex) {
            // Manejo de errores si falla la creación (ej. BD caída, configuración errónea)
            System.err.println("La creación inicial de SessionFactory falló." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    //MEOTODO estático para obtener la SessionFactory
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    // 3. Método para cerrar la factoría (cuando se detiene la aplicación)
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}