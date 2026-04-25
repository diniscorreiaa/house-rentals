const API_BASE_URL = "http://localhost:8080/"

export const fetchLocations = async (limit = 10, skip = 0) => {
    const response = await fetch(API_BASE_URL + "locations?limit=" + limit + "&skip=" + skip);
    if (!response.ok) {
        throw new Error("HTTP error " + response.status);
    }
    const data = await response.json();
    return data.locations;
};

export const fetchLocation = async (id) => {
    const response = await fetch(API_BASE_URL + "locations/" + id);
    if (!response.ok) {
        throw new Error("HTTP error " + response.status);
    }
    const data = await response.json();
    console.log(`Location data fetched:`, data);
    return data;
}