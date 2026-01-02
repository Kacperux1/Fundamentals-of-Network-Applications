package pl.facility_rental.facility.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.facility_rental.facility.dto.CreateFacilityDto;
import pl.facility_rental.facility.dto.ReturnedFacilityDto;

import java.util.List;

@Controller
@RequestMapping("view/facilities")
public class FacilityViewController {

    private final FacilityRestClient facilityClient;

    public FacilityViewController(FacilityRestClient facilityClient) {
        this.facilityClient = facilityClient;
    }

    @GetMapping
    public String getAllFacilities(Model model) {
        model.addAttribute("facilities", facilityClient.getAll());
        return "facilityList";
    }

    @PostMapping("/delete/{id}")
    public String deleteFacility(@PathVariable String id,
                                 RedirectAttributes redirectAttributes) {
        try {
            facilityClient.deleteById(id);
            redirectAttributes.addFlashAttribute("Message", "Usunięto obiekt");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "Message",
                    "Nie można usunąć obiektu"
            );
        }
        return "redirect:/view/facilities";
    }

    @GetMapping("/add")
    public String getAddFacility(Model model) {
        return "addFacility";
    }

    @PostMapping("/add")
    public void addFacility(@ModelAttribute CreateFacilityDto facility) {
        facilityClient.addFacility(facility);
    }

    @GetMapping("/{id}")
    public String getFacilityById(@PathVariable String id, Model model) {
        ReturnedFacilityDto facility = facilityClient.getById(id);
        model.addAttribute("facility", facility);
        return "updateFacility";
    }
}
