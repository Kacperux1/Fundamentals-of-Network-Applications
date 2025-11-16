package pl.facility_rental.rent.endpoints;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.facility_rental.rent.business.RentService;
import pl.facility_rental.rent.data.RentRepository;
import pl.facility_rental.rent.dto.CreateRentDto;
import pl.facility_rental.rent.dto.RentMapper;
import pl.facility_rental.rent.dto.ReturnedRentDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rents")
public class RentController {

    private final RentService rentService;
    private final RentMapper rentMapper;


    public RentController(RentService rentService, RentMapper rentMapper) {
        this.rentService = rentService;
        this.rentMapper = rentMapper;
    }

    @GetMapping
    public List<ReturnedRentDto> getAllRents(){
        return rentService.findAll().stream().map(rentMapper::getRentDetails).toList();
    }

    @PostMapping
    public ReturnedRentDto createRent(@RequestBody CreateRentDto rentDto) throws Exception {
        return rentMapper.getRentDetails(rentService.save(rentMapper.CreateRentRequest(rentDto)));
    }

    @GetMapping("/{id}")
    public ReturnedRentDto getRentById(@PathVariable String id) throws Exception {
        return rentService.findById(id).map(rentMapper::getRentDetails)
                .orElseThrow(() ->new ResponseStatusException(HttpStatus.NOT_FOUND, "Rent with given id was not found"));
    }
    @GetMapping("/{clientId}")
    public List<ReturnedRentDto> getRentsByClientId(@PathVariable String clientId) throws Exception {
        return rentService.getCurrentAndPastClientsRents(clientId).stream().map(rentMapper::getRentDetails).toList();
    }

    @DeleteMapping("/{id}")
    public ReturnedRentDto deleteRent(@PathVariable String id) throws Exception {
        return rentMapper.getRentDetails(rentService.delete(id));
    }

    @PatchMapping("/{id}")
    public ReturnedRentDto endRent(@PathVariable String id) throws Exception {
        return rentMapper.getRentDetails(rentService.endRent(id));
    }
}
