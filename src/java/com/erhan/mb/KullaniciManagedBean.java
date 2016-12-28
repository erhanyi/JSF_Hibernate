package com.erhan.mb;

import com.erhan.dao.KullaniciDao;
import com.erhan.dao.SelectItemIslem;
import com.erhan.model.Araba;
import java.io.Serializable;
import java.net.Inet4Address;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import com.erhan.model.Kullanici;
import com.erhan.model.Menu;
import com.erhan.util.MessagesController;
import com.erhan.validator.TCKimlikNo;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 * @author ERHAN
 *
 */
@ManagedBean(name = "kullaniciManagedBean")
@SessionScoped
public class KullaniciManagedBean implements Serializable {

    private final KullaniciDao kullaniciDao = new KullaniciDao();

    @ManagedProperty(value = "#{navigationBean}")
    private NavigationBean navigationBean;

    private static final Logger log = Logger.getLogger(KullaniciManagedBean.class);
    private String tcKimlikNo;
    private String sifre;

    @TCKimlikNo(message = "Geçerli bir TC Kimlik Numarası giriniz.")
    private String tcKimlikNoYeni;
    private String sifreYeni;
    private String adYeni;
    private String soyadYeni;
    private String sifre2Yeni;
    private byte[] resimYeni;
    private String emailYeni;
    private String resimAdiYeni;
    private UploadedFile uploadedFile;
    private StreamedContent image = null;
    private StreamedContent imageKullanici = null;
    private boolean gosterGuncelle;
    private boolean loggedIn;
    private List<Kullanici> kullaniciListesi;
    private Kullanici kullanici;
    private Kullanici secilenKullanici;
    private static List<Menu> menuListesi;
    private MenuModel menuModel;
    private DefaultSubMenu altMenu;
    
    private Date tarih1;
    private Date tarih2;

    private Araba araba;

//    @PostConstruct
//    public void init() {
//        try {
//            MessagesController.bilgiVer("Sayfa çalıştı.");
//        } catch (Exception e) {
//            MessagesController.hataVer("Sayfa çalışırken hata oluştu.");
//        }
//    }
    public String giris() {
        String result = null;
        try {
            kullanici = new Kullanici();
            kullanici.setTcKimlikNo(this.getTcKimlikNo());
            kullanici.setSifre(this.getSifre());

            if (kullaniciDao.girisKontrol(kullanici)) {
                log.info("Giriş : " + kullanici.getTcKimlikNo()
                        + " " + Inet4Address.getLocalHost()
                        + " giriş yaptı.");
                loggedIn = true;
                kullanici = kullaniciDao.getirObje(Kullanici.class, kullanici.getTcKimlikNo());
                kullaniciListesi = kullaniciDao.getirKullaniciListesi();
                araba = new Araba();
                araba = kullaniciDao.getirAraba("1");
                System.out.println(araba.getKullanici().getAd());
                getirMenu();
                result = navigationBean.redirectToWelcome();
            } else {
                MessagesController.hataVer("Kullanıcı adı veya şifre hatalı!");
            }
        } catch (Exception e) {
            MessagesController.hataVer("Kullanıcı kontrol edilirken hata oluştu");
        }
        return result;
    }  

    public void sil() {
        try {
            kullaniciDao.silObje(secilenKullanici);
            kullaniciListesi.remove(secilenKullanici);
            MessagesController.bilgiVer("Kullanıcı silinmiştir.");
        } catch (Exception e) {
            MessagesController.hataVer("Kullanıcı silme işleminde hata oluştu");
        }
    }

    public void duzenle() {
        try {
            gosterGuncelle = true;
            this.setTcKimlikNoYeni(secilenKullanici.getTcKimlikNo());
            this.setAdYeni(secilenKullanici.getAd());
            this.setSoyadYeni(secilenKullanici.getSoyad());
            this.setImage(new DefaultStreamedContent(new ByteArrayInputStream(secilenKullanici.getResim())));
            this.setResimYeni(secilenKullanici.getResim());
            this.setEmailYeni(secilenKullanici.getEmail());
            this.setResimAdiYeni(secilenKullanici.getResimAdi());
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('dlg').show();");
        } catch (Exception e) {
            MessagesController.hataVer("Kullanıcı düzenleme işleminde hata oluştu");
        }
    }

    public String cikis() {
        try {
            return navigationBean.redirectToLogin();
        } catch (Exception e) {
            MessagesController.hataVer("Çıkış işleminde hata oluştu");
            return null;
        }
    }

    public void temizleKullaniciEkle() {
        try {
            gosterGuncelle = false;
            this.setTcKimlikNoYeni(null);
            this.setAdYeni(null);
            this.setSoyadYeni(null);
            this.setSifreYeni(null);
            this.setSifre2Yeni(null);
            this.setImage(null);
            this.setResimYeni(null);
            this.setEmailYeni(null);
            this.setResimAdiYeni(null);
            this.setUploadedFile(null);

        } catch (Exception e) {
            MessagesController.hataVer("Kullanıcı verileri temizlenirken hata oluştu");
        }
    }

    public void kullaniciEkle() {
        try {
            Kullanici user = new Kullanici();
            user.setTcKimlikNo(this.getTcKimlikNoYeni());
            user.setSifre(this.getSifreYeni());
            user.setAd(this.getAdYeni().toUpperCase());
            user.setSoyad(this.getSoyadYeni().toUpperCase());
            user.setResim(this.getResimYeni());
            user.setEmail(this.getEmailYeni());
            user.setResimAdi(this.getTcKimlikNoYeni());
            kullaniciDao.kaydetObje(user);
            log.debug("Kaydetme : " + user + " kaydedildi.");
            kullaniciListesi.add(user);
            temizleKullaniciEkle();
            MessagesController.bilgiVer("Kullanıcı eklenmiştir.");
            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('dlg').hide();");
        } catch (Exception e) {
            MessagesController.hataVer("Kullanıcı ekleme işleminde hata oluştu");
        }
    }

    public void kullaniciGuncelle() {
        try {
            secilenKullanici.setSifre(this.getSifreYeni());
            secilenKullanici.setAd(this.getAdYeni());
            secilenKullanici.setSoyad(this.getSoyadYeni());
            secilenKullanici.setResim(this.getResimYeni());
            secilenKullanici.setEmail(this.getEmailYeni());
            secilenKullanici.setResimAdi(this.getResimAdiYeni());
            kullaniciDao.kaydetVeyaGuncelleObje(secilenKullanici);
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
        this.setResimAdiYeni(this.getTcKimlikNoYeni() + "." + extension);
        image = new DefaultStreamedContent(new ByteArrayInputStream(uploadedFile.getContents()));
        this.setResimYeni(uploadedFile.getContents());
    }

///////Menüleri dinamik olarak getirme işlemleri yapılıyor.///////////
    private void getirMenu() {
        try {
            menuListesi = new ArrayList<>();
            menuModel = new DefaultMenuModel();
            menuListesi = kullaniciDao.getirMenuListesiUstMenuyeGore(0);
            for (Menu menu : menuListesi) {
                if (menu.isAnaMenuMu()) {
                    DefaultSubMenu sMenu = new DefaultSubMenu(menu.getMenuAdi());
                    altMenu = getirAltMenu(menu.getMenuId(), sMenu);
                }
                menuModel.addElement(altMenu);
            }
        } catch (Exception e) {
            MessagesController.hataVer("Menüleri getirme işleminde hata oluştu");
        }
    }

    private DefaultSubMenu getirAltMenu(int id, DefaultSubMenu subMenu) throws Exception {

        try {
            menuListesi = kullaniciDao.getirMenuListesiUstMenuyeGore(id);
            for (Menu menu : menuListesi) {
                if (menu.isAnaMenuMu()) {
                    DefaultSubMenu sMenu = new DefaultSubMenu(menu.getMenuAdi());
                    this.getirAltMenu(menu.getMenuId(), sMenu);
                    subMenu.addElement(sMenu);
                } else {
                    DefaultMenuItem item = new DefaultMenuItem(menu.getMenuAdi());
                    item.setUrl(menu.getHedef() + "?faces-redirect=true");
                    item.setUpdate("form:content");
                    item.setPartialSubmit(true);
                    subMenu.addElement(item);
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return subMenu;
    }
///////////////////////////////////////////////////////////////////////////////

    public void startUploadToDatabase(FileUploadEvent event) {
        uploadedFile = event.getFile();
        String extension = FilenameUtils.getExtension(event.getFile().getFileName());
        this.setResimAdiYeni(this.getTcKimlikNo() + "." + extension);
        image = new DefaultStreamedContent(new ByteArrayInputStream(uploadedFile.getContents()));
        this.setResimYeni(uploadedFile.getContents());
    }

    public void startDownloadFromDatabase() throws IOException {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.setResponseHeader("Content-Type", externalContext.getMimeType(kullanici.getResimAdi()));
            externalContext.setResponseHeader("Content-Disposition", "attachment;filename=\"" + kullanici.getResimAdi() + "\"");
            try (OutputStream output = externalContext.getResponseOutputStream()) {
                output.write(kullanici.getResim());
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

        this.setResimAdiYeni(file.getFileName().toString());
    }

    public void startDownloadFromDisk() throws IOException {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            File file = new File("D://files//" + kullanici.getResimAdi());
            externalContext.setResponseHeader("Content-Type", externalContext.getMimeType(kullanici.getResimAdi()));
            externalContext.setResponseHeader("Content-Disposition", "attachment;filename=\"" + kullanici.getResimAdi() + "\"");

            OutputStream output = externalContext.getResponseOutputStream();
            Files.copy(file.toPath(), output);

            facesContext.responseComplete();

        } catch (IOException e) {
            throw e;
        }
    }

///////////////////// Getter ve Setter ////////////////////////////////////////
    public Kullanici getKullanici() {
        return kullanici;
    }

    public void setKullanici(Kullanici kullanici) {
        this.kullanici = kullanici;
    }

    public List<Kullanici> getKullaniciListesi() {
        return kullaniciListesi;
    }

    public void setKullaniciListesi(List<Kullanici> kullaniciListesi) {
        this.kullaniciListesi = kullaniciListesi;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public NavigationBean getNavigationBean() {
        return navigationBean;
    }

    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
    }

    public static List<Menu> getMenuListesi() {
        return menuListesi;
    }

    public static void setMenuListesi(List<Menu> menuListesi) {
        KullaniciManagedBean.menuListesi = menuListesi;
    }

    public MenuModel getMenuModel() {
        return menuModel;
    }

    public void setMenuModel(MenuModel menuModel) {
        this.menuModel = menuModel;
    }

    public DefaultSubMenu getAltMenu() {
        return altMenu;
    }

    public void setAltMenu(DefaultSubMenu altMenu) {
        this.altMenu = altMenu;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public StreamedContent getImage() {
        return image;
    }

    public void setImage(StreamedContent image) {
        this.image = image;
    }

    public String getSifreYeni() {
        return sifreYeni;
    }

    public void setSifreYeni(String sifreYeni) {
        this.sifreYeni = sifreYeni;
    }

    public String getAdYeni() {
        return adYeni;
    }

    public void setAdYeni(String adYeni) {
        this.adYeni = adYeni;
    }

    public String getSoyadYeni() {
        return soyadYeni;
    }

    public void setSoyadYeni(String soyadYeni) {
        this.soyadYeni = soyadYeni;
    }

    public String getSifre2Yeni() {
        return sifre2Yeni;
    }

    public void setSifre2Yeni(String sifre2Yeni) {
        this.sifre2Yeni = sifre2Yeni;
    }

    public byte[] getResimYeni() {
        return resimYeni;
    }

    public void setResimYeni(byte[] resimYeni) {
        this.resimYeni = resimYeni;
    }

    public Kullanici getSecilenKullanici() {
        return secilenKullanici;
    }

    public void setSecilenKullanici(Kullanici secilenKullanici) {
        this.secilenKullanici = secilenKullanici;
    }

    public String getEmailYeni() {
        return emailYeni;
    }

    public void setEmailYeni(String emailYeni) {
        this.emailYeni = emailYeni;
    }

    public String getTcKimlikNo() {
        return tcKimlikNo;
    }

    public void setTcKimlikNo(String tcKimlikNo) {
        this.tcKimlikNo = tcKimlikNo;
    }

    public String getTcKimlikNoYeni() {
        return tcKimlikNoYeni;
    }

    public void setTcKimlikNoYeni(String tcKimlikNoYeni) {
        this.tcKimlikNoYeni = tcKimlikNoYeni;
    }

    public boolean isGosterGuncelle() {
        return gosterGuncelle;
    }

    public void setGosterGuncelle(boolean gosterGuncelle) {
        this.gosterGuncelle = gosterGuncelle;
    }

    public String getResimAdiYeni() {
        return resimAdiYeni;
    }

    public void setResimAdiYeni(String resimAdiYeni) {
        this.resimAdiYeni = resimAdiYeni;
    }

    public StreamedContent getImageKullanici() throws IOException {
        if (kullanici.getResim() != null) {
            return new DefaultStreamedContent(new ByteArrayInputStream(kullanici.getResim()));
        } else {
            return null;
        }
    }

    public void setImageKullanici(StreamedContent imageKullanici) {
        this.imageKullanici = imageKullanici;
    }

    public Araba getAraba() {
        return araba;
    }

    public void setAraba(Araba araba) {
        this.araba = araba;
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
}
