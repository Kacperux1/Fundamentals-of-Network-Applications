package pl.facility_rental.facility.endpoints;

import org.springframework.web.bind.annotation.*;
import pl.facility_rental.facility.business.FacilityService;
import pl.facility_rental.facility.dto.CreateFacilityDto;
import pl.facility_rental.facility.dto.FacilityMapper;
import pl.facility_rental.facility.dto.ReturnedFacilityDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class FacilityController {

    private final FacilityService facilityService;

    private final FacilityMapper facilityMapper;
    public FacilityController(FacilityService facilityService, FacilityMapper facilityMapper) {
        this.facilityService = facilityService;
        this.facilityMapper = facilityMapper;
    }


    @GetMapping("/facilities")
    public List<ReturnedFacilityDto> getAllFacilities() {
        return  facilityService.findAll().stream()
                .map(facilityMapper::getFacilityDetails)
                .toList();
    }

    @GetMapping("/facilities")
    public ReturnedFacilityDto getFacilityById(@PathVariable String id) {
        //pozniej dodac wyjatek
        return facilityMapper.getFacilityDetails(facilityService.findById(UUID.fromString(id)).get());
    }

    @PostMapping("/facilities")
    public ReturnedFacilityDto createFacility(@RequestBody CreateFacilityDto createFacilityDto) {
        return facilityMapper.getFacilityDetails(facilityService.save(facilityMapper.CreateClientRequest(createFacilityDto)));
    }

    @DeleteMapping("/facilities")
    public ReturnedFacilityDto deleteFacility(@PathVariable String id) throws Exception {
        return facilityMapper.getFacilityDetails(facilityService.deleteById(UUID.fromString(id)));
    }

}
