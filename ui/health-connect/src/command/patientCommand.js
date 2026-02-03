import api from "../api/api";
import { getAuthHeader } from "../utils/httpUtils";
import { createEntity, updateEntity, deleteEntity, listEntities, getEntity } from "./commonCommand";
import { API_PATHS } from "../config/constants";

const servicePrefix = API_PATHS.PATIENT_SERVICE;
const patientEndpoint = "patient";

export async function createPatient(token, patientData) {
    return createEntity(token, servicePrefix, patientEndpoint, patientData);
}
export async function updatePatient(token, patientId, patientData) {
    return updateEntity(token, servicePrefix, patientEndpoint, patientId, patientData);
}
export async function deletePatient(token, patientId, permanent = false) {
    try {
        await api.delete(
            `${servicePrefix}/${patientEndpoint}/${patientId}${permanent ? "?permanent=true" : ""}`,
            getAuthHeader(token)
        );
        return { success: true, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}

