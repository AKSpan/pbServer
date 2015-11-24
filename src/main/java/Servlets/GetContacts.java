package Servlets;

import Entities.AKdbEntity;
import dbAPI.Mongo;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.GetDataFromRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        String user ="";
        JSONObject answer = new JSONObject();
        if(joReq.has("user")) {
            user = joReq.getString("user");

            Mongo mongo = new Mongo();
            mongo.initMongoConnect();

            List<AKdbEntity> contacts = (List<AKdbEntity>) mongo.find(new AKdbEntity(), "owner", user);
            JSONArray jsonArray = new JSONArray();

            for (AKdbEntity aken : contacts) {
                jsonArray.put(aken.toJSON());
            /*answer.append()
            jsonArray.put()*/
            }
            answer.put("contacts", jsonArray);
        }
        else
        {

        }
        PrintWriter out = response.getWriter();
        out.println(answer);
    }
}
