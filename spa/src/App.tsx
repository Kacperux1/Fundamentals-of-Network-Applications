import {useEffect, useState} from 'react'
import './App.css'
import MyButton from './MyButton'
import axios from 'axios';
import RentList from "./RentList.tsx";
import AddFacilityForm from "./AddFacilityForm.tsx";

axios.defaults.baseURL = 'http://localhost:8080';
function App() {
  //const [count, setCount] = useState(0)
    const [users, setUsers] = useState<User[]>([]);
    async function getUsers() {
        try{
            const response = await axios.get('/users');
            return response.data;
        } catch (error) {
            alert(error.response.data.message);
        }
    }
    useEffect(() => {getUsers().then(r => setUsers(r));}, []);

    interface User {
        id: string;
        login: string;
        email: string;
    }


  return (
    <>
      <div>
          <h1>Welcome to my app</h1>
        <MyButton/>
          <ul>
              {users.map(user => (
                  <li key={user.id}>
                      {user.id}, {user.login}, {user.email}
                  </li>
              ))}
          </ul>
      </div>
        <div>
            <RentList/>
        </div>
        <div>
            <AddFacilityForm/>
        </div>
    </>
  )
}

export default App
