package pl.facility_rental.facility.endpoints;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.facility_rental.facility.business.FacilityService;
import pl.facility_rental.facility.dto.ReturnedFacilityDto;
import pl.facility_rental.facility.dto.mappers.FacilityMapper;

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

        return "facilityList";
    }

    @PostMapping("/delete/{id}")
    public String deleteFacility(@PathVariable String id) {
        facilityService.deleteById(id);
        return "redirect:/view/facilities";
    }

}
