import { API_BASE_URL } from "./handlers.js";


export function fetchData(path = currentPath(), execute) {
    fetch(API_BASE_URL + path)
        .then(res => res.json())
        .then(data => execute(data))
}

export function getAll(mainContent, lastUri) {
    fetch(API_BASE_URL + "houses")
        .then(res => res.json())
        .then(houses => {
                const div = document.createElement("div")
                const h1 = h1("All Houses")
                div.appendChild(h1)

                houses.forEach(house => {
                    const p = document.createElement("p")
                    const a = a("#houses/" + house.hid, "Details")
                    p.appendChild(a)
                    div.appendChild(p)
                })
                mainContent.replaceChildren(div)
            }
        )
}

export function getSomeDetails(mainContent, ...propNames) {
    fetch(API_BASE_URL + currentPath())
        .then(res => res.json())
        .then(data => {
            const listItems = []
            propNames.forEach(propName => {
                listItems.push(li(`${propName}: ${data[propName]}`))
            })
            const ulData = ul(listItems)
            mainContent.replaceChildren(ulData)
        })
}

export const currentPath = () => window.location.hash.replace("#","")

/////////////////////////////////////////////////////

export const ul = (elementsInside) => elementWithElementsInside("ul", elementsInside)
export const ol = (elementsInside) => elementWithElementsInside("ol", elementsInside)

export const div = (elementsInside) => elementWithElementsInside("div", elementsInside)

export const li = (text) => elementWithText("li", text)
export const p = (text) => elementWithText("p", text)

export const a = (href, text) => {
    const elm = elementWithText("a", text)
    elm.href = href
    return elm
}

// TODO: Make the creation of these dynamic
export const h1 = (text) => elementWithText("h1", text)
export const h2 = (text) => elementWithText("h2", text)
export const h3 = (text) => elementWithText("h3", text)
export const h4 = (text) => elementWithText("h4", text)
export const h5 = (text) => elementWithText("h5", text)
export const h6 = (text) => elementWithText("h6", text)


function elementWithText(elementName, text){
    const element = document.createElement(elementName)
    const insideNode = document.createTextNode(text)
    element.appendChild(insideNode)
    return element
}

function elementWithElementsInside(elementName, elementsInside){
    const creatorElement = document.createElement(elementName)
    elementsInside.forEach(element => creatorElement.appendChild(element))
    return creatorElement
}

export function button(text, disabled, onClick) {
    const button = document.createElement("button")
    button.textContent = text
    button.disabled = disabled
    button.onclick = onClick
    return button
}