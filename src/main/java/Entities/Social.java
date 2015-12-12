package Entities;

import org.json.JSONObject;
import org.mongodb.morphia.annotations.Embedded;

@Embedded
class Social {
    private Social() {
    }

    public Social(String name, String url) {
        this.name = name;
        this.url = url;
    }
    public Social(JSONObject json) {
        this.name = json.getString("name");
        this.url = json.getString("url");
    }


    public String getSocialName() {
        return name;
    }

    public String getSocialURL() {
        return url;
    }

    private String name;
    private String url;

    @Override
    public String toString() {
        return "Social{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
