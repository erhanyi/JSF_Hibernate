package com.erhan.mb;

import com.erhan.dao.TemelDao;
import com.erhan.model.Kullanici;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@RequestScoped
public class ImageStreamerMB implements Serializable {

    private final TemelDao kullaniciDao = new TemelDao();

    public StreamedContent getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        } else {
            String imageId = context.getExternalContext().getRequestParameterMap().get("imageId");
            if (!imageId.equals("")) {
                Kullanici kullanici = kullaniciDao.getirKullanici(imageId);
                return new DefaultStreamedContent(new ByteArrayInputStream(kullanici.getResim()));
            } else {
                return null;
            }
        }
    }
}
