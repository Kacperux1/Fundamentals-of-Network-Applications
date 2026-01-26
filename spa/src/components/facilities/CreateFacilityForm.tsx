import {useState} from "react";
import * as yup from "yup";
import type {Facility, FacilityForm} from "../../utils/typedefs.ts";
import {createFacility} from "./services/FacilityService.ts";
import {useNavigate} from "react-router-dom";


function CreateFacilityForm() {

    const [typedFacilityName, setTypedFacilityName] = useState<string>('');
    const [typedStreet, setTypedStreet] = useState<string>('');
    const [typedCity, setTypedCity] = useState<string>('');
    const [typedStreetNumber, setTypedStreetNumber] = useState<string>('');
    const [typedPostalCode, setTypedPostalCode] = useState<string>('');
    const [typedPrice, setTypedPrice] = useState<string>('');
    const [validationErrorMessage, setValidationErrorMessage] = useState<string>('');

    const navigate = useNavigate();

    const createFormValidationSchema = yup.object(({
        name: yup.string().required("nazwa jest wymagana").max(70, "Nazwa jest za długa (max 70 znaków"),
        street: yup.string().required("Ulica jest wymagana")
            .max(50, "Nazwa ulicy jest zbyt długa (max 50 znaków"),
        streetNumber: yup.string().required("Numer ulicy jest obowiązkowy!"),
        city: yup.string().required("Wymagane podanie miasta!"),
        postalCode: yup.string().required("Wymagana podanie koda pocztowego!")
            .matches(/^\d{2}-\d{3}$/, "Niepoprawny kod pocztowy"),
        basePrice: yup.number().min(1, "cena musi być większa od zera!").required("Nazwa podanie miasta!")
    }));

    async function handleFormSubmit() {
        setValidationErrorMessage('');
        const facilityData: FacilityForm = {
            name: typedFacilityName,
            streetNumber: typedStreetNumber,
            street: typedStreet,
            city: typedCity,
            postalCode: typedPostalCode,
            basePrice: Number(typedPrice)
        }

        try {
            await createFormValidationSchema.validate(facilityData, {abortEarly: true});
        } catch (error) {
            if (error instanceof Error) {
                setValidationErrorMessage(error.message);
            } else {
                setValidationErrorMessage("wystompił nieznany błont");
            }
            return;
        }
        sendDataToCreateFacility(facilityData);
    }

    function sendDataToCreateFacility(facilityData: FacilityForm) {
        if (!window.confirm(`Na pewno chcesz dodać obiekt o nazwie ${facilityData.name}, 
        na ulicy ${facilityData.street} ${facilityData.streetNumber}, o kodzie pocztowym ${facilityData.postalCode} ${facilityData.city},
        o stawce za godzinę ${facilityData.basePrice} zł?`)) {
            return;
        }
        createFacility(facilityData).then((e: Facility) => {
            navigate("/");
            alert(`Obiekt o id ${e.id} został dodany do systemu.`);
        });
    }


    return (
        <div className="w-3/5 flex flex-col items-center justify-center">
            <h2>{validationErrorMessage}</h2>
            <form id="facility-form" className="flex flex-col items-center justify-center"
                  onSubmit={(e) => {
                      e.preventDefault();
                      handleFormSubmit().then(r => console.log(r));
                  }}>
                <label htmlFor="name-field" className="font-bold text-xl">Nazwa obiektu:</label>
                <input type="text" id="name-field" placeholder="nazwa"
                       onChange={(e) => setTypedFacilityName(e.target.value)}/>
                <label htmlFor="street-field" className="font-bold text-xl">Ulica:</label>
                <input type="text" id="street-field" placeholder="Ulica"
                       onChange={(e) => setTypedStreet(e.target.value)}/>
                <label htmlFor="streetNumber-field" className="font-bold text-xl">Numer budynku/lokalu:</label>
                <input type="text" id="streetNumber-field" placeholder="Ulica"
                       onChange={(e) => setTypedStreetNumber(e.target.value)}/>
                <label htmlFor="city-field" className="font-bold text-xl">Miasto:</label>
                <input type="text" id="city-field" placeholder="Miasto"
                       onChange={(e) => setTypedCity(e.target.value)}/>
                <label htmlFor="postalCode-field" className="font-bold text-xl">Kod pocztowy:</label>
                <input type="text" id="postalCode-field" placeholder="kod pocztowy"
                       onChange={(e) => setTypedPostalCode(e.target.value)}/>
                <label htmlFor="price-field" className="font-bold text-xl">Cena za godzinę:</label>
                <input type="number" id="price-field"
                       onChange={(e) => setTypedPrice(e.target.value)}></input>
                <button type="submit" className="btn btn-primary">Dodaj obiekt sportowy</button>
            </form>
        </div>
    )
}

export default CreateFacilityForm;