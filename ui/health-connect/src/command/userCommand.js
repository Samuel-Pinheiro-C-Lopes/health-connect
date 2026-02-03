import api from "../api/api";

const url = "user";

export async function registerUser(email, password){
    const request = {email, password};

    let response = {
        succes : false,
        data : null,
        message: "",
        statusCode : 200
    };

    try {
        const res = await api.post(url, request);

        if (res.status === 200) {
            responseResult.success = true;
            responseResult.data = res.data;
            responseResult.statusCode = res.status;
        }
        
    } catch (e) {
        responseResult.success = false;
        responseResult.statusCode = e.response?.status || 500;
        responseResult.message = e.message;
    }

    return responseResult;
}