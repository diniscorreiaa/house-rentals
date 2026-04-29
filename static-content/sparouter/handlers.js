import {getSomeDetails, h1, div, a, p, fetchData, li, ul} from "./dsl-functions.js";

export const API_BASE_URL = "http://localhost:8000/"

function getHome(mainContent) {
    const h1 = document.createElement("h1")
    const text = document.createTextNode("House Rentals HOME")
    h1.appendChild(text)
    mainContent.replaceChildren(h1)
}

function getHouses(mainContent) { // TODO: Implement paging
    fetchData("houses", (houses) => {
            const div = document.createElement("div")
            const h1 = document.createElement("h1")
            const text = document.createTextNode("All Houses")
            h1.appendChild(text)
            div.appendChild(h1)

            houses.forEach(house => {
                const p = document.createElement("p")
                const pText = document.createTextNode(house.title)
                p.appendChild(pText)
                const a = document.createElement("a")
                const aText = document.createTextNode("Details")
                a.appendChild(aText)
                a.href = "#houses/" + house.hid
                p.appendChild(a)
                div.appendChild(p)
            })
            mainContent.replaceChildren(div)
    })
}

function getHouseDetails(mainContent) {
    //getSomeDetails(mainContent, "title", "location", "areaSqMt", "pricePerNight", "description")
    fetchData(undefined, (house) => {
        fetchData(`locations/${house.location}`, (loc) => {
            const ulData = ul([h1(house.title), li(`Location: ${loc.name}`), a(`locations/${house.location}/full`, "See full location"), li(`Area: ${house.areaSqMt}`), li(`Price: ${house.pricePerNight}`), p(house.description)])
            mainContent.replaceChildren(ulData)
        })
    })
}

function getLocationDetails(mainContent) {
    getSomeDetails(mainContent, "name", "type")
    // TODO: Incluir hierarquia?
}

function getFullLocation(mainContent) {

}

function getUserDetails(uid, mainContent) {
    getSomeDetails(uid, mainContent, "name", "email")
}

function getBookings(mainContent) { // TODO: Implement paging
    fetch(API_BASE_URL + "bookings")
        .then(res => res.json())
        .then(bookings => {
                const div = document.createElement("div")
                const h1 = document.createElement("h1")
                const text = document.createTextNode("Bookings")
                h1.appendChild(text)
                div.appendChild(h1)

                bookings.forEach(booking => {
                    const p = document.createElement("p")
                    const a = document.createElement("a")
                    const aText = document.createTextNode("Details")
                    a.appendChild(aText)
                    a.href = "#bookings/" + booking.bid
                    p.appendChild(a)
                    div.appendChild(p)
                })
                mainContent.replaceChildren(div)
            }
        )
}

function getBookingDetails(bid, mainContent) {
    getSomeDetails(bid, mainContent, "bid", "startDate", "endDate", "uid", "hid")
}


export const handlers = {
    getHome,
    getHouses,
    getHouseDetails,
    getLocationDetails,
    getUserDetails,
    getBookings,
    getBookingDetails
}

export default handlers
