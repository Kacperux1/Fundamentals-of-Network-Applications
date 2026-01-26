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
    const results = response.data;
    results.etag  = response.headers[`etag`];
    return results;
}

export async function getUserByLogin(login:string){
    const response = await axios.get(`/users/login/${login}`);
    return response.data;
}

export async function createUser(user:CreateUserFormData) {

    const response = await axios.post('/users', user);
    return response.data;
}


export async function activateUser(id:string, etag: string){
    console.log(etag);
    etag = etag.replace("\"", "");
    const response = await axios.put(`/users/activate/${id}`, null, {
        headers: {
            'If-Match': etag
        }
    });
    return response.data;
}

export async function deactivateUser(id:string,  etag: string){
    console.log(etag);
    etag = etag.replace("\"", "");
    const response = await axios.put(`/users/deactivate/${id}`, null,  {
        headers: {
            'If-Match': etag
        }
    });
    return response.data;
}

export async function updateUser(id:string, userData:UpdateUserFormData, etag: string) {
    console.log(etag);
    etag = etag.replace("\"", "");
    const response = await axios.put(`/users/${id}`,userData, {
        headers: {
            'If-Match': etag
        }
    });
    return response.data;
}





