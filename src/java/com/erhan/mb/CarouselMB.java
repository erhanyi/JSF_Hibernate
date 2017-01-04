package com.erhan.mb;

import com.erhan.dao.TemelDao;
import com.erhan.model.Araba;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import org.apache.log4j.Logger;
import com.erhan.util.MessagesController;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;

/**
 * @author ERHAN
 *
 */
@ManagedBean
@ViewScoped
public class CarouselMB implements Serializable {

    private final TemelDao temelDao = new TemelDao();

    private static final Logger log = Logger.getLogger(CarouselMB.class);
    private List<Araba> arabaListesi;

    @PostConstruct
    public void init() {
        try {
            
            arabaListesi = temelDao.getirArabaListesi();
            
        } catch (Exception e) {
            MessagesController.hataVer("Sayfa çalışırken hata oluştu.");
        }
    }

    public List<Araba> getArabaListesi() {
        return arabaListesi;
    }

    public void setArabaListesi(List<Araba> arabaListesi) {
        this.arabaListesi = arabaListesi;
    }

}
