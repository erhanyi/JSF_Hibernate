package com.erhan.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class MessagesController {  
	  
    public static void bilgiVer(String info) {  
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, info, "Bilgi Mesajı"));  
    }  
  
    public static void uyariVer(String warn) {  
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, warn, "Uyarı Mesajı"));  
    }  
  
    public static void hataVer(String error) {  
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, error, "Hata Mesajı"));  
    }  
  
    public static void OlumculHataVer(String fatal) {  
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, fatal, "Ölümcül Hata Mesajı"));  
    }  
}