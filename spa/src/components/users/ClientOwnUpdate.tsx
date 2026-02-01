import {useState, useEffect, useContext} from 'react';
import {UserContext} from "./context/UserContext.ts";
import * as yup from "yup";
import type {ClientOwnUpdateData, UserEtag} from "../../utils/typedefs.ts";
import {getUserByLogin, updateSelf} from "./services/UserService.ts";
import {useNavigate} from "react-router-dom";

function ClientOwnUpdate () {

    const [typedEmail, setTypedEmail] = useState<string>('');
    const [typedPhone, setTypedPhone] = useState<string>('');
    const [typedFirstName, setTypedFirstName] = useState<string>('');
    const [typedLastName, setTypedLastName] = useState<string>('');
    const [validationMessage, setValidationMessage] = useState<string>('');
    const context = useContext(UserContext);
    const {payload} = context!;
    const navigator = useNavigate();


    const updateClientValidationSchema = yup.object(({
        email: yup.string().email("Niewłaściwy format emaila")
            .max(50, "Email jest zbyt długi (max 50 znaków"),
        firstName: yup.string()
            .matches(/^[a-zA-Z]+$/, "Imię może się skłądać tylko z liter!"),
        lastName: yup.string()
            .matches(/^[a-zA-Z]+$/, "Nazwisko może się skłądać tylko z liter!"),
        phoneNumber: yup.string().length(9, "Niepoprawna długość nr. telefonu")
            .matches(/^[0-9]+$/, "Nr telefonu może się składać tylko z cyfr")
    }));


   function sendDataToUpdateSelf(data: ClientOwnUpdateData ) {
        if(!data) {
            alert("Błont!!!!");
            return;
        }
        getUserByLogin(payload!.sub!).then((user: UserEtag) => {
            if (!window.confirm(`Na pewno chcesz dokonać zmian w informacjach wskazanych w formularzu?`)) {
                return;
            }
            updateSelf(data, user.etag).then(() => {alert("Dokonano zmian."); navigator("/myAccount")});
            }
        )
    }

    async function handleSubmit(){
        console.log("HANDLE SUBMIT");
        setValidationMessage('');
        const userNewData : ClientOwnUpdateData= {
            type: "client",
            firstName: typedFirstName? typedFirstName : null,
            lastName: typedLastName? typedLastName : null,
            email: typedEmail? typedEmail :  null,
            phoneNumber: typedPhone? typedPhone : null,
        }
        try{
            await updateClientValidationSchema.validate(userNewData, {abortEarly: true});
        } catch (error) {
            if(error instanceof Error) {
                console.log("VALIDATION ERROR", error);
                setValidationMessage(error.message);
            } else {
                setValidationMessage("ystompiłe nieznany błont");
            }
            return;
        }

        console.log("VALIDATION OK");
        sendDataToUpdateSelf(userNewData);
    }

    function resetForm(){
       setValidationMessage('');
       setTypedEmail('');
       setTypedPhone('');
       setTypedFirstName('');
       setTypedLastName('');
    }


    return(
        <>
            <h2>wprowadź dane, które chcesz zaktualizować:</h2>
            <form onSubmit={(e) => {e.preventDefault(); handleSubmit();}}>
                <label htmlFor="firstname-field">Imię:</label>
                <input type="text" id="firstname-field"
                       onChange={e => setTypedFirstName(e.target.value)}/>
                <label htmlFor="lastname-field">Nazwisko:</label>
                <input type="text" id="lastname-field"
                       onChange={e => setTypedLastName(e.target.value)}/>
                <label htmlFor="email-field">E-mail:</label>
                <input type ="text" id="email-field" onChange={e => setTypedEmail(e.target.value)}/>
                <label htmlFor="phone-field">Telefon:</label>
                <input type="text" id="phone-field"  onChange={e => setTypedPhone(e.target.value)}/>
                <button type ="submit">wyślij</button>
                <button type = "reset" onClick={resetForm}>wyczyść formularz</button>
            </form>
        </>
    )
}

export default ClientOwnUpdate;