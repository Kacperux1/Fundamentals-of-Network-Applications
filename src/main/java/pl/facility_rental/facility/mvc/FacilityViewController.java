package pl.facility_rental.facility.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.facility_rental.facility.dto.CreateFacilityDto;
import pl.facility_rental.facility.dto.ReturnedFacilityDto;
import pl.facility_rental.facility.dto.UpdateFacilityDto;

import java.math.BigDecimal;
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
    public String addFacility(@RequestParam String name,
                              @RequestParam(required = false) String streetNumber,
                              @RequestParam String street,
                              @RequestParam String city,
                              @RequestParam(required = false) String postalCode,
                              @RequestParam BigDecimal basePrice,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        try {
            CreateFacilityDto dto = new CreateFacilityDto(
                    name,
                    streetNumber,
                    street,
                    city,
                    postalCode,
                    basePrice
            );

            facilityClient.addFacility(dto);
            redirectAttributes.addFlashAttribute("Message", "Dodano obiekt");
            return "redirect:/view/facilities";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Błąd: " + e.getMessage());
            return "addFacility";
        }
    }

    @PostMapping("/edit")
    public String updateFacility(@RequestParam String facilityId,
                                 @RequestParam String name,
                                 @RequestParam BigDecimal basePrice,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        try {
            UpdateFacilityDto dto = new UpdateFacilityDto(name, basePrice);

            facilityClient.updateFacility(facilityId, dto);

            redirectAttributes.addFlashAttribute("Message", "Zapisano zmiany");
            return "redirect:/view/facilities";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Błąd zapisu: " + e.getMessage());
            return "updateFacility";
        }
    }

    @GetMapping("/{id}")
    public String getFacilityById(@PathVariable String id, Model model) {
        ReturnedFacilityDto facility = facilityClient.getById(id);
        model.addAttribute("facility", facility);
        return "updateFacility";
    }
}
