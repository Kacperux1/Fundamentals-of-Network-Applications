package pl.facility_rental.facility.endpoints;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.facility_rental.facility.business.FacilityService;
import pl.facility_rental.facility.data.RedisFacilityRepository;
import pl.facility_rental.facility.dto.ReturnedFacilityDto;
import pl.facility_rental.facility.dto.mappers.FacilityMapper;
import pl.facility_rental.facility.exceptions.RentsForFacilityExistsException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("view/facilities")
public class FacilityViweController {

    private final FacilityService facilityService;

    private final FacilityMapper facilityMapper;

    public FacilityViweController(FacilityService facilityService, FacilityMapper facilityMapper) {
        this.facilityService = facilityService;
        this.facilityMapper = facilityMapper;
    }

    @GetMapping
    public String getAllFacilities(Model model) {
        List<ReturnedFacilityDto> facilities = facilityService.findAll().stream()
                .map(facilityMapper::getFacilityDetails)
                .toList();

        model.addAttribute("facilities", facilities);
        model.addAttribute("breadcrumb", "");

        return "facilityList";
    }

    @PostMapping("/delete/{id}")
    public String deleteFacility(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            facilityService.deleteById(id);
            redirectAttributes.addFlashAttribute("Message", "Usunięto obiekt");
        } catch (RentsForFacilityExistsException e) {
            redirectAttributes.addFlashAttribute("Message", "Obiekt nie może być usunięty - posiada aktywne wypożyczenia");
        }
        return "redirect:/view/facilities";
    }

}
