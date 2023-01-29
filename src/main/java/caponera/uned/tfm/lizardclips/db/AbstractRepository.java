package caponera.uned.tfm.lizardclips.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public abstract class AbstractRepository<T> {
    protected static EntityManager em;
    protected static EntityTransaction currentTransaction;


    public AbstractRepository() {
        if (em == null) {
            em = DBUtils.getEntityManager();
        }
    }

    public static void startTransaction() {
        currentTransaction = em.getTransaction();
        currentTransaction.begin();
        System.out.println("Transaction started");
    }

    public static void endTransaction() {
        currentTransaction.commit();
        System.out.println("Transaction ended");
    }

    protected abstract Integer getIdElemento(T elemento);

    protected abstract Class<T> getObjectClass();

    public List<T> getAll() {
        //Query q = em.createQuery(getAllQuery());
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(getObjectClass());
        Root<T> rootEntry = cq.from(getObjectClass());
        CriteriaQuery<T> all = cq.select(rootEntry);
        TypedQuery<T> allQuery = em.createQuery(all);
        List<T> result = allQuery.getResultList();
        for (T t : result) {
            em.detach(t);
        }
        return result;
        //return (List<T>) q.getResultList();
    }

    public T guardar(T t) {
        System.out.println("Guardando " + t.getClass().getSimpleName() + ": " + t);
        //startTransaction();
        try {
            if (getIdElemento(t) == null) {
                em.persist(t);
            } else {
                em.merge(t);
                em.flush();
            }
            //endTransaction();
            System.out.println(t.getClass().getSimpleName() + " guardado");
        } catch (Exception e) {
            e.printStackTrace();
            currentTransaction.rollback();
        }
        return t;

    }
}
