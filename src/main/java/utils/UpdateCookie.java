package utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Alexey on 13.12.2015.
 */
public class UpdateCookie {
    public static final int COOKIE_MAX_AGE = 300;
    public static void updateCookies(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie[] cukizz = request.getCookies();

        if (cukizz != null) {
            for (Cookie cuk : cukizz)
                if (cuk.getName().equals("JSESSIONID")) {
                    cuk = new Cookie("JSESSIONID", cuk.getValue());
                    cuk.setPath("/phonebk");
                    cuk.setHttpOnly(true);
                    cuk.setMaxAge(COOKIE_MAX_AGE);
                    response.addCookie(cuk);
                   // System.out.println("Cukzz params:\nValue: "+cuk.getValue()+"\nAge: "+cuk.getMaxAge());
                }
        }
    }
}
