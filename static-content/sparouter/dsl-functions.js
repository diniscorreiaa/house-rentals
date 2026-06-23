export const ul = (...args) => element("ul", ...args)
export const ol = (...args) => element("ol", ...args)
export const div = (...args) => element("div", ...args)
export const li = (...args) => element("li", ...args)
export const p = (...args) => element("p", ...args)
export const a = (...args) => element("a", ...args)
export const h1 = (...args) => element("h1", ...args)
export const h2 = (...args) => element("h2", ...args)
export const h3 = (...args) => element("h3", ...args)
export const h4 = (...args) => element("h4", ...args)
export const h5 = (...args) => element("h5", ...args)
export const h6 = (...args) => element("h6", ...args)
export const option = (...args) => element("option", ...args)
export const select = (...args) => element("select", ...args)
export const label = (...args) => element("label", ...args)
export const input = (...args) => element("input", ...args)
export const button = (...args) => element("button", ...args)

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
