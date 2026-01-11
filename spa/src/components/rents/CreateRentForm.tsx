import {useState, useEffect} from 'react';
import type {Client, Facility, RentForm, Rent} from '../../utils/typedefs.ts';
import getAllFacilities from '../facilities/services/FacilityService.ts';
import {createRent} from "./services/RentService.ts";
import {getAllClients} from '../users/services/UserService.ts'

function CreateRentForm() {

    const [currentClients, setCurrentClients] = useState<Client[]>([]);
    const [currentFacilities, setCurrentFacilities] = useState<Facility[]>([]);

    const [selectedClientId, setSelectedClientId] = useState<string>('');
    const [selectedFacilityId, setSelectedFacilityId] = useState<string>('');
    const [chosenStartDate, setChosenStartDate] = useState<string>(``);
    const [chosenEndDate, setChosenEndDate] = useState<string|null>(null);


    const [confirmedRentCreation, setConfirmedRentCreation] = useState<boolean>(false);

    const [openRentCreationPopup, setOpenRentCreationPopup] = useState(false);

    //const [formData, setFormData] = useState<RentForm>();

    function updateFacilityList() {
        getAllFacilities().then((response) => {
            setCurrentFacilities(response);
        })
    }

    function updateClientList() {
        getAllClients().then((response) => {
            setCurrentClients(response);
        })
    }
    useEffect(() => {
        updateClientList();
        updateFacilityList();
    },[])

    function sendCreateRentData() {
        const data: RentForm = {
            clientId: selectedClientId,
            facilityId: selectedFacilityId,
            startDate: new Date(chosenStartDate),
            endDate: undefined
        }
        if(chosenEndDate && chosenStartDate !== ''){
            data.endDate = new Date(chosenEndDate);
        }
        createRent(data).then((rent:Rent) => {
            alert(`Wypożyczenie o ID ${rent.rentId}`);
        })
    }

//toDo: pytanie do dr. chomatka: czemu jak ustawilem ciemne tło to sie naprawiło jego mać?
    //w sensie ze w select-cie nie bylo widac innych opcji niz ta na ktorej był hover

    return(
        <div className="w-3/4 ">
            <form onSubmit={sendCreateRentData} id="rent-form"
             className ="flex flex-col justify-center items-center m-4">
                <label htmlFor="start-date-input" className="m-4">Podaj datę początkową:</label>
                <input onChange={e => {setChosenStartDate(e.target.value)}}
                    type="datetime-local" name="start-date" id="start-date-input "  className="w-full m-4"/>
                <label htmlFor="end-date-input" className="m-4">Podaj datę początkową (opcjonalnie):</label>
                <input onChange={e => {setChosenEndDate(e.target.value)}}
                       type="datetime-local" name="end-date" id="end-date-input" className="w-full m-4"/>
                <label htmlFor="client-select" className="m-4">Wybierz rezerwującego klienta:</label>
                <select onChange={e => setSelectedClientId(e.target.value)}
                        form="rent-form" className="bg-[#242424] w-full m-4" id="client-select">
                    {currentClients.map((client) => (
                        <option key={client.id} value={client.id}>{client.login}</option>
                    ))}
                </select>
                <label htmlFor="facility-select" className="m-4">Wybierz rezerwowany obiekt:</label>
                <select onChange={e => setSelectedFacilityId(e.target.value)}
                        form="rent-form" className="bg-[#242424] w-full m-4" id="facility-select">
                    {currentFacilities.map((facility) => (
                        <option key={facility.id} value={facility.id}>{facility.name}, {facility.street}
                            {facility.streetNumber} {facility.postalCode} {facility.city}, {facility.price} zł/godz.
                        </option>
                    ))}
                </select>
                <button type="submit" className="bg-green-500">Stwórz rezerwację</button>
            </form>
            {}


        </div>
    )
}

export default CreateRentForm;