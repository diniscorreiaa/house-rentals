package pt.isel.ls.houses.services

import pt.isel.ls.houses.repository.DataRepository

class Services(data: DataRepository) {
    val users = UserServices(data)
    val location = LocationServices(data)
    val houses = HouseServices(data)
    val booking = BookingServices(data)
}