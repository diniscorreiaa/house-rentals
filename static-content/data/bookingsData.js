const API_BASE_URL = "http://localhost:8080/"

export const fetchBookings = async (houseId, startDate, endDate, limit = 10, skip = 0) => {
    const response = await fetch(
        API_BASE_URL + "bookings?hid=" + houseId + "&date-interval=" + startDate + ":" + endDate + "&limit=" + limit + "&skip=" + skip
    );
    if (!response.ok) {
        throw new Error("HTTP error " + response.status);
    }
    const data = await response.json();
    return data.bookings;
}

export const fetchBooking = async (id) => {
    const response = await fetch(API_BASE_URL + "bookings/" + id);
    if (!response.ok) {
        throw new Error("HTTP error " + response.status);
    }
    const data = await response.json();
    console.log(`Booking data fetched:`, data);
    return data;
}

export const fetchAvailableHouses = async (startDate, endDate, limit = 10, skip = 0) => {
    const response = await fetch(
        API_BASE_URL + "houses/available?startDate=" + startDate + "&endDate=" + endDate + "&limit=" + limit + "&skip=" + skip
    );
    if (!response.ok) {
        throw new Error("HTTP error " + response.status);
    }
    const data = await response.json();
    return data;
}

export const fetchUserBookingDetails = async (userId, limit = 10, skip = 0) => {
    const response = await fetch(API_BASE_URL + "users/" + userId + "/bookings?limit=" + limit + "&skip=" + skip);
    if (!response.ok) {
        throw new Error("HTTP error " + response.status);
    }
    const data = await response.json();
    return data;
}

