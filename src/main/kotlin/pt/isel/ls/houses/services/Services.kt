package pt.isel.ls.houses.services

import pt.isel.ls.houses.repository.DataRepository

class Services(data: DataRepository) {
    val users = UserServices(data.user)
    val location = LocationServices(data.user, data)
    val houses = HouseServices(data.user, data)
    val booking = BookingServices(data.user, data)
}