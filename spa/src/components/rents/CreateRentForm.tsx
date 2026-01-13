import {useState, useEffect} from 'react';
import type {Client, Facility, RentForm, Rent} from '../../utils/typedefs.ts';
import getAllFacilities from '../facilities/services/FacilityService.ts';
import {createRent} from "./services/RentService.ts";
import {getAllClients} from '../users/services/UserService.ts'
import * as yup from 'yup';

function CreateRentForm() {

    const [currentClients, setCurrentClients] = useState<Client[]>([]);
    const [currentFacilities, setCurrentFacilities] = useState<Facility[]>([]);

    const [selectedClientId, setSelectedClientId] = useState<string>('');
    const [selectedFacilityId, setSelectedFacilityId] = useState<string>('');
    const [chosenStartDate, setChosenStartDate] = useState<string>(``);
    const [chosenEndDate, setChosenEndDate] = useState<string>('');
    const [validationError, setValidationError] = useState<string>('');

    //const [confirmedRentCreation, setConfirmedRentCreation] = useState<boolean>(false);

    //const [openRentCreationPopup, setOpenRentCreationPopup] = useState(false);

    const createRentValidationSchema = yup.object({
        facilityId: yup.string().required("Podanie rezerwowanego obiektu jest wymagane!"),
        clientId: yup.string().required("Podanie zamawiającego klienta jest wymagane"),
        startDate: yup.string().required("Data rozpoczęcia jest wymagana do rezerwacji")
    })

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
    }, [])

    function sendCreateRentData(data: RentForm) {
        const chosenClient = currentClients.find((client:Client) => client.id === data.clientId);
        const chosenFacility = currentFacilities.find((facility:Facility) => facility.id === data.facilityId);
        if(!window.confirm(`Na pewno chcesz dodać wypożyczenie: ${chosenClient?.first_name}, ${chosenClient?.last_name} wypożycza 
        ${chosenFacility?.name} od ${data.startDate}` + data.endDate!==''? `do ${data.endDate}`:'' + '?')) {
            resetForm();
            return;
        }
        createRent(data).then((rent: Rent) => {
            alert(`Wypożyczenie o ID ${rent.rentId}`);
        }).catch((err) => {
            alert(err.body.message)
        });
    }

    async function handleCreateRentSubmit() {
        setValidationError('');
        const data: RentForm = {
            clientId: selectedClientId,
            facilityId: selectedFacilityId,
            startDate: chosenStartDate,
            endDate: chosenEndDate,
        }
        try {
            await createRentValidationSchema.validate(data);
        } catch (error) {
            if(error instanceof Error) {
                setValidationError(error.message);
            }
            return;
        }
        sendCreateRentData(data);
    }

    function resetForm() {
        setSelectedClientId('');
        setSelectedFacilityId('');
        setChosenStartDate('');
        setChosenEndDate('');
    }
//toDo: pytanie do dr. chomatka: czemu jak ustawilem ciemne tło to sie naprawiło jego mać?
    //w sensie ze w select-cie nie bylo widac innych opcji niz ta na ktorej był hover

    return (
        <div className="w-3/4 ">
            {validationError && <h2>Niepoprawne dane: {validationError}</h2>}
            <form onSubmit={ (e) => {e.preventDefault(); handleCreateRentSubmit().then()}} id="rent-form"
                      className="flex flex-col items-center m-4">
                <label htmlFor="start-date-input" className="m-4">Podaj datę początkową:</label>
                <input onChange={e => {
                    setChosenStartDate(e.target.value)
                }}
                       type="datetime-local" name="start-date" id="start-date-input " className="w-full m-4"/>
                <label htmlFor="end-date-input" className="m-4">Podaj datę końcową (opcjonalnie):</label>
                <input onChange={e => {
                    setChosenEndDate(e.target.value)
                }}
                       type="datetime-local" name="end-date" id="end-date-input" className="w-full m-4"/>
                <label htmlFor="client-select" className="m-4">Wybierz rezerwującego klienta:</label>
                <select onChange={e => setSelectedClientId(e.target.value)}
                        form="rent-form" className="bg-[#242424] w-full m-4" id="client-select">
                    <option value="" className="bg-[#242424] w-full m-4"></option>
                    {currentClients.map((client) => (
                        <option key={client.id} value={client.id}>{client.login}</option>
                    ))}
                </select>
                <label htmlFor="facility-select" className="m-4">Wybierz rezerwowany obiekt:</label>
                <select onChange={e => setSelectedFacilityId(e.target.value)}
                        form="rent-form" className="bg-[#242424] w-full m-4" id="facility-select">
                    <option value="" className="bg-[#242424] w-full m-4"></option>
                    {currentFacilities.map((facility) => (
                        <option key={facility.id} value={facility.id}>{facility.name}, {facility.street}
                            {facility.streetNumber} {facility.postalCode} {facility.city}, {facility.price} zł/godz.
                        </option>
                    ))}
                </select>
                <button type="submit"  className="bg-green-500 w-1/2">Stwórz rezerwację</button>
                <button type="reset"  className="bg-green-500 w-1/2" onClick={() => {resetForm()}}>Wyczyść formularz</button>
            </form>
            {}


        </div>
    )
}

export default CreateRentForm;