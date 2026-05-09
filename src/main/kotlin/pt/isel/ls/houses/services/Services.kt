package pt.isel.ls.houses.services

import pt.isel.ls.houses.data.db.BookingDataPostgresql
import pt.isel.ls.houses.repository.DataRepository

class Services(data: DataRepository, dataPostgresql: BookingDataPostgresql) {
    val users = UserServices(data.user, data.booking)
    val location = LocationServices(data.user, data.location)
    val houses = HouseServices(data.user, data.location, data.house)
    val booking = BookingServices(data.user, data.house, data.booking, dataPostgresql)
}