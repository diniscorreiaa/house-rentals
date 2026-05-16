import {API_BASE_URL} from "./handlers.js";


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

export const ul = (...args) => element("ul", ...args)
export const ol = (...args) => element("ol", ...args)

export const div = (...args) => element("div", ...args)

export const li = (...args) => element("li", ...args)
export const p = (...args) => element("p", ...args)

export const a = (...args) => element("a", ...args)

// TODO: Make the creation of these dynamic
export const h1 = (...args) => element("h1", ...args)
export const h2 = (...args) => element("h2", ...args)
export const h3 = (...args) => element("h3", ...args)
export const h4 = (...args) => element("h4", ...args)
export const h5 = (...args) => element("h5", ...args)
export const h6 = (...args) => element("h6", ...args)


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
    const button = elementWithText("button", text)
    button.disabled = disabled
    button.onclick = onClick
    return button
}

export function selector(label, currentLimit, values, onChange) {
    const optElems = []
    values.forEach(value => {
        const option = elementWithText("option", String(value)) //document.createElement("option")
        option.value = value
        option.selected = currentLimit === value
        optElems.push(option)
    })
    const labelElement = elementWithText("label", label)
    const select = elementWithElementsInside("select", optElems)
    select.onchange = () => { onChange(parseInt(select.value)) }
    labelElement.appendChild(select)
    return labelElement
}

/**
 * @param {string} elementName
 * @param {...(string|Array.<HTMLElement>|Object)} args
 * @returns {HTMLElement}
 */
export function element(elementName, ...args) {
    if (!(args.length >= 0 && args.length <= 3)) throw new Error("This function receives from 0 to 3 parameter")
    let element = document.createElement(elementName)

    // TODO: make sure there is only one of each arg type

    args.forEach(value => {
        if (Array.isArray(value)) addNestedElementsToElement(element, value)
        else if (typeof value === "string") addTextNodeToElement(element, value)
        else if (typeof value === "object") addPropertiesToElement(element, value)
        }
    )

    return element
}

function addTextNodeToElement(element, text) {
    const insideNode = document.createTextNode(text)
    element.appendChild(insideNode)
}

function addNestedElementsToElement(element, elementsInside) {
    elementsInside.forEach(nestedElement => element.appendChild(nestedElement))
}

function addPropertiesToElement(element, props) {
    for (const [propName, propValue] of Object.entries(props)) {
        element.setAttribute(propName, propValue)

    }
}
