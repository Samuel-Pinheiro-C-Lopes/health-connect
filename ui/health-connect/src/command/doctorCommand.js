import api from "../api/api";
import { getAuthHeader } from "../utils/httpUtils";
import { createEntity, updateEntity, deleteEntity, listEntities, getEntity } from "./commonCommand";
import { API_PATHS } from "../config/constants";

const servicePrefix = API_PATHS.DOCTOR_SERVICE;
const doctorEndpoint = "doctor";

export async function createDoctor(token, doctorData) {
    return createEntity(token, servicePrefix, doctorEndpoint, doctorData);
}
export async function updateDoctor(token, doctorId, doctorData) {
    return updateEntity(token, servicePrefix, doctorEndpoint, doctorId, doctorData);
}
export async function deleteDoctor(token, doctorId, permanent = false) {
    try {
        await api.delete(
            `${servicePrefix}/${doctorEndpoint}/${doctorId}${permanent ? "?permanent=true" : ""}`,
            getAuthHeader(token)
        );
        return { success: true, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}
export async function listDoctors(token) {
    return listEntities(token, servicePrefix, doctorEndpoint);
}
export async function listDoctorsActive(token) {
    try {
        const response = await api.get(
            `${servicePrefix}/${doctorEndpoint}/active`,
            getAuthHeader(token)
        );
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}
export async function listDoctorsActiveFull(token) {
    try {
        const response = await api.get(
            `${servicePrefix}/${doctorEndpoint}/active/full`,
            getAuthHeader(token)
        );
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}
export async function listDoctorsFull(token) {
    try {
        const response = await api.get(
            `${servicePrefix}/${doctorEndpoint}/full`,
            getAuthHeader(token)
        );
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}
export async function listDoctorsPending(token) {
    try {
        const response = await api.get(
            `${servicePrefix}/${doctorEndpoint}/pending`,
            getAuthHeader(token)
        );
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}
export async function approveDoctor(token, doctorId) {
    try {
        await api.patch(
            `${servicePrefix}/${doctorEndpoint}/${doctorId}/approve`,
            {},
            getAuthHeader(token)
        );
        return { success: true, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}
export async function rejectDoctor(token, doctorId) {
    try {
        await api.patch(
            `${servicePrefix}/${doctorEndpoint}/${doctorId}/reject`,
            {},
            getAuthHeader(token)
        );
        return { success: true, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}
export async function getDoctor(token, doctorId) {
    return getEntity(token, servicePrefix, doctorEndpoint, doctorId);
}
