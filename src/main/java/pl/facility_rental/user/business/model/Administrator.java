package pl.facility_rental.user.business.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class Administrator extends User{
    public Administrator(String id, String login, String email, String password, boolean active) {
        super(id, login, email, password, active);
    }

    public Administrator(String login, String email, String password, boolean active) {

        super(login, email, password, active);
    }
}
