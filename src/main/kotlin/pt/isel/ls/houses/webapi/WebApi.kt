package pt.isel.ls.houses.webapi

import pt.isel.ls.houses.repository.DataRepository
import pt.isel.ls.houses.services.Services


class WebApi(services: Services, data: DataRepository) { // TODO: DEVIA SER UserDataMem
    val user = UserWebApi(services.users, data)
    val location = LocationWebApi(services.location, data)
    val house = HouseWebApi(services.houses, data)
    val booking = BookingWebApi(services.booking, data)
}
