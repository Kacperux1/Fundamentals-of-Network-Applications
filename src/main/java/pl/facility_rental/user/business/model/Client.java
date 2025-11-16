package pl.facility_rental.user.business.model;

import lombok.Getter;


import java.util.UUID;
@Getter
public class Client extends User {

    private final String firstName;

    private final String lastName;

    private final String phone;

    public Client(String id, String login, String email, boolean active, String firstName, String lastName, String phone) {
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
