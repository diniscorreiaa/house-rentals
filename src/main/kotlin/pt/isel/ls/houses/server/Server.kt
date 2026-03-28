package pt.isel.ls.houses.server

import org.http4k.core.Method.GET
import org.http4k.core.Method.POST
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer
import pt.isel.ls.houses.webapi.*


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
    val userWebApi = UserWebApi()
    val locationWebApi = LocationWebApi()
    val houseWebApi = HouseWebApi()
    val bookingWebApi = BookingWebApi()

    val userRoutes = routes(
        "users" bind POST to userWebApi::createUser,
        "users/{uid}" bind GET to userWebApi::getUser
    )

    val locationRoutes = routes(
        "locations" bind POST to locationWebApi::createLocation,
        "locations/{lid}" bind GET to locationWebApi::getLocation,
        "locations/{lid}/children" bind GET to locationWebApi::getChildLocations,
        "locations/{lid}/full" bind GET to locationWebApi::getHierarchy,
    )

    val houseRoutes = routes(
        "houses" bind POST to houseWebApi::createHouse,
        "houses/{hid}" bind GET to houseWebApi::getHouse,
        "houses" bind GET to houseWebApi::getHouses,
    )

    val bookingRoutes = routes(
        "bookings" bind POST to bookingWebApi::createBooking,
        "bookings/{bid}" bind GET to bookingWebApi::getBooking,
        "bookings?{hid}&{date-interval}" bind GET to bookingWebApi::getBookings, // TODO: NAO FAZ SENTIDO TER bookings/{bid} num endpoint e bookings/{hid} noutro, temos de ser consistentes com o formato do path. hid deve ser passado no body (seletor html)!!! (ACHO EU) - Confirmar
        "houses/available?{startDate}&{endDate}" bind GET to bookingWebApi::getAvailableHouses, // TODO: Não faz sentido um URI ter date interval e o outro startDate e endDate separados
    )

    val app = routes(
        userRoutes, locationRoutes, houseRoutes, bookingRoutes
    ).asServer(Jetty(SERVER_PORT)).start().also { println("Started server on port $SERVER_PORT") }


}