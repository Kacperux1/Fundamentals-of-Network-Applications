package pl.facility_rental.user.business.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@Getter
public abstract class User {

    private String id;
    @NotBlank
    private String login;
    private String email;
    private boolean active;
    public User( String id,String login, String email,
                 boolean active) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.active = active;
    }

    public User(String login, String email, boolean active) {
        this.login = login;
        this.email = email;
        this.active = active;
    }

}
