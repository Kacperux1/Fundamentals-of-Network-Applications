import axios from 'axios';

async function getAllFacilities() {

    const res = await axios.get('/facilities');
    return res.data;
}

export default  getAllFacilities;