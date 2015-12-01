package utils;

import org.json.JSONObject;

import java.io.BufferedReader;

public class GetDataFromRequest {
    public static JSONObject getJSON(BufferedReader req) {
        String line;
        JSONObject joResult = null;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = req.readLine()) != null) {
                sb.append(line);
            }
            joResult = new JSONObject(sb.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return joResult;

    }
}
