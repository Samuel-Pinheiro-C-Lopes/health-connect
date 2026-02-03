import api from "../api/api";
import { getAuthHeader } from "../utils/httpUtils";
import { createEntity, updateEntity, deleteEntity, listEntities, getEntity } from "./commonCommand";
const patientEndpoint = "patient";
export async function createPatient(token, patientData) {
    return createEntity(token, patientEndpoint, patientData);
}
export async function updatePatient(token, patientId, patientData) {
    return updateEntity(token, patientEndpoint, patientId, patientData);
}
export async function deletePatient(token, patientId) {
    return deleteEntity(token, patientEndpoint, patientId);
}
export async function listPatients(token) {
    return listEntities(token, patientEndpoint);
}
export async function getPatient(token, patientId) {
    return getEntity(token, patientEndpoint, patientId);
}
