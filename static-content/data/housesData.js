const API_BASE_URL = "http://localhost:8080/"

export const fetchHouses = async (limit = 10, skip = 0) => {
    const response = await fetch(API_BASE_URL + "houses?limit=" + limit + "&skip=" + skip);
    if (!response.ok) {
        throw new Error("HTTP error " + response.status);
    }
    const data = await response.json();
    return data.houses;
}

export const fetchHouse = async (id) => {
    const response = await fetch(API_BASE_URL + "houses/" + id);
    if (!response.ok) {
        throw new Error("HTTP error " + response.status);
    }
    const data = await response.json();
    console.log(`House data fetched:`, data);
    return data;
}

export const fetchLocations = async (id) => {
    const response = await fetch(API_BASE_URL + "locations/" + id);
    if (!response.ok) {
        throw new Error("HTTP error " + response.status);
    }
    const data = await response.json();
    return data;
}

export const fetchUserDetails = async (userId) => {
    const response = await fetch(API_BASE_URL + "users/" + userId);
    if (!response.ok) {
        throw new Error("HTTP error " + response.status);
    }
    const data = await response.json();
    return data;
}
