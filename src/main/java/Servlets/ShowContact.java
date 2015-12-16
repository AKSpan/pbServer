package Servlets;

import Entities.Contact;
import Entities.UsersEntity;
import dbAPI.Mongo;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import utils.UpdateCookie;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by Alexey on 16.12.2015.
 */
@WebServlet(name = "ShowContact", urlPatterns = "/ShowContact")
public class ShowContact extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        worker(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private void worker(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("SHOW CONTACT");
        JSONObject answer = new JSONObject();

        Cookie[] cookies = request.getCookies();
        String session = null;
        ObjectId id;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JSESSIONID"))
                    session = cookie.getValue();
            }
            if (session == null) {
                answer.put("answer", "Invalid cookies!");
                answer.put("code", 400);
            } else {
                try {
                    id = new ObjectId(request.getAttribute("id") + "");
                    UpdateCookie.updateCookies(request, response);
                    Mongo mongo = new Mongo();
                    mongo.initMongoConnect();
                    List<UsersEntity> currUser = (List<UsersEntity>) mongo.find(new UsersEntity(), "session", session);
                    if (currUser.size() > 0) {
                        Contact contact = (Contact) mongo.find(new Contact(),id);
                        answer.put("answer", contact.toJSON());
                        answer.put("code", 200);
                    } else {
                        ///onwer not found
                    }

                } catch (Exception ex) {
                    answer.put("answer", "Server error: " + ex.toString());
                    answer.put("code", 500);
                    ex.getStackTrace();
                }
            }
        } else {
            answer.put("code", 401);
            answer.put("answer", "Unauthorized");
            //answer.put("action", "logout");
            /**Нет кук - нет доступа**/
        }
        PrintWriter out = response.getWriter();
        out.println(answer);
    }
}
