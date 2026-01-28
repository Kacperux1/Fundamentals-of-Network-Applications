package pl.facility_rental.user.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.facility_rental.facility.dto.CreateFacilityDto;
import pl.facility_rental.facility.dto.ReturnedFacilityDto;
import pl.facility_rental.facility.mvc.FacilityRestClient;
import pl.facility_rental.user.business.UserService;
import pl.facility_rental.user.business.model.Administrator;
import pl.facility_rental.user.business.model.Client;
import pl.facility_rental.user.business.model.ResourceMgr;
import pl.facility_rental.user.business.model.User;
import pl.facility_rental.user.dto.CreateUserDto;
import pl.facility_rental.user.dto.ReturnedUserDto;
import pl.facility_rental.user.dto.admin.CreateAdminDto;
import pl.facility_rental.user.dto.admin.ReturnedAdminDto;
import pl.facility_rental.user.dto.admin.UpdateAdminDto;
import pl.facility_rental.user.dto.admin.mappers.AdminMapper;
import pl.facility_rental.user.dto.client.CreateClientDto;
import pl.facility_rental.user.dto.client.ReturnedClientDto;
import pl.facility_rental.user.dto.client.UpdateClientDto;
import pl.facility_rental.user.dto.client.mappers.ClientMapper;
import pl.facility_rental.user.dto.manager.CreateResourceMgrDto;
import pl.facility_rental.user.dto.manager.ReturnedResourceMgrDto;
import pl.facility_rental.user.dto.manager.UpdateResourceMgrDto;
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
    public ReturnedUserDto addUser(CreateUserDto user) {
        ReturnedUserDto newUser = userClient.addUser(user);
        return newUser;
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable String id, Model model) {
        ReturnedUserDto user = userClient.getUserById(id);
        if (user instanceof ReturnedClientDto) {
            model.addAttribute("client", user);
            return "updateClient";
        } else if (user instanceof ReturnedAdminDto) {
            model.addAttribute("admin", user);
            return "updateAdmin";
        } else if (user instanceof ReturnedResourceMgrDto) {
            model.addAttribute("mgr", user);
            return "updateResourceMgr";
        } else {
            throw new RecognizingUserTypeException("Nieobsługiwany typ użytkownika");
        }
    }

    @PostMapping("/activate/{id}")
    public String activate(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            ReturnedUserDto user = userClient.activate(id);
            redirectAttributes.addFlashAttribute("Message", "Użytkownik został aktywowany");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("Message",
                    "Nie można aktywować użytkownika z powodu: " + e.getMessage());
        }
        return "redirect:/view/users";
    }

    @PostMapping("/deactivate/{id}")
    public String deactivate(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            ReturnedUserDto user = userClient.deactivate(id);
            redirectAttributes.addFlashAttribute("Message", "Użytkownik został zdezaktywowany");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("Message",
                    "Nie można zdezaktywować użytkownika z powodu: " + e.getMessage());
        }
        return "redirect:/view/users";
    }

    @PostMapping("/add/admin")
    public String addAdmin(@RequestParam String login,
                           @RequestParam String email,
                           @RequestParam(defaultValue = "false") boolean active,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        try {
            CreateAdminDto dto = new CreateAdminDto(login, email, active);
            userClient.addUser(dto);
            redirectAttributes.addFlashAttribute("Message", "Dodano administratora");
            return "redirect:/view/users";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "addAdmin";
        }
    }


    @PostMapping("/add/client")
    public String addClient(@RequestParam String login,
                            @RequestParam String email,
                            @RequestParam String firstName,
                            @RequestParam String lastName,
                            @RequestParam String phone,
                            @RequestParam(defaultValue = "false") boolean active,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        try {
            CreateClientDto dto =
                    new CreateClientDto(login, email, active, firstName, lastName, phone);

            userClient.addUser(dto);
            redirectAttributes.addFlashAttribute("Message", "Dodano klienta");
            return "redirect:/view/users";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "addClient";
        }
    }

    @PostMapping("/add/manager")
    public String addManager(@RequestParam String login,
                             @RequestParam String email,
                             @RequestParam(defaultValue = "false") boolean active,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        try {
            CreateResourceMgrDto dto = new CreateResourceMgrDto(login, email, active);
            userClient.addUser(dto);
            redirectAttributes.addFlashAttribute("Message", "Dodano managera");
            return "redirect:/view/users";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "addResourceMgr";
        }
    }

    @PostMapping("/edit/admin")
    public String updateAdmin(@RequestParam String id,
                              @RequestParam String login,
                              @RequestParam String email,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        try {
            UpdateAdminDto dto = new UpdateAdminDto(login, email);
            userClient.editUser(dto, id);
            redirectAttributes.addFlashAttribute("Message", "Zmieniono administratora");
            return "redirect:/view/users";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "updateAdmin";
        }
    }


    @PostMapping("/edit/client")
    public String updateClient(@RequestParam String id,
                               @RequestParam String login,
                               @RequestParam String email,
                               @RequestParam String firstName,
                               @RequestParam String lastName,
                               @RequestParam String phone,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        try {
            UpdateClientDto dto =
                    new UpdateClientDto(login, email, firstName, lastName, phone);

            userClient.editUser(dto, id);
            redirectAttributes.addFlashAttribute("Message", "Zmieniono klienta");
            return "redirect:/view/users";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "updateClient";
        }
    }


    @PostMapping("/edit/manager")
    public String updateManager(@RequestParam String id,
                                @RequestParam String login,
                                @RequestParam String email,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        try {
            UpdateResourceMgrDto dto = new UpdateResourceMgrDto(login, email);
            userClient.editUser(dto, id);
            redirectAttributes.addFlashAttribute("Message", "Zmieniono managera");
            return "redirect:/view/users";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "updateResourceMgr";
        }
    }

}
