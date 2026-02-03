import api from "../api/api";
import { getAuthHeader } from "../utils/httpUtils";


export async function createDoctor(token, doctorData) {
    try {
        const response = await api.post("doctor", doctorData, getAuthHeader(token));
        return { success: true, data: response.data, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}


export async function updateDoctor(token, doctorId, doctorData) {
    try {
        const response = await api.put(`doctor/${doctorId}`, doctorData, getAuthHeader(token));
        return { success: true, data: response.data, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}


export async function deleteDoctor(token, doctorId) {
    try {
        const response = await api.delete(`doctor/${doctorId}`, getAuthHeader(token));
        return { success: true, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}


export async function createUser(email, password) {
    try {
        const response = await api.post("user", { email, password });
        return { success: true, data: response.data, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}

export async function loginUser(email, password) {
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