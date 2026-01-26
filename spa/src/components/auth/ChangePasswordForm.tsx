import {useContext, useState} from "react";
import {UserContext} from "../users/context/UserContext.ts";
import type {changePasswordForm} from "../../utils/typedefs.ts";
import * as yup from "yup";
import {changePassword} from "./services/AuthService.ts";
import {useNavigate} from "react-router-dom";


function ChangePasswordForm() {

    const [typedCurrentPassword, setTypedCurrentPassword] = useState<string>('');
    const [typedNewPassword, setTypedNewPassword] = useState<string>('');
    const [validationErrorMessage, setValidationErrorMessage] = useState<string>('');
    const navigate = useNavigate();

    const context = useContext(UserContext);
    const {payload} = context!;

    const createFormValidationSchema = yup.object(({
        password: yup.string().required("Podaj obecne hasło!"),
        newPassword: yup.string()
            .min(4, "Hałso musi być mieć co najmniej 4 znaki!")
            .required("Podaj nowe hasło!")
    }));

    async function handleChangePassword() {
        const newPasswordData: changePasswordForm = {
            login: payload?.sub,
            password: typedCurrentPassword,
            newPassword: typedNewPassword
        }
        try {
            await createFormValidationSchema.validate(newPasswordData, {abortEarly: true});
        } catch (error) {
            if (error instanceof Error) {
                setValidationErrorMessage(error.message);
            } else {
                setValidationErrorMessage("wystompił nieznany błont");
            }
            return;
        }
        sendDataToChangePassword(newPasswordData);
    }

    function sendDataToChangePassword(passwordData: changePasswordForm) {
        if (!window.confirm(`Na pewno chcesz zmienić hałso?`)) {
            return;
        }
        changePassword(passwordData).then(() => {
            navigate("/");
            alert(`Hasuo zostauo zmienione`);
            setValidationErrorMessage("");
        })
    }



    return (
        <div className="flex flex-col justify-center items-center">
            <h1>Zmiana hasła</h1>
            <h2> {validationErrorMessage}</h2>
            <form onSubmit={(e) => {
                e.preventDefault();
            handleChangePassword().then(e => console.log(e));}}>
                <label htmlFor="current-password">Obecne hasło:</label>
                <input type = "password" placeholder="obecne hasło" id="current-password" onChange={(e) => {
                    setTypedCurrentPassword(e.target.value);
                }}/>
                <label htmlFor="new-password">Nowe hasło:</label>
                <input type = "password" placeholder="Nowe hasło" id="new-password" onChange={(e) => {
                    setTypedNewPassword(e.target.value);
                }}/>
                <button type="submit" className="border-8 border-b-fuchsia-500">Zmień hasło</button>
            </form>
        </div>

    )
}

export default ChangePasswordForm;