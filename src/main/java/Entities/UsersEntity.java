package Entities;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * ������� Span 11.11.2015.
 */
@Entity(value = "users")
public class UsersEntity{
    @Id
    private ObjectId _id;
    private String username;
    private String password;
    public UsersEntity() {
    }
    public UsersEntity(String u, String p) {
        this.username = u;
        this.password = p;
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
}
