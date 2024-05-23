import constants from "../common/constants"
import http from "./http";

const getDomainUrl = () => {
    return constants.scheme + "://" + constants.accountingDomain + "/";
}

const getCategoryWiseExpenses = (onSuccess, onError) => {
    http.get(getDomainUrl() + "expenses-by-category", {}, onSuccess, onError);
}

const accountingApi = {
    getCategoryWiseExpenses
}

export default accountingApi;

