import api from "../api/api";
import { getAuthHeader } from "../utils/httpUtils";
import { API_PATHS } from "../config/constants";

const appointmentBase = `${API_PATHS.APPOINTMENT_SERVICE}/appointment`;

export async function listAppointments(token) {
    try {
        const response = await api.get(appointmentBase, getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}


export async function getAppointment(token, appointmentId) {
    try {
        const response = await api.get(`${appointmentBase}/${appointmentId}`, getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}

export async function listAppointmentsByPatientCurrentUser(token) {
    try {
        const response = await api.get(
            `${appointmentBase}/patient/currentlyLoggedIn`,
            getAuthHeader(token)
        );
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}

export async function listAppointmentsByDoctorCurrentUser(token) {
    try {
        const response = await api.get(
            `${appointmentBase}/doctor/currentlyLoggedIn`,
            getAuthHeader(token)
        );
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}