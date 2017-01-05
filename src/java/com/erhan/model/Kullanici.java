package com.erhan.model;

import com.erhan.util.MD5;
import com.erhan.validator.TCKimlikNo;
import java.io.IOException;
import java.io.Serializable;
import java.util.SortedSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.apache.cxf.common.util.SortedArraySet;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SortNatural;

@Entity
@Table(name = "kullanici")
public class Kullanici implements Serializable, Comparable<Kullanici> {

    @Id
    @Column(name = "TCKIMLIKNO", nullable = false, length = 11)  
    @TCKimlikNo (message = "Geçerli bir TC Kimlik Numarası giriniz.")
    private String tcKimlikNo;
    @Column(name = "AD", nullable = false)
    private String ad;
    @Column(name = "SOYAD", nullable = false)
    private String soyad;
    @Column(name = "SIFRE", nullable = false)
    private String sifre;    
    @Column(name = "RESIM")
    private byte[] resim;  
    @Column(name = "EMAIL", nullable = false)
    private String email;
    @Column(name = "RESIMADI")
    private String resimAdi;    
    @OneToMany(mappedBy = "kullanici", fetch = FetchType.EAGER)
    @Fetch (FetchMode.SELECT)
    @SortNatural
    private SortedSet<Araba> arabaListesi=new SortedArraySet<>();

    @Override
    public int compareTo(Kullanici o) {
        return tcKimlikNo.compareTo(o.getTcKimlikNo());
    }
    
    public String getResimAdi() {
        return resimAdi;
    }

    public void setResimAdi(String resimAdi) {
        if (resimAdi!=null) {
            this.resimAdi = resimAdi.toLowerCase();
        }
    } 

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {        
        if (ad!=null) {
            this.ad = ad.toUpperCase();
        }       
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {        
        if (soyad!=null) {
            this.soyad = soyad.toUpperCase();
        }
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
        this.sifre = MD5.crypt(sifre);
    }

    public byte[] getResim() throws IOException {
        return resim;
    }

    public void setResim(byte[] resim) {
        this.resim = resim;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email!=null) {
            this.email = email.toLowerCase();
        }
    }

    public SortedSet<Araba> getArabaListesi() {
        return arabaListesi;
    }

    public void setArabaListesi(SortedSet<Araba> arabaListesi) {
        this.arabaListesi = arabaListesi;
    }     
            
    @Override
    public String toString() {
        StringBuilder strBuff = new StringBuilder();
        strBuff.append("TC Kimlik No : ").append(tcKimlikNo);
        return strBuff.toString();
    }
}
