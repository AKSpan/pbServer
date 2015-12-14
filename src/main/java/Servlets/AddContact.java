package Servlets;

import Entities.Contact;
import Entities.UsersEntity;
import dbAPI.Mongo;
import org.json.JSONObject;
import utils.UpdateCookie;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

/**
 * Created by Alexey on 13.12.2015.
 */
@WebServlet(name = "AddContact", urlPatterns = "/AddContact")
public class AddContact extends HttpServlet {
    private final String DEFAULT_AVATAR = "img/default-avatar.png";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        worker(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private void worker(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("addContact");

        JSONObject answer = new JSONObject(), clientJSON;

        Cookie[] cookies = request.getCookies();
        String session = null, owner;
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
                    clientJSON = (JSONObject) request.getAttribute("contact");
                    //System.out.println("ClientJSON = " + clientJSON);
                    String avatar = clientJSON.getString("avatar"), imagePath = "";


                    if (clientJSON != null) {
                        UpdateCookie.updateCookies(request, response);
                        Mongo mongo = new Mongo();
                        mongo.initMongoConnect();
                        List<UsersEntity> currUser = (List<UsersEntity>) mongo.find(new UsersEntity(), "session", session);
                        if (currUser.size() > 0) {
                            owner = currUser.get(0).getUsername();

                            if (avatar.contains(DEFAULT_AVATAR))
                                imagePath = avatar;
                            else {
                                String imageDataBytes = avatar.substring(avatar.indexOf(",") + 1);
                                byte[] avaBase64 = Base64.getDecoder().decode(imageDataBytes.getBytes());
                                String path = "D:\\Phonebook\\" + owner + "\\img";
                                File ownerFolder = new File(path);
                                boolean canSave;
                                if (ownerFolder.exists())
                                    canSave = true;
                                else {
                                    ownerFolder.mkdirs();
                                    canSave = true;
                                }
                                if (canSave) {
                                    String filename = new BigInteger(130, new SecureRandom()).toString(32);
                                    FileOutputStream fos = new FileOutputStream(path + "\\" + filename + ".jpg");
                                    try {
                                        fos.write(avaBase64);
                                        clientJSON.put("avatar", path + "\\" + filename + ".jpg");
                                    } finally {
                                        fos.close();
                                    }
                                }

                            }

                            clientJSON.put("owner", owner);
                            Contact contact = new Contact(clientJSON);
                            mongo.save(contact);
                            answer.put("answer", "Success registration");
                            answer.put("code", 200);
                        } else {
                            ///onwer not found
                        }
                    } else {
                        answer.put("answer", "Bad request. Some errors in request :(");
                        answer.put("code", 400);
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
