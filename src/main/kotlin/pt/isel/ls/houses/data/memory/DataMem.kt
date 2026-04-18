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
    private val booking = BookingDataMem(house)

    override fun createBooking(
        hid: Int,
        userId: Int,
        startDate: LocalDate,
        endDate: LocalDate
    ): Booking = booking.createBooking(hid, userId, startDate, endDate)

    override fun getBooking(bid: Int): Booking? = booking.getBooking(bid)

    override fun getBookings(
        hid: Int,
        startDate: LocalDate,
        endDate: LocalDate,
        limit: Int?,
        skip: Int?,
    ): List<Booking> = booking.getBookings(hid, startDate, endDate, limit, skip)

    override fun getAvailableHouses(
        startDate: LocalDate,
        endDate: LocalDate,
        limit: Int?,
        skip: Int?,
    ): List<House> = booking.getAvailableHouses(startDate, endDate, limit, skip)
}