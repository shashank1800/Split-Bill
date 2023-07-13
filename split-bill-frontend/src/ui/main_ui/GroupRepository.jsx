import axios from 'axios';

import * as APIConstants from '../utils/APIConstants';


const contentType = APIConstants.contentType;
const AUTHORIZATION = APIConstants.AUTHORIZATION;

export const insert = async (group) => {
    const token = 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmNkZWZnaCIsImV4cCI6MTY4NDE2ODQ1NywiaWF0IjoxNjg0MTUwNDU3fQ.fHRKJdzBKt6iy8R1GoIE4xbap68WOWFScuJ4SdBW5smBBgQV6BXkZCmrEwsNJKMbmLJ3KlCMMPneJewuVdHDtg';
    try {

        const response = await axios.post(APIConstants.BASE_URL + APIConstants.saveGroup, group, {
            headers: {
                'Content-Type' : 'application/json',
                'Authorization' : token
            }
        })

        return response.data;
    } catch (error) {
        throw error;
    }
};


export const getAllGroups = async () => {
    const token = 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmNkZWZnaCIsImV4cCI6MTY4NDE2ODQ1NywiaWF0IjoxNjg0MTUwNDU3fQ.fHRKJdzBKt6iy8R1GoIE4xbap68WOWFScuJ4SdBW5smBBgQV6BXkZCmrEwsNJKMbmLJ3KlCMMPneJewuVdHDtg';
    try {
        const response = await axios.get(APIConstants.BASE_URL + APIConstants.allGroups, {
            headers: {
                'Authorization' : token
            }
        });

        return response.data;
    } catch (error) {
        throw error;
    }
};