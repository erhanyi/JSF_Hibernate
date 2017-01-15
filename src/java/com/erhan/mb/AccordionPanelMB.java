package com.erhan.mb;

import com.erhan.dao.TemelDao;
import com.erhan.model.Araba;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import com.erhan.util.MessagesController;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;

/**
 * @author ERHAN
 *
 */
@ManagedBean
@RequestScoped
public class AccordionPanelMB implements Serializable {

    private final TemelDao temelDao = new TemelDao();

    private List<Araba> arabaListesi;

    @PostConstruct
    public void init() {
        try {

            arabaListesi = temelDao.getirArabaListesi();

            for (Araba araba : arabaListesi) {
                araba.setArabaListesi(new ArrayList<>());

                if (araba.getArabaMarka().equals("HONDA")) {
                    araba.getArabaListesi().addAll(arabaListesi);
                }
            }

        } catch (Exception e) {
            MessagesController.hataVer("Sayfa çalışırken hata oluştu.");
        }
    }

///////////////////// Getter ve Setter ////////////////////////////////////////
    public List<Araba> getArabaListesi() {
        return arabaListesi;
    }

    public void setArabaListesi(List<Araba> arabaListesi) {
        this.arabaListesi = arabaListesi;
    }

}
