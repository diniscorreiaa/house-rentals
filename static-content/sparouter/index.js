import router from "./router.js";
import handlers from "./handlers.js";

window.addEventListener('load', loadHandler)
window.addEventListener('hashchange', hashChangeHandler)

function loadHandler(){
    router.addRouteHandler("home", handlers.getHome)
    router.addRouteHandler("houses", handlers.getHouses)
    router.addRouteHandler("house-details", handlers.getHouseDetails)
    router.addRouteHandler("location-detail", handlers.getLocationDetails)
    router.addRouteHandler("bookings", handlers.getBookings)

    router.addRouteHandler("user-details", handlers.getUserDetails)
    router.addRouteHandler("booking-details", handlers.getBookingDetails)


    router.addDefaultNotFoundRouteHandler(() => window.location.hash = "home")

    hashChangeHandler()
}

function hashChangeHandler(){
    const mainContent = document.getElementById("mainContent")
    const path =  window.location.hash.replace("#", "")

    const handler = router.getRouteHandler(path)
    handler(mainContent)
}