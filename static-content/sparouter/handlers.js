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
    const form = document.createElement("form")

    const labelTitle = document.createElement("label")
    const textTitle = document.createTextNode("Search by title")
    labelTitle.appendChild(textTitle)

    const inputTitle = document.createElement("input")
    inputTitle.type = "text"
    inputTitle.id = "idTitle"

    const inputSubmit = document.createElement("input")
    inputSubmit.type = "submit"
    inputSubmit.value = "Search"

    form.appendChild(labelTitle)
    form.appendChild(inputTitle)
    form.appendChild(inputSubmit)

    form.addEventListener("submit", handleSubmit)

    function handleSubmit(e) {
        e.preventDefault()
        const inputTitle = document.querySelector("#idTitle")
        const params = new URLSearchParams()
        params.set("limit", housesLimit)
        params.set("skip", "0")
        if (inputTitle.value.trim()) params.set("search", inputTitle.value.trim())
        window.location.hash = `houses?${params.toString()}`
    }

    mainContent.replaceChildren(h1("House Rentals HOME"), form)
}

function getHouses(mainContent) {
    const queryPrams = new URLSearchParams(currentPath().split("?")[1])
    housesCurrentSkip = parseInt(queryPrams.get("skip"))
    fetchData(undefined, (houses) => {
        const divChildren = [h1("All Houses")]
        if (houses.length !== 0) {
            houses.forEach(house =>
                divChildren.push(
                    p(house.title, [a("Details", {href: "#houses/" + house.hid})])
                )
            )

            const limitSelector = selector("Select limit:", housesLimit, limitOptions, (newLimit) => {
                window.location.hash = `houses?limit=${newLimit}&skip=0`;
                housesLimit = newLimit
            }) // É necessário dar reset ao skip para garantir alinhamento

            const prevButton = button("Previous", {
                disabled: housesCurrentSkip === 0,
                click: () => {
                    window.location.hash = `houses?limit=${housesLimit}&skip=${housesCurrentSkip - housesLimit}`
                }
            })

            const nextButton = button("Next", {
                disabled: houses.length < housesLimit,
                click: () => {
                    window.location.hash = `houses?limit=${housesLimit}&skip=${housesCurrentSkip + housesLimit}`
                }
            })

            mainContent.replaceChildren(...divChildren,
                limitSelector,
                prevButton,
                nextButton
            )
        }
        else {
            divChildren.push(p("No houses to show"), a("Return to home page", {href: "#home"}))
            mainContent.replaceChildren(...divChildren)
        }
    })
}

function getHouseDetails(mainContent) {
    fetchData(undefined, (house) => {
        fetchData(`users/${house.owner}`, (owner) => {
            fetchData(`locations/${house.location}`, (loc) => {
                const ulData =
                    ul([
                        li(`Location: ${loc.name}`, [a("See full location", {href: `#locations/${house.location}/full`})]),
                        li(`Area: ${house.areaSqMt} m2`),
                        li(`Price: ${house.pricePerNight} euros/night`),
                        li(`Owner: ${owner.name}`, [a("Check user profile", {href: `#users/${house.owner}`})])
                    ])
                const currDate = new Date()
                const nextYearDate = new Date()
                nextYearDate.setFullYear(nextYearDate.getFullYear() + 1)
                const bookingsUri = `bookings?hid=${house.hid}&date-interval=${getFormatedDate(currDate)}:${getFormatedDate(nextYearDate)}` + `&limit=${bookingsLimit}&skip=${bookingsCurrentSkip}` // Estão visíveis bookings até 1 ano a partir da data atual
                mainContent.replaceChildren(h1(house.title), ulData, p(house.description), a("See bookings for this house", {href:`#${bookingsUri}`}))
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
        const listLoc = locations.map((location) => location.name + `(${location.type})`)

        mainContent.replaceChildren(
            h1(`Full location of ${locations[locations.length - 1].name}`),
            p(listLoc.join(" -> "))
        )
    })
}

function getUserDetails(mainContent) {
    fetchData(undefined, (user =>
        mainContent.replaceChildren(
            h1(`${user.name}'s Profile`),
            ul([
                li(`Name: ${user.name}`),
                li(`Email: ${user.email}`)
            ])
        )
    ))
}

function getBookings(mainContent) {
    const queryParams = new URLSearchParams(currentPath().split("?")[1])
    bookingsCurrentSkip = parseInt(queryParams.get("skip"))
    fetchData(undefined, (bookings) => {
        const params = new URLSearchParams(currentPath().split("?")[1]) // https://developer.mozilla.org/en-US/docs/Web/API/URLSearchParams/get
        fetchData(`houses/${params.get("hid")}`, (house) => {
            if (bookings.length !== 0) {
                const items = bookings.map(booking =>
                    li(`${booking.startDate} until ${booking.endDate}`, [
                        a("Details", {href: "#bookings/" + booking.bid})
                    ])
                )

                const baseUri = `bookings?hid=${queryParams.get("hid")}&date-interval=${queryParams.get("date-interval")}`

                const limitSelector = selector("Select limit:", bookingsLimit, limitOptions, (newLimit) => {
                    window.location.hash = `${baseUri}&limit=${newLimit}&skip=0`;
                    bookingsLimit = newLimit
                }) // É necessário dar reset ao skip para garantir alinhamento

                const prevButton = button("Previous", {
                    disabled: bookingsCurrentSkip === 0,
                    click: () => {
                        window.location.hash = `${baseUri}&limit=${bookingsLimit}&skip=${bookingsCurrentSkip - bookingsLimit}`
                    }
                })

                const nextButton = button("Next", {
                    disabled: bookings.length < bookingsLimit,
                    click: () => {
                        window.location.hash = `${baseUri}&limit=${bookingsLimit}&skip=${bookingsCurrentSkip + bookingsLimit}`
                    }
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
                    a("Return to home page", {href: "#home"})
                )
            }
        })
    })
}

function getBookingDetails(mainContent) {
    fetchData(undefined, booking => {
        fetchData(`users/${booking.uid}`, (user) => {
            fetchData(`houses/${booking.hid}`, (house) => {
                mainContent.replaceChildren(
                    h1("Booking details"),
                    ul([
                        li(`Interval: ${booking.startDate} until ${booking.endDate}`),
                        li(`Claimed by: ${user.name}`, [a("Check user profile", {href: `#users/${user.uid}`})]),
                        li(`House: ${house.title}`, [a("Go to house", {href: `#houses/${booking.hid}`})])
                    ])
                )
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
