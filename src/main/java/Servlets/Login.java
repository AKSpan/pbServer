package Servlets;


import dbAPI.Mongo;
import org.json.JSONObject;
import utils.GetDataFromRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Создано Span 09.11.2015.
 */
@WebServlet(name = "login")
public class Login extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        worker(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        worker(request, response);
    }

    private void worker(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Login servlet!");
        JSONObject json, answer = new JSONObject();
        String user, pass;
        try {
            json = GetDataFromRequest.getJSON(request.getReader());
            System.out.println("request json " + json);
            if (json.has("auth")) {
                user = json.getJSONObject("auth").getString("user");
                pass = json.getJSONObject("auth").getString("pass");
                System.out.println(user + " / " + pass);
                answer.put("answer", "Good");

                Mongo myMongoServ = new Mongo();
                myMongoServ.initMongoConnect();
                // List<AKdbEntity> akdbFinded = (List<AKdbEntity>) myMongoServ.find(new AKdbEntity());
                //List<UsersEntity> users = (List<UsersEntity>) myMongoServ.find(new UsersEntity(), "username", "Span");

                /*Map<String, String> updMap = new HashMap<String, String>();
                updMap.put("password", "qazxsw");


                myMongoServ.update(users.get(0), updMap);*/

                System.out.println("   ");

            } else {
                answer.put("answer", "Bad request. No field \"auth\" in json!");
            }
        } catch (Exception e) {
            answer.put("answer", "Server error: " + e.toString());
            e.getStackTrace();
        }


        PrintWriter out = response.getWriter();
        out.write(answer.toString());
    }
}
