package com.erhan.mb;

import com.erhan.dao.KullaniciDao;
import com.erhan.model.Kullanici;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@RequestScoped
public class ImageStreamer implements Serializable{

    private final KullaniciDao kullaniciDao = new KullaniciDao();

    public StreamedContent getImage() throws Exception {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        } else {
            String imageId = context.getExternalContext().getRequestParameterMap().get("imageId");
            Kullanici kullanici = kullaniciDao.getirKullanici(imageId);
            return new DefaultStreamedContent(new ByteArrayInputStream(kullanici.getResim()));
        }
    }
}
