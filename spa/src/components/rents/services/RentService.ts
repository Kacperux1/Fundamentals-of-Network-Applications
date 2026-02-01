import axios, {AxiosError} from 'axios';
import type {RentForm} from "../../../utils/typedefs.ts";

async function getAllRents() {
    try {
        const response = await axios.get('/rents');
        return response.data;
    } catch (err) {
        if (err instanceof AxiosError) {
            alert(err.response?.data?.message);
        }
    }

}

export async function createRent(formData: RentForm) {
    try {
        const response = await axios.post('/rents', formData);
        return response.data;
    } catch (err) {
        if (err instanceof AxiosError) {
            alert(err.response?.data?.message);
        }
    }
}

export async function getClientsRents(clientId: string) {
    try {
        const response = await axios.get(`/rents/client/${clientId}`);
        return response.data;
    } catch (err) {
        if (err instanceof AxiosError) {
            alert(err.response?.data?.message);
        }
    }
}


export async function endRent(url: string) {
    try {
        const response = await axios.put(url);
        return response.data;
    } catch (err) {
        if (err instanceof AxiosError) {
            alert(err.response?.data?.message);
        }
    }
}

export async function deleteRent(url: string) {
    try {
        const response = await axios.delete(url);
        return response.data;
    } catch (err) {
        if (err instanceof AxiosError) {
            alert(err.response?.data?.message);
        }
    }
}


export async function getClientForRentHateoas(url: string) {
    try {
        const response = await axios.get(url);
        return response.data;
    } catch (err) {
        if (err instanceof AxiosError) {
            alert(err.response?.data?.message);
        }
    }
}

export async function getFacilityForRentHateoa(url: string) {
    try {
        const response = await axios.get(url);
        return response.data;
    } catch (err) {
        if (err instanceof AxiosError) {
            alert(err.response?.data?.message);
        }
    }
}
export default getAllRents;
