package com.erhan.model;

import com.erhan.util.MD5;
import java.io.IOException;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "kullanici")
public class Kullanici implements Serializable {

    @Id
    @Column(name = "TCKIMLIKNO", nullable = false, length = 11)    
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

    public String getResimAdi() {
        return resimAdi;
    }

    public void setResimAdi(String resimAdi) {
        this.resimAdi = resimAdi;
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
        this.email = email;
    }
    
    @Override
    public String toString() {
        StringBuilder strBuff = new StringBuilder();
        strBuff.append("TC Kimlik No : ").append(tcKimlikNo);
        return strBuff.toString();
    }
}
