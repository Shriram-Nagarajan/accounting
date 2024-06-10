import constants from "../common/constants"
import http from "./http";

const getDomainUrl = () => {
    return constants.scheme + "://" + constants.UAMDomain + "/";
}
const loginUser = (formData, onSuccess, onError) => {
    http.post(getDomainUrl() + "login", formData, onSuccess, onError);
}

const getSession = (parameters, onSuccess, onError) => {
    http.get(getDomainUrl() + "get-session", parameters, onSuccess, onError);
}
const UAMApi = {
    loginUser,
    getSession,
   
}

export default UAMApi;

