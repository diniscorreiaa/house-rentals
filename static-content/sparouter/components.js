import {button, div, label, option, select} from "./dsl-functions.js";

// The following link explains why we can get the element to which the element was attached
// https://developer.mozilla.org/en-US/docs/Web/API/EventTarget/addEventListener#the_value_of_this_within_the_handler
export function selector(labelText, currentLimit, values, onChange) {
    const optionElements = values.map(value =>
        option(String(value), {value, selected: currentLimit === value})
    )

    return label(labelText, [
        select([...optionElements], {
            change: (event) => {
                onChange(parseInt(event.currentTarget.value, 10))
            }
        })
    ])
}

/**
 *
 * @param {string} hashEndpoint
 * @param {number} limit
 * @param {number} currSkip
 * @param {number} totalLength
 * @param {Array.<number>} limitOptions
 * @return {HTMLElement}
 */
export function pagingPrevNextButtons(hashEndpoint, limit, currSkip, totalLength, limitOptions){
    const separator = hashEndpoint.includes("?") ? "&" : "?"

    const limitSelector = selector("Select limit:", limit, limitOptions, (newLimit) => {
        window.location.hash = `${hashEndpoint}${separator}limit=${newLimit}&skip=0`;
    }) // É necessário dar reset ao skip para garantir alinhamento

    const prevButton = button("Previous", {
        disabled: currSkip === 0,
        click: () => {
            window.location.hash = `${hashEndpoint}${separator}limit=${limit}&skip=${currSkip - limit}`
        }
    })

    const nextButton = button("Next", {
        disabled: totalLength < limit,
        click: () => {
            window.location.hash = `${hashEndpoint}${separator}limit=${limit}&skip=${currSkip + limit}`
        }
    })

    return div([limitSelector, prevButton, nextButton])
}
