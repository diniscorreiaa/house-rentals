package pt.isel.ls.houses.server

import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer
import pt.isel.ls.houses.data.memory.DataMem
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
get /locations/{lid}/children
get /locations/{lid}/full

House Management
post /houses
get /houses/{hid}
get /houses

Booking Management
post /bookings
get /bookings/{bid}
get /bookings?{hid}&{date-interval}
get /houses/available?{startDate}&{endDate}
 */

const val SERVER_PORT = 8000

fun main() {
    val data = DataMem()
    val services = Services(data)
    val webApi = WebApi(services, data)

    val userRoutes = routes(
        "users" bind POST to webApi.user::createUser,
        "users/{uid}" bind GET to webApi.user::getUser
    )

    val locationRoutes = routes(
        "locations" bind POST to webApi.location::createLocation,
        "locations/{lid}" bind GET to webApi.location::getLocation,
        "locations/{lid}/children" bind GET to webApi.location::getChildLocations,
        "locations/{lid}/full" bind GET to webApi.location::getHierarchy,
    )

    val houseRoutes = routes(
        "houses" bind POST to webApi.house::createHouse,
        "houses/{hid}" bind GET to webApi.house::getHouse,
        "houses" bind GET to webApi.house::getHouses,
    )

    val bookingRoutes = routes(
        "bookings" bind POST to webApi.booking::createBooking,
        "bookings?{hid}&{date-interval}" bind GET to webApi.booking::getBookings,
        "bookings/{bid}" bind GET to webApi.booking::getBooking,
        "houses/available?{startDate}&{endDate}" bind GET to webApi.booking::getAvailableHouses,
    )

    routes(
        userRoutes, locationRoutes, houseRoutes, bookingRoutes
    ).withFilter(filter).asServer(Jetty(SERVER_PORT)).start().also {
        println("Started server on port $SERVER_PORT")
    }


}