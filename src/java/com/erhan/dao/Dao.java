package com.erhan.dao;

import com.erhan.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Erhan
 */
public abstract class Dao {

    public void kaydetObje(Object object) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.save(object);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void kaydetVeyaGuncelleObje(Object object) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.saveOrUpdate(object);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void silObje(Object object) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.delete(object);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void kaydetObjeList(List<Object> objectList) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            for (Object object : objectList) {
                session.save(object);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void kaydetVeyaGuncelleObjeList(List<Object> objectList) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            for (Object object : objectList) {
                session.saveOrUpdate(object);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public <T> T getirObje(Class<T> c, String id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        T result;
        try {
            result = c.cast(session.get(c, id));
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
        return result;
    }
}
