package pt.isel.ls.houses.data.memory

import kotlinx.datetime.LocalDate
import pt.isel.ls.houses.domain.Booking
import pt.isel.ls.houses.domain.House
import pt.isel.ls.houses.repository.BookingRepository

class BookingDataMem(private val houseDataMem: HouseDataMem): BookingRepository {
    private val bookings = mutableListOf<Booking>()
    private var id = 1

    override fun createBooking(
        hid: Int,
        userId: Int,
        startDate: LocalDate,
        endDate: LocalDate
    ): Booking {
        val booking = Booking(id++, startDate, endDate, userId, hid)
        bookings.add(booking)
        return booking
    }

    override fun getBooking(bid: Int): Booking? = bookings.find { it.bid == bid }

    override fun getBookings(
        hid: Int,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<Booking> = bookings.filter { it.houseId == hid && (it.startDate >= startDate && it.endDate <= endDate) }

     override fun getAvailableHouses(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<House> {
         val hIds = bookings.filter { it.startDate > endDate || it.endDate < startDate}.map { it.houseId }
         return houseDataMem.getHouses().filter { it.hid in hIds }
     }
}