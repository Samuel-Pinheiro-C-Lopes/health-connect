import axios from "axios";
import { getAuthHeader } from "../utils/httpUtils";
export async function resetPassword(token, newPassword) {
    try {
        const response = await axios.post(
            "/user/reset-password",
            { password: newPassword },
            { headers: getAuthHeader(token) }  
        );
        return { success: true, data: response.data, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}