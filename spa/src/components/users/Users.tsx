import {useState, useEffect} from 'react';
import {getAllUsers, activateUser, deactivateUser, getUserById} from './services/UserService.ts';
import type {User} from '../../utils/typedefs.ts';
import {NavLink, Outlet} from "react-router-dom";


function Users() {

    const [currentUsers, setCurrentUsers] = useState<User[]>([]);


    function updateUserList() {
        getAllUsers().then((users: User[]) => {
            setCurrentUsers(users);
        })
    }

    useEffect(() => {
        updateUserList();
    }, [])

    function activateChosenUser(id: string) {
        activateUser(id).then(() => {
            updateUserList();
            updateUserList();
            getUserById(id).then((user:User) => {
                alert(`Aktywowano użytkownika: ${user.login}`)
            })
        })
    }
    //do zmiany lub nie wiem zeby nei pobierac z api
    function deactivateChosenUser(id: string) {
        deactivateUser(id).then(() => {
            updateUserList();
            getUserById(id).then((user:User) => {
                alert(`Deaktywowano użytkownika: ${user.login}`)
            })
        })
    }
    return (
        <>
            <h2>Lista użytkowników</h2>

            <ul>
                {currentUsers.map((user: User) => (
                    <li key={user.id} className=" m-2 rounded-xl border-2 border-yellow-600 text-lg h-15 p-4">
                        {user.login}, {user.email}, {user.type === "client" ? "klient" : user.type === "resourceMgr" ?
                        "pracownik" : "administrator"},
                        {user.active ? " aktywny" : " nieaktywny"}
                        {user.active ? <button type="button" className="bg-red" onClick={() => {
                            deactivateChosenUser(user.id);
                            }}>Deaktywuj</button>
                            : <button type="button" className="bg-green"
                                      onClick={() => activateChosenUser(user.id)}>Aktywuj</button>}
                        {user.type == "client" ?
                            <NavLink to={`/${user.id}`}>
                                <button>
                                   Szczegóły
                                </button>
                            </NavLink> : ""}
                    </li>
                ))}
            </ul>
            <NavLink to= "/usersView/createUser">
                <button>Dodaj nowego użytkownika</button>
            </NavLink>
            <Outlet/>
        </>
    )
}

export default Users;