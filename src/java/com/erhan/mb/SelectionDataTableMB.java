package com.erhan.mb;

import com.erhan.dao.TemelDao;
import com.erhan.model.Araba;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import org.apache.log4j.Logger;
import com.erhan.util.MessagesController;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 * @author ERHAN
 *
 */
@ManagedBean
@ViewScoped
public class SelectionDataTableMB implements Serializable {

    private final TemelDao temelDao = new TemelDao();

    private static final Logger log = Logger.getLogger(SelectionDataTableMB.class);
    private List<Araba> arabaListesi;
    private List<Araba> secilenArabaListesi = new ArrayList<>();
    private Araba secilenAraba;

    @PostConstruct
    public void init() {
        try {

            arabaListesi = temelDao.getirArabaListesi();

        } catch (Exception e) {
            MessagesController.hataVer("Sayfa çalışırken hata oluştu.");
        }
    }

    public void secAraba() {
        try {
            if (secilenAraba != null && !secilenAraba.equals("")) {
                if (secilenArabaListesi != null && secilenArabaListesi.size() > 0
                        && (secilenArabaListesi.contains(secilenAraba))) {
                } else {
                    secilenArabaListesi.add(secilenAraba);
                }
            }

        } catch (Exception e) {
            MessagesController.hataVer("Araba seçilemedi.");
        }
    }

    public void toogleSelect(ToggleSelectEvent event) {
        try {
            if (secilenArabaListesi.isEmpty() && secilenAraba != null) {

                secilenArabaListesi.add(secilenAraba);
                MessagesController.bilgiVer("Araba kaldırılamaz.");
            }
        } catch (Exception e) {
            MessagesController.hataVer("Araba seçilemedi.");
        }
    }

    public void onRowUnselect(UnselectEvent event) {
        try {
            if (secilenAraba != null && !secilenAraba.equals("")) {

                if (secilenAraba == (Araba) event.getObject()) {

                    secilenArabaListesi.add((Araba) event.getObject());
                    MessagesController.bilgiVer("Araba kaldırılamaz.");
                }
            }
        } catch (Exception e) {
            MessagesController.hataVer("Araba seçilemedi.");
        }
    }

    public List<Araba> getArabaListesi() {
        return arabaListesi;
    }

    public void setArabaListesi(List<Araba> arabaListesi) {
        this.arabaListesi = arabaListesi;
    }

    public List<Araba> getSecilenArabaListesi() {
        return secilenArabaListesi;
    }

    public void setSecilenArabaListesi(List<Araba> secilenArabaListesi) {
        this.secilenArabaListesi = secilenArabaListesi;
    }

    public Araba getSecilenAraba() {
        return secilenAraba;
    }

    public void setSecilenAraba(Araba secilenAraba) {
        this.secilenAraba = secilenAraba;
    }

}
