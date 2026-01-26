import {useState, useEffect, useContext} from 'react';
import {getAllUsers, activateUser, deactivateUser, getUserById} from './services/UserService.ts';
import type {User} from '../../utils/typedefs.ts';
import {NavLink, Outlet} from "react-router-dom";
import {UserContext} from "./context/UserContext.ts";


function Users() {

    const [currentUsers, setCurrentUsers] = useState<User[]>([]);

    const [searchLogin, setSearchLogin] = useState('');

    const context = useContext(UserContext);

    const {payload} = context!;

    const filteredUsers = currentUsers.filter(user =>
        user?.login.toLowerCase().includes(searchLogin.toLowerCase())
    );


    function updateUserList() {
        getAllUsers().then((users: User[]) => {
            setCurrentUsers(users);
        })
    }

    useEffect(() => {
        updateUserList();
    }, [])

    function activateChosenUser(id: string) {
        const chosenUser = currentUsers.find(user => user.id === id);
        if (!window.confirm(`Na pewno chcesz aktywować użytkownika ${chosenUser?.login} ?`)) {
            return;
        }
        activateUser(id).then(() => {
            updateUserList();
            getUserById(id).then((user: User) => {
                alert(`Aktywowano użytkownika: ${user.login}`)
            })
        })
    }

    //do zmiany lub nie wiem zeby nei pobierac z api
    function deactivateChosenUser(id: string) {
        const chosenUser = currentUsers.find(user => user.id === id);
        if (!window.confirm(`Na pewno chcesz deaktywować użytkownika ${chosenUser?.login} ?`)) {
            return;
        }
        deactivateUser(id).then(() => {
            updateUserList();
            getUserById(id).then((user: User) => {
                alert(`Deaktywowano użytkownika: ${user.login}`)
            })
        })
    }

    return (
        <>
            <h2>Lista użytkowników</h2>

            <input
                type="text"
                placeholder="Szukaj po loginie..."
                value={searchLogin}
                onChange={(e) => setSearchLogin(e.target.value)}
                className="border p-2 rounded w-full mb-4"
            />

            <ul>
                {filteredUsers.map((user: User) => (

                    <li key={user.id} className=" m-2 rounded-xl border-2 border-yellow-600 text-lg h-25 p-4">
                        <div className="text-lg">
                            {user.login}, {user.email}, {user.type === "client" ? "klient" : user.type === "resourceMgr" ?
                            "pracownik" : "administrator"},
                            {user.active ? " aktywny" : " nieaktywny"}
                        </div>

                        <div className="flex gap-3 justify-center">
                            {(payload?.roles?.includes("Administrator") && payload?.sub !== user.login) && (
                                <>
                                    {user.active ? (
                                        <button
                                            type="button"
                                            className="bg-red"
                                            onClick={() => deactivateChosenUser(user.id)}
                                        >
                                            Deaktywuj
                                        </button>
                                    ) : (
                                        <button
                                            type="button"
                                            className="bg-green"
                                            onClick={() => activateChosenUser(user.id)}
                                        >
                                            Aktywuj
                                        </button>
                                    )}

                                    <NavLink to={`/usersView/updateUser/${user.id}`}>
                                        <button>Modyfikuj</button>
                                    </NavLink>
                                </>
                            )}
                        {user.type == "client" ?
                            <NavLink to={`/${user.id}`}>
                                <button>
                                    Szczegóły
                                </button>
                            </NavLink> : ""}
                    </div>
                    </li>
                    ))}
            </ul>

            {filteredUsers.length === 0 && (
                <p className="text-gray-500">Brak użytkowników spełniających kryteria</p>
            )}
            {payload?.roles?.includes("Administrator") &&
                <NavLink to="/usersView/createUser">
                    <button>Dodaj nowego użytkownika</button>
                </NavLink>}

            <Outlet/>
        </>
    )
}

export default Users;