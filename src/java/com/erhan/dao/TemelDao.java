package com.erhan.dao;

import com.erhan.model.Araba;
import com.erhan.model.Kullanici;
import com.erhan.model.Menu;
import com.erhan.util.HibernateUtil;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

/**
 *
 * @author Erhan
 */
public class TemelDao extends Dao {
    
    public boolean girisKontrol(Kullanici kullanici) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean girisKabul = false;
        try {
            session.beginTransaction();
            List<Kullanici> objs = session.createCriteria(Kullanici.class)
                    .list();
            
            if ((objs != null) && (objs.size() > 0)) {
                
                for (Kullanici veritabani : objs) {
                    girisKabul = veritabani.getTcKimlikNo().equals(
                            kullanici.getTcKimlikNo())
                            && veritabani.getSifre().equals(
                                    kullanici.getSifre());
                    
                    if (girisKabul) {
                        return girisKabul;
                    }
                }
            }
            session.getTransaction().rollback();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
        return girisKabul;
    }
    
    public Kullanici getirKullanici(String tcKimlikNo) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Kullanici kullanici;
        try {
            session.beginTransaction();
            kullanici = (Kullanici) session.get(Kullanici.class, tcKimlikNo);
            session.getTransaction().rollback();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
        return kullanici;
    }
    
    public List<Kullanici> getirKullaniciListesi() throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Kullanici> kullaniciListesi;
        try {
            session.beginTransaction();
            kullaniciListesi = session.createCriteria(Kullanici.class).list();
            session.getTransaction().rollback();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
        return kullaniciListesi;
    }
    
    public List<Menu> getirMenuListesiUstMenuyeGore(int ustMenuId) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Menu> menuListesi;
        try {
            session.beginTransaction();
            Query query = session.createQuery("from Menu where ustMenuId = :ustMenuId ");
            query.setParameter("ustMenuId", ustMenuId);
            menuListesi = query.list();
            session.getTransaction().rollback();
            return menuListesi;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
    
    public Araba getirAraba(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Araba araba;
        try {
            session.beginTransaction();
            araba = (Araba) session.get(Araba.class, id);
            session.getTransaction().rollback();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
        return araba;
    }
    
    public List<Araba> getirArabaListesi() throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Araba> arabaListesi;
        try {
            session.beginTransaction();
            arabaListesi = session.createCriteria(Araba.class).list();
            session.getTransaction().rollback();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
        return arabaListesi;
    }
    
    public Long getirKullaniciSayisi() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Long kullaniciSayisi;
        try {
            session.beginTransaction();
            kullaniciSayisi = (Long) session.createCriteria(Kullanici.class)
                    .setProjection(Projections.rowCount()).uniqueResult();
            session.getTransaction().rollback();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
        return kullaniciSayisi;
    }
    
    public void silKullanici(Kullanici kullanici) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            
            if (kullanici.getArabaListesi() != null 
                    && kullanici.getArabaListesi().size() > 0) {
                
                for (Araba araba : kullanici.getArabaListesi()) {
                    session.delete(araba);
                }
            }
            
            session.delete(kullanici);
            
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
