import {getSomeDetails, h1, div, a, p, fetchData, li, ul} from "./dsl-functions.js";

export const API_BASE_URL = "http://localhost:8000/"

function getHome(mainContent) {
    mainContent.replaceChildren(h1("House Rentals HOME"))
}

function getHouses(mainContent) { // TODO: Implement paging
    fetchData("houses", (houses) => {
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
    const divChildren = []
    fetchData(undefined, (house) => {
        fetchData(`locations/${house.location}`, (loc) => {
            const ulData = ul([li(`Location: ${loc.name}`), a(`#locations/${house.location}/full`, "See full location"), li(`Area: ${house.areaSqMt} m2`), li(`Price: ${house.pricePerNight} euros/night`)])
            const currDate = new Date()
            const nextYearDate = new Date()
            nextYearDate.setFullYear(nextYearDate.getFullYear() + 1)
            const bookingsUri = `bookings?hid=${house.hid}&date-interval=${getFormatedDate(currDate)}:${getFormatedDate(nextYearDate)}`
            fetchData(bookingsUri, (bookingsForHouse) => {
                    bookingsForHouse.forEach( _ => {
                        const link = a(`#${bookingsUri}`, "See bookings for this house")
                        const bookingParagraph = p("")
                        bookingParagraph.appendChild(link)
                        divChildren.push(bookingParagraph)
                    })
                    mainContent.replaceChildren(h1(house.title), ulData, p(house.description), div(divChildren))
                }
            )
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
    fetch(API_BASE_URL + "bookings")
        .then(res => res.json())
        .then(bookings => {
                const divChildren = []
                divChildren.push(h1("Bookings"))
                bookings.forEach(booking => {
                    const link = link("")
                    link.appendChild(a("#bookings/" + booking.bid, "Details"))
                    divChildren.push(link)
                })
                mainContent.replaceChildren(div(divChildren))
            }
        )
}

function getBookingDetails(bid, mainContent) {
    //TODO: In progress
    const divChildren = []
    fetchData(undefined, booking => {
        fetchData(`locations/${house.location}`, (loc) => {
            const ulData = ul([li(`Location: ${loc.name}`), a(`locations/${house.location}/full`, "See full location"), li(`Area: ${house.areaSqMt}`), li(`Price: ${house.pricePerNight}`)])
            fetchData(`bookings?hid=${house.hid}&date-interval=1900-01-01:2100-02-02`, (bookingsForHouse) => {
                    bookingsForHouse.forEach(booking => {
                        const link = a("#bookings/" + booking.bid, "Booking Details")
                        const bookingParagraph = p("")
                        bookingParagraph.appendChild(link)
                        divChildren.push(bookingParagraph)
                    })
                    mainContent.replaceChildren(h1(booking.title), ulData, p(house.description), div(divChildren))
                }
            )
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
