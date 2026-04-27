import API_BASE_URL from "./handlers";

export function getSomeDetails(id, mainContent, ...propNames) {
    fetch(API_BASE_URL + id)
        .then(res => res.json())
        .then(data => {
            const ulData = document.createElement("ul")
            propNames.forEach(propName => {
                const li = document.createElement("li")
                const text = document.createTextNode(data[propName])
                li.appendChild(text)
                ulData.appendChild(li)
            })
            mainContent.replaceChildren(ulData)
        })
}
export const ul = (elementsInside) => elementWithElementsInside("ul", elementsInside)
export const ol = (elementsInside) => elementWithElementsInside("ol", elementsInside)

export const div = (elementsInside) => elementWithElementsInside("div", elementsInside)

export const li = (text) => elementWithText("li", text)
export const p = (text) => elementWithText("p", text)

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
    return element.appendChild(insideNode)
}

function elementWithElementsInside(elementName, elementsInside){
    const element = document.createElement(elementName)
    elementsInside.forEach(element => element.appendChild(element))
    return element
}