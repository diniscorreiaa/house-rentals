const API_BASE_URL = "http://localhost:8080/"

export const fetchUser = async (id) => {
    const response = await fetch(API_BASE_URL + `users/${id}`);
    if(!response.ok){
        throw new Error("HTTP error " + response.status);
    }
    return await response.json()
}