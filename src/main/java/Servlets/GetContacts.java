package Servlets;

import Entities.AKdbEntity;
import Entities.UsersEntity;
import dbAPI.Mongo;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Создано Span 24.11.2015.
 */
@WebServlet(name = "GetContacts", urlPatterns = "/phonebk/getcontacts", displayName = "GetContacts")
public class GetContacts extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        worker(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private void worker(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("getContacts");
        //JSONObject joReq = GetDataFromRequest.getJSON(request.getReader());
        String owner = "";
        JSONObject answer = new JSONObject(),answerObj;
        JSONArray contactsAnswer, answerArray = new JSONArray();

        Cookie[] cookies = request.getCookies();
        String session = "unreachable-session";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JSESSIONID"))
                    session = cookie.getValue();
            }
            try {
                Mongo mongo = new Mongo();
                mongo.initMongoConnect();
                List<UsersEntity> currUser = (List<UsersEntity>) mongo.find(new UsersEntity(), "session", session);
                if (currUser.size() > 0)
                    owner = currUser.get(0).getUsername();


                List<AKdbEntity> DBcontacts = (List<AKdbEntity>) mongo.find(new AKdbEntity(), "owner", owner);
                /***Группировка контактов по алфавиту***/
                Map<String, List<AKdbEntity>> map = new HashMap<String, List<AKdbEntity>>();
                for (AKdbEntity contact : DBcontacts) {
                    String key = contact.getSurname().substring(0, 1);
                    if (map.containsKey(key)) {
                        List<AKdbEntity> list = map.get(key);
                        list.add(contact);
                    } else {
                        List<AKdbEntity> list = new ArrayList<AKdbEntity>();
                        list.add(contact);
                        map.put(key, list);
                    }
                }
                System.out.println(map);
                /*****/

                for (Map.Entry<String, List<AKdbEntity>> stringListEntry : map.entrySet()) {
                    Map.Entry thisEntry = (Map.Entry) stringListEntry;
                    String currKey = thisEntry.getKey().toString();
                    List<AKdbEntity> currValue = (ArrayList<AKdbEntity>) thisEntry.getValue();

                    contactsAnswer = new JSONArray();
                    for (AKdbEntity t : currValue)
                        contactsAnswer.put(t.toJSON());
                    answerObj = new JSONObject();
                    answerObj.put("group_letter", currKey);
                    answerObj.put("contacts", contactsAnswer);

                    answerArray.put(answerObj);
                }

                answer.put("answer", answerArray);
                answer.put("code", 200);

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        } else {
            answer.put("code", 400);
            /**Нет кук - нет доступа**/
        }

        PrintWriter out = response.getWriter();
        out.println(answer);
    }
}