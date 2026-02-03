import api from "../api/api";
import { getAuthHeader } from "../utils/httpUtils";

function buildPath(servicePrefix, endpoint) {
    return `${servicePrefix}/${endpoint}`;
}

export async function createEntity(token, servicePrefix, endpoint, data) {
    try {
        const response = await api.post(buildPath(servicePrefix, endpoint), data, getAuthHeader(token));
        return { success: true, data: response.data, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}

export async function updateEntity(token, servicePrefix, endpoint, entityId, data) {
    try {
        const response = await api.put(
            `${buildPath(servicePrefix, endpoint)}/${entityId}`,
            data,
            getAuthHeader(token)
        );
        return { success: true, data: response.data, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}

export async function deleteEntity(token, servicePrefix, endpoint, entityId) {
    try {
        await api.delete(`${buildPath(servicePrefix, endpoint)}/${entityId}`, getAuthHeader(token));
        return { success: true, statusCode: 200 };
    } catch (e) {
        return { success: false, message: e.message, statusCode: e.response?.status || 500 };
    }
}

export async function listEntities(token, servicePrefix, endpoint) {
    try {
        const response = await api.get(buildPath(servicePrefix, endpoint), getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}

export async function getEntity(token, servicePrefix, endpoint, entityId) {
    try {
        const response = await api.get(
            `${buildPath(servicePrefix, endpoint)}/${entityId}`,
            getAuthHeader(token)
        );
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}
