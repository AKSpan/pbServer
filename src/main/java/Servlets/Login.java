package Servlets;


import Entities.UsersEntity;
import dbAPI.Mongo;
import org.json.JSONObject;
import utils.GetDataFromRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Создано Span 09.11.2015.
 */
@WebServlet(urlPatterns = "/Login", name = "Login")
public class Login extends HttpServlet {
    private static final int COOKIE_MAX_AGE = 60;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        worker(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        worker(request, response);
    }

    private void worker(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Login servlet!");
        String session = null;
        JSONObject answer = new JSONObject();
        String user, pass;

        try {

            if (request.getAttribute("user")!=null && request.getAttribute("pass")!=null) {
                user = request.getAttribute("user").toString();
                pass = request.getAttribute("pass").toString();
                System.out.println(user + " / " + pass);


//                Cookie[] cukizz = request.getCookies();
//                if (cukizz != null) {
//                    for (Cookie cuk : cukizz)
//                        if (cuk.getName().equals("session"))
//                            session = cuk.getValue();
//                    if (session == null) {
//                        answer.put("answer", "Invalid cookies!");
//                        answer.put("code", 400);
//                    } else {
//                        authenticationBySession(response, answer, session);
//                    }
//                } else {
                    authenticationByPassword(request,response, answer, user, pass);
//                }

            } else {
                answer.put("answer", "Bad request. No fields \"user\" or \"pass\" in request!");
                answer.put("code", 400);
            }
        } catch (Exception e) {
            answer.put("answer", "Server error: " + e.toString());
            answer.put("code", 500);
            e.getStackTrace();
        }
        PrintWriter out = response.getWriter();
        out.write(answer.toString());
    }

    /**
     * Аутентификация пользователя по сессии.
     *
     * @param response ссылка на response клиенту для записи кук
     * @param answer   ссылка на JSONObject ответа для клиента
     * @param session  сессия из кук
     * @throws Exception
     */
    private void authenticationBySession(HttpServletResponse response, JSONObject answer, String session) throws Exception {
        Mongo myMongoServ = new Mongo();
        myMongoServ.initMongoConnect();
        List<UsersEntity> users = (List<UsersEntity>) myMongoServ.find(new UsersEntity(), "session", session);
        if (users.size() > 0) {
            myMongoServ.update(users.get(0), "last_login", new Date().getTime() + "");
            Cookie cuk = new Cookie("session", session);
            cuk.setPath("/pb/");
            cuk.setHttpOnly(true);
            cuk.setMaxAge(COOKIE_MAX_AGE);
            response.addCookie(cuk);
            answer.put("answer", "Auth by session is correct!");
            answer.put("code", 200);
        } else {
            answer.put("answer", "Username or password is invalid!");
            answer.put("code", 401);
        }
    }

    /**
     * Аутентификация пользователя по паролю.
     *
     * @param response ссылка на response клиенту для записи кук
     * @param answer   ссылка на JSONObject ответа для клиента
     * @param user     логин
     * @param pass     пароль
     * @throws Exception
     */
    private void authenticationByPassword(HttpServletRequest request,HttpServletResponse response, JSONObject answer, String user, String pass) throws Exception {
        Mongo myMongoServ = new Mongo();
        myMongoServ.initMongoConnect();
        if (user.length() > 0) {
            List<UsersEntity> users = (List<UsersEntity>) myMongoServ.find(new UsersEntity(), "username", user);
            if (users.size() > 0) {
                if (users.get(0).getPassword().equals(pass)) {
//                    String session = UUID.randomUUID().toString();
                    HttpSession session = request.getSession();
                    System.out.println("Login sess= "+session.getId());
                    session.setMaxInactiveInterval(300);
                    myMongoServ.update(users.get(0), "session", session.getId());
                    myMongoServ.update(users.get(0), "last_login", new Date().getTime() + "");

                    /*Cookie cuk = new Cookie("session", session);
                    cuk.setPath("/pb/");
                    cuk.setHttpOnly(true);
                    cuk.setMaxAge(COOKIE_MAX_AGE);
                    response.addCookie(cuk);*/
                    answer.put("answer", "Auth by pass is correct!");
                    answer.put("code", 200);
                } else {
                    answer.put("answer", "Password is invalid!");
                    answer.put("code", 401);
                }
            } else {
                answer.put("answer", "Username or password is invalid!");
                answer.put("code", 401);
            }
        }
        else
        {
            answer.put("answer", "Username is empty!");
            answer.put("code", 401);
        }
    }

}
