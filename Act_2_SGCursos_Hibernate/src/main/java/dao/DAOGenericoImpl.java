package dao;

import util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class DAOGenericoImpl<T, ID extends Serializable> implements DAOGenerico<T, ID> {

    private final Class<T> persistentClass;


    @SuppressWarnings("unchecked")

    public DAOGenericoImpl() {

        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()

                .getGenericSuperclass()).getActualTypeArguments()[0];

    }


//CREATE/SAVE (Usamos persist para guardar la entidad) ---

    @Override

    public void save(T entity) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(entity); //Guardarmos

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) transaction.rollback();

            e.printStackTrace();

        }

    }
// Esta variacion del metodo deberia solucionar el IllelagStateException, pero tampcoo funciona
//    @Override
//    public void save(T entity) {
//        Session session = null; //
//        Transaction transaction = null;
//
//        try {
//            session = HibernateUtil.getSessionFactory().openSession();
//            transaction = session.beginTransaction();
//
//            session.persist(entity);
//
//            transaction.commit();
//        } catch (Exception e) {
//            if (transaction != null) {
//                transaction.rollback(); //Si falla, el rollback se intenta ANTES de cerrar
//            }
//            e.printStackTrace();
//        } finally {
//            // La sesión se cierra solo al final, asegurando que se gestionó la transacción.
//            if (session != null && session.isOpen()) {
//                session.close();
//            }
//        }
//    }


//READ/GET BY ID (Usamos find para obtener la entidad)---

    @Override

    public T getById(ID id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.find(persistentClass, id); // Leer

        } catch (Exception e) {

            e.printStackTrace();

            return null;

        }

    }


//READ/GET ALL (Usamos HQL para obtener toda la lista) ---

    @Override

    public List<T> getAll() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

//HQL (Hibernate Query Language) es independiente del tipo de DB

// 'from T se refiere al nombre de la clase de la entidad

            return session.createQuery("from " + persistentClass.getSimpleName(), persistentClass).list();

        } catch (Exception e) {

            e.printStackTrace();

            return null;

        }

    }


// UPDATE (Usamos merge para actualizar una entidad desasociada) ---

    @Override

    public void update(T entity) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.merge(entity); //NNos aseguramos de que la entidad se actualiza

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) transaction.rollback();

            e.printStackTrace();

        }

    }


//DELETE (Primero buscamos y luego eliminamos) ---

    @Override

    public void delete(ID id) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            T entity = session.find(persistentClass, id); // Primero buscar la entidad

            if (entity != null) {

                session.remove(entity); // Eliminar la entidad

            }

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) transaction.rollback();

            e.printStackTrace();

        }

    }

}