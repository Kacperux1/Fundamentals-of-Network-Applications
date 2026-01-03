function AddFacilityForm() {

    interface FacilityProps {
        name: string;
        streetNumber: string;
        city: string;
        postalCode: string;
        basePrice: number;
    }

    let givenFacilityProps: FacilityProps = {
        name: "",
        streetNumber: "",
        city: "",
        postalCode: "",
        basePrice: 0,
    }
    return (
        <>
            <form >
                <div className="flex  flex-col mb-4 items-start">
                    <label  htmlFor= "facilityNameField">Nazwa obiektu sportowego:</label>
                    <input type = "text" id = "facilityNameField" placeholder = "Name" className ="mt-4 mb-4 w-100 h-8"/>
                </div>
                <div className="flex flex-col mb-4 items-start">
                    <label  htmlFor= "streetNumberField">Numer budynku/lokalu:</label>
                    <input type = "text" id = "streetNumberField" placeholder = "Street" className ="mt-4 w-100 h-8"/>
                </div>
                <div className="flex flex-col mb-4 items-start">
                    <label  htmlFor= "cityField">Miasto:</label>
                    <input type = "text" id = "cityField" placeholder = "City" className ="mt-4 w-100 h-8" />
                </div>
                <div className="flex flex-col mb-4 items-start">
                    <label  htmlFor= "postalCodeField">Kod pocztowy:</label>
                    <input type = "text" id = "postalCodeField" placeholder = "PostalCode" className ="mt-4 w-100 h-8"/>
                </div>
                <div className="flex flex-col items-start">
                    <label  htmlFor= "basePriceField">Cena za godzinę:</label>
                    <input type = "text" id = "basePriceField" placeholder = "Base Price" className ="mt-4 mb-4 w-100 h-8"/>
                </div>
                <input type = "submit" value="Dodaj"
                       className="bg-blue-600 hover:bg-blue-700 text-white-500 font-semibold py-2 px-20 rounded"/>
            </form>
        </>
    )
}

export default AddFacilityForm;



