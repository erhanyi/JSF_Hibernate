package com.erhan.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "araba")
public class Araba implements Serializable, Comparable<Araba> {

    @Id
    @Column(name = "arabaid", nullable = false)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "arabaidseq")
//    @SequenceGenerator(name = "arabaidseq", sequenceName = "arabaidseq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer arabaId;
    @Column(name = "arabamarka", nullable = false)
    private String arabaMarka;
    @Column(name = "arabamodel", nullable = false)
    private String arabaModel;
    @Column(name = "arabarenk", nullable = false)
    private String arabaRenk;
    @ManyToOne
    @JoinColumn(name = "kullanici")
    private Kullanici kullanici;

    @Column(name = "dosya")
    private byte[] dosya;

    @Column(name = "dosyaAdi")
    private String dosyaAdi;
    
    @Transient
    private List<Araba> arabaListesi;

    @Override
    public int compareTo(Araba o) {
        return arabaMarka.compareTo(o.getArabaMarka());
    }

    public Integer getArabaId() {
        return arabaId;
    }

    public void setArabaId(Integer arabaId) {
        this.arabaId = arabaId;
    }

    public String getArabaMarka() {
        return arabaMarka;
    }

    public void setArabaMarka(String arabaMarka) {
        if (arabaMarka != null) {
            this.arabaMarka = arabaMarka.toUpperCase();
        } else {
            this.arabaMarka = arabaMarka;
        }
    }

    public String getArabaModel() {
        return arabaModel;
    }

    public void setArabaModel(String arabaModel) {
        if (arabaModel != null) {
            this.arabaModel = arabaModel.toUpperCase();
        } else {
            this.arabaModel = arabaModel;
        }
    }

    public String getArabaRenk() {
        return arabaRenk;
    }

    public void setArabaRenk(String arabaRenk) {
        if (arabaRenk != null) {
            this.arabaRenk = arabaRenk.toUpperCase();
        } else {
            this.arabaRenk = arabaRenk;
        }
    }

    public Kullanici getKullanici() {
        return kullanici;
    }

    public void setKullanici(Kullanici kullanici) {
        this.kullanici = kullanici;
    }

    public byte[] getDosya() {
        return dosya;
    }

    public void setDosya(byte[] dosya) {
        this.dosya = dosya;
    }

    public String getDosyaAdi() {
        return dosyaAdi;
    }

    public void setDosyaAdi(String dosyaAdi) {
        this.dosyaAdi = dosyaAdi;
    }
   
    public List<Araba> getArabaListesi() {
        return arabaListesi;
    }

    public void setArabaListesi(List<Araba> arabaListesi) {
        this.arabaListesi = arabaListesi;
    }

}
