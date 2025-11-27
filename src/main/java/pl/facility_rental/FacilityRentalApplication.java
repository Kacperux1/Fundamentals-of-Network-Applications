package pl.facility_rental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "pl.facility_rental.user.data")
public class FacilityRentalApplication {

    public static void main(String[] args) {
        SpringApplication.run(FacilityRentalApplication.class, args);
    }

}
