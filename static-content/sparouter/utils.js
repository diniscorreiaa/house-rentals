import {API_BASE_URL} from "./handlers";

export const currentPath = () => window.location.hash.replace("#", "")

export function fetchData(path = currentPath(), execute) {
    fetch(API_BASE_URL + path)
        .then(res => res.json())
        .then(data => execute(data))
}