import api from "../api/api";
import { getAuthHeader } from "../utils/httpUtils";
export async function listUsers(token) {
    try {
        const response = await api.get("user", getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}
export async function deleteUser(token, userId) {
    try {
       
        const response = await api.delete(`user/${userId}`, getAuthHeader(token));
        return { success: true, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}
export async function listRoles(token) {
    try {
        const response = await api.get("role", getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}
export async function assignRoleToUser(token, userId, roles) {
    try {
       
        const response = await api.post(`user/${userId}/roles`, { roles }, getAuthHeader(token));
        return { success: true, data: response.data, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}