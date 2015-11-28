package Servlets;

import Entities.AKdbEntity;
import Entities.UsersEntity;
import com.mongodb.util.JSON;
import dbAPI.Mongo;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.GetDataFromRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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

    private void worker(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("getContacts");
        JSONObject joReq = GetDataFromRequest.getJSON(request.getReader());
        String owner = "";
        JSONObject answer = new JSONObject();

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
                List<AKdbEntity> DBcontacts = (List<AKdbEntity>) mongo.find(new AKdbEntity(),"owner",owner);
                JSONArray jaAnswer = new JSONArray();
                for(AKdbEntity contact:DBcontacts)
                {
                    ///TODO: Смотри ниже
                    /**
                     * Нужна группировка по алфавиту (фамилии) примерный вид
                     * answer:[
                     *         {
                     *              group_letter:A,
                     *              contacts:[{},{},{}]
                     *         },
                     *         {
                     *              group_letter:Z
                     *              contacts:[{},{},{}]
                     *         }
                     *         ,
                     *         {
                     *              group_letter:0
                     *              contacts:[{},{},{}]
                     *         }
                     *         ,
                     *         {
                     *              group_letter:9
                     *              contacts:[{},{},{}]
                     *         }
                     *      ]
                     */

                    jaAnswer.put(contact.toJSON());
                }
                answer.put("answer",jaAnswer);
                answer.put("code",200);

            }
            catch (Exception ex)
            {
                System.out.println(ex.getMessage());
            }

        }
        else
        {
            /**Нет кук - нет доступа**/
        }

        /*if(joReq.has("user")) {
            user = joReq.getString("user");

            Mongo mongo = new Mongo();
            mongo.initMongoConnect();

            List<AKdbEntity> contacts = (List<AKdbEntity>) mongo.find(new AKdbEntity(), "owner", user);
            JSONArray jsonArray = new JSONArray();

            for (AKdbEntity aken : contacts) {
                jsonArray.put(aken.toJSON());
            *//*answer.append()
            jsonArray.put()*//*
            }
            answer.put("contacts", jsonArray);
        }
        else
        {

        }*/
        PrintWriter out = response.getWriter();
        out.println(answer);
    }
}
