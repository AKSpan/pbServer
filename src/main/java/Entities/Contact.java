package Entities;

import com.google.gson.Gson;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * Создано Span 29.10.2015.
 */
@Entity(value = "dict")
public class Contact implements EntityInterface {
    @Id
    private ObjectId _id;
    private String name;
    private String surname;
    private String thirdname;
    private String birthday;
    @Embedded
    //TODO: На будущее подумать над следующим: заменить списки телефонов и соц. на jsonObject'ы и посмотреть как будет проходить запись в монго. Необходимость - из-за того, что геттеры возвращают json'ы. по сути списки не особо нужны
    private
    List<Phone> phone;
    private String avatar;
    private String owner;
    @Embedded
    private
    List<Social> social;

    public Contact() {
    }

    public Contact(String name,
                   String surname,
                   String thirdname,
                   String birthday,
                   List<Phone> phone,
                   String avatar,
                   String owner, List<Social> social) {
        this.name = name;
        this.surname = surname;
        this.thirdname = thirdname;
        this.birthday = birthday;
        this.phone = phone;
        this.avatar = avatar;
        this.owner = owner;
        this.social = social;
    }

    public Contact(JSONObject json) {
        this._id = (ObjectId) json.get("_id");
        this.name = json.getString("name");
        this.surname = json.getString("surname");
        this.thirdname = json.getString("thirdname");
        this.birthday = json.getString("birthday");
        this.avatar = json.getString("avatar");
        this.owner = json.getString("owner");

        List<Phone> phones = new ArrayList<>();
        List<Social> socials = new ArrayList<>();
        JSONArray tmp = json.getJSONArray("phone");
        for (int i = 0; i < tmp.length(); i++)
            phones.add(new Phone(tmp.getJSONObject(i)));
        tmp = json.getJSONArray("social");
        for (int i = 0; i < tmp.length(); i++)
            socials.add(new Social(tmp.getJSONObject(i)));
        this.social = socials;
        this.phone = phones;

    }

    public ObjectId get_id() {
        return _id;
    }

    private String getName() {
        return name == null ? "" : name;
    }

    public String getSurname() {
        return surname == null ? "" : surname;
    }

    private String getThirdname() {
        return thirdname == null ? "" : thirdname;
    }

    private String getBirthday() {
        return birthday == null ? "" : birthday;
    }

    private JSONArray getPhone() {
        if (phone == null)
            return new JSONArray();
        else {
            JSONArray retJA = new JSONArray();
            JSONObject jo;
            for (int i = 0; i < phone.size(); i++) {
                jo = new JSONObject();
                jo.put("name", phone.get(i).getPhoneName());
                jo.put("number", phone.get(i).getPhoneNumber());
                retJA.put(jo);
            }
            return retJA;
        }
    }

    private String getAvatar() {
        return avatar == null ? "" : avatar;
    }

    private JSONArray getSocial() {
        if (social == null)
            return new JSONArray();
        else {
            JSONArray retJA = new JSONArray();
            JSONObject jo;
            for (int i = 0; i < social.size(); i++) {
                jo = new JSONObject();
                jo.put("name", social.get(i).getSocialName());
                jo.put("url", social.get(i).getSocialURL());
                retJA.put(jo);
            }
            return retJA;
        }
    }

    private String getOwner() {
        return owner == null ? "" : owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public JSONObject toJSON() {
        JSONObject request = null;
        Gson gson = new Gson();
        try {
            request = new JSONObject(gson.toJson(this));
            //System.out.println("toJSON() = " + request);
            JSONObject joID = request.getJSONObject("_id");
            ObjectId _id = new ObjectId(joID.getInt("timestamp"),
                    joID.getInt("machineIdentifier"),
                    (short) joID.getInt("processIdentifier"),
                    joID.getInt("counter"));
            //System.out.println(" _id.toString() = "+ _id.toString());
            request.put("_id", _id.toString());
            request.remove("birthday");
            request.remove("phone");
            request.remove("owner");
            request.remove("social");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "_id=" + get_id() +
                ", name='" + getName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", thirdname='" + getThirdname() + '\'' +
                ", birthday='" + getBirthday() + '\'' +
                ", phone='" + getPhone() + '\'' +
                ", avatar='" + getAvatar() + '\'' +
                ", social= " + getSocial() +
                ", owner= " + getOwner() +
                "}";
    }
}
