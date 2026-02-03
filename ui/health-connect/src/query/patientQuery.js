import api from "../api/api";
import { getAuthHeader } from "../utils/httpUtils";

export async function listPatients(token) {
    try {
        const response = await api.get("patient", getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}

export async function getPatient(token, patientId) {
    try {
      
        const response = await api.get(`patient/${patientId}`, getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}