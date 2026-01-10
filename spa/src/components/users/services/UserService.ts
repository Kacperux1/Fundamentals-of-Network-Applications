import  axios  from 'axios';

async function getAllUsers(){

    const response = await axios.get('/users');
    return response.data;
}

export default  getAllUsers ;

