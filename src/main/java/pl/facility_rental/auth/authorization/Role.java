package pl.facility_rental.auth.authorization;

import pl.facility_rental.user.business.model.Administrator;
import pl.facility_rental.user.business.model.Client;
import pl.facility_rental.user.business.model.ResourceMgr;

public enum Role {

    CLIENT(Client.class),
    MANAGER(ResourceMgr.class),
    ADMIN(Administrator.class),;


    private final String value;

    Role(Class<?> typeForValue) {
        this.value = typeForValue.getSimpleName();
    }

    public String getValue() {
        return value;
    }
}
