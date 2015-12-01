package Filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Создано Span 25.11.2015.
 */
@WebFilter(filterName = "Access", urlPatterns = {"/phonebk/*"})
public class Access implements Filter {
    public void destroy() {
    }
///TODO: Разобраться, почему при первом запуске, при нажатии на вход, пропускает до получения контактов!
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("Filter!");
        HttpServletRequest hsr = (HttpServletRequest) req;
        //HttpServletResponse hsresp = (HttpServletResponse) resp;
        resp.setContentType("application/json;charset=UTF-8");
        Cookie[] currCookies = hsr.getCookies();
        if (currCookies == null) {
            /*String sessionID = UUID.randomUUID().toString();
            Cookie cuk = new Cookie("SESSIONID",sessionID);
            cuk.setMaxAge(600);
            cuk.setPath("/pb");
            cuk.setHttpOnly(true);
            hsresp.addCookie(cuk);*/
            /**Так записывается кука JSESSIONID**/
           hsr.getSession().getId();
            hsr.getServletContext().getSessionCookieConfig().setHttpOnly(true);
            hsr.getServletContext().getSessionCookieConfig().setMaxAge(300);
            hsr.getServletContext().getSessionCookieConfig().setPath("/pb");


        } else {
            /*for(Cookie cuk:currCookies) {
                if(cuk.getName().equals("SESSIONID"))
                    System.out.println("SESS = "+cuk.getValue());
                cuk.setMaxAge(600);
                cuk.setPath("/pb/");
                cuk.setHttpOnly(true);
                hsresp.addCookie(cuk);
            }*/
            System.out.println("not null");
        }

        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
