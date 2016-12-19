package com.erhan.mb;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class NavigationBean implements Serializable {

    private static final long serialVersionUID = 1520318172495977648L;

    public String redirectToLogin() {return "/index.jsf?faces-redirect=true";}

    public String toLogin() {return "/index.jsf";}

    public String redirectToError() {return "/error.jsf?faces-redirect=true";}

    public String toError() {return "/error.jsf";}

    public String redirectToWelcome() {return "/secured/welcome.jsf?faces-redirect=true";}

    public String toWelcome() {return "/secured/welcome.jsf";}

}
