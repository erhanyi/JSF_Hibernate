package com.erhan.dao;

import com.erhan.model.Kullanici;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import org.hibernate.HibernateException;

/**
 *
 * @author Erhan
 */
public class SelectItemIslem {
    
    private final KullaniciDao kullaniciDao=new KullaniciDao();

    public List<SelectItem> getirKullaniciSelectItemListesi() throws Exception {
       List<SelectItem> kullaniciSelectItemList = new ArrayList<>();
        try {
            
            List<Kullanici> kullaniciList = kullaniciDao.getirKullaniciListesi();
            for (Kullanici kullanici : kullaniciList) {                
                kullaniciSelectItemList.add(new SelectItem(kullanici.getTcKimlikNo(), kullanici.getAd()));                                
            }           
        } catch (HibernateException e) {
            throw e;
        } finally {
        }
        return kullaniciSelectItemList;
    }
}
