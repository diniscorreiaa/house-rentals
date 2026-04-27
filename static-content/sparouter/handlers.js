export const API_BASE_URL = "http://localhost:8000/"

function getHome(mainContent) {
    const h1 = document.createElement("h1")
    const text = document.createTextNode("House Rentals HOME")
    h1.appendChild(text)
    mainContent.replaceChildren(h1)
}

function getHouses(mainContent) {
    fetch(API_BASE_URL + "houses")
        .then(res => res.json())
        .then(houses => {
                const div = document.createElement("div")
                const h1 = document.createElement("h1")
                const text = document.createTextNode("All Houses")
                h1.appendChild(text)
                div.appendChild(h1)

                houses.forEach(house => {
                    const p = document.createElement("p")
                    const a = document.createElement("a")
                    const aText = document.createTextNode("Details")
                    a.appendChild(aText)
                    a.href = "#houses/" + house.hid
                    p.appendChild(a)
                    div.appendChild(p)
                })
            mainContent.replaceChildren(div)
            }
        )
}

function getHouseDetails(hid, mainContent) {
    fetch(API_BASE_URL + hid)
        .then(res => res.json())
        .then(house => {
            const ulHouse = document.createElement("ul")

            const liTitle = document.createElement("li")
            const textTitle = document.createTextNode(house.title)
            liTitle.appendChild(textTitle)

            const liLocation = document.createElement("li")
            const textLocation = document.createTextNode(house.location)
            liLocation.appendChild(textLocation)

            const liArea = document.createElement("li")
            const textArea = document.createTextNode(house.areaSqMt)
            liArea.appendChild(textArea)

            const liPrice = document.createElement("li")
            const textPrice = document.createTextNode(house.pricePerNight)
            liPrice.appendChild(textPrice)

            const liDescription = document.createElement("li")
            const textDescription = document.createTextNode(house.description)
            liDescription.appendChild(textDescription)


            ulHouse.appendChild(liTitle)
            ulHouse.appendChild(liLocation)
            ulHouse.appendChild(liArea)
            ulHouse.appendChild(liPrice)
            ulHouse.appendChild(liDescription)

            mainContent.replaceChildren(ulHouse)
        })
}

function getLocationDetails(hid, mainContent) {
    fetch(API_BASE_URL + hid)
        .then(res => res.json())
        .then(house => {
            const ulHouse = document.createElement("ul")

            const liTitle = document.createElement("li")
            const textTitle = document.createTextNode(house.title)
            liTitle.appendChild(textTitle)

            const liLocation = document.createElement("li")
            const textLocation = document.createTextNode(house.location)
            liLocation.appendChild(textLocation)

            const liArea = document.createElement("li")
            const textArea = document.createTextNode(house.areaSqMt)
            liArea.appendChild(textArea)

            const liPrice = document.createElement("li")
            const textPrice = document.createTextNode(house.pricePerNight)
            liPrice.appendChild(textPrice)

            const liDescription = document.createElement("li")
            const textDescription = document.createTextNode(house.description)
            liDescription.appendChild(textDescription)


            ulHouse.appendChild(liTitle)
            ulHouse.appendChild(liLocation)
            ulHouse.appendChild(liArea)
            ulHouse.appendChild(liPrice)
            ulHouse.appendChild(liDescription)

            mainContent.replaceChildren(ulHouse)
        })
}


export const handlers = {
    getHome,
    getHouses,
    getHouseDetails,
    getLocationDetails
}

export default handlers
