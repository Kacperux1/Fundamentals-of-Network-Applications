package pl.facility_rental.rent.endpoints;

import pl.facility_rental.rent.business.RentService;
import pl.facility_rental.rent.dto.mappers.RentMapper;


public class RentViewController {

    private final RentService rentService;
    private final RentMapper rentMapper;

    public RentViewController(RentService rentService, RentMapper rentMapper) {
        this.rentService = rentService;
        this.rentMapper = rentMapper;
    }



}
