import {useEffect, useState} from 'react';
import type {
    CreateClientData,
    CreateUserFormData,
    UpdateClientData,
    UpdateUserFormData,
    User
} from "../../utils/typedefs.ts";
import {createUser, getUserById, updateUser} from "./services/UserService.ts";
import {useParams} from "react-router-dom";


function CreateUserForm() {

    //const [userToSend, setUserToSend] = useState('');

    const {userId} = useParams<{userId: string|undefined}>();

    const updateMode:boolean = Boolean(userId);
    const [typedLogin, setTypedLogin] = useState<string>('');
    const [typedEmail, setTypedEmail] = useState<string>('');
    const [chosenType, setChosenType] = useState<string>('');
    const [willBeActive, setWillBeActive] = useState<boolean>(false);
    const [clientFirstName, setClientFirstName] = useState<string>('');
    const [clientLastName, setClientLastName] = useState<string>('');
    const [clientPhone, setClientPhone] = useState<string>('');

    const [updatedUser, setUpdatedUser] = useState<User | null>(null);


    function sendDataToCreateUser() {

        let userData: CreateUserFormData = {
            login: typedLogin,
            email: typedEmail,
            active: willBeActive,
            type: chosenType
        }
        if (chosenType === "client") {
            userData = {
                login: typedLogin,
                email: typedEmail,
                active: willBeActive,
                type: chosenType,
                first_name: clientFirstName,
                last_name: clientLastName,
                phone: clientPhone
            } as CreateClientData;

        }
        createUser(userData).then((user: User) => {
            alert(`Użytkownik o loginie ${user.login} ostał dodany do systemu.`);
        })
    }

    function getUpdatedUserInfo(userId: string | undefined) {
        if(userId === undefined) {
            return;
        }
        getUserById(userId).then((user: User) => {
            setUpdatedUser(user);
        })
    }
    useEffect(() => {
        getUpdatedUserInfo(userId);
    }, [userId])
    function sendDataToUpdateUser() {
        let userData: UpdateUserFormData = {
            login: typedLogin ===''?null : typedLogin,
            email: typedEmail ===''?null : typedEmail,
        }
        if (updatedUser?.type === "client") {
            userData = {
                login: typedLogin ===''?null : typedLogin,
                email: typedEmail ===''?null : typedEmail,
                first_name: clientFirstName ===''?null : clientFirstName,
                last_name: clientLastName ===''?null : clientLastName,
                phone: clientPhone ===''?null : clientPhone,
            } as UpdateClientData;
        }
        if(userId) {
            updateUser(userId, userData).then((user: User) => {
                alert(`zaktualizowano użytkownika o ID ${user.id}`);
            })
        }

    }


    return (
        <div className="flex flex-col items-center mx-auto w-1/2">
            {updateMode && <h2>Aktualizacja użytkownika o loginie {updatedUser?.login}</h2>
            }
            <form onSubmit={() => updateMode? sendDataToUpdateUser():sendDataToCreateUser() }>
                <label htmlFor="login-field">Podaj nazwę(login) użytkownika:</label>
                <input onChange={(e) => {
                    setTypedLogin(e.target.value)
                }}
                       type="text" id="login-field" name="login-field" className="w-full"/>
                <label htmlFor="email-field"> Podaj adres email:</label>
                <input onChange={(e) => setTypedEmail(e.target.value)}
                       type="text" id="email-field" name="email-field" className="w-full"/>
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

                <button type="submit">{updateMode? "zaktualizuj użytkownika":"Dodaj użytkownika"}</button>
                <button type="reset">Wyczyść formularz</button>
            </form>
        </div>
    )
}

export default CreateUserForm;