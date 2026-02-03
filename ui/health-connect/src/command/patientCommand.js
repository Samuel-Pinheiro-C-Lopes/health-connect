import api from "../api/api";
import { getAuthHeader } from "../utils/httpUtils";

export async function createPatient(token, patientData) {
    try {
        const response = await api.post("patient", patientData, getAuthHeader(token));
        return { success: true, data: response.data, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}

export async function updatePatient(token, patientId, patientData) {
    try {
     
        const response = await api.put(`patient/${patientId}`, patientData, getAuthHeader(token));
        return { success: true, data: response.data, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}

export async function deletePatient(token, patientId) {
    try {
     
        const response = await api.delete(`patient/${patientId}`, getAuthHeader(token));
        return { success: true, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}