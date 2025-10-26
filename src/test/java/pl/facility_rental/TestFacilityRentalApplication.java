package pl.facility_rental;

import org.springframework.boot.SpringApplication;

public class TestFacilityRentalApplication {

    public static void main(String[] args) {
        SpringApplication.from(FacilityRentalApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
