import {getSomeDetails, h1, div, a, p, fetchData, li, ul, currentPath, ol} from "./dsl-functions.js";

export const API_BASE_URL = "http://localhost:8000/"

function getHome(mainContent) {
    mainContent.replaceChildren(h1("House Rentals HOME"))
}

function getHouses(mainContent) { // TODO: Implement paging
    fetchData(undefined, (houses) => {
            const divChildren = [h1("All Houses")]
            houses.forEach(house => {
                const link = a("#houses/" + house.hid, "Details")
                const houseTitle = p(house.title)
                houseTitle.appendChild(link)
                divChildren.push(houseTitle)
            })
            mainContent.replaceChildren(div(divChildren))
    })
}

function getHouseDetails(mainContent) {
    fetchData(undefined, (house) => {
        fetchData(`locations/${house.location}`, (loc) => {
            const ulData = ul([li(`Location: ${loc.name}`), a(`#locations/${house.location}/full`, "See full location"), li(`Area: ${house.areaSqMt} m2`), li(`Price: ${house.pricePerNight} euros/night`)])
            const currDate = new Date()
            const nextYearDate = new Date()
            nextYearDate.setFullYear(nextYearDate.getFullYear() + 1)
            const bookingsUri = `bookings?hid=${house.hid}&date-interval=${getFormatedDate(currDate)}:${getFormatedDate(nextYearDate)}` // Estão visíveis bookings até 1 ano a partir da data atual
            mainContent.replaceChildren(div([h1(house.title), ulData, p(house.description), a(`#${bookingsUri}`, "See bookings for this house")]))
        })
    })
}

/*
function getLocationDetails(mainContent) {
    getSomeDetails(mainContent, "name", "type")
    // TODO: Incluir hierarquia?
}
*/

function getFullLocation(mainContent) {
    fetchData(undefined, (locations) => {
        const listLoc = []
        locations.forEach((location) => {
            listLoc.push(location.name + `(${location.type})`)
        })
        const title = h1(`Full location of ${locations[locations.length - 1].name}`)
        const content = div([title, p(listLoc.join(" -> "))])
        mainContent.replaceChildren(content)
    })
}

function getUserDetails(uid, mainContent) {
    getSomeDetails(uid, mainContent, "name", "email")
}

function getBookings(mainContent) { // TODO: Implement paging
    fetchData(undefined, (bookings) => {
        const params = new URLSearchParams(currentPath().split("?")[1]) // https://developer.mozilla.org/en-US/docs/Web/API/URLSearchParams/get
        fetchData(`houses/${params.get("hid")}`, (house) => {
            const items = []
            bookings.forEach(booking => {
                const item = li(`${booking.startDate} until ${booking.endDate}`)
                item.appendChild(a("#bookings/" + booking.bid, "Details"))
                items.push(item)
            })
            mainContent.replaceChildren(div([h1(`Bookings for ${house.title}`), ol(items)]))
        })
    })
}

function getBookingDetails(mainContent) {
    fetchData(undefined, booking => {
        fetchData(`users/${booking.uid}`, (user) => {
            fetchData(`houses/${booking.hid}`, (house) => {
                const claimed = li(`Claimed by: ${user.name}`)
                claimed.appendChild(a(`#users/${user.uid}`, "Check user profile"))
                const ulData = ul([li(`Interval: ${booking.startDate} until ${booking.endDate}`), claimed, li(`House: ${house.title}`)])
                mainContent.replaceChildren(div([h1("Booking details"), ulData]))
            })
        })
    })
}

function getFormatedDate(date) {
    return date.toISOString().split("T")[0]
}


export const handlers = {
    getHome,
    getHouses,
    getHouseDetails,
    //getLocationDetails,
    getUserDetails,
    getBookings,
    getBookingDetails,
    getFullLocation
}

export default handlers
