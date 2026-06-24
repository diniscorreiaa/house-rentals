import router from "./router.js";
import handlers, {housesLimit} from "./handlers.js";
import {a} from "./dsl-functions.js";
import {currentPath} from "./utils.js";

window.addEventListener('load', loadHandler)
window.addEventListener('hashchange', hashChangeHandler)

function loadHandler(){
    document.getElementById("housesLink").onclick = (ev) => { ev.preventDefault() ;window.location.hash = `houses?limit=${housesLimit}&skip=0`} // https://knowledge.evinced.com/system-validations/anchor-without-href

    router.addRouteHandler("home", handlers.getHome)
    router.addRouteHandler("houses", handlers.getHouses)
    router.addRouteHandler("houses/create", handlers.createHouse)
    router.addRouteHandler("houses/*", handlers.getHouseDetails)
    router.addRouteHandler("houses-search", handlers.getHousesSearch)
    router.addRouteHandler("locations/*/full", handlers.getFullLocation)
    router.addRouteHandler("bookings", handlers.getBookings)
    router.addRouteHandler("users/*", handlers.getUserDetails)
    router.addRouteHandler("bookings/create", handlers.createBooking)
    router.addRouteHandler("bookings/*/update", handlers.updateBooking)
    router.addRouteHandler("bookings/*", handlers.getBookingDetails)
    router.addRouteHandler("locations/create", handlers.createLocation)
    router.addRouteHandler("login", handlers.signIn)
    router.addRouteHandler("register", handlers.signUp)
    router.addDefaultNotFoundRouteHandler(() => window.location.hash = "home")

    updateLoginLogoutLinks()
    hashChangeHandler()
}

function hashChangeHandler(){
    const mainContent = document.getElementById("mainContent")

    const handler = router.getRouteHandler(currentPath())
    handler(mainContent)
}

export function updateLoginLogoutLinks() {
    const div = document.getElementById("top-bar")

    if (sessionStorage.getItem("token") != null) {
        loginLink.remove()
        registerLink.remove()
        div.appendChild(logoutLink)
    } else {
        div.appendChild(loginLink)
        div.appendChild(registerLink)
        logoutLink.remove()
    }
}

const logoutLink = a("Logout", {
    click: () => {
        sessionStorage.removeItem("token")
        updateLoginLogoutLinks()
    }, class: "link-secondary"
})

const loginLink = a("Login", {
    click: () => {
        window.location.hash = "login"
    }, class: "link-secondary"
})

const registerLink = a("Register", {
    click: () => {
        window.location.hash = "register"
    }, class: "link-secondary"
})
