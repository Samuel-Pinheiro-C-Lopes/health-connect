import api from "../api/api";
import { getAuthHeader } from "../utils/httpUtils";
import { createEntity, updateEntity, deleteEntity, listEntities, getEntity } from "./commonCommand";
const doctorEndpoint = "doctor";
export async function createDoctor(token, doctorData) {
    return createEntity(token, doctorEndpoint, doctorData);
}
export async function updateDoctor(token, doctorId, doctorData) {
    return updateEntity(token, doctorEndpoint, doctorId, doctorData);
}
export async function deleteDoctor(token, doctorId) {
    return deleteEntity(token, doctorEndpoint, doctorId);
}
export async function listDoctors(token) {
    return listEntities(token, doctorEndpoint);
}
export async function getDoctor(token, doctorId) {
    return getEntity(token, doctorEndpoint, doctorId);
}
