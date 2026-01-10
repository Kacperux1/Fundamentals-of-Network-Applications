import axios from 'axios';


async function getAllRents() {
    const response = await axios.get('/rents');
    return response.data;
}

export default getAllRents;