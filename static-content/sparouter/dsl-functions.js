export const ul = createCustomElement("ul")
export const ol = createCustomElement("ol")
export const div = createCustomElement("div")
export const li = createCustomElement("li")
export const p = createCustomElement("p")
export const a = createCustomElement("a")
export const h1 = createCustomElement("h1")
export const h2 = createCustomElement("h2")
export const h3 = createCustomElement("h3")
export const h4 = createCustomElement("h4")
export const h5 = createCustomElement("h5")
export const h6 = createCustomElement("h6")
export const option = createCustomElement("option")
export const select = createCustomElement("select")
export const label = createCustomElement("label")
export const input = createCustomElement("input")
export const button = createCustomElement("button")

function createCustomElement(name){
    return (...args) => element(name, ...args)
}

// The following link explains why we can get the element to which the element was attached
// https://developer.mozilla.org/en-US/docs/Web/API/EventTarget/addEventListener#the_value_of_this_within_the_handler
export function selector(labelText, currentLimit, values, onChange) {
    const optionElements = values.map(value =>
        option(String(value), {value, selected: currentLimit === value})
    )

    return label(labelText, [
        select([...optionElements], {
            change: (event) => { onChange(parseInt(event.currentTarget.value, 10)) }
        })
    ])
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

// https://stackoverflow.com/questions/9201471/how-to-add-boolean-attribute-using-javascript
function addPropertiesToElement(element, props) {
    for (const [propName, propValue] of Object.entries(props)) {
        if (typeof propValue === "function")
            element.addEventListener(propName, propValue)
        else if(typeof propValue === "boolean"){
            if (propValue === true)
                element.setAttribute(propName, propName)
            else element.removeAttribute(propName)
        }
        else element.setAttribute(propName, propValue)
    }
}
