package com.erhan.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "menu")
public class Menu implements Serializable {

    @Id
    @Column(name = "MENUID", nullable = false)
    private int menuId;
    @Column(name = "MENUADI", nullable = false)
    private String menuAdi;
    @Column(name = "USTMENUID", nullable = true)
    private int ustMenuId;
    @Column(name = "ANAMENUMU", nullable = false)
    private boolean anaMenuMu;
    @Column(name = "HEDEF", nullable = true)
    private String hedef;

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getMenuAdi() {
        return menuAdi;
    }

    public void setMenuAdi(String menuAdi) {
        this.menuAdi = menuAdi;
    }

    public int getUstMenuId() {
        return ustMenuId;
    }

    public void setUstMenuId(int ustMenuId) {
        this.ustMenuId = ustMenuId;
    }

    public boolean isAnaMenuMu() {
        return anaMenuMu;
    }

    public void setAnaMenuMu(boolean anaMenuMu) {
        this.anaMenuMu = anaMenuMu;
    }    

    public String getHedef() {
        return hedef;
    }

    public void setHedef(String hedef) {
        this.hedef = hedef;
    }

}
