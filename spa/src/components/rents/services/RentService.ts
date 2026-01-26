import axios from 'axios';
import type {RentForm} from "../../../utils/typedefs.ts";

async function getAllRents() {
    const response = await axios.get('/rents');
    return response.data;
}

export async function createRent(formData: RentForm) {
    const response = await axios.post('/rents', formData);
    return response.data;
}

export async function getClientsRents(clientId: string) {
        const response = await axios.get(`/rents/client/${clientId}`);
        return response.data;
}


export async function endRent(url: string) {
    const response = await axios.patch(url);
    return response.data;
}

export async function deleteRent(url: string) {
    const response = await axios.delete(url);
    return response.data;
}


export async function getClientForRentHateoas(url: string) {
    const response = await axios.get(url);
    return response.data;
}

export async function getFacilityForRentHateoa(url: string) {
    const response = await axios.get(url);
    return response.data;
}
export default getAllRents;
