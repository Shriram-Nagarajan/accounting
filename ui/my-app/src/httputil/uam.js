import constants from "../common/constants"
import http from "./http";

const getDomainUrl = () => {
    return constants.scheme + "://" + constants.UAMDomain + "/";
}
const loginUser = (formData, onSuccess, onError) => {
    http.post(getDomainUrl() + "login", formData, onSuccess, onError);
}


const UAMApi = {
    loginUser
   
}

export default UAMApi;

