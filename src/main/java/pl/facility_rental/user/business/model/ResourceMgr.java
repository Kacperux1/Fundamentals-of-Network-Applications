package pl.facility_rental.user.business.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class ResourceMgr extends User{


    public ResourceMgr(String id, String login, String email, String password, boolean active) {
        super(id, login, email, password, active);
    }

    public ResourceMgr(String login, String email,String password, boolean active) {
        super(login, email, password, active);
    }
}
