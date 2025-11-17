package pl.facility_rental.rent.endpoints;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.facility_rental.rent.business.RentService;
import pl.facility_rental.rent.dto.CreateRentDto;
import pl.facility_rental.rent.dto.mappers.RentMapper;
import pl.facility_rental.rent.dto.ReturnedRentDto;

import java.util.List;

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
    @ResponseStatus(HttpStatus.OK)
    public List<ReturnedRentDto> getAllRents(){
        return rentService.findAll().stream().map(rentMapper::getRentDetails).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReturnedRentDto createRent(@RequestBody CreateRentDto rentDto) throws Exception {
        return rentMapper.getRentDetails(rentService.save(rentMapper.CreateRentRequest(rentDto)));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReturnedRentDto getRentById(@PathVariable String id) throws Exception {
        return rentService.findById(id).map(rentMapper::getRentDetails)
                .orElseThrow(() ->new ResponseStatusException(HttpStatus.NOT_FOUND, "Rent with given id was not found"));
    }
    @GetMapping("/client/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ReturnedRentDto> getRentsByClientId(@PathVariable String clientId) throws Exception {
        return rentService.getCurrentAndPastClientsRents(clientId).stream().map(rentMapper::getRentDetails).toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ReturnedRentDto deleteRent(@PathVariable String id) throws Exception {
        return rentMapper.getRentDetails(rentService.delete(id));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReturnedRentDto endRent(@PathVariable String id) throws Exception {
        return rentMapper.getRentDetails(rentService.endRent(id));
    }
}
