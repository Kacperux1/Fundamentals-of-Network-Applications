import {useContext, useEffect, useState} from "react";
import type {Facility} from "../../utils/typedefs.ts";
import {UserContext} from "../users/context/UserContext.ts";
import getAllFacilities from "./services/FacilityService.ts";
import {NavLink, Outlet} from "react-router-dom";


function Facilities() {

    const [currentFacilities, setCurrentFacilities] = useState<Facility[]>([])

    const context = useContext(UserContext);

    const {payload} = context!;

    function updateCurrentRents() {
        getAllFacilities().then((facilities: Facility[]) => {
            setCurrentFacilities(facilities);
        })
    }

    useEffect(() => {
        updateCurrentRents();
    }, [payload])


    return(
        <div className="w-3/5">
            <ul>
                {currentFacilities.map((facility: Facility) => (
                    <li key={facility.id} className=" m-2 rounded-xl border-2 border-yellow-600 text-lg h-25 p-4"> {facility.name} , {facility.street} {facility.streetNumber} {facility.postalCode}
                        {facility.city}, {facility.price} zł/h
                    </li>
                ))}
            </ul>
            {(payload?.roles?.includes("ResourceMgr") || payload?.roles?.includes("Administrator"))
                && <><NavLink to="createFacility">
                    <button className="border-4 border-b-fuchsia-600">Dodaj nowy obiekt sportowy</button>
                </NavLink><Outlet/></>
            }
        </div>
    )
}

export default Facilities;