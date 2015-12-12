package Entities;

import org.json.JSONObject;
import org.mongodb.morphia.annotations.Embedded;

/**
 * Создано Span 07.12.2015.
 */
@Embedded
public class Phone {
    private Phone() {
    }

    public Phone(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public Phone(JSONObject json) {
        this.name = json.getString("name");
        this.number = json.getString("number");
    }
    public String getPhoneName() {
        return name;
    }

    public String getPhoneNumber() {
        return number;
    }

    private String name;
    private String number;
}
