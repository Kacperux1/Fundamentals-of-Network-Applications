package pl.facility_rental.user.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.facility_rental.facility.dto.CreateFacilityDto;
import pl.facility_rental.facility.dto.ReturnedFacilityDto;
import pl.facility_rental.facility.mvc.FacilityRestClient;
import pl.facility_rental.user.business.UserService;
import pl.facility_rental.user.business.model.Administrator;
import pl.facility_rental.user.business.model.Client;
import pl.facility_rental.user.business.model.ResourceMgr;
import pl.facility_rental.user.business.model.User;
import pl.facility_rental.user.dto.ReturnedUserDto;
import pl.facility_rental.user.dto.admin.mappers.AdminMapper;
import pl.facility_rental.user.dto.client.mappers.ClientMapper;
import pl.facility_rental.user.dto.manager.mappers.ResourceMgrMapper;
import pl.facility_rental.user.exceptions.RecognizingUserTypeException;

import java.util.List;

@Controller
@RequestMapping("view/users")
public class UserViewController {

    private final UserRestClient userClient;

    public UserViewController(UserRestClient userClient) {
        this.userClient = userClient;
    }

    @GetMapping
    public String getAllFacilities(Model model) {
        model.addAttribute("users", userClient.getAll());
        return "userList";
    }

    @PostMapping("/delete/{id}")
    public String deleteFacility(@PathVariable String id,
                                 RedirectAttributes redirectAttributes) {
        try {
            userClient.deleteById(id);
            redirectAttributes.addFlashAttribute("Message", "Usunięto użytkownika");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "Message",
                    "Nie można usunąć użytkownika"
            );
        }
        return "redirect:/view/users";
    }

    @GetMapping("/add")
    public String getAddUser(Model model) {return "addUser";}

    @GetMapping("/add/client")
    public String getAddClient(Model model) {return "addClient";}

    @GetMapping("/add/admin")
    public String getAddAdmin(Model model) {return "addAdmin";}

    @GetMapping("/add/manager")
    public String getAddResourceMgr(Model model) {return "addResourceMgr";}

    @PostMapping("/add")
    public ReturnedUserDto addFacility(CreateFacilityDto facility) {
        ReturnedUserDto newUser = userClient.addFacility(facility);
        return newUser;
    }

}
