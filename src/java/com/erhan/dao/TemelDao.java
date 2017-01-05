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
        } catch (Exception e) {
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
            
            kullanici = (Kullanici) session.get(Kullanici.class, tcKimlikNo);

        } catch (Exception e) {
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
            
            kullaniciListesi = session.createCriteria(Kullanici.class).list();
            
        } catch (HibernateException e) {
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
            Query query = session.createQuery("from Menu where ustMenuId = :ustMenuId ");
            query.setParameter("ustMenuId", ustMenuId);
            menuListesi = query.list();
            return menuListesi;
        } catch (Exception e) {
            throw e;
        } finally {
            session.close();
        }
    }
    
    public Araba getirAraba(Integer id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Araba araba;
        try {
            
            araba = (Araba) session.get(Araba.class, id);
            
        } catch (Exception e) {
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

            arabaListesi = session.createCriteria(Araba.class).list();

        } catch (HibernateException e) {            
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
            kullaniciSayisi = (Long) session.createCriteria(Kullanici.class)
                    .setProjection(Projections.rowCount()).uniqueResult();
        } catch (Exception e) {
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
