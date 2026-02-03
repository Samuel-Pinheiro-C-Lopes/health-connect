import api from "../api/api"; 
import { getAuthHeader } from "../utils/httpUtils";

export async function listAppointments(token) {
    try {
        const response = await api.get("appointment", getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}

export async function getAppointment(token, appointmentId) {
    try {
        
        const response = await api.get(`appointment/${appointmentId}`, getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}