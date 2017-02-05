package com.erhan.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tree")
public class Tree implements Serializable {

    @Id
    @Column(name = "TREEID", nullable = false)
    private int treeId;
    @Column(name = "TREEADI", nullable = false)
    private String treeAdi;
    @Column(name = "USTTREEID", nullable = true)
    private int ustTreeId;

    public int getTreeId() {
        return treeId;
    }

    public void setTreeId(int treeId) {
        this.treeId = treeId;
    }

    public String getTreeAdi() {
        return treeAdi;
    }

    public void setTreeAdi(String treeAdi) {
        this.treeAdi = treeAdi;
    }

    public int getUstTreeId() {
        return ustTreeId;
    }

    public void setUstTreeId(int ustTreeId) {
        this.ustTreeId = ustTreeId;
    }

    

}
