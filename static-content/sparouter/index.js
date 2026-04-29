import router from "./router.js";
import handlers from "./handlers.js";

window.addEventListener('load', loadHandler)
window.addEventListener('hashchange', hashChangeHandler)

function loadHandler(){
    router.addRouteHandler("home", handlers.getHome)
    router.addRouteHandler("houses", handlers.getHouses)
    router.addRouteHandler("houses/1", handlers.getHouseDetails)    // TODO: This is temporary, was just to test one house
    router.addRouteHandler("house-details", handlers.getHouseDetails)
    router.addRouteHandler("locations/4/full", handlers.getFullLocation)
    router.addRouteHandler("bookings", handlers.getBookings)

    router.addRouteHandler("user-details", handlers.getUserDetails)
    router.addRouteHandler("bookings/1", handlers.getBookingDetails) // TODO: This is temporary, was just to test one booking


    router.addDefaultNotFoundRouteHandler(() => window.location.hash = "home")

    hashChangeHandler()
}

function hashChangeHandler(){
    const mainContent = document.getElementById("mainContent")
    const path =  window.location.hash.replace("#", "")

    const handler = router.getRouteHandler(path)
    handler(mainContent)
}