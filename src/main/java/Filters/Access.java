package Filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * ??????? Span 25.11.2015.
 */
@WebFilter(filterName = "Access", urlPatterns = {"/phonebk/*"})
public class Access implements Filter {
    public void destroy() {
    }
///TODO: ???????????, ?????? ??? ?????? ???????, ??? ??????? ?? ????, ?????????? ?? ????????? ?????????!
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("Filter!");
        HttpServletRequest hsr = (HttpServletRequest) req;
        resp.setContentType("application/json;charset=UTF-8");
        Cookie[] currCookies = hsr.getCookies();
        if (currCookies == null) {
            /**??? ???????????? ???? JSESSIONID**/
           hsr.getSession().getId();
            hsr.getServletContext().getSessionCookieConfig().setPath("/pb/");
            hsr.getServletContext().getSessionCookieConfig().setMaxAge(120);
            hsr.getServletContext().getSessionCookieConfig().setHttpOnly(true);
        } else {
            System.out.println("not null");
        }

        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
