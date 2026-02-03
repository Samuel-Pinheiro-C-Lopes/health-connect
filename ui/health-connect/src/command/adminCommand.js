import api from "../api/api";
import { getAuthHeader } from "../utils/httpUtils";
import { API_PATHS } from "../config/constants";

const userBase = `${API_PATHS.USER_SERVICE}/user`;
const roleBase = `${API_PATHS.USER_SERVICE}/role`;

export async function deleteUser(token, userId) {
    try {
       
        const response = await api.delete(`${userBase}/${userId}`, getAuthHeader(token));
        return { success: true, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}