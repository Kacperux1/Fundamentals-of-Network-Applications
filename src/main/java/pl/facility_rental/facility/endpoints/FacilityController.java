package pl.facility_rental.facility.endpoints;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.facility_rental.facility.business.FacilityService;
import pl.facility_rental.facility.dto.CreateFacilityDto;
import pl.facility_rental.facility.dto.UpdateFacilityDto;
import pl.facility_rental.facility.dto.mappers.FacilityMapper;
import pl.facility_rental.facility.dto.ReturnedFacilityDto;
import pl.facility_rental.facility.exceptions.BadIdFormatException;
import pl.facility_rental.facility.exceptions.FacilityNotFoundException;
import pl.facility_rental.facility.exceptions.RentsForFacilityExistsException;
import pl.facility_rental.facility.exceptions.ValidationViolationFacilityException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(
        origins = "*",
        allowedHeaders = "*",
        methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE }
)
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
        return facilityService.findAll().stream()
                .map(facilityMapper::getFacilityDetails)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnedFacilityDto> getFacilityById(@PathVariable String id) {
        //pozniej dodac wyjatek
        return ResponseEntity.ok().body(facilityService.findById(id).map(facilityMapper::getFacilityDetails).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "facility with given id was not found")));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('Administrator', 'ResourceMgr')")
    @ResponseStatus(HttpStatus.CREATED)
    public ReturnedFacilityDto createFacility(@RequestBody CreateFacilityDto createFacilityDto) {
        return facilityMapper.getFacilityDetails(facilityService.save(facilityMapper.CreateFacilityRequest(createFacilityDto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('Administrator', 'ResourceMgr')")
    public ReturnedFacilityDto deleteFacility(@PathVariable String id) throws Exception {
        return facilityMapper.getFacilityDetails(facilityService.deleteById(id));
    }

    @PutMapping("/{facilityId}")
    @PreAuthorize("hasAnyRole('Administrator', 'ResourceMgr')")
    public ReturnedFacilityDto updateFacility(@PathVariable String facilityId,
                                              @RequestBody UpdateFacilityDto updateFacilityDto) throws Exception {
        return facilityMapper.getFacilityDetails(facilityService
                .update(facilityId, facilityMapper.updateFacilityRequest(updateFacilityDto)));
    }

    @RestControllerAdvice
    public static class GlobalExceptionHandler {

        @ExceptionHandler(BadIdFormatException.class)
        public ResponseEntity<Map<String, String>> handleBadIdFormat(BadIdFormatException ex) {
            Map<String, String> body = new HashMap<>();
            body.put("message", ex.getMessage());
            return ResponseEntity.badRequest().body(body);
        }

        @ExceptionHandler(FacilityNotFoundException.class)
        public ResponseEntity<Map<String, String>> handleFacilityNotFound(FacilityNotFoundException ex) {
            Map<String, String> body = new HashMap<>();
            body.put("message", ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(body);
        }

        @ExceptionHandler(RentsForFacilityExistsException.class)
        public ResponseEntity<Map<String, String>> handleFacilityNotFound(RentsForFacilityExistsException ex) {
            Map<String, String> body = new HashMap<>();
            body.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
        }

        @ExceptionHandler(ValidationViolationFacilityException.class)
        public ResponseEntity<Map<String, String>> handleValidationViolationFacility(ValidationViolationFacilityException ex) {
            Map<String, String> body = new HashMap<>();
            body.put("message", ex.getMessage());
            return ResponseEntity.badRequest().body(body);
        }


    }


}
