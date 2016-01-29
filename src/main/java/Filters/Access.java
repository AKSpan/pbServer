package Filters;

import org.json.JSONObject;
import utils.GetDataFromRequest;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;


@WebFilter(filterName = "Access", urlPatterns = {"/phonebk/*"})
public class Access implements Filter {
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Filter init!");
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("doFilter!");
        HttpServletRequest request = (HttpServletRequest) req;
        resp.setContentType("application/json;charset=UTF-8");
        String action;
        RequestDispatcher dispatcher;
        JSONObject user;
        JSONObject json = GetDataFromRequest.getJSON(req.getReader());
        req.setAttribute("json", json);
        action = json.optString("action", "");
        System.out.println("ACTION = " + action);
        try {
            switch (action) {
                case "login":
                case "registration":
                    String page = (action.equals("login")) ? "/Login" : "/Registration";

                    dispatcher = req.getServletContext().getRequestDispatcher(page);
                    user = json.optJSONObject("user");
                    if (user != null) {
                        req.setAttribute("user", user.getString("user"));
                        req.setAttribute("pass", user.getString("pass"));
                        dispatcher.forward(req, resp);
                    } else {
                        //если есть кука - кинуть на страницу логина и там смотреть пускать или нет
                        System.out.println("user == null");
                        String session = null;
                        Cookie[] cookies = ((HttpServletRequest) req).getCookies();
                        if (cookies != null)
                            for (Cookie cookie : cookies)
                                if (cookie.getName().equals("JSESSIONID"))
                                    session = cookie.getValue();
                        System.out.println("session = " + session);
                        ////////Редирект на страницу ошибки
                    }
                    break;
                case "contacts":
                    dispatcher = req.getServletContext().getRequestDispatcher("/GetContacts");
                    dispatcher.forward(req, resp);
                    break;
                case "add":
                    System.out.println("add: " + json);
                    if (json.has("contact")) {
                        req.setAttribute("contact", json.getJSONObject("contact"));
                        dispatcher = req.getServletContext().getRequestDispatcher("/AddContact");
                        dispatcher.forward(req, resp);
                    } else {
                        //error bad request
                    }

                    break;
                case "show":
                    if (json.has("id")) {
                        req.setAttribute("id", json.getString("id"));
                        dispatcher = req.getServletContext().getRequestDispatcher("/ShowContact");
                        dispatcher.forward(req, resp);
                    } else {
                        //error bad request
                    }
                    break;
                default:
                    System.out.println("def");
                    break;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}