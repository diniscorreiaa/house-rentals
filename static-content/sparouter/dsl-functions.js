import API_BASE_URL from "./handlers";

function getSomeDetails(id, mainContent, ...propNames) {
    fetch(API_BASE_URL + id)
        .then(res => res.json())
        .then(data => {
            propNames.forEach(prop => {
                const ulData = document.createElement("ul")


            })
        }
}
