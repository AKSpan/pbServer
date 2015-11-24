package Servlets;

import Entities.UsersEntity;
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
 * Created by Alexey on 19.11.2015.
 */
@WebServlet("/phonebk/registration")
public class Registration extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        worker(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private void worker(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("registration");
        JSONObject json, answer = new JSONObject();
        String user, pass;
        try
        {
            json = GetDataFromRequest.getJSON(request.getReader());
            System.out.println("request json " + json);
            if (json.has("user")) {
                user = json.getJSONObject("user").getString("user");
                pass = json.getJSONObject("user").getString("pass");
                System.out.println(user + " / " + pass);

                Mongo myMongoServ = new Mongo();
                myMongoServ.initMongoConnect();

                UsersEntity newUser = new UsersEntity(user,pass);
                myMongoServ.save(newUser);
                answer.put("answer", "Success registration");
                answer.put("code",200);

            } else {
                answer.put("answer", "Bad request. No field \"user\" in json!");
                answer.put("code", 400);
            }
        }
        catch (IllegalArgumentException e)
        {
            answer.put("answer", e.getMessage());
            answer.put("code", 400);
        }
        catch (NullPointerException e)
        {
            answer.put("answer", "Server error: " + e.getMessage());
            answer.put("code", 500);
            e.getStackTrace();
        }
        PrintWriter out = response.getWriter();
        out.write(answer.toString());
    }
}
