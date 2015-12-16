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

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("Filter!");
        HttpServletRequest request = (HttpServletRequest) req;
        resp.setContentType("application/json;charset=UTF-8");
        String action = "";
        RequestDispatcher dispatcher;
        JSONObject user = null;
        JSONObject json = GetDataFromRequest.getJSON(req.getReader());
        req.setAttribute("json", json);
        if (json != null && json.has("action"))
            action = json.getString("action");
        System.out.println("ACTION = " + action);
        Cookie[] cookies = request.getCookies();
        try {
            switch (action) {
                case "login":
                case "registration":
                    String page = (action.equals("login")) ? "/Login" : "/Registration";

                    dispatcher = req.getServletContext().getRequestDispatcher(page);
                    if (json.has("user"))
                        user = json.getJSONObject("user");
                    if (user != null) {
                        req.setAttribute("user", user.getString("user"));
                        req.setAttribute("pass", user.getString("pass"));
                        dispatcher.forward(req, resp);


                    } else {
                        ////////Редирект на страницу ошибки
                    }
                    break;
                case "contacts":
                    dispatcher = req.getServletContext().getRequestDispatcher("/GetContacts");
                    dispatcher.forward(req, resp);
                    break;
                case "add":
                    System.out.println("add: " + json);
                    if (json != null && json.has("contact")) {
                        req.setAttribute("contact", json.getJSONObject("contact"));
                        dispatcher = req.getServletContext().getRequestDispatcher("/AddContact");
                        dispatcher.forward(req, resp);
                    } else {
                        //error bad request
                    }

                    break;
                case "show":
                    if (json != null && json.has("id")) {
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
            //  chain.doFilter(req, resp);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
