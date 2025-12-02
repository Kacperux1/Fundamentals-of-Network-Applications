package pl.facility_rental.user.business.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class Administrator extends User{
    public Administrator(String id, String login, String email, boolean active) {
        super(id, login, email, active);
    }

    public Administrator(String login, String email, boolean active) {
        super(login, email, active);
    }
}
