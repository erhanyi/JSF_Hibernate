package com.erhan.util;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.erhan.mb.KullaniciManagedBean;

public class LoginFilter implements Filter {

    /**
     * Checks if user is logged in. If not it redirects to the index.xhtml page.
     *
     * @param request
     * @param response
     * @param chain
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Get the kullaniciManagedBean from session attribute
        request.setCharacterEncoding("UTF-8");
        KullaniciManagedBean kullaniciManagedBean = (KullaniciManagedBean) ((HttpServletRequest) request).getSession().getAttribute("kullaniciManagedBean");

        // For the first application request there is no kullaniciManagedBean in the session so user needs to log in
        // For other requests loginBean is present but we need to check if user has logged in successfully
        if (kullaniciManagedBean == null || !kullaniciManagedBean.isLoggedIn()) {
            String contextPath = ((HttpServletRequest) request).getContextPath();
            ((HttpServletResponse) response).sendRedirect(contextPath + "/index.jsf");
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub		
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub		
    }
}
