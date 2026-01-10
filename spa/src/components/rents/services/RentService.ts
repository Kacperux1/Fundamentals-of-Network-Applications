import axios from 'axios';
import type {RentForm} from "../../../utils/typedefs.ts";

async function getAllRents() {
    const response = await axios.get('/rents');
    return response.data;
}

export async function createRent(formData: RentForm) {
    return await axios.post('/rents', formData);
}

export default getAllRents;
