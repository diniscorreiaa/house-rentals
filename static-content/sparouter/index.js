import router from "./router.js";
import handlers, {housesLimit} from "./handlers.js";

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
    router.addRouteHandler("bookings/*", handlers.getBookingDetails)
    router.addRouteHandler("locations/create", handlers.createLocation)

    router.addDefaultNotFoundRouteHandler(() => window.location.hash = "home")

    hashChangeHandler()
}

function hashChangeHandler(){
    const mainContent = document.getElementById("mainContent")
    const path =  window.location.hash.replace("#", "")

    const handler = router.getRouteHandler(path)
    handler(mainContent)
}