package pl.facility_rental.user.business.model;

import java.util.UUID;

public class ResourceMgr extends User{


    public ResourceMgr(UUID id, String login, String email, boolean active) {
        super(id, login, email, active);
    }

    public ResourceMgr(String login, String email, boolean active) {
        super(login, email, active);
    }
}
