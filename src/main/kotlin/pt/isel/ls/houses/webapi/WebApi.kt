package pt.isel.ls.houses.webapi

import pt.isel.ls.houses.houses
import pt.isel.ls.houses.services.BookingServices
import pt.isel.ls.houses.services.HouseServices
import pt.isel.ls.houses.services.LocationServices
import pt.isel.ls.houses.services.Services
import pt.isel.ls.houses.services.UserServices


class WebApi(services: Services) {
    val user = UserWebApi(services.users)
    val location = LocationWebApi(services.location)
    val house = HouseWebApi(services.houses)
    val booking = BookingWebApi(services.booking)
}
