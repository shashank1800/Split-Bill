import axios from 'axios';

import * as APIConstants from '../utils/APIConstants';

const contentType = APIConstants.contentType;

export const insert = async (group: Groups) => {
    try {
        const response = await axios.get(`${apiUrl}/some-data`);

        axios.post(APIConstants.BASE_URL + APIConstants.saveGroup, data, {
            headers: {
                contentType : 'application/json'
            }
        })
        return response.data;
    } catch (error) {
        console.error(error);
        throw error;
    }
};