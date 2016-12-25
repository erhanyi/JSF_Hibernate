package com.erhan.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "araba")
public class Araba implements Serializable {

    @Id
    @Column(name = "arabaid", nullable = false, length = 11)    
    private String arabaId;
    @Column(name = "arabamarka", nullable = false)
    private String arabaMarka;
    @Column(name = "arabamodel", nullable = false)
    private String arabaModel;
    @Column(name = "arabarenk", nullable = false)
    private String arabaRenk;
    @ManyToOne
    @JoinColumn (name = "kullanici")
    private Kullanici kullanici;

    public String getArabaId() {
        return arabaId;
    }

    public void setArabaId(String arabaId) {
        this.arabaId = arabaId;
    }

    public String getArabaMarka() {
        return arabaMarka;
    }

    public void setArabaMarka(String arabaMarka) {
        this.arabaMarka = arabaMarka;
    }

    public String getArabaModel() {
        return arabaModel;
    }

    public void setArabaModel(String arabaModel) {
        this.arabaModel = arabaModel;
    }

    public String getArabaRenk() {
        return arabaRenk;
    }

    public void setArabaRenk(String arabaRenk) {
        this.arabaRenk = arabaRenk;
    }

    public Kullanici getKullanici() {
        return kullanici;
    }

    public void setKullanici(Kullanici kullanici) {
        this.kullanici = kullanici;
    }
    
    
    
}
