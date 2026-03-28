package pt.isel.ls.houses.webapi


class WebApi {
    private val user = UserWebApi()
    private val location = LocationWebApi()
    private val house = HouseWebApi()
    private val booking = BookingWebApi()
}
