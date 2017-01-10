package com.erhan.mb;

import com.erhan.dao.TemelDao;
import java.io.Serializable;
import java.net.Inet4Address;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.log4j.Logger;
import com.erhan.model.Kullanici;
import com.erhan.model.Menu;
import com.erhan.util.MessagesController;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 * @author ERHAN
 *
 */
@ManagedBean
@SessionScoped
public class SessionMB implements Serializable {

    private final TemelDao temelDao = new TemelDao();

    @ManagedProperty(value = "#{navigationBean}")
    private NavigationBean navigationBean;

    private static final Logger log = Logger.getLogger(SessionMB.class);
    private Kullanici kullanici = new Kullanici();
    private boolean loggedIn;
    private static List<Menu> menuListesi;
    private MenuModel menuModel;
    private DefaultSubMenu altMenu;
    private StreamedContent imageKullanici = null;
    private StreamedContent image = null;

    private boolean devemEtKontrol=false;

    public String giris() {
        String result = null;
        try {
            if (temelDao.girisKontrol(kullanici)) {
                log.info("Giriş : " + kullanici.getTcKimlikNo()
                        + " " + Inet4Address.getLocalHost()
                        + " giriş yaptı.");
                loggedIn = true;
                kullanici = temelDao.getirObje(Kullanici.class, kullanici.getTcKimlikNo());
                getirMenu();

                if (devemEtKontrol) {
                    devemEtKontrol=false;
                    return FacesContext.getCurrentInstance().getViewRoot().getViewId();
                } else {
                    result = navigationBean.redirectToWelcome();
                }
            } else {
                MessagesController.hataVer("Kullanıcı adı veya şifre hatalı!");
            }
        } catch (Exception e) {
            MessagesController.hataVer("Kullanıcı kontrol edilirken hata oluştu");
        }
        return result;
    }

    public String cikis() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            System.gc();
            return navigationBean.redirectToLogin();
        } catch (Exception e) {
            MessagesController.hataVer("Çıkış işleminde hata oluştu");
            return null;
        }
    }

    public void devamEt() {
        try {
            devemEtKontrol=true;
            giris();
        } catch (Exception e) {
            MessagesController.hataVer("Devam etme işleminde hata oluştu");
        }
    }

///////Menüleri dinamik olarak getirme işlemleri yapılıyor.///////////
    private void getirMenu() {
        try {
            menuListesi = new ArrayList<>();
            menuModel = new DefaultMenuModel();
            menuListesi = temelDao.getirMenuListesiUstMenuyeGore(0);
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
            menuListesi = temelDao.getirMenuListesiUstMenuyeGore(id);
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
///////////////////// Getter ve Setter ////////////////////////////////////////
    public NavigationBean getNavigationBean() {
        return navigationBean;
    }

    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
    }

    public Kullanici getKullanici() {
        return kullanici;
    }

    public void setKullanici(Kullanici kullanici) {
        this.kullanici = kullanici;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public static List<Menu> getMenuListesi() {
        return menuListesi;
    }

    public static void setMenuListesi(List<Menu> menuListesi) {
        SessionMB.menuListesi = menuListesi;
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

    public StreamedContent getImage() {
        return image;
    }

    public void setImage(StreamedContent image) {
        this.image = image;
    }

}
