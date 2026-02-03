import api from "../api/api";
import { getAuthHeader } from "../utils/httpUtils";
import { API_PATHS } from "../config/constants";

const doctorBase = `${API_PATHS.DOCTOR_SERVICE}/doctor`;

export async function listDoctors(token) {
    try {
        const response = await api.get(doctorBase, getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}
export async function getDoctor(token, doctorId) {
    try {
        const response = await api.get(`${doctorBase}/${doctorId}`, getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}

export async function listDoctorsFull(token) {
    try {
        const response = await api.get(`${doctorBase}/full`, getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}

export async function listDoctorsActive(token) {
    try {
        const response = await api.get(`${doctorBase}/active`, getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}

export async function listDoctorsActiveFull(token) {
    try {
        const response = await api.get(`${doctorBase}/active/full`, getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}

export async function listDoctorsPending(token) {
    try {
        const response = await api.get(`${doctorBase}/pending`, getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
} 