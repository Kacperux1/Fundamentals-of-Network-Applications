package pl.facility_rental.user.business.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class User {
    private String id;
    @NotBlank
    private String login;
    private String email;
    private boolean active;

    @JsonCreator
    public User(@JsonProperty("id") String id,
                @JsonProperty("login") String login,
                @JsonProperty("email") String email,
                @JsonProperty("active") boolean active) {
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
