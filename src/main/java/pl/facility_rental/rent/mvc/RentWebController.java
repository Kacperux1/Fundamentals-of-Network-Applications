package pl.facility_rental.rent.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.facility_rental.facility.mvc.FacilityRestClient;
import pl.facility_rental.rent.business.Rent;
import pl.facility_rental.rent.dto.CreateRentDto;
import pl.facility_rental.user.dto.ReturnedUserDto;
import pl.facility_rental.user.mvc.UserRestClient;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/view/rents")
public class RentWebController {

    private final RentRestClient rentRestClient;

    private final UserRestClient userRestClient;

    private final FacilityRestClient facilityRestClient;


    public RentWebController(RentRestClient rentRestClient, UserRestClient userRestClient, FacilityRestClient facilityRestClient) {

        this.rentRestClient = rentRestClient;
        this.userRestClient = userRestClient;
        this.facilityRestClient = facilityRestClient;
    }

    @GetMapping
    public String viewRents(Model model) {
        model.addAttribute("rents", rentRestClient.getAll());
        model.addAttribute("users", userRestClient.getAll());
        return "/rent/RentList";
    }

    @GetMapping("/add")
    public String getAddRent(Model model) {
        var clients = userRestClient.getAllClients();
        var activeClients = clients.stream().filter(ReturnedUserDto::isActive).toList();
        model.addAttribute("clients", activeClients);
        model.addAttribute("facilities", facilityRestClient.getAll());
        return "/rent/AddRent";
    }

    @PostMapping("/add")
    public String addRent(Model model, RedirectAttributes redirectAttributes,
                          @RequestParam String clientId,
                          @RequestParam String facilityId,
                          @RequestParam LocalDateTime startDate,
                          @RequestParam(required = false) LocalDateTime endDate) {
        var addedRent = CreateRentDto.builder()
                .clientId(clientId)
                .facilityId(facilityId)
                .startDate(startDate)
                .endDate(endDate)
                .build();
        try {
            rentRestClient.addRent(addedRent);
            redirectAttributes.addFlashAttribute("Message", "Dodano wypożyczenie");
            return "redirect:/view/rents";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "rent/AddRent";
        }
    }
}
