package pl.facility_rental.rent.endpoints;

import org.apache.catalina.connector.Response;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.facility_rental.rent.business.RentService;
import pl.facility_rental.rent.dto.CreateRentDto;
import pl.facility_rental.rent.dto.ReturnedRentDto;
import pl.facility_rental.rent.dto.mappers.RentMapper;
import pl.facility_rental.rent.exceptions.*;
import pl.facility_rental.rent.hateoas.RentAssembler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@CrossOrigin(
        origins = "*",
        allowedHeaders = "*",
        methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE }
)
@RestController
@RequestMapping("/rents")
public class RentController {

    private final RentService rentService;
    private final RentMapper rentMapper;
    private final RentAssembler rentAssembler;


    public RentController(RentService rentService, RentMapper rentMapper, RentAssembler rentAssembler) {
        this.rentService = rentService;
        this.rentMapper = rentMapper;
        this.rentAssembler = rentAssembler;
    }

    @GetMapping
    public ResponseEntity<List<EntityModel<ReturnedRentDto>>>  getAllRents(){
        var rentList  = rentService.findAll();
               return ResponseEntity.ok().body(rentList.stream().map(rentAssembler::wrapDto).toList());
    }

    @PostMapping
    @PreAuthorize("#rentDto.clientId == authentication.name")
    @ResponseStatus(HttpStatus.CREATED)
    public ReturnedRentDto createRent(@RequestBody CreateRentDto rentDto)  {
        return rentMapper.getRentDetails(rentService.save(rentMapper.CreateRentRequest(rentDto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ReturnedRentDto>> getRentById(@PathVariable String id) {
       var rent = rentService.findById(id)
                .orElseThrow(() ->new ResponseStatusException(HttpStatus.NOT_FOUND, "Rent with given id was not found"));
       return ResponseEntity.ok().body(rentAssembler.wrapDto(rent));
    }
    @GetMapping("/client/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ReturnedRentDto> getRentsByClientId(@PathVariable String clientId) {
        //return rentService.getCurrentAndPastClientsRents(clientId).stream().map(rentMapper::getRentDetails).toList();
        return rentService.getClientsRents(clientId).stream().map(rentMapper::getRentDetails).toList();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('Administrator', 'ResourceMgr')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ReturnedRentDto deleteRent(@PathVariable String id){
        return rentMapper.getRentDetails(rentService.delete(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('Administrator', 'ResourceMgr')")
    @ResponseStatus(HttpStatus.OK)
    public ReturnedRentDto endRent(@PathVariable String id)  {
        return rentMapper.getRentDetails(rentService.endRent(id));
    }

    @RestControllerAdvice
    public static class RentExceptionHandler {

        @ExceptionHandler(UserIncativeException.class)
        public ResponseEntity<?> handleUserInactive(UserIncativeException ex) {
            Map<String, String> body = new HashMap<>();
            body.put("message", ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(body);

        }

        @ExceptionHandler(AlreadyAllocatedException.class)
        public ResponseEntity<?> handleAlreadyAllocated(AlreadyAllocatedException ex) {
            Map<String, String> body = new HashMap<>();
            body.put("message", ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(body);
        }

        @ExceptionHandler(AlreadyEndedRentException.class)
        public ResponseEntity<?> handleAlreadyEndedRent(AlreadyEndedRentException ex) {
            Map<String, String> body = new HashMap<>();
            body.put("message", ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(body);
        }

        @ExceptionHandler(BadIdFormatException.class)
        public ResponseEntity<?> handleBadIdFormat(BadIdFormatException ex) {
            Map<String, String> body = new HashMap<>();
            body.put("message", ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(body);
        }

        @ExceptionHandler(CompletedRentDeletionException.class)
        public ResponseEntity<?> handleCompletedRentDeletion(CompletedRentDeletionException ex) {
            Map<String, String> body = new HashMap<>();
            body.put("message", ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(body);
        }

        @ExceptionHandler(RentNotFoundException.class)
        public ResponseEntity<?> handleRentNotFound(RentNotFoundException ex) {
            Map<String, String> body = new HashMap<>();
            body.put("message", ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(body);
        }

        @ExceptionHandler(ValidationViolationRentException.class)
        public ResponseEntity<?> handleValidationViolationRent(ValidationViolationRentException ex) {
            Map<String, String> body = new HashMap<>();
            body.put("message", ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(body);
        }

        @ExceptionHandler(StartDateAfterEndDateException.class)
        public ResponseEntity<?> handleStartDateAfterEndDate(StartDateAfterEndDateException ex) {
            Map<String, String> body = new HashMap<>();
            body.put("message", ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(body);
        }

        @ExceptionHandler(TooLongRentException.class)
        public ResponseEntity<?> handleTooLongRent(TooLongRentException ex) {
            Map<String, String> body = new HashMap<>();
            body.put("message", ex.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(body);
        }







    }

}
