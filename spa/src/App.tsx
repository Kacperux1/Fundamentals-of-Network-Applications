import {NavLink, Outlet} from "react-router-dom";
import './App.css'

import {useContext, useEffect} from "react";
import {UserContext} from "./components/users/context/UserContext.ts";
import axios from "axios";
import AccessLevelNameMapping from "./utils/AccessLevelNameMapping.ts";


axios.defaults.baseURL = "http://localhost:8080";
axios.interceptors.request.use(config => {
    const token = sessionStorage.getItem("jwt");
    if (token) {
        config.headers.Authorization = `Bearer ${token}`
    }
    return config;
})

axios.interceptors.response.use(
    config => config,
    (error) => {
        if (error.response?.status === 401) {
            sessionStorage.removeItem("jwt");
            return Promise.reject(error);
        }
    }
)


function App() {

    const {payload, setPayload} = useContext(UserContext)!;

    const roles = payload?.roles || [];

    function handleLogout() {
        setPayload(null);
        sessionStorage.removeItem("jwt");
    }

    useEffect(() => {
        const token = sessionStorage.getItem("jwt");
        if (!token) {
            setPayload(null);
        }
    }, [setPayload]);
    return (
        <>
        <h1 className="flex justify-center">Boiskownia</h1>
        {payload !== null ? <>
                <div className="flex m-4 justify-center">
                    {(roles?.includes("Administrator")
                            || roles?.includes("ResourceMgr")) &&
                        <>
                            <NavLink to="/usersView">
                                <button className=" m-4">
                                    Lista użytkowników
                                </button>
                            </NavLink>
                            <NavLink to="/rentsView">
                                <button className=" m-4">
                                    Lista wypożyczeń
                                </button>
                            </NavLink>
                        </>
                    }
                    <NavLink to="/facilitiesView">
                        <button className=" m-4">Lista obiektów sportowych</button>
                    </NavLink>
                    {payload?.roles?.includes("Client") &&
                        <><NavLink to="createRent">
                            <button className=" m-4 border-b-fuchsia-500">Wypożycz obiekt</button>
                        </NavLink>
                            <NavLink to="myAccount">
                            <button className=" m-4 border-b-fuchsia-500">Moje konto</button>
                        </NavLink>
                        </>
                    }
                    <NavLink to="/">
                        <button className=" m-4">
                            Strona główna
                        </button>
                    </NavLink>
                    <NavLink to="passwordChange">
                        <button className=" m-4 border-b-fuchsia-500">Zmień hasło</button>
                    </NavLink>
                    <button className=" m-4" onClick={handleLogout}>Wyloguj się</button>
                </div>
                <Outlet/></>
            : <>
                <NavLink to="/login">
                    <button className=" m-4">Zaloguj się</button>
                </NavLink>
                <div className="flex flex-col items-center justify-center">
                    <Outlet/>
                </div>
            </>}
        {payload &&
            <footer>
            <p>Witaj, {payload?.sub}</p>
                <p>
                    poziom dostępu: {payload?.roles[0] && payload?.roles[0] in AccessLevelNameMapping
                    ? AccessLevelNameMapping[payload?.roles[0] as keyof typeof AccessLevelNameMapping]
                    : "brak dostępu"}
                </p>
            </footer>
        }

</>
)
}

export default App
