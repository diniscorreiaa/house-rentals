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
