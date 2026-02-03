import api from "../api/api";
import { getAuthHeader } from "../utils/httpUtils";


export async function createAppointment(token, appointmentData) {
    try {
        const response = await api.post("appointment", appointmentData, getAuthHeader(token));
        return { success: true, data: response.data, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}


export async function updateAppointment(token, appointmentId, appointmentData) {
    try {
        const response = await api.put(`appointment/${appointmentId}`, appointmentData, getAuthHeader(token));
        return { success: true, data: response.data, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}


export async function deleteAppointment(token, appointmentId) {
    try {
        const response = await api.delete(`appointment/${appointmentId}`, getAuthHeader(token));
        return { success: true, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}