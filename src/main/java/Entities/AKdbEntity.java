package Entities;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.List;

/**
 * Создано Span 29.10.2015.
 */
@Entity(value = "dict")
public class AKdbEntity implements EntityInterface{
    @Id
    private ObjectId _id;
    private String name;
    private String surname;
    private String thirdname;
    private String birthday;
    private String phone;
    private String avatar;
    private String owner;
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
            return "";
        else {
            String s = "";
            for (Social aSocial : social) {
                s += "{" + aSocial.getSocialName() + ": " + aSocial.getSocialURL() + "}";
            }
            return s;
        }
    }

    public String getOwner() {
        return owner == null ? "" : owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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
                ", social= [" + getSocial() +
                "]}";
    }
}