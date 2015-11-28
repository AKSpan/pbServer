package Entities;

import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.List;

/**
 * Создано Span 29.10.2015.
 */
@Entity(value = "dict")
public class AKdbEntity implements EntityInterface {
    @Id
    private ObjectId _id;
    private String name;
    private String surname;
    private String thirdname;
    private String birthday;
    private String phone;
    private String avatar;
    private String owner;
    private String group_letter;
    @Embedded
    private
    List<Social> social;

    public AKdbEntity() {
    }

    public AKdbEntity(String name,
                      String surname,
                      String thirdname,
                      String birthday,
                      String phone,
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

    public ObjectId get_id() {
        return _id;
    }

    private String getName() {
        return name == null ? "" : name;
    }

    private String getSurname() {
        return surname == null ? "" : surname;
    }

    private String getThirdname() {
        return thirdname == null ? "" : thirdname;
    }

    private String getBirthday() {
        return birthday == null ? "" : birthday;
    }

    private String getPhone() {
        return phone == null ? "" : phone;
    }

    private String getAvatar() {
        return avatar == null ? "" : avatar;
    }

    private String getSocial() {
        if (social == null)
            return "[]";
        else {
            String s = "[";
            for (int i = 0; i< social.size();i++) {

                s += "{" + social.get(i).getSocialName() + ": " + social.get(i).getSocialURL() + "}";
                if(i<social.size()-1)
                    s+=",";
            }
            return s+"]";
        }
    }

    public String getOwner() {
        return owner == null ? "" : owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getGroupLetter() {
        return group_letter;
    }

    public void setGroupLetter(String group_letter) {
        this.group_letter = group_letter;
    }

    public JSONObject toJSON() {
        JSONObject jo = new JSONObject();
        jo.put("name",getName());
        jo.put("surname",getSurname());
        jo.put("thirdname",getThirdname());
        jo.put("birthday",getBirthday());
        jo.put("phone",getPhone());
        jo.put("avatar",getAvatar());
        jo.put("owner",getOwner());
        jo.put("social",getSocial());
        jo.put("group_letter",getGroupLetter());
        return jo;
    }

    @Override
    public String toString() {
        return "AKdbEntity{" +
                "_id=" + get_id() +
                ", name='" + getName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", thirdname='" + getThirdname() + '\'' +
                ", birthday='" + getBirthday() + '\'' +
                ", phone='" + getPhone() + '\'' +
                ", avatar='" + getAvatar() + '\'' +
                ", social= " + getSocial() +
                ", owner= " + getOwner() +
                ", group_letter= " + getGroupLetter() +
                "}";
    }
}
