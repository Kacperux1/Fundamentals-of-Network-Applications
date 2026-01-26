import  axios  from 'axios';
import type {CreateUserFormData, UpdateUserFormData} from "../../../utils/typedefs.ts";

export async function getAllUsers(){

    const response = await axios.get('/users');
    return response.data;
}

export async function getAllClients(){
    const response = await axios.get('/users/clients');
    return response.data;
}


export async function getUserById(id:string){
    const response = await axios.get(`/users/${id}`);
    return response.data;
}

export async function getUserByLogin(login:string){
    const response = await axios.get(`/users/${login}`);
    return response.data;
}

export async function createUser(user:CreateUserFormData) {
    const response = await axios.post('/users', user);
    return response.data;
}


export async function activateUser(id:string){
    const response = await axios.patch(`/users/activate/${id}`);
    return response.data;
}

export async function deactivateUser(id:string){
    const response = await axios.patch(`/users/deactivate/${id}`);
    return response.data;
}

export async function updateUser(id:string, userData:UpdateUserFormData) {
    const response = await axios.put(`/users/${id}`,userData);
    return response.data;
}





