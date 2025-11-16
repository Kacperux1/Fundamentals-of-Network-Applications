package pl.facility_rental.rent.endpoints;

import org.springframework.web.bind.annotation.*;
import pl.facility_rental.rent.business.RentService;
import pl.facility_rental.rent.data.RentRepository;
import pl.facility_rental.rent.dto.CreateRentDto;
import pl.facility_rental.rent.dto.RentMapper;
import pl.facility_rental.rent.dto.ReturnedRentDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class RentController {

    private final RentService rentService;
    private final RentMapper rentMapper;


    public RentController(RentService rentService, RentMapper rentMapper) {
        this.rentService = rentService;
        this.rentMapper = rentMapper;
    }

    @GetMapping("/rents")
    public List<ReturnedRentDto> getAllRents(){
        return rentService.findAll().stream().map(rentMapper::getRentDetails).toList();
    }

    @PostMapping("/rents")
    public ReturnedRentDto createRent(@RequestBody CreateRentDto rentDto) throws Exception {
        return rentMapper.getRentDetails(rentService.save(rentMapper.CreateRentRequest(rentDto)));
    }

    @GetMapping("/rents")
    public ReturnedRentDto getRentById(@RequestParam String id) throws Exception {
        return rentMapper.getRentDetails(rentService.findById(id).get());
    }

    @DeleteMapping("/rents")
    public ReturnedRentDto deleteRent(@RequestParam String id) throws Exception {
        return rentMapper.getRentDetails(rentService.delete(id));
    }
}
