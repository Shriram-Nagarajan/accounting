import axios from 'axios';

const queryParamStr = (queryParams) => {

    let str = "";
    if(queryParams && typeof queryParams == "object" && Object.keys(queryParams).length > 0) {
        str += "?";
        Object.keys(queryParams).forEach(key => {
            str += key + "=" + queryParams[key] + "&";
        });
        str = str.substring(0, str.length-1);
    }
    return str;

}

const get = (url, parameters, onSuccess, onError) => {
    axios.get(url + queryParamStr(parameters))
        .then(function (response) {
            console.log(response);
            onSuccess(response);
        })
        .catch(function (error) {
            console.log(error);
            onError(error);
        })
        .finally(function () {
            
        });
}

const postMultipartFormData = (url, parameters, onSuccess, onError) => {
    axios.post(url, parameters ,{
        headers: {
          'Content-Type': 'multipart/form-data'
        }})
    .then(function (response) {
        console.log(response);
        onSuccess(response);
    })
    .catch(function (error) {
        console.log(error);
        onError(error);
    })
    .finally(function () {
        
    });
}

const http = {get, postMultipartFormData};

export default http;

