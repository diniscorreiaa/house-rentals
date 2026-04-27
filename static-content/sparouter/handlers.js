import {getSomeDetails} from "./dsl-functions";

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
    getSomeDetails(hid, mainContent, "title", "location", "areaSqMt", "pricePerNight", "description")
}

function getLocationDetails(lid, mainContent) {
    getSomeDetails(lid, mainContent, "name", "type")
    // TODO: Incluir hierarquia?
}


export const handlers = {
    getHome,
    getHouses,
    getHouseDetails,
    getLocationDetails
}

export default handlers
