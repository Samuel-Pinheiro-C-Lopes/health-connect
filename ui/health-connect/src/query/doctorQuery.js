import api from "../api/api"; 
import { getAuthHeader } from "../utils/httpUtils";

export async function listDoctors(token) {
    try {
        const response = await api.get("doctor", getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}

export async function getDoctor(token, doctorId) {
    try {
        const response = await api.get(`doctor/${doctorId}`, getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}