import api from "../api/api";
import { getAuthHeader } from "../utils/httpUtils";
import { API_PATHS } from "../config/constants";

const patientBase = `${API_PATHS.PATIENT_SERVICE}/patient`;

export async function listPatients(token) {
    try {
        const response = await api.get(patientBase, getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}
export async function getPatient(token, patientId) {
    try {
        const response = await api.get(`${patientBase}/${patientId}`, getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}