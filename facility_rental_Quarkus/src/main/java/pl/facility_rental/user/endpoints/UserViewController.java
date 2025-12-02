package pl.facility_rental.user.endpoints;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    private final UserService userService;
    private final ClientMapper clientMapper;
    private final AdminMapper adminMapper;
    private final ResourceMgrMapper resourceManagerMapper;

    public UserViewController(UserService userService, ClientMapper clientMapper, AdminMapper adminMapper, ResourceMgrMapper resourceManagerMapper) {
        this.userService = userService;
        this.clientMapper = clientMapper;
        this.adminMapper = adminMapper;
        this.resourceManagerMapper = resourceManagerMapper;
    }

    @GetMapping
    public String getAllUsers(Model model){
        List<ReturnedUserDto> users = userService.getAllUsers()
                .stream()
                .map(this::mapSubtypes)
                .toList();

        model.addAttribute("users", users);

        return "userList";
    }


    private ReturnedUserDto mapSubtypes(User user) throws RecognizingUserTypeException {
        if (user instanceof Client) {
            return clientMapper.getClientDetails((Client) user);
        }
        if (user instanceof Administrator) {
            return adminMapper.getAdminDetails((Administrator) user);
        }
        if (user instanceof ResourceMgr) {
            return resourceManagerMapper.getManagerDetails((ResourceMgr) user);
        }
        throw new RecognizingUserTypeException("Error retrieving the user's type.");
    }
}
