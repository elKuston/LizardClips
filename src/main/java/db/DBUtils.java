package db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DBUtils {
    public static final String PERSISTENCE_UNIT_NAME = "lizardClipsPersistenceUnit";

    private static EntityManager em;

    /**
     * EntityManagerSingleton
     *
     * @return The entity manager
     */
    public static EntityManager getEntityManager() {
        if (em == null) {
            EntityManagerFactory factory =
                    Persistence.createEntityManagerFactory(DBUtils.PERSISTENCE_UNIT_NAME);
            em = factory.createEntityManager();
        }

        return em;
    }
}
