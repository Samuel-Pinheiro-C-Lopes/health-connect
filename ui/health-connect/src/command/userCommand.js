import api from "../api/api";
import { getAuthHeader } from "../utils/httpUtils";
import { API_PATHS } from "../config/constants";

const userBase = `${API_PATHS.USER_SERVICE}/user`;
const authBase = `${API_PATHS.USER_SERVICE}/authentication`;

export async function registerUserAsync(userData) {
    try {
        const response = await api.post(userBase, userData);
        return { success: true, data: response.data, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}

export async function loginAsync(email, password) {
    try {
        const response = await api.post(`${authBase}/login`, { username: email, password }); 
        return { success: true, data: response.data, statusCode: 200 }; 
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}

/**
 * Updates a user. newCredentials must match UserRequestDTO:
 * { email, password, doctorId?, patientId?, name, phone, postalCode, avenue, complement, number, city, district, state }
 */
export async function updateUserAsync(token, userId, newCredentials) {
    try {
        const response = await api.put(`${userBase}/${userId}`, newCredentials, getAuthHeader(token));
        return { success: true, data: response.data, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}

export async function deleteUserAsync(token, userId) {
    try {
        const response = await api.delete(`${userBase}/${userId}`, getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}

export async function assignRolesAsync(token, userId, rolesArray) {
    try {
        const response = await api.put(`${userBase}/${userId}/roles`, { roles: rolesArray }, getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}