package pl.facility_rental.facility.endpoints;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.facility_rental.facility.business.FacilityService;
import pl.facility_rental.facility.dto.CreateFacilityDto;
import pl.facility_rental.facility.dto.UpdateFacilityDto;
import pl.facility_rental.facility.dto.mappers.FacilityMapper;
import pl.facility_rental.facility.dto.ReturnedFacilityDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/facilities")
public class FacilityController {

    private final FacilityService facilityService;

    private final FacilityMapper facilityMapper;
    public FacilityController(FacilityService facilityService, FacilityMapper facilityMapper) {
        this.facilityService = facilityService;
        this.facilityMapper = facilityMapper;
    }


    @GetMapping
    public List<ReturnedFacilityDto> getAllFacilities() {
        return  facilityService.findAll().stream()
                .map(facilityMapper::getFacilityDetails)
                .toList();
    }

    @GetMapping("/{id}")
    public ReturnedFacilityDto getFacilityById(@PathVariable String id) {
        //pozniej dodac wyjatek
        return facilityService.findById(id).map(facilityMapper::getFacilityDetails).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "facility with given id was not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReturnedFacilityDto createFacility(@RequestBody CreateFacilityDto createFacilityDto) {
        return facilityMapper.getFacilityDetails(facilityService.save(facilityMapper.CreateFacilityRequest(createFacilityDto)));
    }

    @DeleteMapping("/{id}")
    public ReturnedFacilityDto deleteFacility(@PathVariable String id) throws Exception {
        return facilityMapper.getFacilityDetails(facilityService.deleteById(id));
    }

    @PutMapping("/{facilityId}")
    public ReturnedFacilityDto updateFacility(@PathVariable String facilityId,
                                                  @RequestBody UpdateFacilityDto updateFacilityDto) throws Exception {
        return facilityMapper.getFacilityDetails(facilityService
                .update(facilityId,facilityMapper.updateFacilityRequest(updateFacilityDto)));
    }

//skopiowany na szybko z interneta
    @RestControllerAdvice
    public static class GlobalExceptionHandler {

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
            Map<String, String> body = new HashMap<>();
            body.put("error", ex.getMessage());
            return ResponseEntity.badRequest().body(body);
        }
    }


}
