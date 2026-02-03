import api from "../api/api";
import { getAuthHeader } from "../utils/httpUtils";
import { API_PATHS } from "../config/constants";

const userBase = `${API_PATHS.USER_SERVICE}/user`;
const roleBase = `${API_PATHS.USER_SERVICE}/role`;

export async function listUsers(token) {
    try {
        const response = await api.get(userBase, getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}
export async function deleteUser(token, userId) {
    try {
       
        const response = await api.delete(`${userBase}/${userId}`, getAuthHeader(token));
        return { success: true, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}
export async function listRoles(token) {
    try {
        const response = await api.get(roleBase, getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}
export async function assignRoleToUser(token, userId, roles) {
    try {
        const response = await api.put(`${userBase}/${userId}/roles`, { roles }, getAuthHeader(token));
        return { success: true, data: response.data, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}