package pl.facility_rental.rent.business;

import org.springframework.stereotype.Service;
import pl.facility_rental.rent.data.RentRepository;
import pl.facility_rental.rent.model.MongoRent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RentService {

    private final RentRepository rentRepository;

    public RentService(RentRepository rentRepository) {
        this.rentRepository = rentRepository;
    }


    public List<Rent> findAll() {
        return rentRepository.findAll();
    }

    public Optional<Rent> findById(UUID id) {
        return rentRepository.findById(id);
    }

    public Rent save(Rent rent) {
        return rentRepository.save(rent);
    }

    public Rent delete(UUID id) throws Exception {
         return rentRepository.delete(id);
    }


}
