package com.erhan.mb;

import com.erhan.dao.TemelDao;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import com.erhan.util.MessagesController;
import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;

/**
 * @author ERHAN
 *
 */
@ManagedBean
@RequestScoped
public class AnaSayfaMB implements Serializable {

    private final TemelDao temelDao = new TemelDao();
   
    private Long kullaniciSayisi;

    @PostConstruct
    public void init() {
        try {
                         
            kullaniciSayisi=temelDao.getirKullaniciSayisi();            
           
        } catch (Exception e) {
            MessagesController.hataVer("Sayfa çalışırken hata oluştu.");
        }
    }  

///////////////////// Getter ve Setter ////////////////////////////////////////

    public Long getKullaniciSayisi() {
        return kullaniciSayisi;
    }

    public void setKullaniciSayisi(Long kullaniciSayisi) {
        this.kullaniciSayisi = kullaniciSayisi;
    } 
}
