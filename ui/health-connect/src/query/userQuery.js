import api from "../api/api";
import { getAuthHeader } from "../utils/httpUtils";
import { API_PATHS } from "../config/constants";

const userBase = `${API_PATHS.USER_SERVICE}/user`;

export async function listUsersAsync(token) {
    try {
        const response = await api.get(userBase, getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}

export async function getCurrentUser(token) {
    try {
        const response = await api.get(`${userBase}/loggedIn`, getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}