package pt.isel.ls.houses.data.memory

import kotlinx.datetime.LocalDate
import pt.isel.ls.houses.domain.Booking
import pt.isel.ls.houses.domain.House
import pt.isel.ls.houses.domain.Location
import pt.isel.ls.houses.domain.LocationType
import pt.isel.ls.houses.domain.User
import pt.isel.ls.houses.repository.DataRepository
import kotlin.uuid.Uuid

class DataMem : DataRepository {
    override val user = UserDataMem()
    override val location = LocationDataMem()
    override val house = HouseDataMem()
    override val booking = BookingDataMem(house)
}