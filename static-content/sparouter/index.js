import router from "./router.js";
import handlers, {housesLimit} from "./handlers.js";

window.addEventListener('load', loadHandler)
window.addEventListener('hashchange', hashChangeHandler)

function loadHandler(){
    document.getElementById("housesLink").href += `?limit=${housesLimit}&skip=0`

    router.addRouteHandler("home", handlers.getHome)
    router.addRouteHandler("houses", handlers.getHouses)
    router.addRouteHandler("houses/*", handlers.getHouseDetails)
    router.addRouteHandler("locations/*/full", handlers.getFullLocation)
    router.addRouteHandler("bookings", handlers.getBookings)
    router.addRouteHandler("users/*", handlers.getUserDetails)
    router.addRouteHandler("bookings/*", handlers.getBookingDetails)

    router.addDefaultNotFoundRouteHandler(() => window.location.hash = "home")

    hashChangeHandler()
}

function hashChangeHandler(){
    const mainContent = document.getElementById("mainContent")
    const path =  window.location.hash.replace("#", "")

    const handler = router.getRouteHandler(path)
    handler(mainContent)
}