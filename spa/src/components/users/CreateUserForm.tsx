import {useEffect, useState} from 'react';
import type {
    CreateClientData,
    CreateUserFormData,
    UpdateClientData,
    UpdateUserFormData,
    User, UserEtag
} from "../../utils/typedefs.ts";
import {createUser, getUserById, updateUser} from "./services/UserService.ts";
import {useParams} from "react-router-dom";
import * as yup from 'yup';


function CreateUserForm() {

    //const [userToSend, setUserToSend] = useState('');

    const {userId} = useParams<{ userId: string | undefined }>();

    const updateMode: boolean = Boolean(userId);
    const [typedLogin, setTypedLogin] = useState<string>('');
    const [typedEmail, setTypedEmail] = useState<string>('');
    const [chosenType, setChosenType] = useState<string>('');
    const [willBeActive, setWillBeActive] = useState<boolean | null>(null);
    const [clientFirstName, setClientFirstName] = useState<string>('');
    const [clientLastName, setClientLastName] = useState<string>('');
    const [clientPhone, setClientPhone] = useState<string>('');

    const [validationErrorMessage, setValidationErrorMessage] = useState<string>('');
    const [updatedUser, setUpdatedUser] = useState<User | null>(null);
    const [typedInitialPassword, setTypedInitialPassword] = useState<string>('');

    const createFormValidationSchema = yup.object(({
        login: yup.string().required("Login jest wymagany").max(50, "Login jest zbyt długi (max 50 znaków"),
        email: yup.string().email("Niewłaściwy format emaila").required("Email jet wymagany")
            .max(50, "Email jest zbyt długi (max 50 znaków"),
        active: yup.boolean().required("wymagane określenie stanu konta użytkownika"),
        type: yup.string().required("Wymagane podanie typu konta użytkownika"),
    }));

    const createClientValidationSchema = yup.object(({
        first_name: yup.string().required("Imię jest wymagane!")
            .matches(/^[a-zA-Z]+$/, "Imię może się skłądać tylko z liter!"),
        last_name: yup.string().required(("Nazwisko jet wymagane!"))
            .matches(/^[a-zA-Z]+$/, "Nazwisko może się skłądać tylko z liter!"),
        phone: yup.string().length(9, "Niepoprawna długość nr. telefonu").required("Telefon jest wymagany")
            .matches(/^[0-9]+$/, "Nr telefonu może się składać tylko z cyfr")
    }))

    createFormValidationSchema.concat(createClientValidationSchema);


    const updateUserValidationSchema = yup.object(({
        email: yup.string().nullable().notRequired().
        email("Niewłaściwy format emaila")
            .max(50, "Email jest zbyt długi (max 50 znaków"),
    }));

    const updateClientValidationSchema = yup.object(({
        first_name: yup.string().nullable().notRequired()
            .matches(/^[a-zA-Z]+$/, "Imię może się skłądać tylko z liter!"),
        last_name: yup.string().nullable().notRequired()
            .matches(/^[a-zA-Z]+$/, "Nazwisko może się skłądać tylko z liter!"),
        phone: yup.string().nullable().notRequired()
            .length(9, "Niepoprawna długość nr. telefonu")
            .matches(/^[0-9]+$/, "Nr telefonu może się składać tylko z cyfr")
    }));

    updateUserValidationSchema.concat(updateClientValidationSchema);

    function sendDataToCreateUser(userData: CreateUserFormData) {
        if (!window.confirm(`Na pewno chcesz dodać użytkownika o loginie ${userData.login}, emailu: ${userData.email}, statusie:`
            + (userData.active ? `aktywnym` : `nieaktywnym`) + 'roli: ' +
            (userData.type === 'client' ? 'klient' : userData.type === 'administrator' ? 'administrator' : 'pracownik') + ' ?')) {
            return;
        }
        createUser(userData).then((user: User) => {
            alert(`Użytkownik o loginie ${user.login} został dodany do systemu.`);
        })
    }

    function getUpdatedUserInfo(userId: string | undefined) {
        if (userId === undefined) {
            return;
        }
        getUserById(userId).then((user: UserEtag) => {
            setUpdatedUser(user);
        })
    }

    useEffect(() => {
        getUpdatedUserInfo(userId);
    }, [userId])

    function sendDataToUpdateUser(userData: UpdateUserFormData) {
        if (userId === undefined) {
            alert("Błont!!!! ni ma id usera");
            return;
        }
        getUserById(userId).then((user: UserEtag) => {
            if (!window.confirm(`Na pewno chcesz dodać użytkownika o bieżącym loginie ${user.login} ?`)) {
                return;
            }
            if (userId) {
                updateUser(userId, userData, user.etag).then((user: User) => {
                    alert(`zaktualizowano użytkownika o ID ${user.id}`);
                })
            }
        })


    }


    async function handleCreationSubmit() {
        setValidationErrorMessage('');
        let userData: CreateUserFormData = {
            login: typedLogin,
            email: typedEmail,
            active: willBeActive,
            type: chosenType,
            password: typedInitialPassword
        }
        if (chosenType === "client") {
            userData = {
                login: typedLogin,
                email: typedEmail,
                active: willBeActive,
                type: chosenType,
                first_name: clientFirstName,
                last_name: clientLastName,
                phone: clientPhone,
                password: typedInitialPassword
            } as CreateClientData;

        }

        try {
            if (chosenType === "client") {
                await createClientValidationSchema.validate(userData, {abortEarly: true});
            } else {
                await createFormValidationSchema.validate(userData, {abortEarly: true});
            }
        } catch (error) {
            if (error instanceof Error) {
                setValidationErrorMessage(error.message);
            } else {
                setValidationErrorMessage("wystompił nieznany błont");
            }

            return;
        }
        sendDataToCreateUser(userData);
    }

    async function handleUpdateSubmit() {
        if (!typedLogin && !typedEmail && !clientFirstName && !clientLastName && !clientPhone) {
            alert("Wpisz jakieś dane do aktualizacji!");
            return;
        }
        setValidationErrorMessage('');
        let userData: UpdateUserFormData = {
            email: typedEmail === '' ? null : typedEmail,
            active: willBeActive === null ? null : willBeActive,
            type: updatedUser!.type
        }
        if (updatedUser?.type === "client") {
            userData = {
                email: typedEmail === '' ? null : typedEmail,
                first_name: clientFirstName === '' ? null : clientFirstName,
                last_name: clientLastName === '' ? null : clientLastName,
                phone: clientPhone === '' ? null : clientPhone,
                active: willBeActive === null ? null : willBeActive,
                type: updatedUser!.type
            } as UpdateClientData;
        }
        try {
            if (updatedUser?.type === "client") {
                await updateClientValidationSchema.validate(userData, {abortEarly: true});
            } else {
                await updateUserValidationSchema.validate(userData, {abortEarly: true});
            }
        } catch (error) {
            if (error instanceof Error) {
                setValidationErrorMessage(error.message);
            } else {
                setValidationErrorMessage('Wystompił nieznay błont');
            }
            return;

        }
        sendDataToUpdateUser(userData);
    }

    function resetForm() {
        setTypedLogin('');
        setTypedEmail('');
        setWillBeActive(null);
        setClientPhone('');
        setClientFirstName('');
        setClientLastName('');
        setChosenType('');
    }

    return (
        <div className="flex flex-col items-center mx-auto w-1/2">
            {updateMode && <h2>Aktualizacja użytkownika o loginie {updatedUser?.login}</h2>
            }
            {validationErrorMessage !== '' &&
                <h2>{validationErrorMessage}</h2>}
            <form onSubmit={(e) => {
                e.preventDefault();
                if (updateMode) {
                    handleUpdateSubmit();
                } else {
                    handleCreationSubmit();
                }
            }}>
                {!updateMode && <><label htmlFor="login-field">Podaj nazwę(login) użytkownika:</label><input
                    onChange={(e) => {
                        setTypedLogin(e.target.value);
                    }}
                    type="text" id="login-field" name="login-field" className="w-full"/></>}

                <label htmlFor="email-field"> Podaj adres email:</label>
                <input onChange={(e) => setTypedEmail(e.target.value)}
                       type="text" id="email-field" name="email-field" className="w-full"/>
                {!updateMode &&
                    <><label htmlFor="password-field">Podaj początkowe hasło do konta:</label>
                        <input type="password" id="password-field" name="password-field" className="w-full"
                               onChange={(e) => {
                                   setTypedInitialPassword(e.target.value);
                               }}/></>
                }
                {!updateMode && <fieldset>
                    <legend>Wybierz początkowy stan konta:</legend>
                    <label htmlFor="active-option">Aktywny</label>
                    <input onChange={e => {
                        setWillBeActive(e.target.value === "active")
                    }}
                           type="radio" id="active-option" name="active-field" value="active" className="m-2"/><br/>
                    <label htmlFor="inactive-option">Nieaktywny</label>
                    <input type="radio" id="inactive-option" name="active-field" value="inactive" className="m-2"/><br/>
                </fieldset>}

                {!updateMode && <><label htmlFor="type-selection">Wybierz typ konta użytkownika:</label><select
                    id="type-selection" name="type-selection"
                    onChange={(e) => setChosenType(e.target.value)}>
                    <option value="" className="bg-[#242424] w-full m-4"></option>
                    <option value="client" className="bg-[#242424] w-full m-4">Klient</option>
                    <option value="resourceMgr" className="bg-[#242424] w-full m-4">Pracownik</option>
                    <option value="administrator" className="bg-[#242424] w-full m-4">Administrator</option>
                </select></>}

                {(chosenType === 'client' || updatedUser?.type === 'client') &&
                    <div className="w-full flex flex-col  m-4">
                        <label htmlFor="first-name-field" className="ml-0">Podaj imię klienta:</label>
                        <input onChange={(e) => {
                            setClientFirstName(e.target.value)
                        }}
                               type="text" id="first-name-field" name="first-name-field"/>
                        <label htmlFor="last-name-field">Podaj nazwisko klienta:</label>
                        <input onChange={(e) => {
                            setClientLastName(e.target.value)
                        }}
                               type="text" id="last-name-field" name="last-name-field"/>
                        <label htmlFor="last-name-field">Podaj numer telefona klienta:</label>
                        <input onChange={(e) => {
                            setClientPhone(e.target.value)
                        }}
                               type="text" id="phone-field" name="phone-field"/>
                    </div>
                }

                <button type="submit">{updateMode ? "zaktualizuj użytkownika" : "Dodaj użytkownika"}</button>
                <button type="reset" onClick={() => resetForm()}>Wyczyść formularz</button>
            </form>
        </div>
    )
}

export default CreateUserForm;