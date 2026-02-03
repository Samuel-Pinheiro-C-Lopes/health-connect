import api from "../api/api";
import { getAuthHeader } from "../utils/httpUtils";

export async function registerUser(email, password) {
    try {
        const response = await api.post("user", { email, password });
        return { success: true, data: response.data, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}

export async function login(email, password) {
    try {
        const response = await api.post("authentication/login", { email, password }); 
        return { success: true, data: response.data, statusCode: 200 }; 
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}

export async function createPerson(token, personData) {
    try { 
        const response = await api.post("person", personData, getAuthHeader(token)); 
        return { success: true, data: response.data, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}

export async function updateUser(token, userId, newCredentials) {
    try {
        const response = await api.put(`user/${userId}`, newCredentials, getAuthHeader(token));
        return { success: true, data: response.data, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}

export async function deleteUser(token, userId) {
    try {
        const response = await api.delete(`user/${userId}`, getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}

export async function assignRoles(token, userId, rolesArray) {
    try {
        const response = await api.post(`user/${userId}/roles`, { roles: rolesArray }, getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}