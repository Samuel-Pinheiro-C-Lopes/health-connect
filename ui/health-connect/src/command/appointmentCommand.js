import api from "../api/api";
import { getAuthHeader } from "../utils/httpUtils";
import { API_PATHS } from "../config/constants";

const appointmentBase = `${API_PATHS.APPOINTMENT_SERVICE}/appointment`;

export async function createAppointment(token, appointmentData) {
    try {
        const response = await api.post(appointmentBase, appointmentData, getAuthHeader(token));
        return { success: true, data: response.data, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}


export async function updateAppointment(token, appointmentId, appointmentData) {
    try {
        const response = await api.put(`${appointmentBase}/${appointmentId}`, appointmentData, getAuthHeader(token));
        return { success: true, data: response.data, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}


export async function deleteAppointment(token, appointmentId) {
    try {
        await api.delete(`${appointmentBase}/${appointmentId}`, getAuthHeader(token));
        return { success: true, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}

export async function cancelAppointment(token, appointmentId, reason) {
    try {
        await api.patch(
            `${appointmentBase}/${appointmentId}`,
            { reason },
            getAuthHeader(token)
        );
        return { success: true, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}