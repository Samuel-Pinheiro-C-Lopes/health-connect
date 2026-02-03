import api from "../api/api";
import { getAuthHeader } from "../utils/httpUtils";
import { API_PATHS } from "../config/constants";

export async function listUsersAsync(token) {
    try {
        const response = await api.get(`${API_PATHS.USER_SERVICE}/user`, getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}