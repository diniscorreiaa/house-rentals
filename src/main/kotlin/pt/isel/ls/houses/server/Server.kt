package pt.isel.ls.houses.server

import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.core.Method.DELETE
import org.http4k.core.Method.PUT
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.singlePageApp
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.slf4j.LoggerFactory
import pt.isel.ls.houses.data.db.DataPostgresSQL
import pt.isel.ls.houses.services.Services
import pt.isel.ls.houses.webapi.WebApi


/*
prefix everything /api/v1

User Management
post /users
get /users/{uid}

Location Management
post /locations
get /locations/{lid}
get /locations/{lid}/children (paging)
get /locations/{lid}/full

House Management
post /houses
get /houses/{hid}
get /houses (paging)

Booking Management
post /bookings
get /bookings/{bid}
get /bookings?{hid}&{date-interval} (paging)
get /houses/available?{startDate}&{endDate} (paging)
*/

const val SERVER_PORT = 8000

val logger = LoggerFactory.getLogger("pt.isel.ls.server.ServerKt")

fun main() {
    val data = DataPostgresSQL()
//    val data = DataMem()
    val services = Services(data)
    val webApi = WebApi(services)

    val userRoutes = routes(
        "users" bind POST to webApi.user::createUser,
        "users/{uid}" bind GET to webApi.user::getUser,
        "users/{uid}/bookings" bind GET to webApi.user::userBookings,
        "users/login" bind POST to webApi.user::login,
    )

    val locationRoutes = routes(
        "locations" bind POST to webApi.location::createLocation,
        "locations/{lid}" bind GET to webApi.location::getLocation,
        "locations/{lid}/children" bind GET to webApi.location::getChildLocations,
        "locations/{lid}/full" bind GET to webApi.location::getHierarchy,
        "locations" bind GET to webApi.location::getLocationsFromType
    )

    val houseRoutes = routes(
        "houses" bind POST to webApi.house::createHouse,
        "houses/{hid}" bind GET to webApi.house::getHouse,
        "houses" bind GET to webApi.house::getHouses,
        "houses/predictPrice" bind GET to webApi.house::getPredictedPrice
    )

    val bookingRoutes = routes(
        "bookings" bind POST to webApi.booking::createBooking,
        "bookings" bind GET to webApi.booking::getBookings,
        "bookings/{bid}" bind GET to webApi.booking::getBooking,
        "houses/available" bind GET to webApi.booking::getAvailableHouses,
        "bookings/{bid}" bind DELETE to webApi.booking::deleteBooking,
        "bookings/{bid}" bind PUT to webApi.booking::updateBooking,
        "houses/{hid}/daysAvailable" bind GET to webApi.booking::getAvailableDays
    )

    // bookingRoutes have to come BEFORE houseRoutes, for houses/available endpoint to be matched first to houses/{hid}
    routes(
        userRoutes, locationRoutes, bookingRoutes, houseRoutes, singlePageApp(ResourceLoader.Directory("static-content"))
    ).withFilter(filter).asServer(Jetty(SERVER_PORT)).start().also {
        println("Started server on port $SERVER_PORT")
    }


}