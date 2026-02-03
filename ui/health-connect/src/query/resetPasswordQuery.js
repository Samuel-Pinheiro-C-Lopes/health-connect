import axios from "axios";
import { getAuthHeader } from "../utils/httpUtils";
export async function getResetPasswordStatus(token) {
    try {
        const response = await axios.get("/user/reset-password/status", {
            headers: getAuthHeader(token)  
        });
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}