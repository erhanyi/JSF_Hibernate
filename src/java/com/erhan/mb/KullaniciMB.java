package com.erhan.mb;

import com.erhan.dao.TemelDao;
import com.erhan.model.Araba;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import com.erhan.model.Kullanici;
import com.erhan.util.MessagesController;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.commons.io.FilenameUtils;
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
public class KullaniciMB implements Serializable {

    private final TemelDao temelDao = new TemelDao();

    @ManagedProperty(value = "#{sessionMB}")
    private SessionMB sessionMB;

    private static final Logger log = Logger.getLogger(KullaniciMB.class);

    private Kullanici yeniKullanici = new Kullanici();
    private String sifreYeni;
    private String sifre2Yeni;
    private StreamedContent image=null;

    private UploadedFile uploadedFile;
    private boolean gosterGuncelle;
    private List<Kullanici> kullaniciListesi;
    private Kullanici secilenKullanici;
    private Date tarih1;
    private Date tarih2;

    private Araba araba;

    @PostConstruct
    public void init() {
        try {
            kullaniciListesi = temelDao.getirKullaniciListesi();
        } catch (Exception e) {
            MessagesController.hataVer("Sayfa çalışırken hata oluştu.");
        }
    }

    public void temizleKullaniciEkle() {
        try {
            gosterGuncelle = false;
            yeniKullanici.setTcKimlikNo(null);
            yeniKullanici.setAd(null);
            yeniKullanici.setSoyad(null);            
            yeniKullanici.setResim(null);
            yeniKullanici.setEmail(null);
            yeniKullanici.setResimAdi(null);
            this.setSifreYeni(null);
            this.setSifre2Yeni(null);
            this.setImage(null);
            this.setUploadedFile(null);          
            
        } catch (Exception e) {
            MessagesController.hataVer("Kullanıcı verileri temizlenirken hata oluştu");
        }
    }

    public void kaydetKullanici() {
        try {
            yeniKullanici.setSifre(sifreYeni);
            temelDao.kaydetObje(yeniKullanici);
            log.debug("Kaydetme : " + yeniKullanici + " kaydedildi.");
            kullaniciListesi.add(yeniKullanici);
            temizleKullaniciEkle();
            MessagesController.bilgiVer("Kullanıcı eklenmiştir.");
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('dlg').hide();");
        } catch (Exception e) {
            MessagesController.hataVer("Kullanıcı ekleme işleminde hata oluştu");
        }
    }

    public void sil() {
        try {
            temelDao.silObje(secilenKullanici);
            kullaniciListesi.remove(secilenKullanici);
            MessagesController.bilgiVer("Kullanıcı silinmiştir.");
        } catch (Exception e) {
            MessagesController.hataVer("Kullanıcı silme işleminde hata oluştu");
        }
    }

    public void duzenle() {
        try {
            gosterGuncelle = true;
            yeniKullanici.setTcKimlikNo(secilenKullanici.getTcKimlikNo());
            yeniKullanici.setAd(secilenKullanici.getAd());
            yeniKullanici.setSoyad(secilenKullanici.getSoyad());
            this.setImage(new DefaultStreamedContent(new ByteArrayInputStream(secilenKullanici.getResim())));
            yeniKullanici.setResim(secilenKullanici.getResim());
            yeniKullanici.setEmail(secilenKullanici.getEmail());
            yeniKullanici.setResimAdi(secilenKullanici.getResimAdi());
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('dlg').show();");
        } catch (Exception e) {
            MessagesController.hataVer("Kullanıcı düzenleme işleminde hata oluştu");
        }
    }

    public void kullaniciGuncelle() {
        try {
            secilenKullanici.setSifre(yeniKullanici.getSifre());
            secilenKullanici.setAd(yeniKullanici.getAd());
            secilenKullanici.setSoyad(yeniKullanici.getSoyad());
            secilenKullanici.setResim(yeniKullanici.getResim());
            secilenKullanici.setEmail(yeniKullanici.getEmail());
            secilenKullanici.setResimAdi(yeniKullanici.getResimAdi());
            temelDao.kaydetVeyaGuncelleObje(secilenKullanici);
            log.debug("Güncelleme : " + secilenKullanici + "güncellendi.");
            temizleKullaniciEkle();
            MessagesController.bilgiVer("Kullanıcı bilgileri güncellenmiştir.");
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('dlg').hide();");
        } catch (Exception e) {
            MessagesController.hataVer("Kullanıcı güncelleme işleminde hata oluştu");
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        uploadedFile = event.getFile();
        String extension = FilenameUtils.getExtension(event.getFile().getFileName());
        yeniKullanici.setResimAdi(yeniKullanici.getTcKimlikNo() + "." + extension);
        this.setImage(new DefaultStreamedContent(new ByteArrayInputStream(uploadedFile.getContents())));
        yeniKullanici.setResim(uploadedFile.getContents());
    }

///////////////////////////////////////////////////////////////////////////////
    public void startUploadToDatabase(FileUploadEvent event) {
        uploadedFile = event.getFile();
        String extension = FilenameUtils.getExtension(event.getFile().getFileName());
        yeniKullanici.setResimAdi(sessionMB.getKullanici().getTcKimlikNo() + "." + extension);
        this.setImage(new DefaultStreamedContent(new ByteArrayInputStream(uploadedFile.getContents())));
        yeniKullanici.setResim(uploadedFile.getContents());
    }

    public void startDownloadFromDatabase() throws IOException {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.setResponseHeader("Content-Type", externalContext.getMimeType(sessionMB.getKullanici().getResimAdi()));
            externalContext.setResponseHeader("Content-Disposition", "attachment;filename=\"" + sessionMB.getKullanici().getResimAdi() + "\"");
            try (OutputStream output = externalContext.getResponseOutputStream()) {
                output.write(sessionMB.getKullanici().getResim());
            }
            facesContext.responseComplete();
        } catch (IOException e) {
            throw e;
        }
    }

    public void startUploadToDisk(FileUploadEvent event) throws IOException {
        uploadedFile = event.getFile();
        Path folder = Paths.get("D://files//");
        String filename = FilenameUtils.getBaseName(uploadedFile.getFileName());
        String extension = FilenameUtils.getExtension(uploadedFile.getFileName());

        Path file = Files.createTempFile(folder, filename, "." + extension);

        try (InputStream input = uploadedFile.getInputstream()) {
            Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
        }

        yeniKullanici.setResimAdi(file.getFileName().toString());
    }

    public void startDownloadFromDisk() throws IOException {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            File file = new File("D://files//" + sessionMB.getKullanici().getResimAdi());
            externalContext.setResponseHeader("Content-Type", externalContext.getMimeType(sessionMB.getKullanici().getResimAdi()));
            externalContext.setResponseHeader("Content-Disposition", "attachment;filename=\"" + sessionMB.getKullanici().getResimAdi() + "\"");

            OutputStream output = externalContext.getResponseOutputStream();
            Files.copy(file.toPath(), output);

            facesContext.responseComplete();

        } catch (IOException e) {
            throw e;
        }
    }

///////////////////// Getter ve Setter ////////////////////////////////////////
    public SessionMB getSessionMB() {
        return sessionMB;
    }

    public void setSessionMB(SessionMB sessionMB) {
        this.sessionMB = sessionMB;
    }

    public Kullanici getYeniKullanici() {
        return yeniKullanici;
    }

    public void setYeniKullanici(Kullanici yeniKullanici) {
        this.yeniKullanici = yeniKullanici;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public boolean isGosterGuncelle() {
        return gosterGuncelle;
    }

    public void setGosterGuncelle(boolean gosterGuncelle) {
        this.gosterGuncelle = gosterGuncelle;
    }

    public List<Kullanici> getKullaniciListesi() {
        return kullaniciListesi;
    }

    public void setKullaniciListesi(List<Kullanici> kullaniciListesi) {
        this.kullaniciListesi = kullaniciListesi;
    }

    public Kullanici getSecilenKullanici() {
        return secilenKullanici;
    }

    public void setSecilenKullanici(Kullanici secilenKullanici) {
        this.secilenKullanici = secilenKullanici;
    }

    public Date getTarih1() {
        return tarih1;
    }

    public void setTarih1(Date tarih1) {
        this.tarih1 = tarih1;
    }

    public Date getTarih2() {
        return tarih2;
    }

    public void setTarih2(Date tarih2) {
        this.tarih2 = tarih2;
    }

    public Araba getAraba() {
        return araba;
    }

    public void setAraba(Araba araba) {
        this.araba = araba;
    }

    public String getSifreYeni() {
        return sifreYeni;
    }

    public void setSifreYeni(String sifreYeni) {
        this.sifreYeni = sifreYeni;
    }

    public String getSifre2Yeni() {
        return sifre2Yeni;
    }

    public void setSifre2Yeni(String sifre2Yeni) {
        this.sifre2Yeni = sifre2Yeni;
    }

    public StreamedContent getImage() {
        return image;
    }

    public void setImage(StreamedContent image) {
        this.image = image;
    }

}
