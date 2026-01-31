package pl.facility_rental.rent.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.facility_rental.user.mvc.UserRestClient;

@Controller
@RequestMapping("/view/rents")
public class RentWebController {

    private final RentRestClient rentRestClient;

    private final UserRestClient userRestClient;


    public RentWebController(RentRestClient rentRestClient, UserRestClient userRestClient) {

        this.rentRestClient = rentRestClient;
        this.userRestClient = userRestClient;
    }

    @GetMapping
    public String viewRents(Model model) {
        model.addAttribute("rents", rentRestClient.getAll());
        model.addAttribute("users", userRestClient.getAll());
        return "/rent/RentList";
    }
}
