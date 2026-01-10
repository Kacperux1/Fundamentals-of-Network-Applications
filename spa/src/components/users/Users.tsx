import {useState, useEffect} from 'react';
import {getAllUsers} from './services/UserService.ts';
import type {User} from '../../utils/typedefs.ts';


function Users ()  {

    const [currentUsers, setCurrentUsers ] = useState<User[]>([]);


    function updateUserList() {
        getAllUsers().then((users:User[]) => {
            setCurrentUsers(users);
        })
    }

    useEffect(() => {
        updateUserList();
    },[])
    return(
        <>
            <h2>Lista użytkowników</h2>
           <ul>
               {currentUsers.map((user:User) => (
                   <li key={user.id} className=" m-2 rounded-xl border-2 border-yellow-600 text-lg h-15 p-4">
                       {user.login}, {user.email}, {user.type ==="client"? "klient": user.type ==="resourceMgr"?
                       "pracownik": "administrator"},
                    {user.active? " aktywny": " nieaktywny"}
                   </li>
               ))}
           </ul>
        </>
    )
}

export default Users;