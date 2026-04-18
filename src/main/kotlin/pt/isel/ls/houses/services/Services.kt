package pt.isel.ls.houses.services

import pt.isel.ls.houses.repository.DataRepository

class Services(data: DataRepository) {
    val users = UserServices(data.user)
    val location = LocationServices(data.user, data.location)
    val houses = HouseServices(data.user, data.location, data.house)
    val booking = BookingServices(data.user, data.house, data)
}