import {API_BASE_URL} from "./handlers.js";
import {p} from "./dsl-functions.js";

export const currentPath = () => window.location.hash.replace("#", "")

export function fetchData(path = currentPath(), execute) {
    fetch(API_BASE_URL + path)
        .then(res => res.json())
        .then(data => execute(data))
}

/**
 * Returns an ISO 8601 compliant string, with just the date (no timestamp), from the date parameter
 *
 * @param {Date} date
 * @return string
 */
export function getFormatedDate(date) {
    return date.toISOString().split("T")[0]
}

export function getQueryParamValue(name){
    const queryPrams = new URLSearchParams(currentPath().split("?")[1])
    return decodeURIComponent(queryPrams.get(name))
}

export function checkIfErrorsFromApi(maybeError, errorPlaceholder) {
    if (maybeError.message !== undefined){
        errorPlaceholder.replaceChildren(p(`ERROR: ${maybeError.message}`, {style: "color:red"}))
        throw Error(`ERROR: ${maybeError.message}`)
    }

    return maybeError // not an error
}

export const getToken = () => sessionStorage.getItem("token")
export const setToken = (tokenStr) => sessionStorage.setItem("token", tokenStr)
export const rmToken = () => sessionStorage.removeItem("token")
