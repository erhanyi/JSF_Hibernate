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
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.UploadedFile;
import java.util.Collections;

/**
 * @author ERHAN
 *
 */
@ManagedBean
@ViewScoped
public class KullaniciIslemMB implements Serializable {

    private final TemelDao temelDao = new TemelDao();

    @ManagedProperty(value = "#{sessionMB}")
    private SessionMB sessionMB;

    private static final Logger log = Logger.getLogger(KullaniciIslemMB.class);

    private Kullanici yeniKullanici = new Kullanici();
    private String tcKimlikNo;
    private String sifre;
    private String ad;
    private String soyad;
    private String sifre2;
    private byte[] resim;
    private String email;
    private String resimAdi;
    

    private UploadedFile uploadedFile;
    private boolean gosterGuncelle;
    private List<Kullanici> kullaniciListesi;
    private Kullanici secilenKullanici;
    private Date tarih1;
    private Date tarih2;

    private Araba araba;
    private Araba secilenAraba;

    @PostConstruct
    public void init() {
        try {
            kullaniciListesi = temelDao.getirKullaniciListesi();
            Collections.sort(kullaniciListesi);
        } catch (Exception e) {
            MessagesController.hataVer("Sayfa çalışırken hata oluştu.");
        }
    }

    public void temizleKullaniciEkle() {
        try {
            gosterGuncelle = false;
            this.setTcKimlikNo(null);
            this.setAd(null);
            this.setSoyad(null);
            this.setResim(null);
            this.setEmail(null);
            this.setResimAdi(null);
            this.setSifre(null);
            this.setSifre2(null);
            sessionMB.setImage(null);
            this.setUploadedFile(null);

        } catch (Exception e) {
            MessagesController.hataVer("Kullanıcı verileri temizlenirken hata oluştu");
        }
    }

    public void kaydetKullanici() {
        try {
            yeniKullanici.setTcKimlikNo(this.getTcKimlikNo());
            yeniKullanici.setAd(this.getAd());
            yeniKullanici.setSoyad(this.getSoyad());
            yeniKullanici.setResim(this.getResim());
            yeniKullanici.setEmail(this.getEmail());
            yeniKullanici.setResimAdi(this.getResimAdi());            
            yeniKullanici.setSifre(this.getSifre());
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
            temelDao.silKullanici(secilenKullanici);
            kullaniciListesi.remove(secilenKullanici);
            MessagesController.bilgiVer("Kullanıcı silinmiştir.");
        } catch (Exception e) {
            MessagesController.hataVer("Kullanıcı silme işleminde hata oluştu");
        }
    }

    public void duzenle() {
        try {
            gosterGuncelle = true;
            this.setTcKimlikNo(secilenKullanici.getTcKimlikNo());
            this.setAd(secilenKullanici.getAd());
            this.setSoyad(secilenKullanici.getSoyad());
            sessionMB.setImage(new DefaultStreamedContent(new ByteArrayInputStream(secilenKullanici.getResim())));
            this.setResim(secilenKullanici.getResim());
            this.setEmail(secilenKullanici.getEmail());
            this.setResimAdi(secilenKullanici.getResimAdi());
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('dlg').show();");
        } catch (Exception e) {
            MessagesController.hataVer("Kullanıcı düzenleme işleminde hata oluştu");
        }
    }

    public void kullaniciGuncelle() {
        try {
            secilenKullanici.setSifre(this.getSifre());
            secilenKullanici.setAd(this.getAd());
            secilenKullanici.setSoyad(this.getSoyad());
            secilenKullanici.setResim(this.getResim());
            secilenKullanici.setEmail(this.getEmail());
            secilenKullanici.setResimAdi(this.getResimAdi());
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
        this.setResimAdi(this.getTcKimlikNo() + "." + extension);
        sessionMB.setImage(new DefaultStreamedContent(new ByteArrayInputStream(uploadedFile.getContents())));
        this.setResim(uploadedFile.getContents());
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

    public String getTcKimlikNo() {
        return tcKimlikNo;
    }

    public void setTcKimlikNo(String tcKimlikNo) {
        this.tcKimlikNo = tcKimlikNo;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public String getSifre2() {
        return sifre2;
    }

    public void setSifre2(String sifre2) {
        this.sifre2 = sifre2;
    }

    public byte[] getResim() {
        return resim;
    }

    public void setResim(byte[] resim) {
        this.resim = resim;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResimAdi() {
        return resimAdi;
    }

    public void setResimAdi(String resimAdi) {
        this.resimAdi = resimAdi;
    }   

    public Araba getSecilenAraba() {
        return secilenAraba;
    }

    public void setSecilenAraba(Araba secilenAraba) {
        this.secilenAraba = secilenAraba;
    }   
}
