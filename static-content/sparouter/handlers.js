import {h1, div, a, p, fetchData, li, ul, currentPath, ol, button, selector} from "./dsl-functions.js";

export const API_BASE_URL = "http://localhost:8000/"

export let housesLimit = 2
let housesCurrentSkip = 0

let bookingsLimit = 2
let bookingsCurrentSkip = 0

const limitOptions = [2, 5, 10, 20, 50]

/*function getHome(mainContent) {
    mainContent.replaceChildren(h1("House Rentals HOME"))
}*/

function getHome(mainContent) {
    const div = document.createElement("div")

    const h1 = document.createElement("h1")
    const h1Text = document.createTextNode("House Rentals HOME")
    h1.appendChild(h1Text)
    div.appendChild(h1)

    const searchLabel = document.createElement("label")
    const searchLabelText = document.createTextNode("Search houses: ")
    searchLabel.appendChild(searchLabelText)

    const searchInput = document.createElement("input")
    searchInput.type = "text"
    searchInput.placeholder = "Search by title..."
    searchLabel.appendChild(searchInput)
    div.appendChild(searchLabel)

    const searchButton = document.createElement("button")
    const searchButtonText = document.createTextNode("Search")
    searchButton.appendChild(searchButtonText)
    searchButton.addEventListener("click", () => {
        window.location.hash = "houses"
    })
    div.appendChild(searchButton)

    mainContent.replaceChildren(div)
}

function getHouses(mainContent) {
    const queryPrams = new URLSearchParams(currentPath().split("?")[1])
    housesCurrentSkip = parseInt(queryPrams.get("skip"))
    fetchData(undefined, (houses) => {
        const divChildren = [h1("All Houses")]
        if (houses.length !== 0) {
            houses.forEach(house => {
                const link = a("#houses/" + house.hid, "Details")
                const houseTitle = p(house.title)
                houseTitle.appendChild(link)
                divChildren.push(houseTitle)
            })

            const limitSelector = selector("Select limit:", housesLimit, limitOptions, (newLimit) => {
                window.location.hash = `houses?limit=${newLimit}&skip=0`;
                housesLimit = newLimit
            }) // É necessário dar reset ao skip para garantir alinhamento

            const prevButton = button("Previous", housesCurrentSkip === 0, () => {
                window.location.hash = `houses?limit=${housesLimit}&skip=${housesCurrentSkip - housesLimit}`
            })
            const nextButton = button("Next", houses.length < housesLimit, () => {
                window.location.hash = `houses?limit=${housesLimit}&skip=${housesCurrentSkip + housesLimit}`
            })

            mainContent.replaceChildren(...divChildren,
                limitSelector,
                prevButton,
                nextButton
            )
        }
        else {
            divChildren.push(p("No houses to show"))
            divChildren.push(a("#home", "Return to home page"))
            mainContent.replaceChildren(...divChildren)
        }
    })
}

function getHouseDetails(mainContent) {
    fetchData(undefined, (house) => {
        fetchData(`users/${house.owner}`, (owner) => {
            fetchData(`locations/${house.location}`, (loc) => {
                const locElem = li(`Location: ${loc.name}`)
                locElem.appendChild(a(`#locations/${house.location}/full`, "See full location"))
                const ownerElem = li(`Owner: ${owner.name}`)
                ownerElem.appendChild(a(`#users/${house.owner}`, "Check user profile"))
                const ulData = ul([locElem, li(`Area: ${house.areaSqMt} m2`), li(`Price: ${house.pricePerNight} euros/night`), ownerElem])
                const currDate = new Date()
                const nextYearDate = new Date()
                nextYearDate.setFullYear(nextYearDate.getFullYear() + 1)
                const bookingsUri = `bookings?hid=${house.hid}&date-interval=${getFormatedDate(currDate)}:${getFormatedDate(nextYearDate)}` + `&limit=${bookingsLimit}&skip=${bookingsCurrentSkip}` // Estão visíveis bookings até 1 ano a partir da data atual
                mainContent.replaceChildren(h1(house.title), ulData, p(house.description), a(`#${bookingsUri}`, "See bookings for this house"))
            })
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

function getUserDetails(mainContent) {
    fetchData(undefined, (user => {
        mainContent.replaceChildren(h1(`${user.name}'s Profile`), ul([li(`Name: ${user.name}`), li(`Email: ${user.email}`)]))
    }))
}

function getBookings(mainContent) {
    const queryParams = new URLSearchParams(currentPath().split("?")[1])
    bookingsCurrentSkip = parseInt(queryParams.get("skip"))
    fetchData(undefined, (bookings) => {
        const params = new URLSearchParams(currentPath().split("?")[1]) // https://developer.mozilla.org/en-US/docs/Web/API/URLSearchParams/get
        fetchData(`houses/${params.get("hid")}`, (house) => {
            if (bookings.length !== 0) {
                const items = []
                bookings.forEach(booking => {
                    const item = li(`${booking.startDate} until ${booking.endDate}`)
                    item.appendChild(a("#bookings/" + booking.bid, "Details"))
                    items.push(item)
                })

                const baseUri = `bookings?hid=${queryParams.get("hid")}&date-interval=${queryParams.get("date-interval")}`

                const limitSelector = selector("Select limit:", bookingsLimit, limitOptions, (newLimit) => {
                    window.location.hash = `${baseUri}&limit=${newLimit}&skip=0`;
                    bookingsLimit = newLimit
                }) // É necessário dar reset ao skip para garantir alinhamento

                const prevButton = button("Previous", bookingsCurrentSkip === 0, () => {
                    window.location.hash = `${baseUri}&limit=${bookingsLimit}&skip=${bookingsCurrentSkip - bookingsLimit}`
                })
                const nextButton = button("Next", bookings.length < bookingsLimit, () => {
                    window.location.hash = `${baseUri}&limit=${bookingsLimit}&skip=${bookingsCurrentSkip + bookingsLimit}`
                })

                mainContent.replaceChildren(
                    h1(`Bookings for ${house.title}`), ol(items),
                    limitSelector,
                    prevButton,
                    nextButton
                )
            }
            else {
                mainContent.replaceChildren(
                    h1(`Bookings for ${house.title}`),
                    p("No bookings to show"),
                    a("#home", "Return to home page")
                )
            }
        })
    })
}

function getBookingDetails(mainContent) {
    fetchData(undefined, booking => {
        fetchData(`users/${booking.uid}`, (user) => {
            fetchData(`houses/${booking.hid}`, (house) => {
                const claimed = li(`Claimed by: ${user.name}`)
                claimed.appendChild(a(`#users/${user.uid}`, "Check user profile"))
                const houseTitle = li(`House: ${house.title}`)
                houseTitle.appendChild(a(`#houses/${booking.hid}`, "Go to house"))
                const ulData = ul([li(`Interval: ${booking.startDate} until ${booking.endDate}`), claimed, houseTitle])
                mainContent.replaceChildren(h1("Booking details"), ulData)
            })
        })
    })
}

function getHouseAvailableDays(mainContent) {
    const queryParams = new URLSearchParams(currentPath().split("?")[1]) // to get year and month from query
    const hid = currentPath().split("?")[0].split("/")[1] //to get the hid from the query as well
    const year = queryParams.get("year")
    const month = queryParams.get("month")

    fetchData(`houses/${hid}/available-days?year=${year}&month=${month}`, (availableDays) => {
        mainContent.replaceChildren(
            h1(`Available days for house ${hid} — ${month}/${year}`), // h1 with the month and year
            availableDays.length > 0 ? ul(availableDays.map(day => li(String(day)))) : p("No available days"),
            a("Back to the house", {href: `#houses/${hid}`})
        )
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
    getFullLocation,
    getHouseAvailableDays
}

export default handlers
