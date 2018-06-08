package data;

import org.hibernate.Session;

import java.io.Serializable;

public class ORMHelper<T> {
    /*public T GetById(Class entityClass, Serializable entityId) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        T entity = (T) session.get(entityClass, entityId);
        session.close();
        return entity;
    }

    public Serializable Save(T entity) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Serializable identifier = session.save(entity);
        session.close();
        return identifier;
    }

    public void SaveOrUpdate(T entity) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        session.saveOrUpdate(entity);
        session.close();
    }*/
}
