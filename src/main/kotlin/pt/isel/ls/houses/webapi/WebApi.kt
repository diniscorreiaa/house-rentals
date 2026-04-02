package pt.isel.ls.houses.webapi

import pt.isel.ls.houses.repository.DataRepository
import pt.isel.ls.houses.services.Services


class WebApi(services: Services, data: DataRepository) { // TODO: DEVIA SER UserDataMem
    val user = UserWebApi(services.users)
    val location = LocationWebApi(services.location)
    val house = HouseWebApi(services.houses)
    val booking = BookingWebApi(services.booking, data)
}
