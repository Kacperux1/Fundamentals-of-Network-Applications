package pl.facility_rental.user.business.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class User {
    private String id;
    @NotBlank
    private String login;
    private String email;
    private String password;
    private boolean active;

    @JsonCreator
    public User(@JsonProperty("id") String id,
                @JsonProperty("login") String login,
                @JsonProperty("email") String email,
                @JsonProperty("password") String password,
                @JsonProperty("active") boolean active) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.password = password;
        this.active = active;
    }

    public User(String login, String email, String password, boolean active) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.active = active;
    }

}
