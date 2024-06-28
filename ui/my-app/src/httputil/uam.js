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

const logoutUser = (parameters, onSuccess, onError) => {
    http.get(getDomainUrl() + "logout", parameters, onSuccess, onError);
}
const sendOTPForRegister = (formData, onSuccess, onError) => {
    http.post(getDomainUrl() + "registration/send-email-otp", formData, onSuccess, onError);
}
const verifyOTPForRegister = (formData, onSuccess, onError) => {
    http.post(getDomainUrl() + "registration/verify-email-otp", formData, onSuccess, onError);
}
const registerUser = (formData, onSuccess, onError) => {
    http.post(getDomainUrl() + "registration/register-user", formData, onSuccess, onError);
}
const sendOTPForForgotPassword = (formData, onSuccess, onError) => {
    http.post(getDomainUrl() + "forgot-password/send-token/email", formData, onSuccess, onError);
}
const verifyOTPForForgotPassword = (formData, onSuccess, onError) => {
    http.post(getDomainUrl() + "forgot-password/verify-token/email", formData, onSuccess, onError);
}
const resetPassword = (formData, onSuccess, onError) => {
    http.post(getDomainUrl() + "forgot-password/reset-password/email", formData, onSuccess, onError);
}
const UAMApi = {
    loginUser,
    getSession,
    logoutUser,
    sendOTPForRegister,
    verifyOTPForRegister,
    registerUser,
    sendOTPForForgotPassword,
    verifyOTPForForgotPassword,
    resetPassword
}

export default UAMApi;

