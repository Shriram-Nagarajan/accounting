import constants from "../common/constants"
import http from "./http";

const getDomainUrl = () => {
    return constants.scheme + "://" + constants.accountingDomain + "/";
}

const getCategoryWiseExpenses = (onSuccess, onError) => {
    http.get(getDomainUrl() + "expenses-by-category", {}, onSuccess, onError);
}

const uploadExpensesFile = (formData, onSuccess, onError) => {
    http.postMultipartFormData(getDomainUrl() + "upload", formData, onSuccess, onError);
}

const accountingApi = {
    getCategoryWiseExpenses,
    uploadExpensesFile
}

export default accountingApi;

