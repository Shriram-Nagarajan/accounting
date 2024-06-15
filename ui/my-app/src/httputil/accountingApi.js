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

const getIncome = (parameters, onSuccess, onError) => {
    http.get(getDomainUrl() + "income-details", parameters, onSuccess, onError);
}
const getDefaultCategories = (parameters, onSuccess, onError) => {
    http.get(getDomainUrl() + "category/default", parameters, onSuccess, onError);
}
const getUserCategories = (parameters, onSuccess, onError) => {
    http.get(getDomainUrl() + "category/user", parameters, onSuccess, onError);
}
const saveExpenses = (formData, onSuccess, onError) => {
    http.post(getDomainUrl() + "save-expenses", formData, onSuccess, onError);
}

const saveIncome = (formData, onSuccess, onError) => {
    http.post(getDomainUrl() + "save-income-details", formData, onSuccess, onError);
}

const uploadExpensesFile = (formData, onSuccess, onError) => {
    http.postMultipartFormData(getDomainUrl() + "upload", formData, onSuccess, onError);
}

const accountingApi = {
    getCategoryWiseExpenses,
    uploadExpensesFile,
    getExpenses,
    saveExpenses,
    saveIncome,
    getIncome,
    getDefaultCategories,
    getUserCategories
}

export default accountingApi;

