package pt.isel.ls.houses.data.memory

import kotlinx.datetime.LocalDateTime
import pt.isel.ls.houses.domain.Booking
import pt.isel.ls.houses.domain.House
import pt.isel.ls.houses.repository.BookingRepository

class BookingDataMem(private val houseDataMem: HouseDataMem): BookingRepository {
    private val bookings = mutableListOf<Booking>()
    private var id = 1

    override fun createBooking(
        hid: Int,
        userId: Int,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Booking {
        val booking = Booking(id++, startDate, endDate, userId, hid)
        bookings.add(booking)
        return booking
    }

    override fun getBooking(bid: Int): Booking? = bookings.find { it.bid == bid }

    override fun getBookings(
        hid: Int,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<Booking> = bookings

     override fun getAvailableHouses(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<House> {
         val hIds = bookings.filter { it.startDate > endDate || it.endDate < startDate}.map { it.houseId }
         return houseDataMem.getHouses().filter { it.hid in hIds }
     }
}