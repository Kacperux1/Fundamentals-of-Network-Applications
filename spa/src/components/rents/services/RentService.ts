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


export async function endRent(rentId: string) {
    const response = await axios.patch(`/rents/${rentId}`);
    return response.data;
}

export async function deleteRent(id: string) {
    const response = await axios.delete(`/rents/${id}`);
    return response.data;
}

export default getAllRents;
