import  axios  from 'axios';

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



