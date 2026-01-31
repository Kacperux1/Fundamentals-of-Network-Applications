package pl.facility_rental.rent.endpoints;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.facility_rental.rent.business.RentService;
import pl.facility_rental.rent.dto.CreateRentDto;
import pl.facility_rental.rent.dto.mappers.RentMapper;
import pl.facility_rental.rent.dto.ReturnedRentDto;
import pl.facility_rental.rent.exceptions.*;

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
        //return rentService.getCurrentAndPastClientsRents(clientId).stream().map(rentMapper::getRentDetails).toList();
        return rentService.getClientsRents(clientId).stream().map(rentMapper::getRentDetails).toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ReturnedRentDto deleteRent(@PathVariable String id) throws Exception {
        return rentMapper.getRentDetails(rentService.delete(id));
    }

    @PutMapping("/{id}")
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
