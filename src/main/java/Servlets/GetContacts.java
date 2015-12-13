package Servlets;

import Entities.Contact;
import Entities.UsersEntity;
import dbAPI.Mongo;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.GetDataFromRequest;
import utils.UpdateCookie;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Создано Span 24.11.2015.
 */
@WebServlet(name = "GetContacts", urlPatterns = "/GetContacts", displayName = "GetContacts")
public class GetContacts extends HttpServlet{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        worker(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private void worker(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("getContacts");

        String owner = "";
        JSONObject answer = new JSONObject(), answerObj;
        JSONArray contactsAnswer, answerArray = new JSONArray();

        Cookie[] cookies = request.getCookies();
        String session = null;
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
                    UpdateCookie.updateCookies(request, response);
                    Mongo mongo = new Mongo();
                    mongo.initMongoConnect();
                    List<UsersEntity> currUser = (List<UsersEntity>) mongo.find(new UsersEntity(), "session", session);
                    if (currUser.size() > 0)
                        owner = currUser.get(0).getUsername();

                    JSONObject joReq = (JSONObject) request.getAttribute("json");
                    System.out.println("json getcontacts = "+joReq);

                    List<Contact> DBcontacts = (List<Contact>) mongo.find(new Contact(), "owner", owner);
                    /***Группировка контактов по алфавиту***/
                    Map<String, List<Contact>> map = new HashMap<>();
                    for (Contact contact : DBcontacts) {
                        String key = contact.getSurname().substring(0, 1);
                        if (map.containsKey(key)) {
                            List<Contact> list = map.get(key);
                            list.add(contact);
                        } else {
                            List<Contact> list = new ArrayList<>();
                            list.add(contact);
                            map.put(key, list);
                        }
                    }
                    System.out.println("[MAP]: " + map);
                    /*!*Группировка контактов по алфавиту*!*/


                    for (Map.Entry<String, List<Contact>> stringListEntry : map.entrySet()) {
                        Map.Entry thisEntry = (Map.Entry) stringListEntry;
                        String currKey = thisEntry.getKey().toString();
                        List<Contact> currValue = (ArrayList<Contact>) thisEntry.getValue();

                        contactsAnswer = new JSONArray();
                        for (Contact t : currValue)
                            contactsAnswer.put(t.toJSON());
                        answerObj = new JSONObject();
                        answerObj.put("group_letter", currKey);
                        answerObj.put("contacts", contactsAnswer);

                        answerArray.put(answerObj);
                    }
                    answer.put("answer", answerArray);
                    answer.put("code", 200);

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