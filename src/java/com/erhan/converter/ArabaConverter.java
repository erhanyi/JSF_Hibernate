package com.erhan.converter;

import com.erhan.model.Araba;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

//@FacesConverter(value = "classeConverter")    
@FacesConverter(forClass = Araba.class)
public class ArabaConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        if (value != null && !value.isEmpty()) {
            return (Araba) uiComponent.getAttributes().get(value);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value instanceof Araba) {
            Araba entity = (Araba) value;
            if (entity instanceof Araba && entity.getArabaId()!= null) {
                uiComponent.getAttributes().put(entity.getArabaId().toString(), entity);
                return entity.getArabaId().toString();
            }
        }
        return "";
    }
}