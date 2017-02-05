package com.erhan.mb;

import com.erhan.dao.TemelDao;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import org.apache.log4j.Logger;
import com.erhan.model.Tree;
import com.erhan.util.MessagesController;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 * @author ERHAN
 *
 */
@ManagedBean
@RequestScoped
public class TreeSelectionMB implements Serializable {

    private final TemelDao temelDao = new TemelDao();
    private static final Logger log = Logger.getLogger(TreeSelectionMB.class);
    private static List<Tree> treeListesi;
    private TreeNode root;
    private TreeNode selectedNode;

    @PostConstruct
    public void init() {
        try {
            getirTree();
        } catch (Exception e) {
            MessagesController.hataVer("Sayfa çalışırken hata oluştu.");
        }
    }

///////Menüleri dinamik olarak getirme işlemleri yapılıyor.///////////
    private void getirTree() {
        try {
            treeListesi = new ArrayList<>();
            root = new DefaultTreeNode("Root", null);
            treeListesi = temelDao.getirTreeListesiUstTreeyeGore(0);
            for (Tree tree : treeListesi) {
                TreeNode node1 = new DefaultTreeNode(tree.getTreeAdi(), root);
                node1.setSelectable(false);
                List<Tree> altTreeList = temelDao.getirTreeListesiUstTreeyeGore(tree.getTreeId());
                if (altTreeList != null && altTreeList.size() > 0) {
                    for (Tree treeAlt : altTreeList) {
                        TreeNode node2 = new DefaultTreeNode(treeAlt.getTreeAdi(), node1);
//                        if (treeAlt.getTreeId() == 2) {
//                            node2.setSelectable(false);
//                        }
                        List<Tree> alt1TreeList = temelDao.getirTreeListesiUstTreeyeGore(treeAlt.getTreeId());
                        if (alt1TreeList != null && alt1TreeList.size() > 0) {
                            for (Tree treeAlt1 : alt1TreeList) {
                                TreeNode node3 = new DefaultTreeNode(treeAlt1.getTreeAdi(), node2);

                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            MessagesController.hataVer("Menüleri getirme işleminde hata oluştu");
        }
    }

///////////////////////////////////////////////////////////////////////////////
///////////////////// Getter ve Setter ////////////////////////////////////////

    public static List<Tree> getTreeListesi() {
        return treeListesi;
    }

    public static void setTreeListesi(List<Tree> treeListesi) {
        TreeSelectionMB.treeListesi = treeListesi;
    }    

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

}
