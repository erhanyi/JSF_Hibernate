package com.erhan.mb;

import com.erhan.dao.TemelDao;
import com.erhan.model.Araba;
import com.erhan.model.Kullanici;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import com.erhan.util.MessagesController;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 * @author ERHAN
 *
 */
@ManagedBean
@ViewScoped
public class UploadDowloadFileMB implements Serializable {

    private final TemelDao kullaniciDao = new TemelDao();

    @ManagedProperty(value = "#{navigationBean}")
    private NavigationBean navigationBean;

    private Araba araba = new Araba();
    private UploadedFile uploadedFile;
    private StreamedContent dosya = null;
    private List<Araba> arabaListesi;
    private Araba secilenAraba;
    private List<Kullanici> kullaniciListesi;

    private List<String> documentTypeList;

    @PostConstruct
    public void init() {
        try {

            documentTypeList = new ArrayList<>();
            documentTypeList.add("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); //XLXS
            documentTypeList.add("application/vnd.openxmlformats-officedocument.wordprocessingml.document"); //DOCX
            documentTypeList.add("application/vnd.ms-excel"); //XLS
            documentTypeList.add("application/msword"); //DOC
            documentTypeList.add("application/pdf"); //PDF
            documentTypeList.add("image/gif"); //GIF
            documentTypeList.add("image/jpeg"); //JPEG
            documentTypeList.add("image/png"); //PNG

            arabaListesi = kullaniciDao.getirArabaListesi();
            kullaniciListesi = kullaniciDao.getirKullaniciListesi();
        } catch (Exception e) {
            MessagesController.hataVer("Sayfa çalışırken hata oluştu.");
        }
    }

    public void startUploadToDatabase(FileUploadEvent event) {
        try {
            uploadedFile = event.getFile();
            Tika tika = new Tika();
            String dosyaTuru = tika.detect(uploadedFile.getContents());
            if (documentTypeList.contains(dosyaTuru)) {

                String extension = FilenameUtils.getExtension(event.getFile().getFileName());
                araba.setDosyaAdi("deneme." + extension);
                dosya = new DefaultStreamedContent(new ByteArrayInputStream(uploadedFile.getContents()));
                this.getAraba().setDosya(uploadedFile.getContents());
                //   System.gc();
            } else {
                MessagesController.hataVer("Yüklemeye çalıştığınız dosya türü yüklenemez. ("+dosyaTuru+")");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void startDownloadFromDatabase() throws IOException {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.setResponseHeader("Content-Type", externalContext.getMimeType(secilenAraba.getDosyaAdi()));
            externalContext.setResponseHeader("Content-Disposition", "attachment;filename=\"" + secilenAraba.getDosyaAdi() + "\"");
            try (OutputStream output = externalContext.getResponseOutputStream()) {
                output.write(secilenAraba.getDosya());
            }
            facesContext.responseComplete();
            //    System.gc();
        } catch (IOException e) {
            throw e;
        }
    }

    public void kaydetAraba() {
        try {

            kullaniciDao.kaydetObje(araba);
            arabaListesi = kullaniciDao.getirArabaListesi();
            temizleAraba();
            MessagesController.bilgiVer("Araba kaydedildi.");

        } catch (Exception e) {
            MessagesController.hataVer("Araba kaydedilemedi.");
        }
    }

    public void sil() {
        try {
            kullaniciDao.silObje(secilenAraba);
            arabaListesi.remove(secilenAraba);
            MessagesController.bilgiVer("Araba silinmiştir.");
        } catch (Exception e) {
            MessagesController.hataVer("Araba silme işleminde hata oluştu");
        }
    }

    public void temizleAraba() {
        try {

            araba.setArabaId(null);
            araba.setArabaMarka(null);
            araba.setArabaModel(null);
            araba.setArabaRenk(null);
            araba.setDosya(null);
            araba.setDosyaAdi(null);
            araba.setKullanici(new Kullanici());

        } catch (Exception e) {
            MessagesController.hataVer("Kullanıcı verileri temizlenirken hata oluştu");
        }
    }

    public NavigationBean getNavigationBean() {
        return navigationBean;
    }

    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
    }

    public Araba getAraba() {
        return araba;
    }

    public void setAraba(Araba araba) {
        this.araba = araba;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public StreamedContent getDosya() {
        return dosya;
    }

    public void setDosya(StreamedContent dosya) {
        this.dosya = dosya;
    }

    public List<Araba> getArabaListesi() {
        return arabaListesi;
    }

    public void setArabaListesi(List<Araba> arabaListesi) {
        this.arabaListesi = arabaListesi;
    }

    public Araba getSecilenAraba() {
        return secilenAraba;
    }

    public void setSecilenAraba(Araba secilenAraba) {
        this.secilenAraba = secilenAraba;
    }

    public List<Kullanici> getKullaniciListesi() {
        return kullaniciListesi;
    }

    public void setKullaniciListesi(List<Kullanici> kullaniciListesi) {
        this.kullaniciListesi = kullaniciListesi;
    }

    public TemelDao getKullaniciDao() {
        return kullaniciDao;
    }
}
