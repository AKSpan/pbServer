package Entities;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.Date;

/**
 * Создано Span 11.11.2015.
 */
@Entity(value = "users")
public class UsersEntity implements EntityInterface {
    @Id
    private ObjectId _id;
    private String username;
    private String password;
    private String session;
    private String last_login;

    public UsersEntity() {
    }

    public UsersEntity(String u, String p) {
        this.username = u;
        this.password = p;
        this.session = null;
        this.last_login = null;
    }

    public ObjectId get_id() {
        return _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }


    @Override
    public String toString() {
        return "UsersEntity{" +
                "_id=" + get_id() +
                ", username='" + getUsername() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", session='" + getSession() + '\'' +
                '}';
    }
}
