package Servlets;


import Entities.UsersEntity;
import dbAPI.Mongo;
import org.json.JSONObject;
import utils.GetDataFromRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

/**
 * Создано Span 09.11.2015.
 */
@WebServlet(urlPatterns = "/phonebk/login", name = "Login")
public class Login extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        worker(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        worker(request, response);
    }

    private void worker(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Login servlet!");
        String session = null;
        JSONObject json, answer = new JSONObject();
        String user, pass;
        Cookie[] reqCookies = request.getCookies();
        if (reqCookies != null) {
            for (Cookie cookie : reqCookies) {
                System.out.println(cookie.getName() + ": " + cookie.getValue());
                if (cookie.getName().equals("JSESSIONID"))
                    session = cookie.getValue();
            }
            try {
                json = GetDataFromRequest.getJSON(request.getReader());
                System.out.println("request json " + json);
                if (json.has("user")) {
                    user = json.getJSONObject("user").getString("user");
                    pass = json.getJSONObject("user").getString("pass");
                    System.out.println(user + " / " + pass);

                    Mongo myMongoServ = new Mongo();
                    myMongoServ.initMongoConnect();
                    List<UsersEntity> users = (List<UsersEntity>) myMongoServ.find(new UsersEntity(), "username", user);
                    if (users.size() > 0)
                        if (users.get(0).getPassword().equals(pass)) {
                            myMongoServ.update(users.get(0), "session", session);
                            myMongoServ.update(users.get(0), "last_login", new Date().getTime()+"");
                            answer.put("answer", "Pass is correct");
                            answer.put("code", 200);
                        } else {
                            answer.put("answer", "Pass isn't correct");
                            answer.put("code", 401);
                        }
                } else {
                    answer.put("answer", "Bad request. No field \"user\" in json!");
                    answer.put("code", 400);
                }
            } catch (Exception e) {
                answer.put("answer", "Server error: " + e.toString());
                answer.put("code", 500);
                e.getStackTrace();
            }
        } else {
            answer.put("answer", "Unauthorized!");
            answer.put("code", 401);
        }

        PrintWriter out = response.getWriter();
        out.write(answer.toString());
    }
}
