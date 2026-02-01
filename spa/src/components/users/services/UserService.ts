import axios, {AxiosError} from 'axios';
import type {ClientOwnUpdateData, CreateUserFormData, UpdateUserFormData} from "../../../utils/typedefs.ts";


export async function getAllUsers() {
    try {
        const response = await axios.get('/users');
        return response.data;
    } catch (err) {
        if (err instanceof AxiosError) {
            alert(err.response?.data);
        }
    }
}

export async function getAllClients() {
    try {
        const response = await axios.get('/users/clients');
        return response.data;
    } catch (err) {
        if (err instanceof AxiosError) {
            alert(err.response?.data);
        }
    }
}


export async function getUserById(id: string) {
    try {
        const response = await axios.get(`/users/${id}`);
        const results = response.data;
        results.etag = response.headers[`etag`];
        return results;
    } catch (err) {
        if (err instanceof AxiosError) {
            alert(err.response?.data);
        }
    }
}

export async function getUserByLogin(login: string) {
    try {
        const response = await axios.get(`/users/login/${login}`);
        const results = response.data;
        results.etag = response.headers['etag'];
        console.log(response.headers['etag']);
        return results;
    } catch (err) {
        if (err instanceof AxiosError) {
            alert(err.response?.data);
        }
    }

}

export async function createUser(user: CreateUserFormData) {
    try {
        const response = await axios.post('/users', user);
        return response.data;
    } catch (err) {
        if (err instanceof AxiosError) {
            alert(err.response?.data);
        }
    }
}


export async function activateUser(id: string, etag: string) {
    console.log(etag);
    etag = etag.replace("\"", "");
    try {
        const response = await axios.put(`/users/activate/${id}`, null, {
            headers: {
                'If-Match': etag
            }
        });
        return response.data;
    } catch (err) {
        if (err instanceof AxiosError) {
            alert(err.response?.data);
        }
    }
}

export async function deactivateUser(id: string, etag: string) {
    console.log(etag);
    etag = etag.replace("\"", "");
    try {
        const response = await axios.put(`/users/deactivate/${id}`, null, {
            headers: {
                'If-Match': etag
            }
        });
        return response.data;
    } catch (err) {
        if (err instanceof AxiosError) {
            alert(err.response?.data);
        }
    }

}

export async function updateUser(id: string, userData: UpdateUserFormData, etag: string) {
    etag = etag.replace("\"", "");
    try {
        const response = await axios.put(`/users/${id}`, userData, {
            headers: {
                'If-Match': etag
            }
        });
        return response.data;
    } catch (err) {
        if (err instanceof AxiosError) {
            alert(err.response?.data);
        }
    }
}


export async function updateSelf(userData: ClientOwnUpdateData, etag: string) {
    console.log(etag);
    etag = etag.replace("\"", "");
    try {

        const response = await axios.put(`/users/self`, userData, {
            headers: {
                'If-Match': etag
            }
        });
        return response.data;
    } catch (err) {
        if (err instanceof AxiosError) {
            alert(err.response?.data);
        }
    }


}





