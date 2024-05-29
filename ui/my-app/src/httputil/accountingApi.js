import constants from "../common/constants"
import http from "./http";

const getDomainUrl = () => {
    return constants.scheme + "://" + constants.accountingDomain + "/";
}

const getCategoryWiseExpenses = (parameters, onSuccess, onError) => {
    http.get(getDomainUrl() + "expenses-by-category", parameters, onSuccess, onError);
}

const getExpenses = (parameters, onSuccess, onError) => {
    http.get(getDomainUrl() + "expenses", parameters, onSuccess, onError);
}

const uploadExpensesFile = (formData, onSuccess, onError) => {
    http.postMultipartFormData(getDomainUrl() + "upload", formData, onSuccess, onError);
}

const accountingApi = {
    getCategoryWiseExpenses,
    uploadExpensesFile,
    getExpenses
}

export default accountingApi;

