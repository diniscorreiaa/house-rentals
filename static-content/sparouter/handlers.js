import {
    h1,
    div,
    a,
    p,
    fetchData,
    li,
    ul,
    currentPath,
    ol,
    button,
    selector,
    label,
    input,
    select, option
} from "./dsl-functions.js";

// this is only temporary
const tempToken = "4657f27c-5218-4bec-ba9a-b195807cb2ec"

export const API_BASE_URL = "http://localhost:8000/"

export let housesLimit = 2
let housesCurrentSkip = 0

let bookingsLimit = 2
let bookingsCurrentSkip = 0

const limitOptions = [2, 5, 10, 20, 50]

function getHome(mainContent) {
    // todo: make sure clicking ENTER on input ALSO changes the page.

    mainContent.replaceChildren(
        h1("House Rentals HOME"),
        label("Search houses: ", [
            input({
                type: "text",
                placeholder: "Search by title...",
                id: "search-term-input"
            }),
        ]),
        button("Search", {click: () => {
                const searchTerm = document.getElementById("search-term-input").value
                const encodedSearchTerm = encodeURIComponent(searchTerm)
                window.location.hash = `houses-search?term=${encodedSearchTerm}`
        }})
    )
}

function getHousesSearch(mainContent){
    const queryPrams = new URLSearchParams(currentPath().split("?")[1])
    const searchTerm = decodeURIComponent(queryPrams.get("term"))

    fetchData("houses", (houses) => {
        const housesLinks = []
        const filteredHouses = houses.filter((house) => house.title.toLowerCase().includes(searchTerm.toLowerCase()))

        if (filteredHouses.length !== 0) {
            filteredHouses.forEach(house =>
                housesLinks.push(
                    p(house.title, [a("Details", {href: "#houses/" + house.hid})])
                )
            )
        } else housesLinks.push(p("No houses found for search"))

        mainContent.replaceChildren(
            h1(`Houses for search "${searchTerm}"`),
            ...housesLinks
        )
    })
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
        } else {
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

                ///// Filter bookings ////

                // Estas variáveis garantem que tem de haver pelo menos uma noite de reserva
                const maxStartDate = new Date(nextYearDate)
                maxStartDate.setDate(nextYearDate.getDate() - 1)

                const minEndDate = new Date(currDate)
                minEndDate.setDate(currDate.getDate() + 1)


                const startDate = input({
                    type: "date",
                    id: "start-date-bookings",
                    min: getFormatedDate(currDate),
                    max: getFormatedDate(maxStartDate),
                    value: getFormatedDate(currDate)
                })
                const endDate = input({
                    type: "date",
                    id: "end-date-bookings",
                    min: getFormatedDate(minEndDate),
                    max: getFormatedDate(nextYearDate),
                    value: getFormatedDate(nextYearDate)
                })

                mainContent.replaceChildren(
                    h1(house.title),
                    ulData,
                    p(house.description),
                    p([a("See bookings for this house", {href: `#${bookingsUri}`})]),
                    p("Interested? ", [a("Book now!", { href: `#bookings/create?hid=${house.hid}`})]),
                    label("Or filter bookings by date: ", [
                        p([
                            label("Start Date: ", [startDate]),
                            label("End Date: ", [endDate])
                        ])
                    ]),
                    button("See filtered bookings", {
                        click: () => {
                            window.location.hash = `bookings?hid=${house.hid}&date-interval=${startDate.value}:${endDate.value}` + `&limit=${bookingsLimit}&skip=${bookingsCurrentSkip}`
                        }})
                )
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
                const items = bookings.map((booking, idx) =>
                    li(`${booking.startDate} until ${booking.endDate}`, {id: `booking-${idx}`}, [
                            a("Details", {href: "#bookings/" + booking.bid}),

                            button("Delete", {
                                click: () => {
                                    fetch(`bookings/${booking.bid}`, {
                                            headers: {"Authorization": tempToken},
                                            method: "DELETE"
                                        }
                                    ).then(() =>
                                        document.getElementById(`booking-${idx}`).remove()
                                    )
                                }
                            }),

                            button("Update", {
                                click: () => {
                                    window.location.hash = `bookings/${booking.bid}/update`
                                }
                            })
                        ]
                    )
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
            } else {
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

function createLocation(mainContent) {
    const locTypes = ["COUNTRY", "REGION", "DISTRICT", "MUNICIPALITY", "LOCALITY"]

    const nameInput = input({
        type: "text",
        placeholder: "Insert location name"
    })

    const parentLocations = div() // Div vazia para colocar os parentLocations dinamicamente

    const typeSelect = select(locTypes.map(type => option(type, {value: type})))

    typeSelect.addEventListener("change", (ev) => {
        const selectedType = ev.currentTarget.value

        let parentType = null
        if (selectedType !== "COUNTRY") parentType = locTypes[locTypes.findIndex( type => type === selectedType) - 1]

        if (!parentType) {
            parentLocations.replaceChildren()
            return
        }

        fetchData(`locations?type=${parentType}`, (locations) => {
            if (locations.length === 0) {
                parentLocations.replaceChildren(p(`No parent locations exist for type ${selectedType} yet. Create one first.`))
                return
            }

            parentLocations.replaceChildren(label("Parent: ", [select({ id: "parent-locations-select"}, locations.map( loc => option(loc.name, { value: loc.lid })))]))
        })
    })

    // TODO: Adicionar header Authorization na próxima fase
    const createButton = button("Create", {
        click: () => {
            const parentSelector = document.getElementById("parent-locations-select")

            const body = { name: nameInput.value, type: typeSelect.value, parentId: parentSelector ? parentSelector.value : null}

            fetch(API_BASE_URL + "locations", {
                method: "POST",
                headers: {
                    "Authorization": tempToken,
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(body)
            }).then( () => {
                window.location.hash = currentPath()
            })
        }
    })

    mainContent.replaceChildren(
        h1("Create location"),
        label("Name: ", [nameInput]),
        label("Type: ", [typeSelect]),
        parentLocations,
        createButton
    )
}

function createHouse(mainContent) {
    const titleInput = input({type: "text", placeholder: "Insert House Title"})
    const locations = div()
    const areaInput = input({ type: "number", placeholder: "Insert area" })
    const priceInput = input({ type: "number", placeholder: "Insert price" })
    const descriptionInput = input({ type: "text", placeholder: "Insert description" })

    fetchData(`locations?type=locality`, (locs) => {
        if (locs.length === 0) {
            locations.replaceChildren(p(`No available locations`))
            return
        }

        const locationSelect = select(locs.map(loc => option(loc.name, {value: loc.lid})))
        locations.replaceChildren(locationSelect)

        // TODO: Adicionar header Authorization na próxima fase
        const createButton = button("Create", {
            click: () => {
                const body = {
                    title: titleInput.value,
                    location: parseInt(locationSelect.value),
                    areaSqMt: parseFloat(areaInput.value),
                    pricePerNight: parseFloat(priceInput.value),
                    description: descriptionInput.value}

                fetch(API_BASE_URL + "houses", {
                    method: "POST",
                    headers: {
                        "Authorization": tempToken,
                        "Content-Type": "application/json" },
                    body: JSON.stringify(body)
                })
                    .then( res => res.json())
                    .then( (house) => {
                    window.location.hash = `houses/${house.hid}`
                })
            }
        })

        mainContent.replaceChildren(
            label("Title: ", [titleInput]),
            label("Location: ", [locations]),
            label("Area (m2): ", [areaInput]),
            label("Price per night (euros): ", [priceInput]),
            label("Description: ", [descriptionInput]),
            createButton
        )
    })
}

function createBooking(mainContent) {
    const months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"]

    const queryParams = new URLSearchParams(currentPath().split("?")[1]) // https://developer.mozilla.org/en-US/docs/Web/API/URLSearchParams/get
    const houseId = queryParams.get("hid")

    const currDate = new Date()
    const nextYearDate = new Date()
    nextYearDate.setFullYear(nextYearDate.getFullYear() + 1)


    // Estas variáveis garantem que tem de haver pelo menos uma noite de reserva
    const maxStartDate = new Date(nextYearDate)
    maxStartDate.setDate(nextYearDate.getDate() - 1)

    const minEndDate = new Date(currDate)
    minEndDate.setDate(currDate.getDate() + 1)


    const startDate = input({
        type: "date",
        id: "start-date-bookings",
        min: getFormatedDate(currDate),
        max: getFormatedDate(maxStartDate),
        value: getFormatedDate(currDate)
    })
    const endDate = input({
        type: "date",
        id: "end-date-bookings",
        min: getFormatedDate(minEndDate),
        max: getFormatedDate(nextYearDate),
        value: getFormatedDate(nextYearDate)
    })


    const currentYear = currDate.getFullYear()
    const nextYear = nextYearDate.getFullYear()
    const currentMonth = currDate.getMonth() + 1
    const nextYearMonth = nextYearDate.getMonth() + 1
    const currentDay = currDate.getDate()
    const nextYearDay = nextYearDate.getDate()

    const yearSelect = select([
        option(String(currentYear), { value: currentYear }),
        option(String(nextYear), { value: nextYear })
    ])

    const monthsDiv = div()
    const availableDaysDiv = div()

    function updateAvailableDays(month, year) {
        fetchData(`houses/${houseId}/daysAvailable?month=${month}&year=${year}`, (days) => {
            if (days.length === 0) {
                availableDaysDiv.replaceChildren(p("No available days for this month."))
                return
            }

            let validDays = days

            const monthInt = parseInt(month)
            const yearInt = parseInt(year)

            // Lógica para não incluir dias passados ou com mais de exatamente um ano

            if (monthInt === currentMonth && yearInt === currentYear)
                validDays = days.filter( day => day >= currentDay)

            else if (monthInt === nextYearMonth && yearInt === nextYear)
                validDays = days.filter( day => day <= nextYearDay)

            availableDaysDiv.replaceChildren(
                p(`Available days: ${validDays.join(", ")}`)
            )
        })
    }

    function updateMonthSelector() {
        const selectedYear = parseInt(yearSelect.value)

        let validMonths = []
        if (selectedYear === currentYear) {
            validMonths = months.slice(currentMonth - 1)
        } else {
            validMonths = months.slice(0, nextYearMonth)
        }

        const monthSelect = select(
            validMonths.map(month =>
                option(month, { value: months.indexOf(month) + 1 })
            )
        )

        monthSelect.addEventListener("change", () => {
            updateAvailableDays(monthSelect.value, yearSelect.value)
        })

        monthsDiv.replaceChildren(
            label("Month: ", [monthSelect])
        )

        updateAvailableDays(monthSelect.value, yearSelect.value)
    }

    yearSelect.addEventListener("change", updateMonthSelector)

    fetchData(`houses/${houseId}`, (house) => {
        updateMonthSelector() // Temos de chamar antes de apresentar monthsDiv na página pois monthsDiv é um div vazio e queremos que seja atualizado com o seletor

        mainContent.replaceChildren(
            h1(`You are booking for house ${house.title}`),
            p("Check for available days: ", [
                label("Year: ", [yearSelect]),
                monthsDiv,
                availableDaysDiv
            ]),
            label("Start date: ", [startDate]),
            label("End date: ", [endDate]),
            button("Create booking", {
                click: () => {
                    fetch(API_BASE_URL + "bookings", {
                        method: "POST",
                        headers: {
                            "Authorization": tempToken,
                            "Content-Type": "application/json" },
                        body: JSON.stringify({
                            hid: parseInt(houseId, 10),
                            startDate: startDate.value,
                            endDate: endDate.value
                        })
                    })
                        .then( res => res.json())
                        .then( (booking) => {
                            window.location.hash = `bookings/${booking.bid}`
                        })
                }
            })
        )
    })
}


function updateBooking(mainContent){
    const bid = window.location.hash.split('/')[1]
    fetchData(`bookings/${bid}`, (booking) => {
        // TODO: this is repeated code. in future, refactor to function
        const currDate = new Date()
        const nextYearDate = new Date()
        nextYearDate.setFullYear(nextYearDate.getFullYear() + 1)


        // Estas variáveis garantem que tem de haver pelo menos uma noite de reserva
        const maxStartDate = new Date(nextYearDate)
        maxStartDate.setDate(nextYearDate.getDate() - 1)

        const minEndDate = new Date(currDate)
        minEndDate.setDate(currDate.getDate() + 1)


        const startDate = input({
            type: "date",
            min: getFormatedDate(currDate),
            max: getFormatedDate(maxStartDate),
            value: booking.startDate
        })
        const endDate = input({
            type: "date",
            min: getFormatedDate(minEndDate),
            max: getFormatedDate(nextYearDate),
            value: booking.endDate
        })

        const updateButton = button("Update", {
            click: () => {
                booking.startDate = startDate.value
                booking.endDate = endDate.value
                console.log(booking)
                fetch(`bookings/${bid}`, {
                    method: "PUT",
                    headers: {"Authorization": tempToken},
                    body: JSON.stringify({
                        startDate: startDate.value,
                        endDate: endDate.value
                    })
                })
            }
        })

        mainContent.replaceChildren(
            h1("Update Booking"),
            startDate,
            endDate,
            updateButton
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
    getHousesSearch,
    //getLocationDetails,
    getUserDetails,
    getBookings,
    getBookingDetails,
    getFullLocation,
    createLocation,
    createHouse,
    createBooking,
    updateBooking
}

export default handlers
