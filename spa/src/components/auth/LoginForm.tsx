import {UserContext} from "../users/context/UserContext.ts";
import {useContext, useState} from "react";
import login, {decodePayload} from "./services/AuthService.ts"
import {useNavigate} from "react-router-dom";

function LoginForm() {


    const {setPayload} = useContext(UserContext)!;
    const [typedPassword, setTypedPassword] = useState<string>("");
    const [typedUsername, setTypedUsername] = useState<string>("");
    const navigate = useNavigate();

    async function handleLogin() {
        const result = await login(typedUsername, typedPassword);
        if(result) {
            sessionStorage.setItem("jwt", result.token);
            setPayload(decodePayload(result.token));
            setTypedPassword("");
            setTypedUsername("");
            navigate("/");
        }
    }

    return (
        <div className="flex-col items-center justify-center">
            <form onSubmit={(e) => {
                e.preventDefault();
                handleLogin();
            }}
                  id="login-form" className="justify-center w-1/2 m-4">
                <label htmlFor="login-field" className="w-full m-3 ml-5">Nazwa użytkownika</label>
                <input type="text" placeholder="Login" id="login-field" className="w-full m-2"
                       onChange={e => setTypedUsername(e.target.value)}/>
                <label htmlFor="password-field" className="w-full m-2">Hasło</label>
                <input type="password" placeholder="Hasło" id="password-field" className="w-full m-2"
                       onChange={e => setTypedPassword(e.target.value)}/>
                <button type="submit" className="w-1/2 border-2 border-b-blue-700">Zaloguj się</button>
            </form>
        </div>
    )

}

export default LoginForm;