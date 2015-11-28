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
        resp.setContentType("application/json;charset=UTF-8");
        Cookie[] currCookies = hsr.getCookies();
        if (currCookies == null) {
            /**Так записывается кука JSESSIONID**/
           hsr.getSession().getId();
        } else {
            System.out.println("not null");
        }

        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
