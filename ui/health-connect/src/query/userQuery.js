import { getAuthHeader} from "../utils/httpUtils";

export async function listUsers(token) {
    try {
        const response = await api.get("user", getAuthHeader(token));
        return { success: true, data: response.data };
    } catch (e) {
        return { success: false, message: e.message };
    }
}