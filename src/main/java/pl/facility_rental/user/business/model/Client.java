package pl.facility_rental.user.business.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Client extends User {

    private String firstName;

    private String lastName;

    private String phone;

    @JsonCreator
    public Client(@JsonProperty("id") String id,
                  @JsonProperty("login") String login,
                  @JsonProperty("email") String email,
                  @JsonProperty("active") boolean active,
                  @JsonProperty("firstName") String firstName,
                  @JsonProperty("lastName") String lastName,
                  @JsonProperty("phone") String phone) {
        super(id, login, email, active);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public Client(String login, String email, boolean active, String firstName, String lastName, String phone) {
        super(login, email, active);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }
}
