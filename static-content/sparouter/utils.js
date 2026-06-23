import {API_BASE_URL} from "./handlers";

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